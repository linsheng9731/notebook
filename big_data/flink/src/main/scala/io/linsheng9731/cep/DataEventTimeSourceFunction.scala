package io.linsheng9731.cep

import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.watermark.Watermark

/**
  * DataEventTimeSourceFunction
  *
  * @author damon lin
  *         2019/12/25
  */
class DataEventTimeSourceFunction(data: Seq[Event]) extends SourceFunction[Event]{

  override def run(ctx: SourceFunction.SourceContext[Event]): Unit = {
    data.foreach { d =>
      ctx.collectWithTimestamp(d, d.stm)
      ctx.emitWatermark(new Watermark(d.stm))
    }

  }

  override def cancel(): Unit = ???

}
