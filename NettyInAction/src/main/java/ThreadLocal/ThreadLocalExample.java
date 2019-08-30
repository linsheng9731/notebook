package ThreadLocal;

public class ThreadLocalExample {

    private static class MyRunnable implements Runnable {

        private ThreadLocal<Integer> local1  = new ThreadLocal<Integer>();
        private ThreadLocal<Integer> local2 = new ThreadLocal<Integer>();
        private ThreadLocal<Integer> local3 = new ThreadLocal<Integer>();

        public void run() {
            local1.set(1);
            local2.set(2);
            local3.set(3);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            System.out.println(local1.get());
            System.out.println(local2.get());
            System.out.println(local3.get());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyRunnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable);
        thread1.start();

        thread1.join();
    }
}
