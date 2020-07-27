package foo

import javax.sound.midi._
import javax.sound.midi.ShortMessage._

import cats.effect._
import cats.implicits._
import scala.concurrent.duration._

class PurePiano(deviceReceiver: Receiver)(implicit T: Timer[IO]) {

  val bpm = 66
  val sq = 60L * 1000 / bpm / 4

  def note(key: Int, gateTime: Long): IO[Unit] = {
    val press = IO {
      val msg = new ShortMessage
      msg.setMessage(NOTE_ON, 0, key, 93)
      deviceReceiver.send(msg, -1)
    }
    val wait = IO.sleep(gateTime.millis)
    val release = IO {
      val msg = new ShortMessage
      msg.setMessage(NOTE_OFF, 0, key, 0)
      deviceReceiver.send(msg, -1)
    }
    press *> wait *> release
  }

  def C(n: Int = 1) = note(60, sq * n)
  def D(n: Int = 1) = note(62, sq * n)
  def E(n: Int = 1) = note(64, sq * n)
  def F(n: Int = 1) = note(65, sq * n)
  def G(n: Int = 1) = note(67, sq * n)
  def A(n: Int = 1) = note(69, sq * n)
  def B(n: Int = 1) = note(71, sq * n)
  def c(n: Int = 1) = note(72, sq * n)
}

object PurePiano {

  def make(implicit T: Timer[IO]): Resource[IO, PurePiano] =
    Resource
      .fromAutoCloseable(device)
      .evalTap(d => IO(d.open()))
      .map(d => new PurePiano(d.getReceiver()))

  val info = IO {
    MidiSystem.getMidiDeviceInfo().filter(_.getName == "Gervill").headOption
  }
  val device = info.flatMap {
    case Some(d) =>
      IO(MidiSystem.getMidiDevice(d))
    case None =>
      IO.raiseError(new Throwable("Could not find Gervill synthesizer"))
  }

}
