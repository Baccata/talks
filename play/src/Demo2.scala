package foo

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import cats.implicits._

import scala.concurrent.duration._

object Demo2 extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    PurePiano.make
      .evalTap(_ => IO.sleep(1.second))
      .map(new Songs(_))
      .use(playSong(args))
      .as(ExitCode.Success)

  def playSong(args: List[String])(songs: Songs): IO[Unit] =
    args.headOption.flatMap(_.toIntOption).foldMap {
      case 1 => songs.song1
      case 2 => songs.song2
      case 3 => songs.song3
      case 4 => songs.song4
      case 5 => songs.song5
      case 6 => songs.song6
      case _ => IO.unit
    } *> IO.sleep(1.second)

}
