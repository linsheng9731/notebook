package io.linsheng9731.connectors

import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala._
import scala.collection.JavaConverters._

/**
  * Iterations
  *
  * @author damon lin
  *         2019/12/18
  */
object Iterations {

  def main(args: Array[String]): Unit = {

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val someIntegers: DataStream[Long] = env.generateSequence(1, 10)

    val iteratedStream = someIntegers.iterate(
      iteration => {
        val minusOne = iteration.map( v => v - 1)
        val stillGreaterThanZero = minusOne.filter (v => v > 0)
        val lessThanZero = minusOne.filter(v => v <= 0)
        (stillGreaterThanZero, lessThanZero) // (feedback, output)
      }
    )

    val result = DataStreamUtils.collect(iteratedStream.setParallelism(1).javaStream).asScala
    while(result.hasNext) {
      println("next:")
      println(result.next())
    }
  }

}
