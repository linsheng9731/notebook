## 问：为什么ConcurrentHashMap可以在高并发的情况下比HashMap更为高效？

答：主要是ConcurrentHashMap在实现时采用的拆分锁（事务分离，避免锁住大量数据），使用final、volatile。
```
 static final class HashEntry<K,V> {  
     final K key;  
     final int hash;  
     volatile V value;  
     final HashEntry<K,V> next;  
 } 
```
除了value不是final的，其它值都是final的，这意味着不能从hash链的中间或尾部添加或删除节点，因为这需要修改next 引用值，所有的节点的修改只能从头部开始。为了确保读操作能够看到最新的值，将value设置成volatile，这避免了加锁。

## 问：ArrayBlockingQueue、LinkedBlockingQueue的区别是什么？

回答：LinkedBlockingQueue和ArrayBlockingQueue都是可阻塞的队列，内部都是使用ReentrantLock和Condition来保证生产和消费的同步；当队列为空，消费者线程被阻塞；当队列装满，生产者线程被阻塞；LinkedBlockingQueue中的锁是分离的，生产者的锁PutLock，消费者的锁takeLock。而ArrayBlockingQueue生产者和消费者使用的是同一把锁；LinkedBlockingQueue内部维护的是一个链表结构；在生产和消费的时候，需要创建Node对象进行插入或移除，大批量数据的系统中，其对于GC的压力会比较大。而ArrayBlockingQueue内部维护了一个数组，在生产和消费的时候，是直接将枚举对象插入或移除的，不会产生或销毁任何额外的对象实例。

