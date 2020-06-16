package io.akka.learn.stream.cookbook

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.Attributes
import akka.stream.javadsl.Sink
import akka.stream.scaladsl.Source

/**
  * Attributes
  *
  * @author damon lin
  *         2020/6/1
  */
object AttributesLogLevelDemo {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("AttributesDemo")
    implicit val ec = system.dispatcher
    val source = Source(List(1,2,3, 0))
    source
      .map(r => 1 / r)
      .recover {
        case e: Exception =>
          // 恢复策略
          e.getMessage
      }
      .log("before") // 记录日志
      .withAttributes(Attributes.logLevels(onElement = Logging.WarningLevel, onFinish = Logging.InfoLevel, onFailure = Logging.ErrorLevel))
      .runWith(Sink.ignore())
  }

}
