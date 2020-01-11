package io.linsheng9731.join

import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
  * JoinStreams
  *
  * @author damon lin
  *         2019/12/18
  */
object JoinStreams {

  def main(args: Array[String]): Unit = {

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val users1 = env.fromCollection(Seq(User(1L, "tom"), User(2L, "jack"), User(3L, "jackson"), User(4L, "alice")))
    val users2 = env.fromCollection(Seq(User(1L, "tom"), User(2L, "jack"), User(3L, "jackson"), User(4L, "alice")))

    // union
    // 合并所有同类型的 stream
    users1.union(users2).print().setParallelism(1)

    // join
    // 类似 inner join 按照 key 进行匹配 匹配不上不输出
    users1
      .assignAscendingTimestamps(u => withTime(u.id))
      .join(users2.assignAscendingTimestamps(u => withTime(u.id)))
      .where(t => t.id).equalTo(t => t.id)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      .apply((u1, u2) => User(-1, u2.name))
      .print().setParallelism(1)

    // co-group 基本和 join 相同
    // 不同于如果没有按照 key 匹配上 co-group 仍然会输出
    users1
      .assignAscendingTimestamps(u => withTime(u.id))
      .coGroup(users2.assignAscendingTimestamps(u => withTime(u.id)))
      .where(t => t.id).equalTo(t => t.id)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      .apply((us1, us2) => (us1 ++ us2).map(_.name).mkString(","))
      .print().setParallelism(1)


    // interval join
    // 根据 key join 两个 keyedStream
    // KeyedStream,KeyedStream → DataStream
    // key1 == key2 && e1.timestamp + lowerBound <= e2.timestamp <= e1.timestamp + upperBound
    users1
      .assignAscendingTimestamps(u => withTime(u.id))
      .keyBy("id")
      .intervalJoin(users2.assignAscendingTimestamps(u => withTime(u.id)).keyBy("id"))
      .between(Time.seconds(0), Time.seconds(4))
      .process(new ProcessJoinFunction[User, User, User](){
        override def processElement(left: User, right: User, ctx: ProcessJoinFunction[User, User, User]#Context, out: Collector[User]): Unit = {
          out.collect(User(left.id + right.id, left.name + ":" + right.name))
        }
      })
      .print().setParallelism(1)

    env.execute("Operations.")
  }

  def withTime(input: Long): Long = {
    System.currentTimeMillis() + Time.seconds(input + 1).toMilliseconds
  }

}

case class User(id: Long, name: String)


