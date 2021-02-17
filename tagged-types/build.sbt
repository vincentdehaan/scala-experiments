name := "tagged-types"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.29"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"
libraryDependencies += "io.estatico" %% "newtype" % "0.4.4"
libraryDependencies += "com.softwaremill.common" %% "tagging" % "2.2.1"
libraryDependencies += "io.circe" %% "circe-generic" % "0.14.0-M3"
libraryDependencies += "io.circe" %% "circe-core" % "0.14.0-M3"
libraryDependencies += "io.circe" %% "circe-shapes" % "0.14.0-M3"
libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % "1.2.5"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "org.scanamo" %% "scanamo" % "1.0.0-M11"
libraryDependencies += "org.scanamo" %% "scanamo-formats" % "1.0.0-M11"

scalacOptions += "-Ymacro-annotations"