package de.berlin.arzt.mandelbrot

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._
import matchers.should._


class MandelbrotTest extends AnyFlatSpec with Matchers {

  behavior of "MandelbrotTest"

  it should "im" in {
    Mandelbrot.im(3, 5) should equal (5)
  }

  it should "prodIm" in {
    Mandelbrot.prodIm(1, 3, 2, 4) should equal (10)
  }

  it should "squareIm" in {
    Mandelbrot.squareIm(3, 4) should equal (24)
  }

  it should "len" in {
    Mandelbrot.len(3, 4) should be (25)
  }

  it should "plusRe" in {
    Mandelbrot.plusRe(1, 4, 5, 0) shouldBe 6
  }

  it should "prodRe" in {
    Mandelbrot.prodRe(3, 0, 4, 0) shouldBe 12
  }

  it should "mandelbrot" in {
    Mandelbrot.mandelbrot(0, 0,50) shouldBe 50
  }

  it should "re" in {
    Mandelbrot.re(0, 7) shouldBe 0
  }

  it should "plusIm" in {
    Mandelbrot.plusIm(4, 0, 5, 6) shouldBe 6
  }

  it should "squareRe" in {
    Mandelbrot.squareRe(6, 0) shouldBe 36
  }

}
