package foo

import cats.effect._
import cats.implicits._

import scala.concurrent.duration._

// format: off
class Songs(piano: PurePiano)(implicit T: Timer[IO], CS: ContextShift[IO]) {
  import piano._


  val notes = List(C(), D(), E(), F(), G(), A(), B(), c(2))

  val song1: IO[Unit] = notes.sequence.void

  val song2: IO[Unit] = notes.reverse.sequence.void

  val song3 = song1 *> song2

  val song4 = repeat(song1)

  val await = IO.sleep((2 * sq).millis)

  val song5 = (await *> song4)

  val song6 = (song4, song5).parTupled.void

  def repeat(io: IO[Unit]): IO[Nothing] = io >> repeat(io)

}



















