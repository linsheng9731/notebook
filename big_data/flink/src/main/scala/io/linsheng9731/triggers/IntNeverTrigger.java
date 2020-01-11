package io.linsheng9731.triggers;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;


public  class IntNeverTrigger extends Trigger<Integer, GlobalWindow> {
    private static final long serialVersionUID = 1L;

    @Override
    public TriggerResult onElement(Integer element, long timestamp, GlobalWindow window, TriggerContext ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onEventTime(long time, GlobalWindow window, TriggerContext ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public TriggerResult onProcessingTime(long time, GlobalWindow window, TriggerContext ctx) {
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(GlobalWindow window, TriggerContext ctx) throws Exception {}

    @Override
    public void onMerge(GlobalWindow window, OnMergeContext ctx) {
    }
}
