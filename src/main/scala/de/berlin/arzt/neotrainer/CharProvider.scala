package de.berlin.arzt.neotrainer

trait CharProvider {

  def getNextChar: Char

  def reward(c: Char)

  def penalize(c: Char)
}