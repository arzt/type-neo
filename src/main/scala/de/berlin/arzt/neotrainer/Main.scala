package de.berlin.arzt.neotrainer

import java.nio.file.{Files, Path, Paths}
import javafx.animation.AnimationTimer
import javafx.application.{Application, Platform}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.control.{CheckBox, RadioButton, ToggleGroup}
import javafx.scene.input.{KeyEvent, MouseEvent}
import javafx.scene.layout.{BorderPane, GridPane, HBox}
import javafx.stage.{Stage, WindowEvent}

object Main {
  private val resGermanDict: String = "/de/berlin/arzt/neotype/dict/german"
  private val resEnglishDict: String = "/de/berlin/arzt/neotype/dict/english"

  def main(args: Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }
}

class Main extends Application {
  var controlRandom = new RadioButton("Random")
  var controlDictionary = new RadioButton("Dictionary")
  var controlLowercase = new CheckBox("Lowercase")
  var controlUppercase = new CheckBox("Uppercase")
  var controlNumbers = new CheckBox("Numbers")
  var controlSpecialI = new CheckBox("Special I")
  var controlSpecialII = new CheckBox("Special II")
  var controlSpecialIII = new CheckBox("Special III")
  var controlDictionaries: Iterable[RadioButton] = List.empty[RadioButton]
  var checkBoxEnabled: Option[CheckBox] = None
  var checkBoxDisabled: CheckBox = null
  var vb1 = new HBox
  var vb2 = new HBox
  var germanDict: Path = null
  var englishDict: Path = null
  var timer: AnimationTimer = null
  var typeDrawer: TypeCanvas = null
  var context: GraphicsContext = null
  var provider: Option[CharProvider] = None
  var dictionaries: Option[Map[String, Set[String]]] = None

  import collection.JavaConversions._

  def start(primaryStage: Stage) {
    val p: Path = Paths.get(System.getProperty("user.home"), ".config", "type-neo")
    val p2: Path = Files.createDirectories(p)
    val f = Files.list(p2).iterator().toStream.view
    val v = f.view

    primaryStage.setTitle("TipeWryte")
    val canvas: Canvas = new Canvas
    context = canvas.getGraphicsContext2D
    typeDrawer = new TypeCanvas
    vb1.setSpacing(10)
    vb2.setSpacing(10)
    vb1.getChildren.addAll(controlLowercase, controlUppercase, controlNumbers, controlSpecialI, controlSpecialII, controlSpecialIII)
    vb2.getChildren.addAll(controlDictionaries)
    controlDictionary.setSelected(true)
    controlDictionaries.headOption.foreach(_.setSelected(true))
    val group: ToggleGroup = new ToggleGroup
    controlDictionary.setToggleGroup(group)
    controlRandom.setToggleGroup(group)
    val group2: ToggleGroup = new ToggleGroup
    val handler: EventHandler[ActionEvent] = new EventHandler[ActionEvent] {
      def handle(event: ActionEvent) {
        handleInputs(event)
      }
    }

    class DictEventHandler(name: String) extends EventHandler[ActionEvent] {
      def handle(event: ActionEvent): Unit = {
        println("clicked at: " + name)
      }
    }
    controlDictionaries = for (dict <- dictionaries.toIterable; d <- dict) yield {
      val b = new RadioButton(d._1)
      b.setOnAction(new DictEventHandler(d._1))
      b
    }
    controlDictionaries.foreach { control =>
      control.setToggleGroup(group2)
      control.setOnAction(handler)
    }

    controlLowercase.setOnAction(handler)
    controlLowercase.setSelected(true)
    controlUppercase.setOnAction(handler)
    controlNumbers.setOnAction(handler)
    controlSpecialI.setOnAction(handler)
    controlSpecialII.setOnAction(handler)
    controlSpecialIII.setOnAction(handler)
    controlDictionary.setOnAction(handler)
    controlRandom.setOnAction(handler)
    val grid: GridPane = new GridPane
    grid.setHgap(30)
    grid.setVgap(30)
    grid.setPadding(new Insets(40))
    grid.add(controlRandom, 0, 0)
    grid.add(vb1, 1, 0)
    grid.add(controlDictionary, 0, 1)
    grid.add(vb2, 1, 1)
    val pane: BorderPane = new BorderPane
    pane.setCenter(canvas)
    pane.setTop(grid)
    val s: Scene = new Scene(pane, 1200, 800)
    canvas.widthProperty.bind(s.widthProperty)
    canvas.heightProperty.bind(s.heightProperty)
    typeDrawer.widthProperty.bind(s.widthProperty)
    typeDrawer.heightProperty.bind(s.heightProperty)

    s.addEventHandler(KeyEvent.ANY, new EventHandler[KeyEvent] {
      def handle(event: KeyEvent): Unit = {
        filterKeyPressedEvent(event)
      }
    }
    )
    timer = new AnimationTimer() {
      def handle(now: Long) {
        typeDrawer.paintComponent(now, context)
      }
    }
    timer.start
    handleInputs(null)
    primaryStage.setScene(s)
    primaryStage.show
    Platform.setImplicitExit(true)
    primaryStage.setOnCloseRequest(
      new EventHandler[WindowEvent] {
        def handle(e: WindowEvent) {
          handleExit
        }
      }
    )
  }

  def handleExit {
    timer.stop
    typeDrawer.shutdownPool
  }

  def handleInputs(event: ActionEvent) {
    var chars = Set.empty[Char]
    var count: Int = 0
    if (controlRandom.isSelected) {
      vb2.setDisable(true)
      vb1.setDisable(false)
      if (controlLowercase.isSelected) {
        chars ++= CharClasses.LOWERCASE_LETTERS
        count += 1
        checkBoxEnabled = Some(controlLowercase)
      }
      if (controlUppercase.isSelected) {
        chars ++= CharClasses.UPPERCASE_LETTERS
        count += 1
        checkBoxEnabled = Some(controlUppercase)
      }
      if (controlNumbers.isSelected) {
        chars ++= CharClasses.NUMBERS
        count += 1
        checkBoxEnabled = Some(controlNumbers)
      }
      if (controlSpecialI.isSelected) {
        chars ++= CharClasses.SPECIAL_CHARACTERS_1
        count += 1
        checkBoxEnabled = Some(controlSpecialI)
      }
      if (controlSpecialII.isSelected) {
        chars ++= CharClasses.SPECIAL_CHARACTERS_2
        count += 1
        checkBoxEnabled = Some(controlSpecialII)
      }
      if (controlSpecialIII.isSelected) {
        chars ++= CharClasses.SPECIAL_CHARACTERS_3
        count += 1
        checkBoxEnabled = Some(controlSpecialIII)
      }
      if (count == 1) {
        checkBoxEnabled.foreach(_.setDisable(true))
        checkBoxDisabled = checkBoxEnabled.get
      }
      else if (checkBoxDisabled != null) {
        checkBoxDisabled.setDisable(false)
      }
      val p = new DefaultCharProvider(chars)
      this.provider = Some(p)
      typeDrawer.setCharProvider(p)
    }
    else if (controlDictionary.isSelected) {
      vb1.setDisable(true)
      vb2.setDisable(false)
    }
  }

  def handleMouseEvent(event: MouseEvent) {
    System.out.println("test: " + event.getX + "," + event.getY + ", " + event.getZ)
  }

  def filterKeyPressedEvent(event: KeyEvent) {
    if (event.getEventType eq KeyEvent.KEY_TYPED) {
      for (p <- provider) {
        typeDrawer.processKeyEvent(event, p)
      }
    }
    event.consume
  }
}
