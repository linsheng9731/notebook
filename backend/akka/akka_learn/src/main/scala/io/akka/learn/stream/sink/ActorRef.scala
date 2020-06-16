package io.akka.learn.stream.sink

import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

/**
  * ActorRef
  *
  * @author damon lin
  *         2020/5/28
  */
object ActorRef {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("sink")
    implicit val ec = system.dispatcher

    val source = Source(1 to 3)
    val actor = system.actorOf(Props[MyActor])
    val last = source.runWith(Sink.actorRef(actor, onCompleteMessage = "done", onFailureMessage = (t: Throwable) => print(t)))
  }

}

class MyActor extends Actor {
  override def receive: Receive = {
    case i: Int => println(i)
  }
}
