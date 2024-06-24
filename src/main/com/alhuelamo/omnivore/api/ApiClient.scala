package com.alhuelamo.omnivore.api

class ApiClient(apiToken: String) {

  def getArticleContent(username: String, articleSlug: String, format: Option[String] = Some("markdown")): ArticleResponse = ???

}
