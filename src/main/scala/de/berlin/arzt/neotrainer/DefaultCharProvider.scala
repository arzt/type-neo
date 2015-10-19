package de.berlin.arzt.neotrainer

class DefaultCharProvider(chars: Iterable[Char]) extends CharProvider {
  var charSource: DiscreteDistribution[Char] = null
  var lastChar: Char = 0

  setChars(chars)

  def getNextChar: Char = {
    var newChar = charSource.getSample
    while (newChar == lastChar) {
      newChar = charSource.getSample
    }
    lastChar = newChar
    newChar
  }

  def setChars(chars: Iterable[Char]) {
    charSource = new DiscreteDistribution[Char]()
    for (c <- chars) {
      charSource.put(c, 1d)
    }
    charSource.normalize
  }

  def penalize(c: Char) {
    charSource.muliply(c, 2)
    charSource.uniformize(0.05)
    System.out.println("Penalized: " + c)
    System.out.println(charSource)
  }

  def reward(c: Char) {
    charSource.muliply(c, 0.8)
    charSource.uniformize(0.05)
    System.out.println("Rewarded: " + c)
    System.out.println(charSource)
  }
}