package thread;

/**
 * 模拟多线程操作同一资源
 */
public class ThreadTest {

    static int[] queue = new int[100];
    static int size = 0;

    public static void main(String[] args) {
        // 第一个线程填充 1
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    // 加了锁不会出现问题
                    synchronized (this) {
                        queue[size % 100] = 1;
                        size++;
                        if(size>99) {
                            size=99;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        });

        // 第二个线程填充 0
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    synchronized (this) {
                        queue[size % 100] = 0;
                        size--;
                        if(size<1) {
                            size=1;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        });

        // 第三个线程打印资源
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                   for(int element: queue) {
                       System.out.print(element);
                   }
                    System.out.println("=========================");
                   try {
                       Thread.sleep(10000);
                   } catch (Exception e) {

                   }
                }
            }
        });

        // 在不加锁的情况下线程 1 的结果会被 线程 2 覆盖 造成类似 1011 交替结果
        t1.start();
        t2.start();
        t3.start();
    }
}
