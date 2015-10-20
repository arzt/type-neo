package de.berlin.arzt.neotrainer

import java.util.concurrent.Executors
import javafx.beans.property.{DoubleProperty, SimpleDoubleProperty}
import javafx.geometry.Bounds
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import javafx.scene.text.{Font, Text}

object TypeCanvas {
  val BACKSPACE: Char = 8.toChar
}

class TypeCanvas {
  var width: DoubleProperty = new SimpleDoubleProperty(0)
  var heigt: DoubleProperty = new SimpleDoubleProperty(0)

  def widthProperty: DoubleProperty = {
    return width
  }

  def heightProperty: DoubleProperty = {
    return heigt
  }

  var hitsPerMinute: Int = 0
  var maxHitsPerMinute: Int = 0
  var fps: Int = 0
  var xOffset: Double = 0.3
  var yOffset: Double = .0
  var shiftOffset: Double = .0
  var lastWidth: Double = .0
  var animationDiscount: Double = 0.999999999
  var wrong: StringBuilder = new StringBuilder()
  var then = 0L
  var pool = Executors.newCachedThreadPool
  var glyph: Text = new Text
  final val hitPerMinuteRunnable: Runnable = new Runnable() {
    def run(): Unit = {
      hitsPerMinute += 1
      maxHitsPerMinute = Math.max(hitsPerMinute, maxHitsPerMinute);
      try {
        Thread.sleep(60000);
      } catch {
        case e: Throwable => ;
      }
      hitsPerMinute -= 1
    }
  }
  private var right = Ring.ofChar(15)
  private var left = Ring.ofChar(15)

  def getAnimationDiscount: Double = {
    return animationDiscount
  }

  def setAnimationDiscount(animationDiscount: Double) {
    this.animationDiscount = animationDiscount
  }

  def paintComponent(now: Long, g: GraphicsContext) {
    val width: Double = this.width.get
    val height: Double = this.heigt.get
    g.clearRect(0, 0, width, height)
    val unit: Double = width
    yOffset = height / 2.0
    var lastX: Double = xOffset * width + shiftOffset
    val fontsize: Int = (unit * 0.08).toInt
    val f: Font = new Font("serif", fontsize)
    glyph.setFont(f)
    g.setFont(f)
    g.setFill(Color.BLACK)
    val text: String = hitsPerMinute + " " + maxHitsPerMinute
    g.fillText(text, width * 0.1d, height * 0.2d)
    var c: Char = right(0)
    var character: String = Character.toString(c)
    if (c == 8.toChar) {
      character = "<back>"
    }
    glyph.setText(character)
    lastWidth = glyph.getLayoutBounds.getWidth
    g.setFill(Color.BLACK)
    g.fillText(character, lastX, yOffset)
    lastX += lastWidth
    g.setFill(Color.RED)

    var i: Int = 0
    while (i < wrong.length) {
      {
        c = wrong.charAt(i)
        character = String.valueOf(c)
        if (c == 8.toChar) {
          character = "<back>"
        }
        glyph.setText(character)
        g.fillText(character, lastX, yOffset)
        lastX += glyph.getLayoutBounds.getWidth
      }
      i += 1
    }

    g.setFill(Color.GRAY)

    i = 1
    while (i < right.length) {

      c = right(i)
      character = String.valueOf(c)
      if (c == 8.toChar) {
        character = "<back>"
      }
      glyph.setText(character)
      g.fillText(character, lastX, yOffset)
      lastX += glyph.getLayoutBounds.getWidth


      i += 1;
    }

    lastX = xOffset * width + shiftOffset
    g.setFill(Color.LIGHTGRAY)

    i = left.length - 1
    while (i >= 0) {
      {
        c = left(i)
        var s: String = String.valueOf(c)
        if (c == 8.toChar) {
          s = "<back>"
        }
        glyph.setText(s)
        val bounds: Bounds = glyph.getLayoutBounds
        val glyphX: Double = lastX - bounds.getWidth
        g.fillText(s, glyphX, yOffset)
        lastX = lastX - bounds.getWidth
      }
      i -= 1
    }

    val discount: Double = Math.pow(animationDiscount, now - then)
    val discount2: Double = Math.pow(0.99999, shiftOffset)
    shiftOffset *= discount2 * discount
    this.then = now
  }

  def shutdownPool {
    pool.shutdownNow
  }

  def processKeyEvent(e: KeyEvent, provider: CharProvider) {
    if (e.getEventType eq KeyEvent.KEY_TYPED) {
      //println(s"Key Event: $e")
      val character: String = e.getCharacter
      val code = e.getCode
      val text = e.getText
      //println(s"Character: '$character' (Length: ${character.length}}), Code: $code, Text: $text")
      if (character.length == 1) {
        val c = character.charAt(0)
        if (c == right(0) && wrong.length == 0) {
          pool.execute(hitPerMinuteRunnable)
          provider.reward(c)
          val nextC = provider.getNextChar
          val cOld: Char = right(0)
          right.append(nextC)
          if (left.length > 0) {
            left.append(cOld)
          }
          shiftOffset += lastWidth
        } else {
          if (c == TypeCanvas.BACKSPACE) {
            if (wrong.length > 0) {
              wrong.deleteCharAt(wrong.length - 1)
            }
          } else {
            //wrong.append(c)
            wrong += c
            provider.penalize(right(0))
          }
        }
      } else if (character.length == 0) {
        if (wrong.length > 0) {
          wrong.deleteCharAt(wrong.length - 1)
        }
      }
    }
  }

  def setCharProvider(provider: CharProvider) {
    var i = 0
    while (i < left.length) {
      val c = provider.getNextChar
      left.update(i, c)
      i += 1
    }
    i = 0
    while (i < right.length) {
      val c = provider.getNextChar
      right.update(i, c)
      i += 1
    }
  }
}