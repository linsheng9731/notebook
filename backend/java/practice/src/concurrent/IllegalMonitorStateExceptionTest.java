package concurrent;

import java.util.Deque;
import java.util.LinkedList;

public class IllegalMonitorStateExceptionTest {
    private final static Object lockGet = new Object();
    private final static Object lockPut = new Object();
    private static final Integer MAX_VALUE = 10;
    private static final Deque<String> queue = new LinkedList<>();

    public static String get() throws Exception {
        synchronized (lockGet) {
            if (queue.size() > 0) {
                String ele = queue.getFirst();
                // 当前线程指持有 lockGet 监视器 并不持有 lockPut
                // 所有会抛出 IllegalMonitorStateException 异常
                lockPut.notifyAll();
                queue.removeFirst();
                return ele;
            }
            while (queue.size() == 0) {
                lockGet.wait();
            }
            lockPut.notifyAll();
            return queue.removeFirst();
        }
    }

    public static void put(String ele) throws Exception {
        synchronized (lockPut) {
            if (queue.size() < MAX_VALUE) {
                queue.addLast(ele);
                lockGet.notifyAll();
                return;
            }
            while (queue.size() == MAX_VALUE) {
                lockPut.wait();
            }
            queue.addLast(ele);
            lockGet.notifyAll();
        }
    }

    public static void main(String[] args) throws Exception {
        int i = 20;
        while (i >= 0) {
            new Thread(new GetThread()).start();
            Thread.sleep(5000);
            new Thread(new PutThread()).start();
            i--;
        }
        Thread.sleep(1000000);
    }

    private static class PutThread implements Runnable {
        @Override
        public void run() {
            try {
                put("hi");
            } catch (Exception e) {
                System.out.println("Catch exception when put: " + e.getMessage());
            }
        }
    }

    private static class GetThread implements Runnable {
        @Override
        public void run() {
            try {
                String s = get();
                System.out.println(s);
            } catch (Exception e) {
                System.out.println("Catch exception when get: " + e.getMessage());
            }
        }
    }
}

