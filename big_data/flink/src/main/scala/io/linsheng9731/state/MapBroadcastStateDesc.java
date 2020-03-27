package io.linsheng9731.state;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class MapBroadcastStateDesc {

    MapStateDescriptor<Integer, Integer> desc;

    public MapBroadcastStateDesc(){
        desc = new MapStateDescriptor<Integer, Integer>(
                "RulesBroadcastState",
                BasicTypeInfo.INT_TYPE_INFO,
                TypeInformation.of(new TypeHint<Integer>(){}));

    }


}
