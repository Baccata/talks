import weaver._
import cats.effect._

import org.http4s.client.blaze._
import org.http4s.client._

object HttpSuite extends IOSuite {

  override type Res = Http
  override def sharedResource: Resource[IO, Http] =
    BlazeClientBuilder[IO](ec).resource.map(new Http(_))

  test("Good requests lead to good results") { http =>
    for {
      (status, _) <- http.get("https://httpbin.org/get")
    } yield expect(status == 200)
  }

  test("Bad requests lead to bad results") { (http, log) =>
    for {
      (status, body) <- http.get("https://httpbin.org/oops")
      _ <- log.info(body)
    } yield expect(status == 404)
  }

}
