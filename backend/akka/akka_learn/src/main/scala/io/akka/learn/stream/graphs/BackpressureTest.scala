package io.akka.learn.stream.graphs

import java.time.LocalTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}

/**
  * BackpressureTest
  *
  * @author damon lin
  *         2020/6/1
  */
object BackpressureTest {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("BackpressureTest")
    implicit val ec = scala.concurrent.ExecutionContext.global

    val dataFmt = DateTimeFormatter.ofPattern("hh:mm:ss:SSS")

    def getTime = {
      LocalTime.now().format(dataFmt)
    }

    val source = Source(1 to 30)

    // 模拟计算缓慢的操作
    val flow = Flow[Int].map { element =>
      Thread.sleep(1000)
      println(s"Flow1: $element $getTime")
      element
    }

    // 模拟计算缓慢的操作
    val sink = Sink.foreach[Int](element => {
      Thread.sleep(1000)
      println(s"Sink: $element $getTime")
    })

    println(s"Start at $getTime")

    val result = source
      .via(flow.buffer(10, OverflowStrategy.backpressure))
      .async
      .runWith(sink)

    result.onComplete(_ => {
      println(s"End at $getTime")
      system.terminate()
    })
  }

}
