package io.akka.learn.stream.graphs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.FlowShape
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Source, Zip}

/**
  * FlowShape
  *
  * @author damon lin
  *         2020/6/1
  */
object FlowShapeGraph {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("testStreams")
    implicit val ec = scala.concurrent.ExecutionContext.global

    // Step 1 基本组件
    val flow1 = Flow[Int].map(_ * 10)
    val flow2 = Flow[Int].map(_ * 100)

    // Step 2 基本构建模版
    val flow = Flow.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>

      // Step 3 导入构建操作符
      import GraphDSL.Implicits._

      // Step 4 创建连接构件
      val broadcast = builder.add(Broadcast[Int](2))
      val zip = builder.add(Zip[Int, Int])

      // Step 5 连接功能组件和构件，连接相应的输入输出接口
      broadcast ~> flow1 ~> zip.in0
      broadcast ~> flow2 ~> zip.in1

      FlowShape(broadcast.in, zip.out)

    })

    val source = Source(1 to 10)
    val sink = Sink.foreach[(Int, Int)](r => println(r))
    source.via(flow).runWith(sink)

  }

}
