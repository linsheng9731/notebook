package io.linsheng9731.timestamp

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.api.scala._

/**
  * EventTimeSourceFunction
  *
  * @author damon lin
  *         2019/12/25
  */
class EventTimeSourceFunction[Integer](data: Seq[Integer]) extends SourceFunction[Integer]{

  override def run(ctx: SourceFunction.SourceContext[Integer]): Unit = {
    data.foreach { d =>
      ctx.collectWithTimestamp(d, d.asInstanceOf[Int].toLong)
      ctx.emitWatermark(new Watermark(d.asInstanceOf[Int].toLong))
    }

  }

  override def cancel(): Unit = ???

}
