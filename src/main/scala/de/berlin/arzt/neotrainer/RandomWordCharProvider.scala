package de.berlin.arzt.neotrainer

class RandomWordCharProvider(val words: IndexedSeq[String]) extends CharProvider {

  var current: List[Char] = List.empty

  def randomWord = (words.length*Math.random()).toInt

  def next() {
    current = words(randomWord).seq.toList
  }

  override def getNextChar: Char = current match {
    case Nil =>
      next()
      ' '
    case head :: tail =>
      current = tail
      head
  }

  override def reward(c: Char): Unit = ???

  override def penalize(c: Char): Unit = ???
}
