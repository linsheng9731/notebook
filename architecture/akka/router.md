# 路由
消息可以通过路由发送到目的地 actor。不同的路由策略可以被使用，也可以自定义路由策略。

## 默认的路由策略
- akka.routing.RoundRobinRoutingLogic（轮询策略）
- akka.routing.RandomRoutingLogic（随机选择策略）
- akka.routing.SmallestMailboxRoutingLogic（最小邮箱大小策略）
- akka.routing.BroadcastRoutingLogic（广播策略 发给所有的 routees）
- akka.routing.ScatterGatherFirstCompletedRoutingLogic（首先响应策略 发送给所有 routees 返回第一个完成的结果）
- akka.routing.TailChoppingRoutingLogic（尾部断续策略 随机选择一个发送 延迟一段时间再随机选择一个发送 直到第一个结果返回）
- akka.routing.ConsistentHashingRoutingLogic（一致性 hash 策略）

两种 router actor 类型：
- Pool - router 创建子 actor 作为 routees。router 会监控子 actor，当子 actor 终止的时候移除 routees。
- Group - routee actor 从外部创建，router 通过 actor 选择器发送消息到目标 actor，router 不监控 routees。

## 监管
因为 pool 模式的 routees 是 router 的子 actor，所以 router 默认是 routees 的监督者。默认的监督策略是”always escalate“ 即向上抛出错误。可以通过 supervisorStrategy 属性配置监督策略。

## Pool 模式
Pool 模式通过创建子 actor 的方式创建 routees，通过配置指定 routees 的个数。

```
akka.actor.deployment {
  /parent/router1 {
    router = round-robin-pool
    nr-of-instances = 5
  }
}
```

创建代码：

```
val router1: ActorRef = context.actorOf(FromConfig.props(Props[Worker]), "router1")
```

## Group 模式
很多时候需要路由 actor 在远端或者有不同的路径，这个时候需要使用 group 模式。可以传递 routees 的路径列表给 router 的配置文件来创建 router。
比如下面这个配置文件的例子：

```
akka.actor.deployment {
  /parent/router3 {
    router = round-robin-group
    routees.paths = ["/user/workers/w1", "/user/workers/w2", "/user/workers/w3"]
  }
}
```

创建代码：

```
val router3: ActorRef = context.actorOf(FromConfig.props(), "router3")
```
## 特殊的消息类型
- PoisonPill Messages（接受到的 actor 会停止）
- Kill Messages（接受到的 actor 会停止，同时停止子 actor）
- Management Messages（查询、添加、移除 routee）

## 动态资源池
可以配置动态的资源池，通过定义压力参数（每个 actor 的邮件队列临界值），资源池的上限和下限。

```
akka.actor.deployment {
  /parent/router29 {
    router = round-robin-pool
    resizer {
      lower-bound = 2
      upper-bound = 15
      messages-per-resize = 100
    }
  }
}
```

自动优化的资源池配置器，遵循三条原则优化：
- 在一个时间段内所有 actor 都没有满负荷运作，尝试减小资源池大小.
- 尝试随机减小资源池大小，同时收集指标。
- 尝试优化资源池大小，为了更好的指标。

因为需要在运行时收集指标，所以会消耗大量的内存。

```
akka.actor.deployment {
  /parent/router31 {
    router = round-robin-pool
    optimal-size-exploring-resizer {
      enabled = on
      action-interval = 5s
      downsize-after-underutilized-for = 72h
    }
  }
}
```

## 自定义路由
为了获得更高的性能，akka 的 router 实现不是一个单进程的普通 actor。取而代之的是直接将路有逻辑嵌入到代理的 routees 内。这样发完 routee 的消息能立马被接收到，而不是先通过一个单进程的 actor ，大大提高了性能。
自定义路由：

```
import scala.collection.immutable
import java.util.concurrent.ThreadLocalRandom
import akka.routing.RoundRobinRoutingLogic
import akka.routing.RoutingLogic
import akka.routing.Routee
import akka.routing.SeveralRoutees

class RedundancyRoutingLogic(nbrCopies: Int) extends RoutingLogic {
  val roundRobin = RoundRobinRoutingLogic()
  def select(message: Any, routees: immutable.IndexedSeq[Routee]): Routee = {
    val targets = (1 to nbrCopies).map(_ => roundRobin.select(message, routees))
    SeveralRoutees(targets)
  }
}
```

## 配置 Dispatchers
在 pool 模式下创建子 actor 可以通过配置指定 dispatcher。Group 模式引用的是外部的 actor ，所以无法配置 dispatcher。

```
akka.actor.deployment {
  /poolWithDispatcher {
    router = random-pool
    nr-of-instances = 5
    pool-dispatcher {
      fork-join-executor.parallelism-min = 5
      fork-join-executor.parallelism-max = 5
    }
  }
}
```
router 和 routee 的作用不同，所以 akka 支持给 router 配置不同的 dispatcher，即 router-dispatcher。
代码：

```
val router: ActorRef = system.actorOf(
  // “head” router actor will run on "router-dispatcher" dispatcher
  // Worker routees will run on "pool-dispatcher" dispatcher
  RandomPool(5, routerDispatcher = "router-dispatcher").props(Props[Worker]),
  name = "poolWithDispatcher")
```
