package io.linsheng9731.dataStream

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
/**
  * DataStreamAPI
  *
  * @author damon lin
  *         2020/3/15
  */
object DataStreamAPI {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val data = Seq(1,2,3,4,5,6,7,8)
    val dataStream = env.fromCollection(data)
    dataStream
      .map(_ + 1)
      .filter(e => e > 0)
      .split(e => if(e%2==0) List("even") else List("ood"))
      .select("even")
      .union(dataStream.map(_ / 10))
      .print()
      .setParallelism(2)
    env.execute("Data Stream api...")
  }

}
