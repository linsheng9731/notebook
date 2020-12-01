package ArrayBlockingQueue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 底层基于数组实现的线程安全的阻塞队列
 */
public class MyArrayBlockingQueue<E> {

    // 底层存储数组
    Object items[];
    // 入队游标
    int putIndex;
    // 出队游标
    int takeIndex;
    // 实际队列元素个数
    int count = 0;
    // 容量
    int cap;
    // 可重入锁
    ReentrantLock lock = new ReentrantLock();
    // 不为空条件变量
    Condition notEmpty = lock.newCondition();
    // 未填满条件变量
    Condition notFull = lock.newCondition();

    MyArrayBlockingQueue(int cap) {
        this.items = new Object[cap];
        this.cap = cap;
        this.putIndex = 0;
        this.takeIndex = 0;
    }

    public void enqueue(E item) {
        lock.lock();
        try {
            while (count >= cap) {
                notFull.await();
            }
            items[putIndex] = item;
            putIndex++;
            count++;
            if(putIndex==cap) {
                putIndex=0;
            }
        } catch (InterruptedException e) {
            // log
        }
        finally {
            lock.unlock();
        }
    }

    public E dequeue() {
        lock.lock();
        try {
            while(count==0) {
                notEmpty.await();
            }
            E e = (E)items[takeIndex];
            takeIndex++;
            count--;
            if(takeIndex==cap) {
                takeIndex=0;
            }
            return e;
        } catch (InterruptedException e) {
            // log
            return null;
        }
        finally {
            lock.unlock();
        }
    }

}



