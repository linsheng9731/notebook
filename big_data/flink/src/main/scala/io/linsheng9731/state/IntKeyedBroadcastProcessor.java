package io.linsheng9731.state;

import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.LinkedList;

public class IntKeyedBroadcastProcessor {

  public  KeyedBroadcastProcessFunction<java.lang.Integer, java.lang.Integer,java.lang.Integer,java.lang.Integer> function =
          new KeyedBroadcastProcessFunction<java.lang.Integer, java.lang.Integer,java.lang.Integer,java.lang.Integer>() {

//        private final MapStateDescriptor<Integer, Integer> stateDesc = new MapBroadcastStateDesc().desc;
              private final LinkedList<Integer> rules = new LinkedList<Integer>();

        @Override
        public void processBroadcastElement(Integer value, Context ctx, Collector<Integer> out) throws Exception {
//            ctx.getBroadcastState(stateDesc).put(value, value);
            rules.add(value);
        }

        @Override
        public void processElement(Integer value, ReadOnlyContext ctx, Collector<Integer> out) throws Exception {
            for (Integer rule: rules){
                if(rule == value) {
                    out.collect(value);
                }
            }
        }

    };
}

