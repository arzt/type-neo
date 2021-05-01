package de.berlin.arzt.mandelbrot

class MandelbrotDrawer(maxIter: Int = 600, callback: (Int, Int, Int) => Unit,
                       abort: () => Boolean) {


    def compute(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int,
                xOffset: Double, yOffset: Double, step: Double): Unit = {
      var x = xStart
      var y = yStart
      while (y < yEnd && !abort()) {
        while (x < xEnd && !abort()) {
          val xTrans = xOffset + x * step
          val yTrans = yOffset + y * step
          val mandelVal = Mandelbrot.mandelbrot(xTrans, yTrans, maxIter)
          val mandelValNorm = 1 - mandelVal / maxIter.toDouble
          val mandelScaled = mandelValNorm * 0xffffff
          val argb = 0xff000000 | mandelScaled.toInt
          callback(x, y, argb)
          x += 1
        }
        y += 1
        x = xStart
      }

      if (abort()) {
        //println(f"aborted! ${x},${y}")
      }
    }

}
