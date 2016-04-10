package com.github.walfie

import java.io.File
import sbt._
import sbt.complete.DefaultParsers._
import sbt.Keys._

object FindClassesPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    lazy val allClasses = taskKey[Stream[String]]("All classes in classpath")
    lazy val findClasses = inputKey[Stream[String]]("Find and print classes with matching name")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    findClasses := {
      val any = ".*"
      val parts: Seq[String] = spaceDelimited("<arg>").parsed
      val caseSensitive: Boolean = parts.exists(_.matches(s"$any[A-Z]$any"))
      val regex: String = (if (caseSensitive) "" else "(?i)") + parts.mkString(any, any, any)

      val matches: Stream[String] = allClasses.value.filter(_.matches(regex))
      matches.foreach(println)
      matches
    },

    allClasses := fullClasspath.in(Compile).value.toStream.flatMap { attr =>
      classNamesFromFile(attr.data)
    }
  )

  private def classNamesFromFile(file: File): Iterable[String] = {
    val filePath: String = file.getAbsolutePath

    def isClass(path: String): Boolean =
      path.endsWith(".class") && !path.contains("$")

    def clean(path: String): String = path
      .replaceAll(s"^$filePath", "")
      .replaceAll(".class$", "")
      .replaceAll("^/", "")
      .replace("/", ".")

    def getClassFileNames(f: File): Iterable[String] = {
      if (f.isFile) {
        val path: String = f.getAbsolutePath

        if (isClass(path)) {
          Seq(clean(path))
        } else if (path.endsWith(".jar")) {
          // Could do this without an external command, but whatever
          s"jar tf ${f.getAbsolutePath}"
            .lines_!
            .withFilter(isClass)
            .map(clean)
        } else Seq.empty
      } else if (f.isDirectory) {
        f.listFiles.flatMap(getClassFileNames)
      } else {
        Seq.empty
      }
    }

    getClassFileNames(file)
  }
}

