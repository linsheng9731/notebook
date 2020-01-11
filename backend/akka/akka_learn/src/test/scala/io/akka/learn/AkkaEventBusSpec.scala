package io.akka.learn

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.{ActorSystem, Props}
import io.akka.learn.event.{AllKindsOfMusic, Electronic, Jazz, Listener}
import org.scalatest.WordSpecLike

class AkkaEventBusSpec extends ScalaTestWithActorTestKit with WordSpecLike {

  "Event bus" must {
    "return " in {
      val system = ActorSystem("system")
      val jazzListener = system.actorOf(Props[Listener])
      val musicListener = system.actorOf(Props[Listener])
      system.eventStream.subscribe(jazzListener, classOf[Jazz])
      system.eventStream.subscribe(musicListener, classOf[AllKindsOfMusic])

      // only musicListener gets this message, since it listens to *all* kinds of music:
      system.eventStream.publish(Electronic("Electronic"))

      // jazzListener and musicListener will be notified about Jazz:
      system.eventStream.publish(Jazz("Jazz"))
    }
  }

}
