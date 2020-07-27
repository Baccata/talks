import cats.effect._
import scala.concurrent.duration._
import cats.implicits._

class Piano(audioContext: AudioContext, soundfont: Instrument)(implicit
    T: Timer[IO]
) {

  val bpm = 66
  val sq = 60L * 1000 / bpm / 4

  def note(n: String, t: Int): IO[Unit] = {
    val duration = (t * sq).millis
    IO {
      soundfont.play(
        n,
        audioContext.currentTime,
        (t * sq)
      )
    } >> IO.sleep(duration)
  }

  def C(n: Int = 1) = note("C5", n)
  def D(n: Int = 1) = note("D5", n)
  def E(n: Int = 1) = note("E5", n)
  def F(n: Int = 1) = note("F5", n)
  def G(n: Int = 1) = note("G5", n)
  def A(n: Int = 1) = note("A5", n)
  def B(n: Int = 1) = note("B5", n)
  def c(n: Int = 1) = note("C6", n)

}

object Piano {

  def make(implicit CS: ContextShift[IO], T: Timer[IO]): Resource[IO, Piano] = {
    val setup = for {
      ac <- IO(new AudioContext())
      sf <- IO.fromFuture(
        IO(Soundfont.instrument(ac, "acoustic_grand_piano").toFuture)
      )
    } yield (ac, sf)

    Resource.liftF(setup).map {
      case (ac, sf) => new Piano(ac, sf)
    }
  }

}
