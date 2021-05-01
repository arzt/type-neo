package de.berlin.arzt.mandelbrot

import scala.annotation.tailrec

object Mandelbrot {

  def len(a: Double, b: Double): Double = a * a + b * b

  def im(a: Double, b: Double): Double = b

  def re(a: Double, b: Double): Double = a

  def prodRe(a1: Double, b1: Double, a2: Double, b2: Double): Double = a1 * a2 - b1 * b2

  def prodIm(a1: Double, b1: Double, a2: Double, b2: Double): Double = a1 * b2 + b1 * a2

  def plusRe(a1: Double, b1: Double, a2: Double, b2: Double): Double = a1 + a2

  def plusIm(a1: Double, b1: Double, a2: Double, b2: Double): Double = b1 + b2

  def squareRe(a: Double, b: Double): Double = prodRe(a, b, a, b)

  def squareIm(a: Double, b: Double): Double = prodIm(a, b, a, b)

  def mandelbrot(a: Double, b: Double, maxIter: Int = 1000): Int = {
    @tailrec def mandelbrotRec(
                                za: Double,
                                zb: Double,
                                i: Int
                              ): Int = {
      if (len(za, zb) > 4 || i >= maxIter) {
        i
      } else {
        val pa = za + a
        val pb = zb + b
        val sa = squareRe(pa, pb)
        val sb = squareIm(pa, pb)
        mandelbrotRec(sa, sb, i + 1)
      }
    }
    mandelbrotRec(0, 0, 0)
  }

}
