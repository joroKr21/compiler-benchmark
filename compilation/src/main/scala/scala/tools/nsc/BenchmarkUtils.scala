package scala.tools.nsc

import java.io.{File, IOException}
import java.net.URL
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

object BenchmarkUtils {
  class FilterExprProcessor(scalaVersion: String) {
    var on = true

    // 0: no matching //#if yet
    // 1: matching //#if found
    private var state = 0

    def apply(trimmedLine: String): Unit = {
      val (cond, expr) = trimmedLine.split(" ").toList.filter(_.nonEmpty) match {
        case c :: e :: _ => (c, e)
        case c :: _ => (c, "")
      }
      def exprMatches = expr.split('|').exists(scalaVersion.startsWith)
      cond match {
        case "//#if" =>
          if (exprMatches && state == 0) {
            on = true
            state = 1
          } else {
            on = false
          }
        case "//#else" =>
          on = state == 0
        case "//#fi" =>
          on = true
          state = 0
      }
    }
  }

  def deleteRecursive(directory: Path): Unit = {
    if (Files.exists(directory)) {
      Files.walkFileTree(directory, new SimpleFileVisitor[Path]() {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          Files.delete(file)
          FileVisitResult.CONTINUE
        }
        override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
          Files.delete(dir)
          FileVisitResult.CONTINUE
        }
      })
    }
  }

  def initDeps(corpusSourcePath: Path): Seq[Path] = {
    val depsDir = Paths.get(ConfigFactory.load.getString("deps.localdir"))
    val depsFile = corpusSourcePath.resolve("deps.txt")
    val depsClasspath = Seq.newBuilder[Path]
    if (Files.exists(depsFile)) {
      for (depUrlString <- Files.lines(depsFile).iterator().asScala) {
        val depUrl = new URL(depUrlString)
        val filename = Paths.get(depUrl.getPath).getFileName.toString
        val depFile = depsDir.resolve(filename)
        // TODO: check hash if file exists, or after downloading
        if (!Files.exists(depFile)) {
          if (!Files.exists(depsDir)) Files.createDirectories(depsDir)
          val in = depUrl.openStream
          Files.copy(in, depFile, StandardCopyOption.REPLACE_EXISTING)
          in.close()
        }
        depsClasspath += depFile
      }
      depsClasspath.result()
    } else Nil
  }
}
