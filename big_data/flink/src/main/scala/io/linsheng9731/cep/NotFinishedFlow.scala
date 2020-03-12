package io.linsheng9731.cep

/**
  * NotFinishFlow
  *
  * @author damon lin
  *         2020/3/9
  */
case class NotFinishedFlow(
                  startEvents: Seq[Condition],
                  notFinishEvents: Seq[Condition],
                  within: Long // 多久内未完成触发
                )

case class Condition(
                    rule: String,
                    filterOp: String,
                    filters: Seq[Filter]
                    )

case class Filter(dim: String, op: String, values: Seq[String])

object NotFinishedFlow {
  val flow = NotFinishedFlow(
    Seq(Condition("login", "or" ,Seq(Filter("bw", "in", Seq("chrome", "firefox"))))),
    Seq(Condition("pay", "or" ,Seq(Filter("bw", "in", Seq("chrome", "firefox"))))),
    1000L
  )
}