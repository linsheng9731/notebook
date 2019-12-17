package io.akka.learn.props

import akka.actor.Actor

/**
  * IllegalProps
  *
  * @author damon lin
  *         2019/11/14
  */
object IllegalProps {
  case class MyValueClass(v: Int) extends AnyVal

  class ValueActor(value: MyValueClass) extends Actor {
    def receive = {
      case multiplier: Long => sender() ! (value.v * multiplier)
    }
  }

  class DefaultValueActor(a: Int, b: Int = 5) extends Actor {
    def receive = {
      case x: Int => sender() ! ((a + x) * b)
    }
  }
}

