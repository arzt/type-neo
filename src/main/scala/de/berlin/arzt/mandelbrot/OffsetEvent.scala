package de.berlin.arzt.mandelbrot

import javafx.scene.Node
import javafx.scene.input.MouseEvent

class OffsetEvent(node: Node, callBack: (Double, Double) => Unit) {
  var last: Option[MouseEvent] = None
  node.setOnMouseDragged(
    x => {
      last.foreach(
        event => {
          val xOffset = x.getX - event.getX
          val yOffset = x.getY - event.getY
          callBack(xOffset, yOffset)
        }
      )
      last = Some(x)
    }
  )

  node.setOnMouseReleased(_ => last = None)
}
