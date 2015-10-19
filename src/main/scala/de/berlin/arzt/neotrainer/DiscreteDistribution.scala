package de.berlin.arzt.neotrainer

import scala.collection.mutable

class DiscreteDistribution[E] extends mutable.HashMap[E, Double] {

  def uniformize(w: Double) {
    val pUni: Double = 1d / keySet.size
    for (key <- keySet) {
      val pOrg: Double = this(key)
      val pWeighted: Double = w * pUni + (1 - w) * pOrg
      put(key, pWeighted)
    }
  }

  def normalize {
    val sum: Double = values.sum
    for (key <- keySet) {
      val value: Double = this(key) / sum
      put(key, value)
    }
  }

  def muliply(key: E, factor: Double) {
    put(key, this(key) * factor)
    normalize
  }

  def getSample(p: Double): E = {
    assert((p >= 0))
    assert((p < 1))
    var retval: E = null.asInstanceOf[E]
    val keys: Iterator[E] = keySet.iterator
    var sum: Double = 0
    do {
      retval = keys.next
      val value: Double = this(retval)
      sum += value
    } while (p > sum && keys.hasNext)
    assert((retval != null))
    return retval
  }

  def getSample: E = {
    return getSample(Math.random)
  }

  def toUniformDistribution {
    val p: Double = 1d / keySet.size
    for (key <- keySet) {
      put(key, p)
    }
  }
}