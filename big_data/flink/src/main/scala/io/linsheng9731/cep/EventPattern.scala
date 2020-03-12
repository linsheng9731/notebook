package io.linsheng9731.cep

import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.TimeCharacteristic
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

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 依据事件时间处理
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 迟到数据 tag
    val lateData = new OutputTag[Event]("late")

    // 输入源
    val dataStream = env.addSource(new DataEventTimeSourceFunction(Event.data))

    val keyedStream = dataStream.keyBy(v => v.csid)

    val skipStrategy = AfterMatchSkipStrategy.skipPastLastEvent()
    val outputTag = OutputTag[Event]("side-output")


    // 做了 A 两秒内没做 B
//    val pattern1 = Pattern.begin[Event]("start", skipStrategy).where(_.rule == "addCart")
//      .followedBy("end").where(_.rule == "pay")
//      .within(Time.seconds(2))

    val pattern1 = Pattern.begin[Event]("start", skipStrategy)
    val flow = NotFinishedFlow.flow
    for(e <- flow.startEvents) {
      pattern1.where { pe =>
         check(e, pe)
        }
    }
    val pattern2 = Pattern.begin[Event]("end", skipStrategy)
    for(e <- flow.notFinishEvents) {
      pattern2.where{ pe =>
        check(e, pe)
      }
    }
    val finalPattern = pattern1.followedBy(pattern2).within(Time.milliseconds(flow.within))

//    // 做了 A or B or C
//    val pattern2 = Pattern.begin[Event]("start", skipStrategy)
//      .where(_.rule == "addCart")
//      .or(_.rule == "look")
//      .followedBy("end")
//      .where(_.rule == "pay")

    // 1,1,1,1 3,3,4
    val patternStream1 = CEP.pattern(keyedStream, finalPattern)
//    val patternStream2 = CEP.pattern(keyedStream, pattern2)
    val result = patternStream1.select(outputTag){
      (timeoutPattern: Map[String, Iterable[Event]], timestamp: Long) =>
        println("timeout: " + timeoutPattern)
        Event("",1,1)
    } {
      selectPattern: Map[String, Iterable[Event]] =>
        println("selectPattern : " + selectPattern)
    }

    result.getSideOutput(outputTag)


    //    patternStream2.select(x =>
//      println("pattern2 find: " + x)
//    )

    env.execute("EventPattern")

  }

  private def check(e: Condition, pe: Event) = {
    val cond1 = pe.rule == e.rule
    if (e.filterOp == "and") {
      val cond2 = e.filters.map { v =>
        val key = v.dim
        val op = v.op
        val values = v.values
        values.contains(pe.variables.getOrElse(key, ""))
      }.reduce((l, r) => l && r)
      cond1 && cond2
    } else {
      val cond2 = e.filters.map { v =>
        val key = v.dim
        val op = v.op
        val values = v.values
        values.contains(pe.variables.getOrElse(key, ""))
      }.reduce((l, r) => l || r)
       cond1 && cond2
    }
  }
}




