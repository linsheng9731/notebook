package io.linsheng9731.aggregates

import io.linsheng9731.triggers.AllWindowTrigger
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows

/**
  * AggregateStream
  *
  * @author damon lin
  *         2019/12/23
  */
object AggregateStream {

  def main(args: Array[String]): Unit = {
    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val nums = env.fromCollection(Seq(1,1,1,2))

    nums
      .map(n => (n, n+1)) // (1, 2),(1, 2),(1, 2),(2, 3)
      .keyBy(0) // (1, 2),(1, 2),(1, 2) || (2, 3)
      .window(GlobalWindows.create())
      .trigger(new AllWindowTrigger())
      .aggregate(new AverageAggregate())
      .print().setParallelism(1)

    env.execute("Operations.")
  }

}
