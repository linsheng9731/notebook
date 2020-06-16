package io.akka.learn.stream.graphs

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.javadsl.RunnableGraph
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Sink, Source}

/**
  * BroadcastTweets
  *
  * @author damon lin
  *         2020/6/1
  */
object BroadcastTweets {

  final case class Author(handle: String)

  final case class Hashtag(name: String)

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashtags: Set[Hashtag] =
      body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
  }

  val akkaTag = Hashtag("#akka")

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("broadcast-tweets")
    implicit val ec = system.dispatcher
    implicit val mt = ActorMaterializer()

    val tweets = Source(List(
      Tweet(author = Author("Dave"), timestamp = 1, body = "blah blah #akka #second"),
      Tweet(author = Author("Bill"), timestamp = 1, body = "you dummy #akka #winning"),
      Tweet(author = Author("Teddy"), timestamp = 1, body = "you dummy #snack #not-akka")
    ))

    val writeAuthors = Sink.foreach(println)
    val writeHashTags = Sink.foreach(println)

    val g = RunnableGraph.fromGraph(GraphDSL.create(){ implicit b =>
      import akka.stream.scaladsl.GraphDSL.Implicits._

      val bt = b.add(Broadcast[Tweet](2))
      tweets ~> bt
      bt.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
      bt.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashTags
      ClosedShape
    })

    g.run(mt)
  }


}
