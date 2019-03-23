## java 线程的生命周期

关于线程生命周期的不同状态，在 Java 5 以后，线程状态被明确定义在其公共内部枚举类型 java.lang.Thread.State 中，分别是：
1. 新建（NEW），表示线程被创建出来还没真正启动的状态，可以认为它是个 Java 内部状态。
2. 就绪（RUNNABLE），表示该线程已经在 JVM 中执行，当然由于执行需要计算资源，它可能是正在运行，也可能还在等待系统分配给它 CPU 片段，在就绪队列里面排队。
3. 阻塞（BLOCKED），这个状态和我们前面两讲介绍的同步非常相关，阻塞表示线程在等待 Monitor lock。比如，线程试图通过synchronized 去获取某个锁，但是其他线程已经独占了，那么当前线程就会处于阻塞状态。
4. 等待（WAITING），表示正在等待其他线程采取某些操作。一个常见的场景是类似生产者消费者模式，发现任务条件尚未满足，就让当前消费者线程等待（wait），另外的生产者线程去准备任务数据，然后通过类似 notify 等动作，通知消费线程可以继续工作了。Thread.join() 也会令线程进入等待状态。
5. 计时等待（TIMED_WAIT），其进入条件和等待状态类似，但是调用的是存在超时条件的方法，比如 wait 或 join 等方法的指定超时版本。
6. 终止（TERMINATED），不管是意外退出还是正常执行结束，线程已经完成使命，终止运行，也有人把这个状态叫作死亡

## 什么是守护线程？

有的时候应用中需要一个长期驻留的服务程序，但是不希望其影响应用退出，就可以将其设置为守护线程。守护线程最典型的应用就是GC(垃圾回收器)，
```
Thread daemonThread = new Thread();
daemonThread.setDaemon(true);
daemonThread.start();
```

## Volatitle 的作用和基本原理是什么？

Volatile 两大作用：
1. 保证内存可见性
2. 防止指令重排
但是并不保证操作的原子性。

Volatitle 利用内存屏障实现多线程之间的变量可见性。
- 对该变量的写操作之后，编译器会插入一个写屏障。
- 对该变量的读操作之前，编译器会插入一个读屏障。
内存屏障能够在类似变量读、写操作之后，保证其他线程对 volatile 变量的修改对当前线程可见，或者本地修改对其他线程提供可见性。换句话说，线程写入，写屏障会通过类似强迫刷出处理器缓存的方式，让其他线程能够拿到最新数值。

## 指令重排
指令重排序是JVM为了优化指令，提高程序运行效率，在不影响单线程程序执行结果的前提下，尽可能地提高并行度。编译器、处理器也遵循这样一个目标。注意是单线程。多线程的情况下指令重排序就会给程序员带来问题。
```
double r = 2.3d;//(1)
double pi =3.1415926; //(2)
double area = pi* r * r; //(3)
```
(1） – happensbefore -> （2）,（2） – happens before -> （3），但是计算顺序(1)(2)(3)与(2)(1)(3) 对于r、pi、area变量的结果并无区别。编译器、Runtime在优化时可以根据情况重排序（1）与（2），而丝毫不影响程序的结果。对于在同一个线程内，这样的改变是不会对逻辑产生影响的，但是在多线程的情况下指令重排序会带来问题。看下面这个情景:
在线程A中:
```
context = loadContext();
inited = true;
```
在线程B中:
```
while(!inited ){ //根据线程A中对inited变量的修改决定是否使用context变量
   sleep(100);
}
```
doSomethingwithconfig(context);
假设线程A中发生了指令重排序:
```
inited = true;
context = loadContext();
```
那么B中很可能就会拿到一个尚未初始化或尚未初始化完成的context,从而引发程序错误。

## synchronized 和 ReentrantLock 有什么区别？

synchronized 是 Java 内建的同步机制，它提供了互斥的语义和可见性，当一个线程已经获取当前锁时，其他试图获取的线程只能等待或者阻塞在那里。在代码中， synchronized 可以用来修饰方法，也可以使用在特定的代码块儿上，本质上 synchronized 方法等同于把方法全部语句用 synchronized 块包起来。
ReentrantLock，通常翻译为再入锁，它的语义和 synchronized 基本相同。再入锁通过代码直接调用 lock() 方法获取，代码书写也更加灵活。ReentrantLock 提供了很多实用的方法，能够实现很多 synchronized 无法做到的细节控制，比如可以控制 fairness，也就是公平性，或者利用定义条件等。但是，编码中也需要注意，必须要明确调用 unlock() 方法释放，不然就会一直持有该锁。

## synchronized 实现原理是什么？

synchronized 代码块是由一对儿 monitorenter/monitorexit 指令实现的，Monitor 对象是同步的基本实现单元。在 Java 6 之前，Monitor 的实现完全是依靠操作系统内部的互斥锁，因为需要进行用户态到内核态的切换，所以同步操作是一个无差别的重量级操作。

现代的（Oracle）JDK 中，JVM 对此进行了大刀阔斧地改进，提供了三种不同的 Monitor 实现，也就是常说的三种不同的锁：偏斜锁（Biased Locking）、轻量级锁和重量级锁，大大改进了其性能。

所谓锁的升级、降级，就是 JVM 优化 synchronized 运行的机制，当 JVM 检测到不同的竞争状况时，会自动切换到适合的锁实现，这种切换就是锁的升级、降级。

当没有竞争出现时，默认会使用偏斜锁。JVM 会利用 CAS 操作（compare and swap），在对象头上的 Mark Word 部分设置线程 ID，以表示这个对象偏向于当前线程，所以并不涉及真正的互斥锁。这样做的假设是基于在很多应用场景中，大部分对象生命周期中最多会被一个线程锁定，使用偏斜锁可以降低无竞争开销。

如果有另外的线程试图锁定某个已经被偏斜过的对象，JVM 就需要撤销（revoke）偏斜锁，并切换到轻量级锁实现。轻量级锁依赖 CAS 操作 Mark Word 来试图获取锁，如果重试成功，就使用普通的轻量级锁；否则，进一步升级为重量级锁。

锁降级会发生在当 JVM 进入安全点（SafePoint: In HotSpot JVM Stop-the-World pause mechanism is called safepoint. ）的时候，会检查是否有闲置的 Monitor，然后试图进行降级。

## ReentrantLock 的实现原理是什么？

ReentrantLock重入锁，是实现Lock接口的一个类，也是在实际编程中使用频率很高的一个锁，支持重入性，表示能够对共享资源能够重复加锁，即当前线程获取该锁再次获取不会被阻塞。
ReentrantLock的实现分两个部分：
1. 重入性的实现原理。
2. 公平锁和非公平锁。

重入性实现原理：
```
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    //1. 如果该锁未被任何线程占有，该锁能被当前线程获取
	if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
	//2.若被占有，检查占有线程是否是当前线程
    else if (current == getExclusiveOwnerThread()) {
		// 3. 再次获取，计数加一
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```
在第二步增加了处理逻辑，如果该锁已经被线程所占有了，会继续检查占有线程是否为当前线程，如果是的话，同步状态加1返回true，表示可以再次获取成功。每次重新获取都会对同步状态进行加一的操作。
释放代码：
```
protected final boolean tryRelease(int releases) {
	//1. 同步状态减1
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
		//2. 只有当同步状态为0时，锁成功被释放，返回true
        free = true;
        setExclusiveOwnerThread(null);
    }
	// 3. 锁未被完全释放，返回false
    setState(c);
    return free;
}
```
重入锁的释放必须得等到同步状态为0时锁才算成功释放，否则锁仍未释放。如果锁被获取n次，释放了n-1次，该锁未完全释放返回false，只有被释放n次才算成功释放，返回true。大部分情况下使用重入锁需要配合finally释放资源。
```
ReentrantLock fairLock = new ReentrantLock(true);// 这里是演示创建公平锁，一般情况不需要。
fairLock.lock();
try {
	// do something
} finally {
 	fairLock.unlock();
}
```

公平锁每次都是从同步队列中的第一个节点获取到锁，而非公平性锁则不一定，有可能刚释放锁的线程能再次获取到锁。在原来的代码基础上，公平锁加入了队列判断：
```
   if (c == 0) {
        if (!hasQueuedPredecessors() &&
            compareAndSetState(0, acquires)) {
            setExclusiveOwnerThread(current);
            return true;
        }
    }
```
如果有前驱节点说明有线程比当前线程更早的请求资源，根据公平性，当前线程请求资源失败。如果当前节点没有前驱节点的话，再才有做后面的逻辑判断的必要性。

公平锁每次获取到锁为同步队列中的第一个节点，保证请求资源时间上的绝对顺序，而非公平锁有可能刚释放锁的线程下次继续获取该锁，则有可能导致其他线程永远无法获取到锁，造成“饥饿”现象。
公平锁为了保证时间上的绝对顺序，需要频繁的上下文切换，而非公平锁会降低一定的上下文切换，降低性能开销。因此，ReentrantLock默认选择的是非公平锁，则是为了减少一部分上下文切换，保证了系统更大的吞吐量。

## 描述一下ReentrantLock的condition机制？

条件变量很大一个程度上是为了解决Object.wait/notify/notifyAll难以使用的问题。条件（也称为条件队列 或条件变量）为线程提供了一个含义，以便在某个状态条件现在可能为 true 的另一个线程通知它之前，一直挂起该线程（即让其“等待”）。
一个具体的例子是ArrayBlockingQueue的实现：
```
/** Condition for waiting takes */
private final Condition notEmpty;

/** Condition for waiting puts */
private final Condition notFull;
 
public ArrayBlockingQueue(int capacity, boolean fair) {
	if (capacity <= 0)
    	throw new IllegalArgumentException();
	this.items = new Object[capacity];
	lock = new ReentrantLock(fair);
	notEmpty = lock.newCondition();
	notFull =  lock.newCondition();
}

public E take() throws InterruptedException {
	final ReentrantLock lock = this.lock;
	lock.lockInterruptibly();
	try {
    	while (count == 0)
        	notEmpty.await();
    	return dequeue();
	} finally {
    	lock.unlock();
	}
}

private void enqueue(E e) {
	// assert lock.isHeldByCurrentThread();
	// assert lock.getHoldCount() == 1;
	// assert items[putIndex] == null;
	final Object[] items = this.items;
	items[putIndex] = e;
	if (++putIndex == items.length) putIndex = 0;
	count++;
	notEmpty.signal(); // 通知等待的线程，非空条件已经满足
}
```

## java并发中锁的种类

锁从宏观上分类，分为悲观锁与乐观锁。乐观锁是一种乐观思想，即认为读多写少，遇到并发写的可能性低，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，采取在写时先读出当前版本号，然后加锁操作（比较跟上一次的版本号，如果一样则更新），如果失败则要重复读-比较-写的操作。java中的乐观锁基本都是通过CAS操作实现的，CAS是一种更新的原子操作，比较当前值跟传入值是否一样，一样则更新，否则失败。

悲观锁是就是悲观思想，即认为写多，遇到并发写的可能性高，每次去拿数据的时候都认为别人会修改，所以每次在读写数据的时候都会上锁，这样别人想读写这个数据就会block直到拿到锁。java中的悲观锁就是Synchronized,AQS框架下的锁则是先尝试cas乐观锁去获取锁，获取不到，才会转换为悲观锁，如RetreenLock。

在java中锁可以细分为：偏向锁、轻量级锁、自旋锁、重量级锁。
- Java偏向锁(Biased Locking)是Java6引入的一项多线程优化。偏向锁，顾名思义，它会偏向于第一个访问锁的线程，如果在运行过程中，同步锁只有一个线程访问，不存在多线程争用的情况，则线程是不需要触发同步的，这种情况下，就会给线程加一个偏向锁。 
- 轻量级锁是相对于重量级锁而言的。使用轻量级锁时，不需要申请互斥量，仅仅_将Mark Word中的部分字节CAS更新指向线程栈中的Lock Record，如果更新成功，则轻量级锁获取成功_，记录锁状态为轻量级锁；否则，说明已经有线程获得了轻量级锁，目前发生了锁竞争（不适合继续使用轻量级锁），接下来膨胀为重量级锁。
- 重量级锁被抽象为监视器锁（monitor）。监视器锁可以认为直接对应底层操作系统中的互斥量（mutex）。这种同步方式的成本非常高，包括系统调用引起的内核态与用户态切换、线程阻塞造成的线程切换等。因此，后来称这种锁为“重量级锁”。
- 自旋锁也可以说不是一种锁技术，而是针对短期等待的性能优化技术。自旋锁原理非常简单，如果持有锁的线程能在很短时间内释放锁资源，那么那些等待竞争锁的线程就不需要做内核态和用户态之间的切换进入阻塞挂起状态，它们只需要等一等（自旋），等持有锁的线程释放锁后即可立即获取锁，这样就避免用户线程和内核的切换的消耗。

## ThreadLocal基本原理和使用方法

ThreadLocal 是 Java 提供的一种保存线程私有信息的机制，因为其在整个线程生命周期内有效，所以可以方便地在一个线程关联的不同业务模块之间传递信息，比如事务 ID、Cookie 等上下文相关信息。它的实现结构，可以参考源码，数据存储于线程相关的 ThreadLocalMap，其内部条目是弱引用，如下面片段。
```
static class ThreadLocalMap {
	static class Entry extends WeakReference<ThreadLocal<?>> {
    	/** The value associated with this ThreadLocal. */
    	Object value;
    	Entry(ThreadLocal<?> k, Object v) {
        	super(k);
    	value = v;
    	}
      }
   // …
}
```
通常弱引用都会和引用队列配合清理机制使用，但是 ThreadLocal 是个例外，它并没有这么做。这意味着，废弃项目的回收依赖于显式地触发，否则就要等待线程结束，进而回收相应 ThreadLocalMap！这就是很多 OOM 的来源，所以通常都会建议，应用一定要自己负责 remove，并且不要和线程池配合，因为 worker 线程往往是不会退出的。

## 什么情况下会发生死锁？

死锁是一种特定的程序状态，在实体之间，由于循环依赖导致彼此一直处于等待之中，没有任何个体可以继续前进。死锁不仅仅是在线程之间会发生，存在资源独占的进程之间同样也可能出现死锁。通常来说，我们大多是聚焦在多线程场景中的死锁，指两个或多个线程之间，由于互相持有对方需要的锁，而永久处于阻塞的状态。

## 在java中如何定位死锁问题？

- 利用 jstack 等工具获取线程栈，然后定位互相之间的依赖关系，进而找到死锁。如果是比较明显的死锁，往往 jstack 等就能直接定位。
- 利用JConsole 甚至可以在图形界面进行有限的死锁检测。
- 使用程序化的方式扫描服务进程、定位死锁，使用Java 提供的标准管理 API ThreadMXBean，其直接就提供了 findDeadlockedThreads() 方法用于定位。

## 写一个死锁的程序
```
public class DeadLockSample extends Thread {
	private String first;
	private String second;
	public DeadLockSample(String name, String first, String second) {
    	super(name);
    	this.first = first;
    	this.second = second;
	}

	public void run() {
    	synchronized (first) {
        	System.out.println(this.getName() + " obtained: " + first);
        	try {
            	Thread.sleep(1000L);
            	synchronized (second) {
                	System.out.println(this.getName() + " obtained: " + second);
            	}
        	} catch (InterruptedException e) {
            	// Do nothing
        	}
    	}
	}
	public static void main(String[] args) throws InterruptedException {
    	String lockA = "lockA";
    	String lockB = "lockB";
    	DeadLockSample t1 = new DeadLockSample("Thread1", lockA, lockB);
    	DeadLockSample t2 = new DeadLockSample("Thread2", lockB, lockA);
    	t1.start(); //获取到lockA 等待lockB
    	t2.start();//获取到lockB 等待lockA
    	t1.join();
    	t2.join();
	}
}
```

## 如何避免写出死锁的程序？

- 避免使用多个锁。
- 设计好锁的获取顺序，银行家算法。
- 使用超时控制（timed_wait），使用非阻塞式的获取锁方法（tryLock）。
- 静态代码分析，FindBugs。

## java.util.concurrent 提供了哪些并发工具？

具体主要包括几个方面：
- 提供了比 synchronized 更加高级的各种同步结构，包括 CountDownLatch、CyclicBarrier、Semaphore 等，可以实现更加丰富的多线程操作，比如利用 Semaphore 作为资源控制器，限制同时进行工作的线程数量。
- 各种线程安全的容器，比如最常见的 ConcurrentHashMap、有序的 ConcunrrentSkipListMap，或者通过类似快照机制，实现线程安全的动态数组 CopyOnWriteArrayList 等。
- 各种并发队列实现，如各种 BlockedQueue 实现，比较典型的 ArrayBlockingQueue、SynchorousQueue 或针对特定场景的 PriorityBlockingQueue 等。
- 强大的 Executor 框架，可以创建各种不同类型的线程池，调度任务运行等，绝大部分情况下，不再需要自己从头实现线程池和任务调度器。

## 实现一个自定义的ThreadFactory的作用通常是？

通常的作用是给线程取名字，便于以后查问题，很多查过问题的同学应该都会发现，看到jstack出来后一堆看不出名字意义的线程是多么的崩溃。

## 简述一下 CAS 算法 

 CAS算法的过程是这样：它包含三个参数 CAS（V,E,N）。V表示要更新的变量，E表示预期的值，N表示新值。仅当V值等于E值时，才会将V的值设置成N，否则什么都不做。最后CAS返回当前V的值。CAS算法需要你额外给出一个期望值，也就是你认为现在变量应该是什么样子，如果变量不是你想象的那样，那说明已经被别人修改过。你就重新读取，再次尝试修改即可。JDK并发包有一个atomic包，里面实现了一些直接使用CAS操作的线程安全的类型。其中最常用的一个类应该就是AtomicInteger。我们以此为例来研究一下没有锁的情况下如何做到线程安全。

## CountDownLatch、CyclicBarrier和Semaphore的用法？

CountDownLatch
```
//调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
public void await() throws InterruptedException { };   
//和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };  
 //将count值减1
public void countDown() { }; 
```

CyclicBarrier:实现让一组线程等待至某个状态之后再全部同时执行.
```
CyclicBarrier barrier  = new CyclicBarrier(N); // N是等待的线程数量
cyclicBarrier.await();
```
Semaphore可以控同时访问的线程个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可
```
Semaphore semaphore = new Semaphore(5); //5个可用信号
semaphore.acquire(); //占用1个
semaphore.release(); //释放1个
```
总结：
1. CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
2. 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
3. Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。

## 为什么ConcurrentHashMap可以在高并发的情况下比HashMap更为高效？

主要是ConcurrentHashMap在实现时采用的拆分锁（事务分离，避免锁住大量数据），使用final、volatile。
```
 static final class HashEntry<K,V> {
     final K key;  
     final int hash;  
     volatile V value;  
     final HashEntry<K,V> next;  
 } 
```
除了value不是final的，其它值都是final的，这意味着不能从hash链的中间或尾部添加或删除节点，因为这需要修改next 引用值，所有的节点的修改只能从头部开始。为了确保读操作能够看到最新的值，将value设置成volatile，这避免了加锁。

在 Java 8 和之后的版本中，ConcurrentHashMap 发生了哪些变化呢？总体结构上，它的内部存储变得和我在专栏上一讲介绍的 HashMap 结构非常相似，同样是大的桶（bucket）数组，然后内部也是一个个所谓的链表结构（bin），同步的粒度要更细致一些。
 - 其内部仍然有 Segment 定义，但仅仅是为了保证序列化时的兼容性而已，不再有任何结构上的用处。
 - 因为不再使用 Segment，初始化操作大大简化，修改为 lazy-load 形式，这样可以有效避免初始开销，解决了很多人抱怨的一点。
 - 数据存储利用 volatile 来保证可见性。
 - 使用 CAS 等操作，在特定场景进行无锁并发操作。
 - 使用 Unsafe、LongAdder 之类底层手段，进行极端情况的优化。

## 为什么jdk没有提供 ConcurrentTreeMap？

这是因为 TreeMap 要实现高效的线程安全是非常困难的，它的实现基于复杂的红黑树。为保证访问效率，当我们插入或删除节点时，会移动节点进行平衡操作，这导致在并发场景中难以进行合理粒度的同步。

## 使用jdk的并发容器（concurrent*）需要注意什么？

Concurrent 往往提供了较低的遍历一致性。你可以这样理解所谓的弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历。与弱一致性对应的，就是我介绍过的同步容器常见的行为“fail-fast”，也就是检测到容器在遍历过程中发生了修改，则抛出 ConcurrentModificationException，不再继续遍历。弱一致性的另外一个体现是，size 等操作准确性是有限的，未必是 100% 准确。与此同时，读取的性能具有一定的不确定性。

## 使用BlockingQueue实现一个典型的生产者-消费者场景。

```
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConsumerProducer {
    public static final String EXIT_MSG  = "Good bye!";
    public static void main(String[] args) {
        // 使用较小的队列，以更好地在输出中展示其影响
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    static class Producer implements Runnable {
        private BlockingQueue<String> queue;
        public Producer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                try{
                    Thread.sleep(5L);
                    String msg = "Message" + i;
                    System.out.println("Produced new item: " + msg);
                    queue.put(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("Time to say good bye!");
                queue.put(EXIT_MSG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable{
        private BlockingQueue<String> queue;
        public Consumer(BlockingQueue<String> q){
            this.queue=q;
        }

        @Override
        public void run() {
            try{
                String msg;
                while(!EXIT_MSG.equalsIgnoreCase( (msg = queue.take()))){
                    System.out.println("Consumed item: " + msg);
                    Thread.sleep(10L);
                }
                System.out.println("Got exit message, bye!");
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
## 用Executors.newCachedThreadPool创建的线程池，在运行的过程中有可能产生的风险是？

newCachedThreadPool最大的风险就是可能会创建超多的线程，导致最后不能创建线程。

## Java并发类库提供的线程池有哪几种？

Executors 目前提供了 5 种不同的线程池创建配置：
- newCachedThreadPool()，它是一种用来处理大量短时间工作任务的线程池，具有几个鲜明特点：它会试图缓存线程并重用，当无缓存线程可用时，就会创建新的工作线程；如果线程闲置的时间超过 60 秒，则被终止并移出缓存；长时间闲置时，这种线程池，不会消耗什么资源。其内部使用
SynchronousQueue 作为工作队列。
- newFixedThreadPool(int nThreads)，重用指定数目（nThreads）的线程，其背后使用的是无界的工作队列，任何时候最多有 nThreads 个工作线程是活动的。这意味着，如果任务数量超过了活动队列数目，将在工作队列中等待空闲线程出现；如果有工作线程退出，将会有新的工作线程被创建，以补足指定的数目 nThreads。
- newSingleThreadExecutor()，它的特点在于工作线程数目被限制为 1，操作一个无界的工作队列，所以它保证了所有任务的都是被顺序执行，最多会有一个任务处于活动状态，并且不允许使用者改动线程池实例，因此可以避免其改变线程数目。
- newSingleThreadScheduledExecutor() 和 newScheduledThreadPool(int corePoolSize)，创建的是个 ScheduledExecutorService，可以进行定时或周期性的工作调度，区别在于单一工作线程还是多个工作线程。
- newWorkStealingPool(int parallelism)，这是一个经常被人忽略的线程池，Java 8 才加入这个创建方法，其内部会构建ForkJoinPool，利用Work-Stealing算法，并行地处理任务，不保证处理顺序。

## 使用线程池需要注意什么？

线程池虽然为提供了非常强大、方便的功能，但是也不是银弹，使用不当同样会导致问题。比如：
- 避免任务堆积。前面我说过 newFixedThreadPool 是创建指定数目的线程，但是其工作队列是无界的，如果工作线程数目太少，导致处理跟不上入队的速度，这就很有可能占用大量系统内存，甚至是出现 OOM。诊断时，你可以使用 jmap 之类的工具，查看是否有大量的任务对象入队。
- 避免过度扩展线程。我们通常在处理大量短时任务时，使用缓存的线程池，比如在最新的 HTTP/2 client API 中，目前的默认实现就是如此。我们在创建线程池的时候，并不能准确预计任务压力有多大、数据特征是什么样子（大部分请求是 1K 、100K 还是 1M 以上？），所以很难明确设定一个线程数目。
- 另外，如果线程数目不断增长（可以使用 jstack 等工具检查），也需要警惕另外一种可能性，就是线程泄漏，这种情况往往是因为任务逻辑有问题，导致工作线程迟迟不能被释放。建议你排查下线程栈，很有可能多个线程都是卡在近似的代码处。
- 避免死锁等同步问题。
- 尽量避免在使用线程池时操作 ThreadLocal，因为它需要显示的触发垃圾回收，操作不当有可能发生OOM。

