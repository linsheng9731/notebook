//#full-example
package io.akka.learn

import akka.actor.{ActorSystem, Props}
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import io.akka.learn.mailbox.MyLogger
import org.scalatest.WordSpecLike

class AkkaMailboxSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "Priority mailbox" must {
    "return " in {
      val system = ActorSystem("system")
      val prio_dispatcher = system.actorOf(Props(classOf[MyLogger]).withDispatcher("prio_dispatcher"))
    }
  }

}
