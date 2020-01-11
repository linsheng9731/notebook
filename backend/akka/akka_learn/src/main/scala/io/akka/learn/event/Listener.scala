package io.akka.learn.event

import akka.actor.Actor

/**
  * Listener
  *
  * @author damon lin
  *         2019/11/18
  */
class Listener extends Actor {

  def receive = {
    case m: Jazz       => println(s"${self.path.name} is listening to: ${m.artist}")
    case m: Electronic => println(s"${self.path.name} is listening to: ${m.artist}")
  }

}

abstract class AllKindsOfMusic { def artist: String }
case class Jazz(artist: String) extends AllKindsOfMusic
case class Electronic(artist: String) extends AllKindsOfMusic

