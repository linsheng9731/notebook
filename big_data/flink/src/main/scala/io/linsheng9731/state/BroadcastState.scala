package io.linsheng9731.state

import io.common.StreamHelper._
import org.apache.flink.streaming.api.scala._

/**
  * BroadcastState
  *
  * @author damon lin
  *         2020/3/19
  */
object BroadcastState {

  def main(args: Array[String]): Unit = {
//    val ruleStateDesc = new MapBroadcastStateDesc().desc
    val ruleBroadcastStream = dataStream.broadcast()
    val output = keyedStream
      .connect(ruleBroadcastStream)
      .process(new IntKeyedBroadcastProcessor().function)
      .print()
      .setParallelism(1)
    val r = keyedStream.reduce((l, r) =>  l+ r)
      r.map(v=> v). print().setParallelism(1)
    env.execute("Broadcast stream test...")
  }

}


