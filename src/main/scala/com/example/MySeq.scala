package com.example

import scala.annotation.unchecked.uncheckedVariance
import scala.collection.generic.DefaultSerializable
import scala.collection.{IterableFactoryDefaults, SeqFactory, mutable}
import scala.collection.immutable.{AbstractSeq, IndexedSeqOps, StrictOptimizedSeqOps}

class MySeq[+A](data: Array[Any])
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
    val a = new Array[Any](len)
    var i = 0
    while(i < len) {
      a(i) = f(data(i).asInstanceOf[A])
      i += 1
    }
    new MySeq[B](a)
  }
}

object MySeq extends SeqFactory[MySeq] {
  private[this] val _empty = new MySeq(Array.empty)
  def empty[A]: MySeq[A] = _empty

  def newBuilder[A]: mutable.Builder[A, MySeq[A]] =
    Array.newBuilder[Any]
      .mapResult(new MySeq(_))

  def from[A](source: IterableOnce[A]): MySeq[A] =
    new MySeq(Array.from(source))
}

object MySeqDemo extends App {
  val s = new MySeq[String](Array("a", "b", "c"))
  s.foreach(println)
  println(s.indexWhere(_ > "b"))
  println(s)
  println(s.filterNot(_ == "a"))

  val s2 = MySeq.tabulate(5)(_ * 10)
}
