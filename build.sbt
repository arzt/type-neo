lazy val root = (project in file(".")).
  settings(
    name := "Type Neo!",
    version := "0.0.1",
    scalaVersion := "2.12.13",
    libraryDependencies ++= Seq(
      "org.openjfx" % "javafx-controls" % "16",
      "org.scalatest" %% "scalatest" % "3.2.8" % Test
    )
  )

mainClass in (Compile, run) := Some("de.berlin.arzt.neotrainer.Main")
