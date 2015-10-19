package de.berlin.arzt.neotrainer

object CharClasses {
  val BACKSPACE = Set(8.toChar)
  val ENTER = Set(13.toChar)
  val LOWERCASE_LETTERS = "abcdeftghijklmnopqrstuvwxyzäöüß".toSet
  val UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜẞ".toSet
  val NUMBERS = "0123456789".toSet
  val SPECIAL_CHARACTERS_1 = ".,?!\"'-:;".toSet
  val SPECIAL_CHARACTERS_2 = "+*/(){}[]=<>".toSet
  val SPECIAL_CHARACTERS_3 = "\\#&$|~@`…_%§€$".toSet & BACKSPACE
  val ALL = ENTER ++ LOWERCASE_LETTERS ++ UPPERCASE_LETTERS ++ NUMBERS ++ SPECIAL_CHARACTERS_1 ++ SPECIAL_CHARACTERS_2 ++ SPECIAL_CHARACTERS_3
}

