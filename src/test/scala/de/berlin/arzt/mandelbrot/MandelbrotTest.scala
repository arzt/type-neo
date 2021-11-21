package de.berlin.arzt.mandelbrot

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._
import matchers.should._


class MandelbrotTest extends AnyFlatSpec with Matchers {

  behavior of "MandelbrotTest"

  it should "mandelbrot" in {
    Mandelbrot.mandelbrot(0, 0,50) shouldBe 50
  }

}
