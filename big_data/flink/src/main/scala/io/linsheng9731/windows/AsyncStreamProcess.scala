package io.linsheng9731.windows

import java.util.concurrent.TimeUnit
import org.apache.flink.streaming.api.scala._
import scala.concurrent.{ExecutionContext, Future}
import org.apache.flink.runtime.concurrent.Executors
import org.apache.flink.streaming.api.scala.async.{AsyncFunction, ResultFuture}

/**
  * AsyncStreamProcess
  *
  * @author damon lin
  *         2019/12/26
  */
object AsyncStreamProcess {

  def main(args: Array[String]): Unit = {
    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val nums = env.fromCollection(Seq("a","b"))

    AsyncDataStream
      .unorderedWait(nums, new AsyncRequest(), 1000, TimeUnit.MILLISECONDS, 100)
      .print()
      .setParallelism(1)

    env.execute("Operations.")
  }

}

class AsyncRequest extends AsyncFunction[String, (String, String)] {

  /** The context used for the future callbacks */
  implicit lazy val executor: ExecutionContext = ExecutionContext.fromExecutor(Executors.directExecutor())

  override def asyncInvoke(input: String, resultFuture: ResultFuture[(String, String)]): Unit = {
    val result = Future.successful(input)
    result.onSuccess {
      case r: String => resultFuture.complete(Iterable((r, r)))
    }
  }
}