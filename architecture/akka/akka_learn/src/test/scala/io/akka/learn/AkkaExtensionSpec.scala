package io.akka.learn

import akka.actor.{ActorSystem, Props}
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import io.akka.learn.extension.{ActorWithExtension, Counter}
import org.scalatest.WordSpecLike

/**
  * AkkaExtensionSpec
  *
  * @author damon lin
  *         2019/11/18
  */
class AkkaExtensionSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "Extension " must {
    "return " in {
      val system = ActorSystem("system")
      val actor = system.actorOf(Props[ActorWithExtension](ActorWithExtension("a")))
      val actor2 = system.actorOf(Props[ActorWithExtension](ActorWithExtension("b")))
      actor2 ! Counter
      val actor3 = system.actorOf(Props[ActorWithExtension](ActorWithExtension("a")))
      actor3 ! Counter

      actor ! Counter
      actor ! Counter
      actor ! Counter
    }
  }

}

