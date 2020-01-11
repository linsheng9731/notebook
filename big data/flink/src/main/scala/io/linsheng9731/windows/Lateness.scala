package io.linsheng9731.windows

import io.linsheng9731.timestamp.TestPunctuatedWatermarkAssigner
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import util.{TestKeySelector, TestListResultSink}

/**
  * Lateness
  *
  * @author damon lin
  *         2019/12/24
  */
object Lateness {

  def main(args: Array[String]): Unit = {

    val resultSink = new TestListResultSink[String]() // Array list 包装的 sink
    val resultSink_ = new TestListResultSink[String]() // Array list 包装的 sink
    val lateResultSink = new TestListResultSink[Integer]()

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 依据事件时间处理
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // 输入源 1 2 5 4 3 3 6
    // 1, w(1), 2, w(2), 5, w(5), 4, w(4), 3(late 2), 3(late 2), 6, w(6)
    val dataStream = env.fromCollection(Seq(new Integer(1),new Integer(2), new Integer(5), new Integer(4), new Integer(3), new Integer(3), new Integer(6)))

    // 迟到数据 tag
    val lateData = new OutputTag[Integer]("late")

    // 将 key 和 value 拼接成字符串输出
    val operator = new WindowFunction[Integer, String, Integer, TimeWindow]() {
      @throws(classOf[Exception])
      override def apply(key: Integer, window: TimeWindow, input: Iterable[Integer], out: Collector[String]): Unit = {
        for (v <- input) {
          out.collect(String.valueOf(key) + "-" + String.valueOf(v))
        }
      }
    }

    // 当 watermark 大于当前所有元素的 timestamp 触发计算
    val windowOperator = dataStream
      .assignTimestampsAndWatermarks(new TestPunctuatedWatermarkAssigner()) // 根据数值生成 timestamp 和 watermark
      .keyBy(new TestKeySelector())
      .timeWindow(Time.milliseconds(1), Time.milliseconds(1)) // 滑动时间窗口 1 毫秒宽 间隔 1 毫秒
      .allowedLateness(Time.milliseconds(2)) // 允许 2 毫秒以内迟到时间 timestamp + lateness <= watermark
      .sideOutputLateData(lateData) // 单独输出迟到的数据
      .apply(operator)

    windowOperator
      .addSink(resultSink)

    windowOperator
      .addSink(resultSink_)

    windowOperator
      .getSideOutput(lateData)
      .addSink(lateResultSink) // 将迟到的 sideOutput 输入到单独的 sink 中

    env.execute("Operations.")
    println(resultSink.getSortedResult) // [1-1, 2-2, 4-4, 5-5, 6-6]
    println(resultSink_.getSortedResult) // [1-1, 2-2, 4-4, 5-5, 6-6]
    println(lateResultSink.getSortedResult) // [3, 3]

  }

}
