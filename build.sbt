import bintray.Keys._

sbtPlugin := true

name := "sbt-find-classes"

description := "Find classes in classpath"

organization := "com.github.walfie"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

publishMavenStyle := false

repository in bintray := "sbt-plugins"

bintrayPublishSettings

bintrayOrganization in bintray := None

