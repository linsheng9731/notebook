package io.linsheng9731.triggers

import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

/**
  * AllWindowTrigger
  *
  * @author damon lin
  *         2019/12/23
  */
class AllWindowTrigger extends Trigger[Object, GlobalWindow]{

  override def onElement(element: Object, timestamp: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.FIRE
  }

  override def onEventTime(time: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.FIRE
  }

  override def onProcessingTime(time: Long, window: GlobalWindow, ctx: Trigger.TriggerContext): TriggerResult = {
    TriggerResult.FIRE
  }

  override def clear(window: GlobalWindow, ctx: Trigger.TriggerContext): Unit = {

  }


}
