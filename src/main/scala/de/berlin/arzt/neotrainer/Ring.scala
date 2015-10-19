package de.berlin.arzt.neotrainer

import scala.collection.mutable
import scala.collection.mutable.WrappedArray

class Ring[A](val buf: mutable.WrappedArray[A])  {
  var first = 0

  lazy val length = buf.length
  
  def abs(i: Int) = (first + i) % length

  def apply(i: Int) = buf(abs(i))

  //override def iterator = buf.iterator

  def update(i: Int, c: A) {
    buf(abs(i)) = c
  }

  def append(c: A) {
    buf(first) = c
    first += 1
    first %= length
  }

  def prepend(c: A) {
    first += length - 1
    first %= length
    buf(first) = c
  }

  def seq = {
    val out = new mutable.ArraySeq[A](buf.length)
    var i = 0
    while (i < length) {
      out(i) = this(i)
      i += 1
    }
    out.seq
  }

  override def toString = seq.toString()
}

object Ring {
  def ofChar(array: Array[Char]) = new Ring(new WrappedArray.ofChar(array))
  def ofChar(length: Int): Ring[Char] = ofChar(new Array[Char](length))
}