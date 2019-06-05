package com.example

import scala.annotation.unchecked.uncheckedVariance
import scala.collection.generic.DefaultSerializable
import scala.collection.{IterableFactoryDefaults, SeqFactory, mutable}
import scala.collection.immutable.{AbstractSeq, IndexedSeqOps, StrictOptimizedSeqOps}

class MySeq[+A](data: Array[AnyRef])
  extends AbstractSeq[A]
  with IndexedSeq[A]
  with IndexedSeqOps[A, MySeq, MySeq[A]]
  with StrictOptimizedSeqOps[A, MySeq, MySeq[A]]
  with IterableFactoryDefaults[A, MySeq]
  with DefaultSerializable {

  override def iterableFactory: SeqFactory[MySeq] = MySeq

  def length: Int = data.length
  def apply(i: Int): A = data(i).asInstanceOf[A]
  override def className = "MySeq"

  override def map[B](f: A => B): MySeq[B] = {
    val len = length
    val a = new Array[AnyRef](len)
    var i = 0
    while(i < len) {
      a(i) = f(data(i).asInstanceOf[A]).asInstanceOf[AnyRef]
      i += 1
    }
    new MySeq[B](a)
  }
}

object MySeq extends SeqFactory[MySeq] {
  private[this] val _empty = new MySeq(Array.empty)
  def empty[A]: MySeq[A] = _empty

  def newBuilder[A]: mutable.Builder[A, MySeq[A]] =
    Array.newBuilder[AnyRef]
      .mapResult(b => new MySeq(b.toArray))
      .asInstanceOf[mutable.Builder[A, MySeq[A]]]

  def from[A](source: IterableOnce[A]): MySeq[A] =
    new MySeq[A](Array.from(source.asInstanceOf[IterableOnce[AnyRef]]))
}

object MySeqDemo extends App {
  val s = new MySeq[String](Array[AnyRef]("a", "b", "c"))
  s.foreach(println)
  println(s.indexWhere(_ > "b"))
  println(s)
  println(s.filterNot(_ == "a"))

  val s2 = MySeq.tabulate(5)(_ * 10)
}
