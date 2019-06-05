package com.example

import scala.collection.{MapFactory, MapFactoryDefaults, View, immutable, mutable}
import scala.collection.generic.DefaultSerializable
import scala.collection.immutable.{AbstractMap, StrictOptimizedMapOps}
import scala.jdk.CollectionConverters._
import java.util.{HashMap => JHashMap}

class MyMap[K, +V] private (m: JHashMap[K, _ <: V])
  extends AbstractMap[K, V]
  with StrictOptimizedMapOps[K, V, MyMap, MyMap[K, V]]
  with MapFactoryDefaults[K, V, MyMap, immutable.Iterable] {

  //override def iterableFactory = immutable.Iterable
  override def mapFactory = MyMap

  def iterator: Iterator[(K, V)] =
    m.entrySet().iterator().asScala.map(e => (e.getKey, e.getValue))
  def get(key: K): Option[V] =
    Option(m.get(key)).orElse(if(m.containsKey(key)) Some(null.asInstanceOf[V]) else None)
  def removed(key: K): MyMap[K,V] = if(!contains(key)) this else {
    val m2 = m.clone().asInstanceOf[JHashMap[K, V]]
    m2.remove(key)
    new MyMap(m2)
  }
  def updated[V1 >: V](key: K, value: V1): MyMap[K,V1] = {
    val m2 = m.clone().asInstanceOf[JHashMap[K, V1]]
    m2.put(key, value)
    new MyMap(m2)
  }

  // Methods that return a CC are overloaded
  override def map[K2, V2](f: ((K, V)) => (K2, V2)): MyMap[K2, V2] = super.map(f)
  override def map[B](f: ((K, V)) => B): immutable.Iterable[B] = super.map(f)
}

object MyMap extends MapFactory[MyMap] {
  private[this] val _empty = new MyMap(new JHashMap[Any, Any])
  def empty[K, V]: MyMap[K,V] = _empty.asInstanceOf[MyMap[K, V]]
  def from[K, V](it: IterableOnce[(K, V)]): MyMap[K,V] =
    newBuilder[K, V].addAll(it).result()
  def newBuilder[K, V]: mutable.Builder[(K, V), MyMap[K,V]] =
    new mutable.Builder[(K, V), MyMap[K, V]] {
      private[this] val m = new JHashMap[K, V]
      def clear() = m.clear()
      def result() = new MyMap[K, V](m)
      def addOne(elem: (K, V)) = { m.put(elem._1, elem._2); this }
    }
}
