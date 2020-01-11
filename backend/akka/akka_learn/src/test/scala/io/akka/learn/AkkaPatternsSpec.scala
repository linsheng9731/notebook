package io.akka.learn

import akka.actor.ActorSystem
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.WordSpecLike
import akka.pattern.Patterns.ask
import akka.pattern._
import akka.util.Timeout
import io.akka.learn.patterns.AskActor
import io.akka.learn.patterns.AskActor.{Ping, Pong}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * AkkaPatternsSpec
  *
  * @author damon lin
  *         2019/11/14
  */
class AkkaPatternsSpec  extends ScalaTestWithActorTestKit with WordSpecLike {

  "Akka patterns " must {
    "ask an actor " in {
      val timeout = Timeout(50.seconds) // needed for `?` below
      val system = ActorSystem("system")
      import system.dispatcher
      val actorA = system.actorOf(AskActor.props)
      val actorB = system.actorOf(AskActor.props)
      val f: Future[Pong] = {
        for {
          x <- ask(actorA, Ping(), timeout).mapTo[Pong]
        } yield x
      }
      pipe(f) to actorB
//      Await.result(f, Duration.Inf)
    }
  }

}
