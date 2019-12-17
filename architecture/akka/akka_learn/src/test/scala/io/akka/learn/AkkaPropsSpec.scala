package io.akka.learn

import akka.actor.{ActorSystem, Props}
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import io.akka.learn.mailbox.MyLogger
import io.akka.learn.props.IllegalProps.{DefaultValueActor, MyValueClass, ValueActor}
import org.scalatest.WordSpecLike

/**
  * AkkaPropsSpec
  *
  * @author damon lin
  *         2019/11/14
  */
class AkkaPropsSpec  extends ScalaTestWithActorTestKit with WordSpecLike {

  "Akka props " must {
    "throw exception " in {
      val system = ActorSystem("system")
//      val valueClassProp = Props(classOf[ValueActor], MyValueClass(5)) // Unsupported
//      val defaultValueProp1 = Props(classOf[DefaultValueActor], 2) // Unsupported
    }
  }

}
