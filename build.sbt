import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "A Very Brief Introduction to cats-effect",
    libraryDependencies ++= Seq("org.typelevel" %% "cats-effect" % "3.3.12")
  )
