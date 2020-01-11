package io.linsheng9731.timestamp;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * 根据数据流的特征生成水印
 * 根据 integer 的数值赋予 时间戳和水印
 */
public class TestPunctuatedWatermarkAssigner implements AssignerWithPunctuatedWatermarks<Integer> {
    private static final long serialVersionUID = 1L;

    @Nullable
    public Watermark checkAndGetNextWatermark(Integer lastElement, long extractedTimestamp) {
        return new Watermark(extractedTimestamp);
    }

    public long extractTimestamp(Integer element, long previousElementTimestamp) {
        return Long.valueOf(element);
    }
}
