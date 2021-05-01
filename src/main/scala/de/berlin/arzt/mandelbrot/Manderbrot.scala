package de.berlin.arzt.mandelbrot

import scala.annotation.tailrec

class Mandelbrot(width: Int, height: Int, maxIter: Int = 600) {
  val i = new Array[Int](width * height)

  def index(x: Int, y: Int): Int = y * width + x

  def compute(xOffset: Double, yOffset: Double, step: Double): Unit = {
    var x = 0
    var y = 0
    while (y < height) {
      while (x < width) {
        val xTrans = xOffset + x * step
        val yTrans = yOffset + y * step
        val mandel = Mandelbrot.mandelbrot(xTrans, yTrans, maxIter)
        i(index(x, y)) = mandel
        x += 1
      }
      y += 1
      x = 0
    }
  }

  def get(x: Int, y: Int): Int = i(index(x, y))

}

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

  def mandelbrotCallback(a: Double, b: Double, maxIter: Int = 1000): Int = {
    @tailrec def mandelbrotRec(
                                za: Double,
                                zb: Double,
                                i: Int
                              ): Int = {
      if (len(za, zb) > 4 || i >= maxIter) {
        maxIter - i
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
