package string;

import org.openjdk.jmh.annotations.Benchmark;

public class StringConnectBenchmark {

    private void print(String var) {
        System.out.println(var);
    }

    @Benchmark
    public void testStringBuilder() {
        print(new StringBuilder().append(1).append(2).append(3).toString());
    }

}