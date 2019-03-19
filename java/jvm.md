问：Parallel GC、CMS GC、ZGC、Azul Pauseless GC最主要的不同是？背后的原理也请简单描述下？
答：Parallel GC的Young区采用的是Mark-Copy算法，Old区采用的是Mark-Sweep-Compact来实现，Parallel执行，所以决定了Parallel GC在执行YGC、FGC时都会Stop-The-World，但完成GC的速度也会比较快。
CMS GC的Young区采用的也是Mark-Copy，Old区采用的是Concurrent Mark-Sweep，所以决定了CMS GC在对old区回收时造成的STW时间会更短，避免对应用产生太大的时延影响。
G1 GC采用了Garbage First算法，比较复杂，实现的好呢，理论上是会比CMS GC可以更高效，同时对应用的影响也很小。
ZGC、Azul Pauseless GC采用的算法很不一样，尤其是Pauseless GC，其中的很重要的一个技巧是通过增加Read Barrier来更好的识别对GC而言最关键的references变化的情况。

问：什么时候执行ygc，fullgc？
答：当young gen中的eden区分配满的时候触发young gc，当年老代内存不足时，将执行Major GC，也叫 Full GC。

问：java对象的生命周期？
答：Eden -> Survivor (From -> To) -> Old -> Perm (永生带)

问：如何提高JVM的性能？
答：
1. 新对象预留在年轻代
通过设置一个较大的年轻代预留新对象，设置合理的 Survivor 区并且提供 Survivor 区的使用率，可以将年轻对象保存在年轻代。

2. 大对象进入年老代
使用参数-XX:PetenureSizeThreshold 设置大对象直接进入年老代的阈值

3. 设置对象进入年老代的年龄
这个阈值的最大值可以通过参数-XX:MaxTenuringThreshold 来设置，默认值是 15

4. 稳定的 Java 堆 
获得一个稳定的堆大小的方法是使-Xms 和-Xmx 的大小一致，即最大堆和最小堆 (初始堆) 一样。

5. 增大吞吐量提升系统性能
–Xmx380m –Xms3800m：设置 Java 堆的最大值和初始值。一般情况下，为了避免堆内存的频繁震荡，导致系统性能下降，我们的做法是设置最大堆等于最小堆。假设这里把最小堆减少为最大堆的一半，即 1900m，那么 JVM 会尽可能在 1900MB 堆空间中运行，如果这样，发生 GC 的可能性就会比较高；
-Xss128k：减少线程栈的大小，这样可以使剩余的系统内存支持更多的线程；
-Xmn2g：设置年轻代区域大小为 2GB；
–XX:+UseParallelGC：年轻代使用并行垃圾回收收集器。这是一个关注吞吐量的收集器，可以尽可能地减少 GC 时间。
–XX:ParallelGC-Threads：设置用于垃圾回收的线程数，通常情况下，可以设置和 CPU 数量相等。但在 CPU 数量比较多的情况下，设置相对较小的数值也是合理的；
–XX:+UseParallelOldGC：设置年老代使用并行回收收集器。

6. 尝试使用大的内存分页
–XX:+LargePageSizeInBytes：设置大页的大小。
内存分页 (Paging) 是在使用 MMU 的基础上，提出的一种内存管理机制。它将虚拟地址和物理地址按固定大小（4K）分割成页 (page) 和页帧 (page frame)，并保证页与页帧的大小相同。这种机制，从数据结构上，保证了访问内存的高效，并使 OS 能支持非连续性的内存分配。

7. 使用非占有的垃圾回收器
为降低应用软件的垃圾回收时的停顿，首先考虑的是使用关注系统停顿的 CMS 回收器，其次，为了减少 Full GC 次数，应尽可能将对象预留在年轻代。