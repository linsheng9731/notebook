package io.linsheng9731.cep

/**
  * Event
  *
  * @author damon lin
  *         2020/3/9
  */
case class Event(
                  rule: String,
                  csid: Int,
                  stm: Long,
                  variables: Map[String, String] = Map("bw" -> "firefox")
                )

object Event {

  val data = Seq(
//    Event("login",  1, 1), // trigger
//    Event("look",  1, 2),
//    Event("look",  1, 3),
//    Event("addCart",  1, 4),
//    Event("look",  1, 5),
//    Event("addCart",  1, 6),
//
//    Event("login",  2, 1),
//    Event("login",  2, 2),
//    Event("addCart",  2, 4),
//
//    Event("login",  3, 1),

    Event("login",  4, 1),
    Event("login",  4, 2), // trigger twice
    Event("addCart",  4, 3),
    Event("look",  4, 4),
    Event("look",  4, 5),
    Event("pay",  4, 6), // find addCart-pay

    Event("login",  5, 1), // filtered
    Event("look",  5, 2 ),
    Event("pay",  5, 3),
    Event("look",  5, 4 ),
    Event("pay",  5, 5)

  )
}
