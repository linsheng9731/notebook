package io.akka.learn

import akka.actor.{ActorSystem, Props}
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import io.akka.learn.starter.HotSwapActor
import org.scalatest.WordSpecLike

/**
  * AkkaHotSwapSpec
  *
  * @author damon lin
  *         2019/12/6
  */
class AkkaHotSwapSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "Akka actor " must {
    "stash and become " in {
      val system = ActorSystem("system")
      val actor = system.actorOf(Props[HotSwapActor])
      actor ! "hello"
      actor ! "write"
      actor ! "write"
      actor ! "close" // stash
      actor ! "open"  // open

    }
  }

}