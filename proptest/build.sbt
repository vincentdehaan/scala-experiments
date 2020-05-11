name := "proptest"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test"
libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.3"

scalacOptions += "-Ybrowse:typer"