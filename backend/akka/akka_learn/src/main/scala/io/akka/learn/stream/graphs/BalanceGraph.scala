package io.akka.learn.stream.graphs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.{RunnableGraph, Sink}
import akka.stream.scaladsl.{Balance, GraphDSL, Merge, Source}

/**
  * BalanceGraph
  *
  * 有二个快慢不一 Source 的数据单元需要发送到二个 Sink 打印。
  * 常见的场景是需要平衡工作量，二个简单的线性流程显然无法满足要求。
  * 为了平衡负载，需要先合并所有的输入数据再平衡的分发
  * @author damon lin
  *         2020/6/1
  */
object BalanceGraph {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("testStreams")
    implicit val mt = ActorMaterializer()
    implicit val ec = scala.concurrent.ExecutionContext.global

    import scala.concurrent.duration._
    // 构造两个快慢不一致的数据源
    val source1 = Source.repeat("Repeat").throttle(3, 1.seconds).take(7)
    val source2 = Source.tick(0.seconds, 1.seconds, "Tick").take(3)

    val sink1 = Sink.foreach[String](message => println(s"Sink 1: ${message}"))
    val sink2 = Sink.foreach[String](message => println(s"Sink 2: ${message}"))

    val graph = RunnableGraph.fromGraph(GraphDSL.create(){ implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val merge = builder.add(Merge[String](2))
      val balance = builder.add(Balance[String](2))

      // merge source1 和 source2
      // balance 负载均衡到两个 sink
      source1 ~> merge ~> balance ~> sink2
      source2 ~> merge
      balance ~> sink1

      ClosedShape
    })

    graph.run(mt)

  }

}
