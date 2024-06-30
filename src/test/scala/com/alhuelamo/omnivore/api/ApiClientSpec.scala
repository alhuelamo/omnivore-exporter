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
    assertEquals(article, ArticleResponse.ArticleError(Seq("NOT_FOUND")))
  }

}

private val stubBackend = SttpBackendStub.synchronous
  .addValidArticleRequest()
  .addNonExistingArticleRequest()

extension (backend: SttpBackendStub[Identity, WebSockets]) {

  def addValidArticleRequest(): SttpBackendStub[Identity, WebSockets] =
    backend.addToBackend(QueryMatchers.validArticle, ArticleResponses.validArticle)

  def addNonExistingArticleRequest(): SttpBackendStub[Identity, WebSockets] =
    backend.addToBackend(QueryMatchers.nonExistingArticle, ArticleResponses.nonExistingArticle)

  private def addToBackend(requestMatcher: Request[?, ?] => Boolean, response: String): SttpBackendStub[Identity, WebSockets] =
    backend.whenRequestMatches(requestMatcher).thenRespond(response)

}

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
          "errorCodes" -> Seq("NOT_FOUND"),
        ),
      ),
    )
    .toString

}
