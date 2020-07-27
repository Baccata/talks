import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill._
import scalalib._
import scalajslib._
import scalajslib.api._

import $file.plugins.mdoc
import $file.plugins.webpack
import webpack.JsDeps

object foo extends ScalaModule {
  def scalaVersion = "2.13.1"
  def scalacPluginIvyDeps =
    Agg(
      ivy"com.olegpy::better-monadic-for:0.3.1"
    )

  object test extends Tests {
    def ivyDeps =
      Agg(
        ivy"com.disneystreaming::weaver-framework:0.4.2-RC1",
        ivy"com.disneystreaming::weaver-scalacheck:0.4.2-RC1",
        ivy"com.disneystreaming::weaver-zio:0.4.2-RC1",
        ivy"org.http4s::http4s-dsl:0.21.0",
        ivy"org.http4s::http4s-blaze-client:0.21.0"
      )
    def testFrameworks = Seq("weaver.framework.TestFramework")
  }
}

object talks extends mdoc.MdocModule {

  def scalaVersion = "2.13.2"
  def mdocVersion = "2.2.1"
  def ivyDeps =
    Agg(
      ivy"org.typelevel::cats-effect:2.1.3"
    )

  override def mdocSourceDirectory = T.sources { millSourcePath }

  def build =
    T {
      val dest = T.ctx().dest
      os.makeDir.all(dest)
      val output = mdoc()
      os.list(os.pwd / "talks").filterNot(_.ext == "md").foreach { path =>
        os.copy.over(path, dest / path.last)
      }
      os.list(output.path).filter(_.ext == "md").map { path =>
        os.copy.over(path, dest / path.last)
      }
      val jsBundle = playjs.devWebpack()
      jsBundle.foreach { pathRef =>
        val path = pathRef.path
        os.copy.over(path, dest / "assets" / path.last)
      }
      os.makeDir.all(os.pwd / "docs")
      os.proc(
        "reveal-md",
        dest,
        "--static",
        os.pwd / "docs",
        s"--static-dirs=assets"
      ).call(cwd = dest)
    }

  def title(path: os.Path) = path.last.dropRight(path.ext.size + 1).capitalize

}

object play extends ScalaModule {
  def scalaVersion = "2.13.2"
  def ivyDeps =
    Agg(
      ivy"org.typelevel::cats-effect:2.1.3"
    )

  def demo1() = runMain("foo.Demo1")
  def song(x: Int) = runMain("foo.Demo2", x.toString)

}

object playjs extends webpack.ScalaJSWebpackApplicationModule {

  def scalaVersion = "2.13.2"
  def scalaJSVersion = "1.1.0"
  def moduleKind = ModuleKind.CommonJSModule

  def ivyDeps =
    Agg(
      ivy"org.typelevel::cats-effect::2.1.3",
      ivy"com.raquo::laminar::0.9.2"
    )

  override def jsDeps =
    T {
      super.jsDeps() ++ JsDeps("soundfont-player" -> "0.12.0")
    }

}
