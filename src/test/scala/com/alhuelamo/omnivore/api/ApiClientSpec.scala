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

  test("gets errosrs given a non-existing article") {
    val article = testApiClient.getArticleContent("username", "slugNonExisting")
    assertEquals(article, ArticleResponse.ArticleError(Seq("\"NOT_FOUND\"")))
  }

}

private val stubBackend = SttpBackendStub.synchronous
  .whenRequestMatches(QueryMatchers.validArticle)
  .thenRespond(ArticleResponses.validArticle)
  .whenRequestMatches(QueryMatchers.nonExistingArticle)
  .thenRespond(ArticleResponses.nonExistingArticle)

object QueryMatchers {

  def validArticle(request: Request[?, ?]): Boolean =
    stubRequestForPayload(request, ApiClient.getArticlePayload("username", "slugValid"))

  def nonExistingArticle(request: Request[?, ?]): Boolean =
    stubRequestForPayload(request, ApiClient.getArticlePayload("username", "slugNonExisting"))

  private def stubRequestForPayload(request: Request[?, ?], expectedBodyData: ujson.Value): Boolean = {
    val requestBody = request.body
    val expectedBody = StringBody(ujson.write(expectedBodyData), "utf-8")
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

  val nonExistingArticle: String = ujson
    .Obj(
      "data" -> ujson.Obj(
        "article" -> ujson.Obj(
          "errorCodes" -> ujson.Arr("NOT_FOUND"),
        ),
      ),
    )
    .toString

}
