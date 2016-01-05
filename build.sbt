name := """play-knockout-sample"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  // persistence
  "com.github.unterstein" %% "play-elasticplugin" % "0.2.0",
  // utility
  "com.google.code.gson" % "gson" % "2.4",
  // webjars
  "org.webjars" %% "webjars-play" % "2.4.0-2",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "bootswatch-flatly" % "3.3.5+4",
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "font-awesome" % "4.5.0",
  // webjars -> knockout
  "org.webjars" % "knockout" % "3.4.0",
  "org.webjars.bower" % "knockout-mapping" % "2.4.1",
  "org.webjars.bower" % "knockout-validation" % "2.0.3" exclude("org.webjars.bower", "knockout")
)

resolvers += "unterstein.github.io" at "http://unterstein.github.io/repo"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
