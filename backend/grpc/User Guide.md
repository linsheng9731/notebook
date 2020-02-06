# User Guide
Grpc 是google正式发布的的RPC框架，底层基于http2通信。Grpc 是一种远程调用协议，服务端和客户端可以基于不同的语言实现，接口由 protocol buffers 描述。

## Quick Start
使用 protocol buffers 定义协议格式：
```
// greeting 服务定义
service Greeter {
  // SayHello 方法 接受一个 HelloRequest 请求并返回一个 HelloReply
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// 请求体格式
message HelloRequest {
  string name = 1;
}

// 响应体格式
message HelloReply {
  string message = 1;
}
```
使用插件根据 protocol buffers 文件生成服务接口，请求客户端，请求体和响应体类。
服务端通过实现接口定义接口行为：
```  
static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }
```
客户端组装请求体：
```
  public void greet(String name, String host, String port) {
    // 通道 用于连接服务端
    ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port);
    // stub grpc 生成的客户端
    GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStu(channel);
    // 请求体
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response;
    try {
      response = blockingStub.sayHello(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("Greeting: " + response.getMessage());
  }

```

## Server-side streaming RPC
客户端发送一个消息，服务端返回一组流式结果：
```
    // 服务端定义 入参是一个 request 
    // 通过 responseObserver 返回一组流式结果
    @Override
    public void listFeatures(Rectangle request, StreamObserver<Feature> responseObserver) {
      int left = min(request.getLo().getLongitude(), request.getHi().getLongitude());
      int right = max(request.getLo().getLongitude(), request.getHi().getLongitude());
      int top = max(request.getLo().getLatitude(), request.getHi().getLatitude());
      int bottom = min(request.getLo().getLatitude(), request.getHi().getLatitude());

      for (Feature feature : features) {
        if (!RouteGuideUtil.exists(feature)) {
          continue;
        }

        int lat = feature.getLocation().getLatitude();
        int lon = feature.getLocation().getLongitude();
        if (lon >= left && lon <= right && lat >= bottom && lat <= top) {
          responseObserver.onNext(feature); // 多个返回值
        }
      }
      responseObserver.onCompleted();
    }

   // 客户端调用
   // Looking for features between 40, -75 and 42, -73.
   client.listFeatures(400000000, -750000000, 420000000, -730000000);

```
proto 文件定义：
```
service RouteGuide {
  // A server-to-client streaming RPC.
  rpc ListFeatures(Rectangle) returns (stream Feature) {}
}
```
## Client-side streaming RPC
客户端发送多个请求体，服务端返回一个结果:
```
    // 服务端定义 入参是一个 StreamObserver 表示一个流式的输入 
    // 通过 responseObserver 返回一个结果
    @Override
    public StreamObserver<Point> recordRoute(final StreamObserver<RouteSummary> responseObserver) {
      return new StreamObserver<Point>() {
        int pointCount;
        int featureCount;
        int distance;
        Point previous;
        final long startTime = System.nanoTime();

        @Override
        public void onNext(Point point) {
          pointCount++;
          if (RouteGuideUtil.exists(checkFeature(point))) {
            featureCount++;
          }
          // 累计每个输入的 point 得到 distance
          if (previous != null) {
            distance += calcDistance(previous, point);
          }
          previous = point;
        }

        @Override
        public void onError(Throwable t) {
          logger.log(Level.WARNING, "recordRoute cancelled");
        }

        @Override
        public void onCompleted() {
          long seconds = NANOSECONDS.toSeconds(System.nanoTime() - startTime);
          responseObserver.onNext(RouteSummary.newBuilder().setPointCount(pointCount)
              .setFeatureCount(featureCount).setDistance(distance)
              .setElapsedTime((int) seconds).build());
          responseObserver.onCompleted();
        }
      };
    }

    // 客户端调用
    // Record a few randomly selected points from the features file.
    client.recordRoute(features, 10);
```
proto 文件定义：
```
service RouteGuide {
  // A client-to-server streaming RPC.
  rpc RecordRoute(stream Point) returns (RouteSummary) {}
}
```

## Bidirectional streaming RPC
客户端发送多个请求体，服务端返回一组结果:
```
 @Override
    public StreamObserver<RouteNote> routeChat(final StreamObserver<RouteNote> responseObserver) {
      return new StreamObserver<RouteNote>() {
        @Override
        public void onNext(RouteNote note) {
          List<RouteNote> notes = getOrCreateNotes(note.getLocation());

          // Respond with all previous notes at this location.
          for (RouteNote prevNote : notes.toArray(new RouteNote[0])) {
            responseObserver.onNext(prevNote);
          }

          // Now add the new note to the list
          notes.add(note);
        }

        @Override
        public void onError(Throwable t) {
          logger.log(Level.WARNING, "routeChat cancelled");
        }

        @Override
        public void onCompleted() {
          responseObserver.onCompleted();
        }
      };
    }
```
如上所示，入参和返回类型都是 StreamObserver。