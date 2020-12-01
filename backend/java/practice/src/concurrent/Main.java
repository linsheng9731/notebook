package concurrent;

public class Main {

    static  MyArrayBlockingQueue<Integer> q = new MyArrayBlockingQueue<Integer>(100);
    public static void main(String[] args) {
        q.enqueue(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 100; i++) {
                        q.enqueue(i);
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e) {
                            // log
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    System.out.println(q.dequeue());
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        // log
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(1000000L);
        } catch (InterruptedException e) {
            // log
        }
    }
}
