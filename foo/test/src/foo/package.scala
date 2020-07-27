import weaver.SourceLocation
import weaver._
import cats.effect.IO
import scala.concurrent.duration._

package object foo {

  implicit def toExp(
      se: IO[SingleExpectation]
  )(implicit s: SourceLocation): IO[Expectations] =
    se.map(_.toExpectations)

  def printStr(a: Any) = IO(println(a))

}
