import cats.effect._

import org.http4s.client.blaze._
import org.http4s.client._

class Http(client: Client[IO]) {
  def get(url: String): IO[(Int, String)] =
    client.get(url)(response =>
      response.as[String].map(body => (response.status.code, body))
    )
}
