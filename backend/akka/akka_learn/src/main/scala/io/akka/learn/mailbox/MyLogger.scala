package io.akka.learn.mailbox

import akka.actor.{Actor, PoisonPill}
import akka.event.{Logging, LoggingAdapter}

/**
  * MyLogger
  *
  * @author damon lin
  *         2019/11/13
  */
class MyLogger extends Actor {
  val log: LoggingAdapter = Logging(context.system, this)

  self ! 'lowpriority
  self ! 'lowpriority
  self ! 'highpriority
  self ! 'pigdog
  self ! 'pigdog2
  self ! 'pigdog3
  self ! 'highpriority
  self ! PoisonPill

  def receive = {
    case x => log.info(x.toString)
  }
}