package io.linsheng9731.windows

import io.linsheng9731.triggers.AllWindowTrigger
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
/**
  * GlobalWindowStream
  *
  * @author damon lin
  *         2019/12/23
  */
object GlobalWindowStream {

  def main(args: Array[String]): Unit = {

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val nums = env.fromCollection(Seq(1,1,1,2))

    nums
      .map(n => (n, n+1))
      .keyBy(0)
      .window(GlobalWindows.create())
      .trigger(new AllWindowTrigger())
      .sum(0)
      .print().setParallelism(1)

    env.execute("Operations.")
  }

}
