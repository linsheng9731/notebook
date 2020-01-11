package io.akka.learn.starter

import akka.actor.{Actor, Stash}

/**
  * HotSwapActor
  *
  * @author damon lin
  *         2019/12/4
  */
class HotSwapActor extends Stash  {

  def receive = {
    case "open" =>
      // 热更新行为
      context.become({
        case "write" =>
            println("writing ...")
        case "close" =>
          println("un become !")
          context.unbecome() // 还原回去
          unstashAll()
        case msg =>
          println("new behavior for stash msg : " +  msg)
          stash()
      }, discardOld = false)
      println("open ...")
      unstashAll()
    case msg =>
      println("stash msg : " +  msg)
      stash()
  }

}
