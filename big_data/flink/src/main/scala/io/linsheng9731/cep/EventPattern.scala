package io.linsheng9731.cep

import io.common.Entry
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.windowing.time.Time
import Entry._
import org.apache.flink.streaming.api.scala._
import scala.collection.Map

/**
  * EventPattern
  *
  * @author damon lin
  *         2020/3/5
  */
object EventPattern {

  def main(args: Array[String]): Unit = {

    val outputTag = OutputTag[Integer]("side-output")

    // 做了 A 两秒内没做 B
    val pattern1 = Pattern.begin[Integer]("login").where(_ == 3)
      .followedBy("00").where(_ == 0)
      .within(Time.seconds(2))

    // 做了 A or B or C
    val pattern2 = Pattern.begin[Integer]("login")
      .where(_ == 0)
      .or(_ == -1)
    pattern2.or(_ ==4)

    // 1,1,1,1 3,3,4
    val patternStream1 = CEP.pattern(dataStream, pattern1)
    val patternStream2 = CEP.pattern(dataStream, pattern2)
    val resutl = patternStream1.select(outputTag){
      (timeoutPattern: Map[String, Iterable[Integer]], timestamp: Long) =>
        println("timeout: " + timeoutPattern)
        -1
    } {
      selectPattern: Map[String, Iterable[Integer]] =>
        println("select: " + selectPattern)
    }

    patternStream2.select(x => println("pattern2 find: " + x))

    resutl.getSideOutput(outputTag)

    env.execute("EventPattern")

  }

}
