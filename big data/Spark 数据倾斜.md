# 背景
Spark 数据倾斜的几种场景以及对应的解决方案，包括避免数据源倾斜，调整并行度，使用自定义 Partitioner，使用 Map 侧 Join 代替 Reduce 侧 Join（内存表合并），给倾斜 Key 加上随机前缀等。

## 什么是数据倾斜 
对 Spark/Hadoop 这样的大数据系统来讲，数据量大并不可怕，可怕的是数据倾斜。数据倾斜指的是，并行处理的数据集中，某一部分（如 Spark 或 Kafka 的一个 Partition）的数据显著多于其它部分，从而使得该部分的处理速度成为整个数据集处理的瓶颈（木桶效应）。

## 数据倾斜是如何造成的 
在 Spark 中，同一个 Stage 的不同 Partition 可以并行处理，而具有依赖关系的不同 Stage 之间是串行处理的。假设某个 Spark Job 分为 Stage 0和 Stage 1两个 Stage，且 Stage 1依赖于 Stage 0，那 Stage 0完全处理结束之前不会处理Stage 1。而 Stage 0可能包含 N 个 Task，这 N 个 Task 可以并行进行。如果其中 N-1个 Task 都在10秒内完成，而另外一个 Task 却耗时1分钟，那该 Stage 的总时间至少为1分钟。换句话说，一个 Stage 所耗费的时间，主要由最慢的那个 Task 决定。由于同一个 Stage 内的所有 Task 执行相同的计算，在排除不同计算节点计算能力差异的前提下，不同 Task 之间耗时的差异主要由该 Task 所处理的数据量决定。

## 具体解决方案
1. 调整并行度分散同一个 Task 的不同 Key: Spark 在做 Shuffle 时，默认使用 HashPartitioner对数据进行分区。如果并行度设置的不合适，可能造成大量不相同的 Key 对应的数据被分配到了同一个 Task 上，造成该 Task 所处理的数据远大于其它 Task，从而造成数据倾斜。如果调整 Shuffle 时的并行度，使得原本被分配到同一 Task 的不同 Key 发配到不同 Task 上处理，则可降低原 Task 所需处理的数据量，从而缓解数据倾斜问题造成的短板效应。图中左边绿色框表示 kv 样式的数据，key 可以理解成 name。可以看到 Task0 分配了许多的 key，调整并行度，多了几个 Task，那么每个 Task 处理的数据量就分散了。
2. 自定义Partitioner: 使用自定义的 Partitioner（默认为 HashPartitioner），将原本被分配到同一个 Task 的不同 Key 分配到不同 Task，可以拿上图继续想象一下，通过自定义 Partitioner 可以把原本分到 Task0 的 Key 分到 Task1，那么 Task0 的要处理的数据量就少了。 
3. 将 Reduce side（侧） Join 转变为 Map side（侧） Join: 通过 Spark 的 Broadcast 机制，将 Reduce 侧 Join 转化为 Map 侧 Join，避免 Shuffle 从而完全消除 Shuffle 带来的数据倾斜。可以看到 RDD2 被加载到内存中了。
4. 为 skew 的 key 增加随机前/后缀: 为数据量特别大的 Key 增加随机前/后缀，使得原来 Key 相同的数据变为 Key 不相同的数据，从而使倾斜的数据集分散到不同的 Task 中，彻底解决数据倾斜问题。Join 另一则的数据中，与倾斜 Key 对应的部分数据，与随机前缀集作笛卡尔乘积，从而保证无论数据倾斜侧倾斜 Key 如何加前缀，都能与之正常 Join。
5. 大表随机添加 N 种随机前缀，小表扩大 N 倍: 如果出现数据倾斜的 Key 比较多，上一种方法将这些大量的倾斜 Key 分拆出来，意义不大（很难一个 Key 一个 Key 都加上后缀）。此时更适合直接对存在数据倾斜的数据集全部加上随机前缀，然后对另外一个不存在严重数据倾斜的数据集整体与随机前缀集作笛卡尔乘积（即将数据量扩大 N 倍），可以看到 RDD2 扩大了 N 倍了，再和加完前缀的大数据做笛卡尔积。