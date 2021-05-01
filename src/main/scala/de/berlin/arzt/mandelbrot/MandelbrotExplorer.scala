package de.berlin.arzt.mandelbrot

import javafx.animation.AnimationTimer
import javafx.application.{Application, Platform}
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.{Executors, TimeUnit}

object MandelbrotExplorer {
  def main(args: Array[String]) {
    Application.launch(classOf[MandelbrotExplorer], args: _*)
  }
}

class MandelbrotExplorer extends Application {

  def start(primaryStage: Stage) {
    primaryStage.setTitle("Mandel")
    val canvas: Canvas = new Canvas()
    val context = canvas.getGraphicsContext2D
    val pane: BorderPane = new BorderPane()
    pane.setCenter(canvas)
    val width = 1600
    val height = 900
    val maxIter = 2048
    val s: Scene = new Scene(pane, width, height)
    canvas.widthProperty.bind(s.widthProperty)
    canvas.heightProperty.bind(s.heightProperty)
    val wi = new WritableImage(width, height)
    var abort = false
    var xOffset = -4d
    var yOffset = -2.0d
    var zoom = 7

    val scheduler = Executors.newScheduledThreadPool(20)
    val mbd = new MandelbrotDrawer(maxIter, wi.getPixelWriter.setArgb, () => abort)

    def step(): Unit = {
      val step = 1.0 / math.pow(2, zoom)
      println(f"step: ${step}")
      abort = false
      def task(xStart: Double, yStart: Double, xEnd: Double, yEnd: Double): Runnable =
        () => {
          mbd.compute(
            xStart = (xStart * width).toInt,
            yStart = (yStart * height).toInt,
            xEnd = (xEnd * width).toInt,
            yEnd = (yEnd * height).toInt,
            xOffset = xOffset,
            yOffset = yOffset,
            step = step
          )
        }
      Array(
        task(0/8d, 0/8d, 2/8d, 2/8d), task(2/8d, 0/8d, 4/8d, 2/8d), task(4/8d, 0/8d, 6/8d, 2/8d), task(6/8d, 0/8d, 8/8d, 2/8d),
        task(0/8d, 2/8d, 2/8d, 4/8d), task(2/8d, 2/8d, 4/8d, 4/8d), task(4/8d, 2/8d, 6/8d, 4/8d), task(6/8d, 2/8d, 8/8d, 4/8d),
        task(0/8d, 4/8d, 2/8d, 6/8d), task(2/8d, 4/8d, 4/8d, 6/8d), task(4/8d, 4/8d, 6/8d, 6/8d), task(6/8d, 4/8d, 8/8d, 6/8d),
        task(0/8d, 6/8d, 2/8d, 8/8d), task(2/8d, 6/8d, 4/8d, 8/8d), task(4/8d, 6/8d, 6/8d, 8/8d), task(6/8d, 6/8d, 8/8d, 8/8d)
      ).foreach(t => scheduler.schedule(t, 0, SECONDS))
    }

    val timer = new AnimationTimer() {
      def handle(now: Long): Unit = {
        context.drawImage(wi, 0, 0)
      }
    }
    step()

    canvas.setOnScroll(
      x => {
        abort = true
        val xx = x.getX
        val yy = x.getY
        val delta = x.getDeltaY.signum
        if (delta != 0) {
          val ax = xx/math.pow(2, zoom)
          val ay = yy/math.pow(2, zoom)
          zoom += delta
          val bx = xx/math.pow(2, zoom)
          val by = yy/math.pow(2, zoom)
          xOffset += ax - bx
          yOffset += ay - by
          step()
        }
/**/
        println((xOffset, yOffset, zoom))
      }
    )

    new OffsetEvent(
      canvas,
      (x, y) => {
        abort = true
        xOffset -= x/math.pow(2, zoom)
        yOffset -= y/math.pow(2, zoom)
        println((xOffset, yOffset, zoom))
        step()
      }
    )
    timer.start()
    primaryStage.setScene(s)
    primaryStage.show()
    Platform.setImplicitExit(true)
    primaryStage.setOnCloseRequest(_ => {
      timer.stop()
      scheduler.shutdownNow()
    })
  }

}
