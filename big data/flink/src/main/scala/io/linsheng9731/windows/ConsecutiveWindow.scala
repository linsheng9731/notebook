package io.linsheng9731.windows

import io.linsheng9731.timestamp.TestPunctuatedWatermarkAssigner
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import util.{TestKeySelector, TestListResultSink}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.scala.function.{ProcessAllWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.scala._

/**
  * ConsecutiveWindow
  *
  * @author damon lin
  *         2019/12/25
  */
object ConsecutiveWindow {

  def main(args: Array[String]): Unit = {

    val resultSink = new TestListResultSink[String]() // Array list 包装的 sink
    val firstResultSink = new TestListResultSink[Integer]()
    val lateResultSink = new TestListResultSink[Integer]()

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 依据事件时间处理
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 迟到数据 tag
    val lateData = new OutputTag[Integer]("late")

    // 输入源
    val dataStream = env.fromCollection(Seq(new Integer(1),new Integer(1), new Integer(1), new Integer(1), new Integer(3), new Integer(3)))

    def reducer(l: Integer, r: Integer): Integer = {
//      println(l)
      l + r
    }

    // 当 watermark 大于当前所有元素的 timestamp 触发计算
    val windowOperator = dataStream
      .assignTimestampsAndWatermarks(new TestPunctuatedWatermarkAssigner()) // 根据数值生成 timestamp 和 watermark
      .keyBy(new TestKeySelector()) // 1,1,1,1 3,3
      .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
      .sideOutputLateData(lateData)
      .reduce((l, r) => reducer(l, r)) // 4, 6

    val processAllOperator = new ProcessAllWindowFunction[Integer, String, TimeWindow] {
      override def process(context: Context, elements: Iterable[Integer], out: Collector[String]): Unit = {
        out.collect("max: " + elements.max)
      }
    }

    windowOperator
      .windowAll(TumblingEventTimeWindows.of(Time.milliseconds(1)))
      .process(processAllOperator) // -4, -6
      .addSink(resultSink)

    windowOperator
      .addSink(firstResultSink)

    windowOperator
      .getSideOutput(lateData)
      .addSink(lateResultSink) // 将迟到的 sideOutput 输入到单独的 sink 中

    env.execute("Operations.")
    println(firstResultSink.getSortedResult)
    println("Result data:")
    println(resultSink.getSortedResult)
    println("Late data:")
    println(lateResultSink.getSortedResult)

  }
}
