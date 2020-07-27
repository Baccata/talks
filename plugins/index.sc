
import $ivy.`com.lihaoyi::scalatags:0.8.2`

import scalatags.Text.all._

import scalatags.Text.TypedTag

case class Talk(title: String, link: String)

def build(talks : Vector[Talk]) = {
  layout(
    row(
      column(
        card("Talks", renderTalks(talks))
      )
    )
  )
}

private def layout(contents: TypedTag[String]*) = {
  html(
    head(
      scalatags.Text.tags2.title("Olivier's talks"),
      link(
        rel := "stylesheet",
        href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css",
        attr("integrity") := "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk",
        attr("crossorigin") := "anonymous"
      )
    ),
    body(
      div(cls := "container-fluid",
          div(
            cls := "jumbotron",
            h1(cls := "display-4", "Olivier's talks"),
            hr,
            for (c <- contents) yield c
          )))
  )
}

private def row(contents: TypedTag[String]*) = {
  div(cls := "row justify-content-md-center", for (c <- contents) yield c)
}

private def column(contents: TypedTag[String]*) = {
  div(cls := "col-sm", for (c <- contents) yield c)
}

private def card(_title: String, _body: TypedTag[String]) = {
  div(
    cls := "card",
    div(cls := "card-header", h3(_title)),
    div(
      cls := "card-body",
      _body
    )
  )
}

private def renderTalks(talks: Vector[Talk]) = {
  ul(
    for (talk <- talks) yield {
      li(
        strong(raw(talk.title)),
        p(a(href := talk.link, "Slides"))
      )
    }
  )
}

implicit class MkOps[A](it: Vector[A]) {
  def joinWith(sep: A): Vector[A] = {
    if (it.nonEmpty)
      it.flatMap(el => Vector[A](el, sep)).init
    else it
  }
}
