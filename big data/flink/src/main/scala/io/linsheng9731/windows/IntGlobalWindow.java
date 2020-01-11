package io.linsheng9731.windows;

import io.linsheng9731.triggers.IntNeverTrigger;
import org.apache.flink.annotation.Internal;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

import java.util.Collection;
import java.util.Collections;


public class IntGlobalWindow extends WindowAssigner<Integer, GlobalWindow> {
    private static final long serialVersionUID = 1L;

    private IntGlobalWindow() {}

    @Override
    public Collection<GlobalWindow> assignWindows(Integer element, long timestamp, WindowAssignerContext context) {
        return Collections.singletonList(GlobalWindow.get());
    }

    @Override
    public Trigger<Integer, GlobalWindow> getDefaultTrigger(StreamExecutionEnvironment env) {
        return new IntNeverTrigger();
    }

    @Override
    public String toString() {
        return "GlobalWindows()";
    }

    /**
     * Creates a new {@code GlobalWindows} {@link WindowAssigner} that assigns
     * all elements to the same {@link GlobalWindow}.
     *
     * @return The global window policy.
     */
    public static IntGlobalWindow create() {
        return new IntGlobalWindow();
    }

    /**
     * A trigger that never fires, as default Trigger for GlobalWindows.
     */
    @Internal
    public static class NeverTrigger extends Trigger<Integer, GlobalWindow> {
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

    @Override
    public TypeSerializer<GlobalWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return new GlobalWindow.Serializer();
    }

    @Override
    public boolean isEventTime() {
        return false;
    }
}
