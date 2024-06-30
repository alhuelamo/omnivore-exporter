package com.alhuelamo.omnivore.api

import sttp.capabilities.WebSockets
import sttp.client3.{Identity, Request, StringBody}
import sttp.client3.testing.SttpBackendStub

class ApiClientSpec extends munit.FunSuite {

  val testApiClient = new ApiClient("token", stubBackend)

  test("gets article content given an existing article") {
    val article = testApiClient.getArticleContent("username", "slugValid")
    assertEquals(article, ArticleResponse.ArticleSuccess("idValid", "titleValid", "slugValid", "contentValid"))
  }

}

private val stubBackend = SttpBackendStub.synchronous.addValidArticleRequest()

extension (backend: SttpBackendStub[Identity, WebSockets]) {

  def addValidArticleRequest(): SttpBackendStub[Identity, WebSockets] =
    backend.whenRequestMatches(QueryMatchers.validArticle).thenRespond(ArticleResponses.validArticle)

}

object QueryMatchers {

  def validArticle(request: Request[?, ?]): Boolean = {
    val requestBody = request.body
    val expectedBody = StringBody(ujson.write(ApiClient.getArticlePayload("username", "slugValid")), "utf-8")
    requestBody == expectedBody
  }

}

object ArticleResponses {

  val validArticle: String = ujson
    .Obj(
      "data" -> ujson.Obj(
        "article" -> ujson.Obj(
          "article" -> ujson.Obj(
            "id" -> "idValid",
            "title" -> "titleValid",
            "slug" -> "slugValid",
            "content" -> "contentValid",
          ),
        ),
      ),
    )
    .toString

}
