package de.berlin.arzt.mandelbrot

import de.berlin.arzt.mandelbrot.Mandelbrot.{squareIm, squareRe}

class MandelbrotDrawer2(width: Int, height: Int, maxIter: Int = 600, callback: (Int, Int, Int) => Unit,
                        abort: () => Boolean) {
  val za = new Array[Double](width * height)
  val zb = new Array[Double](za.length)
  val is = new Array[Int](za.length)

  def index(x: Int, y: Int): Int = y * width + x

  def compute(xOffset: Double, yOffset: Double, step: Double): Unit = {
    var i = 0
    while (i < za.length) {
      za(i) = 0
      zb(i) = 0
      is(i) = 0
      i += 1
    }
    i = 0
    var x = 0
    var y = 0
    while (i <= maxIter && !abort()) {
      while (y < height && !abort()) {
        while (x < width && !abort()) {
          val ind = index(x, y)

          val xTrans = xOffset + x * step
          val yTrans = yOffset + y * step

          val pa = za(ind) + xTrans
          val pb = zb(ind) + yTrans
          za(ind) = squareRe(pa, pb)
          zb(ind) = squareIm(pa, pb)

          if (Mandelbrot.len(za(ind), zb(ind)) < 4 || i == 0) {
            is(ind) += 1
            val mandelValNorm = is(ind) / maxIter.doubleValue()
            val mandelScaled = mandelValNorm * 0xffffff
            val argb = 0xff000000 | mandelScaled.toInt
            callback(x, y, argb)
          }
          x += 1
        }
        y += 1
        x = 0
      }
      i += 1
      //print(f"${i}, ")
      y = 0
      x = 0
    }

    if (abort()) {
      //println(f"aborted! ${x},${y}")
    }
  }

}
