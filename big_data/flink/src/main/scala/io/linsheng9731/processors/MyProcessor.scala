package io.linsheng9731.processors

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

/**
  * MyProcessor
  *
  * @author damon lin
  *         2019/12/23
  */
class MyProcessor extends ProcessWindowFunction[(Int, Int), Int, Int, GlobalWindow ] {

  override def process(key: Int, context: Context, elements: Iterable[(Int, Int)], out: Collector[Int]): Unit = {
    if(key == 1) {
      println(elements)
      out.collect(elements.count(_._2 > 0))
    }
  }

}
