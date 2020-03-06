package io.common

import io.linsheng9731.timestamp.EventTimeSourceFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import util.TestKeySelector
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

/**
  * Entry
  *
  * @author damon lin
  *         2020/3/5
  */
object Entry {

  // get the execution environment
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  // 依据事件时间处理
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

  // 迟到数据 tag
  val lateData = new OutputTag[Integer]("late")

  val data = Seq(new Integer(1),new Integer(1), new Integer(1), new Integer(1), new Integer(3), new Integer(3), new Integer(4))
  // 输入源
  val dataStream = env.addSource(new EventTimeSourceFunction[Integer](data))

  // 当 watermark 大于当前所有元素的 timestamp 触发计算
  val reduceDataStream = dataStream
    .keyBy(new TestKeySelector()) // 1,1,1,1 3,3
    .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
    .sideOutputLateData(lateData)
    .reduce((l, r) => l + r) // 4, 6

}
