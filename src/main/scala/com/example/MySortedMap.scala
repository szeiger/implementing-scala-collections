package com.example

import scala.collection.{SortedMapFactory, SortedMapFactoryDefaults, immutable, mutable}
import scala.collection.immutable.{SortedMap, StrictOptimizedSortedMapOps}


class MySortedMap[K, +V]
  extends SortedMap[K, V]
    with StrictOptimizedSortedMapOps[K, V, MySortedMap, MySortedMap[K, V]]
    with SortedMapFactoryDefaults[K, V, MySortedMap, immutable.Iterable, Map] {

  override def sortedMapFactory = MySortedMap
  //override def mapFactory = immutable.Map
  //override def iterableFactory = immutable.Iterable

  def iterator: Iterator[(K, V)] = ???
  def removed(key: K): MySortedMap[K,V] = ???
  def get(key: K): Option[V] = ???
  def updated[V1 >: V](key: K, value: V1): MySortedMap[K,V1] = ???
  def iteratorFrom(start: K): Iterator[(K, V)] = ???
  def keysIteratorFrom(start: K): Iterator[K] = ???
  def ordering: Ordering[K] = ???
  def rangeImpl(from: Option[K], until: Option[K]): MySortedMap[K,V] = ???
}

object MySortedMap extends SortedMapFactory[MySortedMap] {
  def empty[K : Ordering, V]: MySortedMap[K,V] = ???
  def from[K : Ordering, V](it: IterableOnce[(K, V)]): MySortedMap[K,V] = ???
  def newBuilder[K : Ordering, V]: mutable.Builder[(K, V), MySortedMap[K,V]] = ???
}
