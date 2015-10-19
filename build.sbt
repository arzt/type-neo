lazy val root = (project in file(".")).
  settings(
    name := "Type Neo!",
    version := "0.0.1",
    scalaVersion := "2.11.4"
  )

mainClass in (Compile, run) := Some("de.berlin.arzt.neotrainer.Main")