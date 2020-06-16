package io.akka.learn.stream.graphs

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.RunnableGraph
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, Sink, Source}

/**
  * ScatterGather
  * 演示 Broadcast 和 Merge
  * @author damon lin
  *         2020/6/1
  */
object ScatterGather {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("scatter-gather")
    implicit val materializer = ActorMaterializer()
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._

      val in = Source(1 to 10)
      val out = Sink.foreach(println)
      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)
      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
      // broadcast 必须有两个 output
      // merge 必须有两个 input
      bcast ~> f4 ~> merge
      ClosedShape

    })

    g.run(materializer)
  }

}
