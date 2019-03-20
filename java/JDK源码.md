## String、StringBuffer、StringBuilder 有什么区别？

String 是典型的Immutable 类，被声明成为 final class，所有属性也都是 final 的。也由于它的不可变性，类似拼接、
裁剪字符串等动作，都会产生新的 String 对象。
StringBuffer 是为解决上面提到拼接产生太多中间对象的问题而提供的一个类，本质是一个线程安全的可
修改字符序列，它保证了线程安全，也随之带来了额外的性能开销，所以除非有线程安全的需要，不然
还是推荐使用它的后继者，也就是 StringBuilder。
StringBuilder 是 Java 1.5 中新增的，在能力上和 StringBuffer 没有本质区别，但是它去掉了线程安全
的部分，有效减小了开销，是绝大部分情况下进行字符串拼接的首选。

## int 和 Integer 有什么区别？谈谈 Integer 的值缓存范围

int 是我们常说的整形数字，是 Java 的 8 个原始数据类型（Primitive Types，boolean、byte 、
short、char、int、float、double、long）之一。Java 语言虽然号称一切都是对象，但原始数据类型是
例外。Integer 是 int 对应的包装类，它有一个 int 类型的字段存储数据，并且提供了基本操作，比如数学运
算、int 和字符串之间转换等。在 Java 5 中，引入了自动装箱和自动拆箱功能（boxing/unboxing），
Java 可以根据上下文，自动进行转换，极大地简化了相关编程。关于 Integer 的值缓存，这涉及 Java 5 中另一个改进。构建 Integer 对象的传统方式是直接调用构造器，直接 new 一个对象。但是根据实践，我们发现大部分数据操作都是集中在有限的、较小的数值范围，因而，在 Java 5 中新增了静态工厂方法 valueOf，在调用它的时候会利用一个缓存机制，带来了明显的性能改进。按照 Javadoc，这个值默认缓存是 -128 到 127 之间。

## 讲一讲Java的集合框架

Java 的集合框架，Collection 接口是所有集合的根，然后扩展开提供了三大类集合，分别是：
1. List，也就是我们前面介绍最多的有序集合，它提供了方便的访问、插入、删除等操作。
2. Set，Set 是不允许重复元素的，这是和 List 最明显的区别，也就是不存在两个对象 equals 返回true。我们在日常开发中有很多需要保证元素唯一性的场合。
3. Queue/Deque，则是 Java 提供的标准队列结构的实现，除了集合的基本功能，它还支持类似先入先出（FIFO， First-in-First-Out）或者后入先出（LIFO，Last-In-First-Out）等特定行为。这里不包括 BlockingQueue，因为通常是并发编程场合，所以被放置在并发包里。

## 对比 Vector、ArrayList、LinkedList 有何区别？

1. Vector 是 Java 早期提供的线程安全的动态数组，如果不需要线程安全，并不建议选择，毕竟同步是有额外开销的。Vector 内部是使用对象数组来保存数据，可以根据需要自动的增加容量，当数组已满时，会创建新的数组，并拷贝原有数组数据。
2. ArrayList 是应用更加广泛的动态数组实现，它本身不是线程安全的，所以性能要好很多。与 Vector 近似，ArrayList 也是可以根据需要调整容量，不过两者的调整逻辑有所区别，Vector 在扩容时会提高 1倍，而 ArrayList 则是增加 50%。
3. LinkedList 顾名思义是 Java 提供的双向链表，所以它不需要像上面两种那样调整容量，它也不是线程安全的。

Vector、和 ArrayList 适合随机访问，LnikedList 进行节点插入、删除高效的多，但是随机访问则比动态数组慢。


## TreeSet 和 HashSet、LinkHashSet 的底层实现数据结构，以及适用场景？

TreeSet 代码里实际默认是利用 TreeMap 实现的，Java 类库创建了一个 Dummy 对象“PRESENT”作为 value，然后所有插入的元素其实是以键的形式放入了 TreeMap 里面；同理，HashSet 其实也是以HashMap 为基础实现的。LinkedHashSet 

TreeSet.java
```
 public TreeSet() {
        this(new TreeMap<E,Object>());
    }
```
HashSet.java
```
public HashSet() {
        map = new HashMap<>();
    }
```
LinkedHashSet.java
```
public LinkedHashSet(int initialCapacity) {
        super(initialCapacity, .75f, true);
    }
// super(int initialCapacity, float loadFactor, boolean dummy)
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }
```

典型使用场景：
1. TreeSet 支持自然顺序访问，但是添加、删除、包含等操作要相对低效（log(n) 时间）。
2. HashSet 则是利用哈希算法，理想情况下，如果哈希散列正常，可以提供常数时间的添加、删除、包含等操作，但是它不保证有序。
3. LinkedHashSet，内部构建了一个记录插入顺序的双向链表，因此提供了按照插入顺序遍历的能力，与此同时，也保证了常数时间的添加、删除、包含等操作，这些操作性能略低于 HashSet，因为需要维护链表的开销。

## Java 提供的默认排序算法有哪些？

这个问题本身就是有点陷阱的意味，因为需要区分是 Arrays.sort() 还是 Collections.sort() （底层是调用 Arrays.sort()）；什么数据类型；多大的数据集（太小的数据集，复杂排序是没必要的，Java 会直接进行二分插入排序）等。

1. 对于原始数据类型，目前使用的是所谓双轴快速排序（Dual-Pivot QuickSort），是一种改进的快速排序算法，早期版本是相对传统的快速排序，你可以阅读源码。
2. 而对于对象数据类型，目前则是使用TimSort，思想上也是一种归并和二分插入排序（binarySort）结合的优化排序算法。TimSort 并不是 Java 的独创，简单说它的思路是查找数据集中已经排好序的分区（这里叫 run），然后合并这些分区来达到排序的目的。

另外，Java 8 引入了并行排序算法（直接使用 parallelSort 方法），这是为了充分利用现代多核处理器的计算能力，底层实现基于 fork-join 框架（专栏后面会对 fork-join 进行相对详细的介绍），当处理的数据集比较小的时候，差距不明显，甚至还表现差一点；但是，当数据集增长到数万或百万以上时，提高就非常大了，具体还是取决于处理器和系统环境。

## 对比 HashTable、HashMap、TreeMap有什么不同？

Hashtable、HashMap、TreeMap 都是最常见的一些 Map 实现，是以键值对的形式存储和操作数据的容器类型。

1. Hashtable 是早期 Java 类库提供的一个哈希表实现，本身是同步的，不支持 null 键和值，由于同步导致的性能开销，所以已经很少被推荐使用。HashTable 是继承 Dictionary 类的，类结构上和 HashMap 明显不同。
2. HashMap 是应用更加广泛的哈希表实现，行为上大致上与 HashTable 一致，主要区别在于 HashMap不是同步的，支持 null 键和值等。通常情况下，HashMap 进行 put 或者 get 操作，可以达到常数时间的性能。
同时，HashMap 的性能表现非常依赖于哈希码的有效性，请务必掌握 hashCode 和 equals 的一些基本约定，比如：
    - equals 相等，hashCode 一定要相等。
    - 重写了 hashCode 也要重写 equals。
    - hashCode 需要保持一致性，状态改变返回的哈希值仍然要一致。
    - equals 的对称、反射、传递等特性。
3. TreeMap 则是基于红黑树的一种提供顺序访问的 Map，和 HashMap 不同，它的 get、put、remove之类操作都是 O（log(n)）的时间复杂度，具体顺序可以由指定的 Comparator 来决定，或者根据键的自然顺序来判断。

## 使用一种 JDK 的数据结构完成一个自动释放资源的资源池，比如资源池容量为3，当元素个数超过3个，自动删除最近未被使用过的资源。

使用 LinkedHashMap，该数据结构提供的是遍历顺序符合插入顺序，它的实现是通过为条目（键值对）维护一个双向链表。注意，通过特定构造函数，我们可以创建反映访问顺序的实例，所谓的 put、get、compute 等，都算作“访问”。我们构建一个空间占用敏感的资源池，希望可以自动将最不常被访问的对象释放掉，这就可以利用 LinkedHashMap 提供的机制来实现，参考下面的示例：

```
import java.util.LinkedHashMap;
import java.util.Map;  
public class LinkedHashMapSample {
    public static void main(String[] args) {
        LinkedHashMap<String, String> accessOrderedMap = new LinkedHashMap<String, String>(16, 0.75F, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) { // 实现自定义删除策略，否则行为就和普遍 Map 没有区别
                return size() > 3;
            }
        };
        accessOrderedMap.put("Project1", "Valhalla");
        accessOrderedMap.put("Project2", "Panama");
        accessOrderedMap.put("Project3", "Loom");
        accessOrderedMap.forEach( (k,v) -> {
            System.out.println(k +":" + v);
        });
        // 模拟访问
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project3");
        System.out.println("Iterate over should be not affected:");
        accessOrderedMap.forEach( (k,v) -> {
            System.out.println(k +":" + v);
        });
        // 触发删除
        accessOrderedMap.put("Project4", "Mission Control");
        System.out.println("Oldest entry should be removed:");
        accessOrderedMap.forEach( (k,v) -> {// 遍历顺序不变
            System.out.println(k +":" + v);
        });
    }
}
```
## HashMap的实现原理？/怎么设计一个HashMap？/HashMap源码分析

通过三个方面来看：
 - 底层数据结构
 - 容量和负载系数
 - 树化

HashMap 内部的结构，它可以看作是数组（Node<K,V>[] table）和链表结合组成的复合结构，数组被分为一个个桶（bucket），通过哈希值决定了键值对在这个数组的寻址；哈希值相同的键值对，则以链表形式存储。这里需要注意的是，如果链表大小超过阈值（TREEIFY_THRESHOLD, 8），图中的链表就会被改造为树形结构。

初始化保存容量和负载系数两个属性，在put操作的时候如果发现元素个数超过了门限阈值（负载系数*容量）机会触发resize操作。resize操作的主要逻辑是创建一个新的2倍的数组，将老的元素移动到新的数组中，这里有额外的开销。

那么，为什么 HashMap 要树化呢？
本质上这是个安全问题。因为在元素放置过程中，如果一个对象哈希冲突，都被放置到同一个桶里，则会形成一个链表，我们知道链表查询是线性的，会严重影响存取的性能。而在现实世界，构造哈希冲突的数据并不是非常复杂的事情，恶意代码就可以利用这些数据大量与服务器端交互，导致服务器端 CPU 大量占用，这就构成了哈希碰撞拒绝服务攻击，国内一线互联网公司就发生过类似攻击事件。

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
