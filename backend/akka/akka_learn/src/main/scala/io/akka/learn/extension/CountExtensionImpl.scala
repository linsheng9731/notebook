package io.akka.learn.extension

import java.util.concurrent.atomic.AtomicLong

import akka.actor.{Actor, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}

/**
  * CountExtensionImpl
  *
  * @author damon lin
  *         2019/11/18
  */
class CountExtensionImpl(system: ActorSystem) extends Extension {
  private val counter = new AtomicLong(0)

  //This is the operation this Extension provides
  def increment() = {
    counter.incrementAndGet()
//    println("CountExtensionImpl get message from : " + system.name + " count: " + counter.get())
  }
}

object CountExtension extends ExtensionId[CountExtensionImpl] with ExtensionIdProvider {

  override def lookup = CountExtension

  override def createExtension(system: ExtendedActorSystem) = new CountExtensionImpl(system)

  override def get(system: ActorSystem): CountExtensionImpl = super.get(system)

}


class ActorWithExtension(name: String) extends Actor {

  var counter = 0
  def receive = {
    case _ =>
      counter += 1
      println(name + " have counter: " + counter )
      CountExtension(context.system).increment()
  }
}

object ActorWithExtension {
  def apply(name: String): ActorWithExtension = {
    println("ActorWithExtension apply method().")
    new ActorWithExtension(name)
  }
}

case object Counter
