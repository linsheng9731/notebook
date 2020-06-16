package io.akka.learn.stream.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}

/**
  * Map
  *
  * @author damon lin
  *         2020/5/28
  */
object Map {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("flow")
//    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val filter = new Filter
    val flow = Flow[List[Int]].map(i => filter.filter(i) )
    val source: Source[List[Int], NotUsed] = Source(List(List(1, 2, 3,4,5,6)))

    val graph = source
        .via(flow)
       .toMat(Sink.foreach(println))(Keep.left)
    graph.run()

  }

}

class Filter {
  def filter(input: Seq[Int]): Seq[Int] = {
    input.filter(_ / 2 ==1)
  }
}
