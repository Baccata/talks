import com.raquo.laminar.api.L._
import org.scalajs.dom
import cats.implicits._
import cats.effect._
import scala.concurrent.Future
import com.raquo.airstream.signal.Signal
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext

object Main {
  implicit val cs = IO.contextShift(ExecutionContext.global)
  implicit val t = IO.timer(ExecutionContext.global)

  def main(args: Array[String]): Unit = {

    def repeat[A](io: IO[A]): IO[Nothing] =
      io >> IO.cancelBoundary >> repeat(io)

    def demo1(piano: Piano) = {
      import piano._
      val scale = List(C(), D(), E(), F(), G(), A(), B(), c(2))
      scale.sequence.void
    }

    def demo2(piano: Piano) = {
      import piano._
      val scale = List(C(), D(), E(), F(), G(), A(), B(), c(2))
      val back = repeat(scale.sequence.void)
      val front =
        repeat(scale.reverse.sequence.void).delayBy((piano.sq * 2).millis)
      (front, back).parTupled.void
    }

    def play(song: Piano => IO[Unit]): IO[Unit] = {
      Piano.make.use(song)
    }

    def makeButton(action: IO[Unit]) = {
      val variable = Var[Option[Fiber[IO, Unit]]](None)
      val buttonLabel: Signal[String] =
        variable.signal.map(_.fold("Play")(_ => "Stop"))
      val observer = Observer.apply[Any](_ =>
        variable.now() match {
          case None => variable.set(Some(action.start.unsafeRunSync()))
          case Some(value) =>
            println("hello ?"); value.cancel.unsafeRunAsyncAndForget();
            variable.set(None)
        }
      )
      div(button(child.text <-- buttonLabel, onClick --> observer))
    }

    render(
      dom.document.querySelector("#demo1"),
      div(
        div(idAttr := "test1"),
        makeButton(play(demo1))
      )
    )
    render(
      dom.document.querySelector("#demo2"),
      div(
        div(idAttr := "test2"),
        makeButton(play(demo2))
      )
    )
  }
}
