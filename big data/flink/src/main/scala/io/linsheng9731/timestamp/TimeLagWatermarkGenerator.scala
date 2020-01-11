package io.linsheng9731.timestamp

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

/**
  * TimeLagWatermarkGenerator
  * 周期性生成水印
  *
  * @author damon lin
  *         2019/12/25
  */
class TimeLagWatermarkGenerator extends AssignerWithPeriodicWatermarks[Integer] {

  val maxTimeLag = 5000L // 5 seconds

  override def extractTimestamp(element: Integer, previousElementTimestamp: Long): Long = {
    element.toLong
  }

  // 每隔一段时间会被系统调用 时间间隔可以设置
  override def getCurrentWatermark(): Watermark = {
    // return the watermark as current time minus the maximum time lag
    new Watermark(System.currentTimeMillis() - maxTimeLag)
  }
}