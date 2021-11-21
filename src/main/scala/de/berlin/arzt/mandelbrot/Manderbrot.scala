package de.berlin.arzt.mandelbrot

import de.berlin.arzt.math.Complex.{squareIm, squareRe, squaredAbs}

import scala.annotation.tailrec

object Mandelbrot {

  def mandelbrot(x: Double, y: Double, maxIter: Int = 1000): Int = {
    @tailrec def mandelbrotRec(xi: Double, yi: Double, i: Int): Int = {
      if (squaredAbs(xi, yi) > 4 || i >= maxIter) {
        i
      } else {
        val xTmp = xi + x
        val yTmp = yi + y
        val xNext = squareRe(xTmp, yTmp)
        val yNext = squareIm(xTmp, yTmp)
        mandelbrotRec(xNext, yNext, i + 1)
      }
    }

    mandelbrotRec(0, 0, 0)
  }

}
