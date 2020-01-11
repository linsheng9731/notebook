package io.linsheng9731.aggregates

import org.apache.flink.api.common.functions.AggregateFunction


/**
  * AverageAggregate
  *
  * @author damon lin
  *         2019/12/23
  */
class AverageAggregate extends AggregateFunction[(Int, Int), (Int, Int), Double] {

  override def createAccumulator(): (Int, Int) = (0, 0)

  override def add(in: (Int, Int), acc: (Int, Int)): (Int, Int) = {
    (acc._1 + in._1, acc._2 + in._2)
  }

  override def merge(acc: (Int, Int), acc1: (Int, Int)): (Int, Int) = {
    (acc._1 + acc1._1, acc._2 + acc._2)
  }

  override def getResult(acc: (Int, Int)): Double = {
    acc._1.toDouble / acc._2.toDouble
  }

}
