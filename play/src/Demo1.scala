package foo

import javax.sound.midi._
import javax.sound.midi.ShortMessage._

object Demo1 {

  def main(args: Array[String]): Unit = {
    import javax.sound.midi._
    import javax.sound.midi.ShortMessage._

    val info =
      MidiSystem.getMidiDeviceInfo().filter(_.getName == "Gervill").headOption
    val device = info.map(MidiSystem.getMidiDevice).getOrElse {
      println("[ERROR] Could not find Gervill synthesizer.")
      sys.exit(1)
    }

    device.open()
    val rcvr = device.getReceiver()

    def note(key: Int, gateTime: Long) = {
      val msg = new ShortMessage

      msg.setMessage(NOTE_ON, 0, key, 93)
      rcvr.send(msg, -1)

      Thread.sleep(gateTime)

      msg.setMessage(NOTE_OFF, 0, key, 0)
      rcvr.send(msg, -1)
    }

    val bpm = 66
    val sq =
      60L * 1000 / bpm / 4 // 16th note (semiquaver) duration in milli second

    def C(n: Int = 1) = note(60, sq * n)
    def D(n: Int = 1) = note(62, sq * n)
    def E(n: Int = 1) = note(64, sq * n)
    def F(n: Int = 1) = note(65, sq * n)
    def G(n: Int = 1) = note(67, sq * n)
    def A(n: Int = 1) = note(69, sq * n)
    def B(n: Int = 1) = note(71, sq * n)
    def c(n: Int = 1) = note(72, sq * n)

    def silent() = Thread.sleep(sq)

    silent()

    C()
    D()
    E()
    F()
    G()
    A()
    B()
    c(2)

    Thread.sleep(sq * 2)

    device.close()

  }

}
