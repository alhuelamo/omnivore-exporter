package com.alhuelamo.omnivore.api

import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend}
import sttp.client3.quick.*
import sttp.model.Uri
import upickle.core.LinkedHashMap

class ApiClient(apiToken: String) {
  import ApiClient._

  def getArticleContent(username: String, articleSlug: String): ArticleResponse = {
    val response = request
      .body(ujson.write(getArticlePayload(username, articleSlug)))
      .send(backend)

    val jsonResponse = ujson.read(response.body)
    parseArticleResponse(jsonResponse)
  }

  private def request = quickRequest
    .post(url)
    .header("authorization", apiToken)
    .contentType("application/json")

}

object ApiClient {

  private val backend: SttpBackend[Identity, Any] = HttpClientSyncBackend()

  private val url: Uri = uri"https://api-prod.omnivore.app/api/graphql"

  private def getArticlePayload(username: String, slug: String): ujson.Obj = {
    ujson.Obj(
      "query" -> "query MdArticle($username: String!, $slug: String!, $format: String) { article(username: $username, slug: $slug, format: $format) { ... on ArticleSuccess { article { id title slug content } } ... on ArticleError { errorCodes } } }",
      "variables" -> ujson.Obj(
        "username" -> username,
        "slug" -> slug,
        "format" -> "markdown",
      ),
    )
  }

  private def parseArticleResponse(jsonResponse: ujson.Value) = {
    val jsonArticleResponse = jsonResponse("data")("article")

    def parseSuccess(json: LinkedHashMap[String, ujson.Value]): ArticleResponse.ArticleSuccess = {
      ArticleResponse.ArticleSuccess(
        json("id").str,
        json("title").str,
        json("slug").str,
        json("content").str,
      )
    }

    def parseErrors = {
      jsonArticleResponse("errorCodes").arrOpt.map { arr =>
        val codes = arr.map(_.toString).toSeq
        ArticleResponse.ArticleError(codes)
      }
    }

    jsonArticleResponse("article").objOpt
      .map(parseSuccess)
      .orElse(parseErrors)
      .get
  }

}
