package io.linsheng9731.timestamp

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import util.{TestKeySelector, TestListResultSink}

/**
  * EventTimeSourceStream
  *
  * @author damon lin
  *         2019/12/25
  */
object EventTimeSourceStream {

  def main(args: Array[String]): Unit = {
    val firstResultSink = new TestListResultSink[Integer]()
    val lateResultSink = new TestListResultSink[Integer]()

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 依据事件时间处理
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 迟到数据 tag
    val lateData = new OutputTag[Integer]("late")

    val data = Seq(new Integer(1),new Integer(1), new Integer(1), new Integer(1), new Integer(3), new Integer(3))
    // 输入源
    val dataStream = env.addSource(new EventTimeSourceFunction[Integer](data))

    // 当 watermark 大于当前所有元素的 timestamp 触发计算
    val windowOperator = dataStream
      .keyBy(new TestKeySelector()) // 1,1,1,1 3,3
      .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
      .sideOutputLateData(lateData)
      .reduce((l, r) => l + r) // 4, 6


    windowOperator
      .addSink(firstResultSink)

    windowOperator
      .getSideOutput(lateData)
      .addSink(lateResultSink) // 将迟到的 sideOutput 输入到单独的 sink 中

    env.execute("Operations.")
    println(firstResultSink.getSortedResult)
    println("Late data:")
    println(lateResultSink.getSortedResult)
  }

}
