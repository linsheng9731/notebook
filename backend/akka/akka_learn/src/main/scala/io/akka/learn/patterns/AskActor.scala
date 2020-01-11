package io.akka.learn.patterns

import akka.actor.{Actor, Props}
import io.akka.learn.patterns.AskActor.{Ping, Pong}

/**
  * AskActor
  *
  * @author damon lin
  *         2019/12/3
  */
class AskActor extends Actor {

  override def receive: Receive = {
    case _: Ping =>
      println(this.self.path.address.toString + " receive Ping .")
      sender() ! Pong()
    case _ : Pong =>
      println(this.self.path.address.toString + " receive Pong .")
      sender() ! Ping()
  }

}

object AskActor {

  def props = Props(new AskActor)

  case class Ping()
  case class Pong()
}
