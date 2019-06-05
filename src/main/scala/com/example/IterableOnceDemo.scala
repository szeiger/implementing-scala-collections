package com.example

import java.util.PrimitiveIterator

import scala.collection.{Stepper, StepperShape, mutable}

class MyIterableOnce extends IterableOnce[Int] {
  private[this] val data = Array(1, 2, 3)

  def iterator = data.iterator
  override def knownSize = data.length
  override def stepper[S <: Stepper[_]](implicit shape: StepperShape[Int, S]): S = data.stepper[S]
}

object IterableOnceDemo extends App {
  val it: PrimitiveIterator.OfInt = (new MyIterableOnce).stepper.javaIterator
  while(it.hasNext)
    println(it.next())

  val b = mutable.ArrayBuffer.empty[Int]
  b.addAll(new MyIterableOnce)
}
