package util;

import org.apache.flink.api.java.functions.KeySelector;

/**
 * Integer 数值本身作为 key
 */
public class TestKeySelector implements KeySelector<Integer, Integer> {
    private static final long serialVersionUID = 1L;

    public Integer getKey(Integer value) throws Exception {
        return value;
    }
}