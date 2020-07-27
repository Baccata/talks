import scala.scalajs.js
import js.annotation._
import scala.concurrent.duration.FiniteDuration

@js.native
@JSImport("soundfont-player", JSImport.Namespace)
object Soundfont extends js.Object {
  def instrument(
      audioContext: AudioContext,
      name: String
  ): js.Promise[Instrument] = js.native
}

@js.native
@JSGlobal
class Instrument extends js.Object {
  def play(
      note: String,
      start: js.UndefOr[Double] = js.undefined,
      duration: js.UndefOr[Double] = js.undefined,
  ): AudioNode = js.native
}

@js.native
trait PlayOptions extends js.Object {
  val duration: Float = js.native
}
object PlayOptions {
  def apply(duration: FiniteDuration): PlayOptions =
    js.Dynamic
      .literal(duration = duration.toMillis * 1000)
      .asInstanceOf[PlayOptions]
}

@js.native
@JSGlobal
class AudioContext() extends js.Object {
  def currentTime: Double = js.native
}

@js.native
@JSGlobal
class AudioNode extends js.Object {
  def stop(when: Float): Unit = js.native
}
