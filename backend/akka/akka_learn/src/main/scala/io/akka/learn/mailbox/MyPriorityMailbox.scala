package io.akka.learn.mailbox

import akka.actor.{ActorSystem, PoisonPill}
import akka.dispatch.{PriorityGenerator, UnboundedStablePriorityMailbox}
import com.typesafe.config.Config

/**
  * MyPriorityMailbox
  *
  * @author damon lin
  *         2019/11/13
  */
class MyPriorityMailbox(settings: ActorSystem.Settings, config: Config)
extends UnboundedStablePriorityMailbox(
  PriorityGenerator {
    case 'highpriority => 0
    case 'lowpriority => 2
    case  PoisonPill => 3
    case _ => 1
  }
)

object MyPriorityMailbox {
  def apply(settings: ActorSystem.Settings, config: Config): MyPriorityMailbox = new MyPriorityMailbox(settings, config)
}