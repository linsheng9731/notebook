# Spark 容错机制

## 基本原理

一般来说，分布式数据集的容错性有两种方式：数据检查点和记录数据的更新。 
面向大规模数据分析，数据检查点操作成本很高，需要通过数据中心的网络连接在机器之间复制庞大的数据集，而网络带宽往往比内存带宽低得多，同时还需要消耗更多的存储资源。 

因此，Spark选择记录更新的方式。但是，如果更新粒度太细太多，那么记录更新成本也不低。因此，RDD只支持粗粒度转换，即只记录单个块上执行的单个操作，然后将创建RDD的一系列变换序列（每个RDD都包含了他是如何由其他RDD变换过来的以及如何重建某一块数据的信息。因此RDD的容错机制又称“血统(Lineage)”容错）记录下来，以便恢复丢失的分区。Lineage本质上很类似于数据库中的重做日志（Redo Log），只不过这个重做日志粒度很大，是对全局数据做同样的重做进而恢复数据。

## Lineage 机制
相比其他系统的细颗粒度的内存数据更新级别的备份或者LOG机制，RDD的Lineage记录的是粗颗粒度的特定数据Transformation操作（如filter、map、join等）行为。当这个RDD的部分分区数据丢失时，它可以通过Lineage获取足够的信息来重新运算和恢复丢失的数据分区。因为这种粗颗粒的数据模型，限制了Spark的运用场合，所以Spark并不适用于所有高性能要求的场景，但同时相比细颗粒度的数据模型，也带来了性能的提升。

RDD在Lineage依赖方面分为两种：窄依赖(Narrow Dependencies)与宽依赖(Wide Dependencies,源码中称为Shuffle Dependencies)，用来解决数据容错的高效性。

窄依赖是指父RDD的每一个分区最多被一个子RDD的分区所用，表现为一个父RDD的分区对应于一个子RDD的分区或多个父RDD的分区对应于一个子RDD的分区，也就是说一个父RDD的一个分区不可能对应一个子RDD的多个分区。1个父RDD分区对应1个子RDD分区，这其中又分两种情况：1个子RDD分区对应1个父RDD分区（如map、filter等算子），1个子RDD分区对应N个父RDD分区（如co-paritioned（协同划分）过的Join）。

宽依赖是指子RDD的分区依赖于父RDD的多个分区或所有分区，即存在一个父RDD的一个分区对应一个子RDD的多个分区。1个父RDD分区对应多个子RDD分区，这其中又分两种情况：1个父RDD对应所有子RDD分区（未经协同划分的Join）或者1个父RDD对应非全部的多个RDD分区（如groupByKey）。 

## 依赖关系的特性
第一，窄依赖可以在某个计算节点上直接通过计算父RDD的某块数据计算得到子RDD对应的某块数据；宽依赖则要等到父RDD所有数据都计算完成之后，并且父RDD的计算结果进行hash并传到对应节点上之后才能计算子RDD。 
第二，数据丢失时，对于窄依赖只需要重新计算丢失的那一块数据来恢复；对于宽依赖则要将祖先RDD中的所有数据块全部重新计算来恢复。所以在长“血统”链特别是有宽依赖的时候，需要在适当的时机设置数据检查点。也是这两个特性要求对于不同依赖关系要采取不同的任务调度机制和容错恢复机制。


## Checkpoint机制
DAG中的Lineage过长，如果重算，则开销太大（如在PageRank中）。在宽依赖上做Checkpoint获得的收益更大。
由于RDD是只读的，所以Spark的RDD计算中一致性不是主要关心的内容，内存相对容易管理，这也是设计者很有远见的地方，这样减少了框架的复杂性，提升了性能和可扩展性，为以后上层框架的丰富奠定了强有力的基础。
在RDD计算中，通过检查点机制进行容错，传统做检查点有两种方式：通过冗余数据和日志记录更新操作。在RDD中的doCheckPoint方法相当于通过冗余数据来缓存数据，而之前介绍的血统就是通过相当粗粒度的记录更新操作来实现容错的。检查点（本质是通过将RDD写入Disk做检查点）是为了通过lineage做容错的辅助，lineage过长会造成容错成本过高，这样就不如在中间阶段做检查点容错，如果之后有节点出现问题而丢失分区，从做检查点的RDD开始重做Lineage，就会减少开销。

## Checkpoint 的时机
在 Spark Streaming 中，JobGenerator 用于生成每个 batch 对应的 jobs，它有一个定时器，定时器的周期即初始化 StreamingContext 时设置的 batchDuration。这个周期一到，JobGenerator 将调用generateJobs方法来生成并提交 jobs，这之后调用 doCheckpoint 方法来进行 checkpoint。doCheckpoint 方法中，会判断当前时间与 streaming application start 的时间之差是否是 checkpoint duration 的倍数，只有在是的情况下才进行 checkpoint。

## Checkpoint 的形式
最终 checkpoint 的形式是将类 Checkpoint的实例序列化后写入外部存储，值得一提的是，有专门的一条线程来做将序列化后的 checkpoint 写入外部存储。Spark Streaming 的 checkpoint 机制看起来很美好，却有一个硬伤。上文提到最终刷到外部存储的是类 Checkpoint 对象序列化后的数据。那么在 Spark Streaming application 重新编译后，再去反序列化 checkpoint 数据就会失败。这个时候就必须新建 StreamingContext。针对这种情况，在我们结合 Spark Streaming + kafka 的应用中，我们自行维护了消费的 offsets，这样一来及时重新编译 application，还是可以从需要的 offsets 来消费数据，这里只是举个例子，不详细展开了。
