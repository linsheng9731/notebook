package thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForJoinTest {

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool(4);
        Fibonacci f0  = new Fibonacci(30);
        int result = fjp.invoke(f0);
        System.out.println(result);
    }

}

class Fibonacci extends RecursiveTask<Integer> {
    int n;
    Fibonacci(int n) {
        this.n = n;
    }
    @Override
    protected Integer compute() {
        if(n<=1) {
            return n;
        }
        System.out.println("compute :" + n);
        Fibonacci f1 = new Fibonacci(n-1);
        Fibonacci f2 = new Fibonacci(n-2);
        return f1.compute() + f2.compute();
    }
}