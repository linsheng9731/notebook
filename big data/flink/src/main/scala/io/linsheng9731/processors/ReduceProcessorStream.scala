package io.linsheng9731.processors

import io.linsheng9731.triggers.AllWindowTrigger
import io.linsheng9731.windows.IntGlobalWindow
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
  * ReduceProcessorStream
  *
  * @author damon lin
  *         2019/12/23
  */
object ReduceProcessorStream {

  def main(args: Array[String]): Unit = {
    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime)

    val nums = env.fromCollection(Seq(new Integer(1), new Integer(2), new Integer(3), new Integer(4)))

    nums
      .windowAll(IntGlobalWindow.create()) // window for non-key stream
      .trigger(new AllWindowTrigger())
      .reduce((l,r) => { if(l < r) r else l })
      .process(
        new ProcessFunction[Integer, Integer](){
          override def processElement(value: Integer, ctx: ProcessFunction[Integer, Integer]#Context, out: Collector[Integer]): Unit = {
            out.collect(value)
          }
        }
      )
      .print()
      .setParallelism(1)


    env.execute("Operations.")
  }

  case class Element(id: Int, name: String)

}
