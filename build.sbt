lazy val root = (project in file(".")).
  settings(
    name := "Type Neo!",
    version := "0.0.1",
    scalaVersion := "2.11.5"
  )

mainClass in (Compile, run) := Some("de.berlin.arzt.neotrainer.Main")

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))