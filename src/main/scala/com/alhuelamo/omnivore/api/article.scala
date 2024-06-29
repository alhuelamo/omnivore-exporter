package com.alhuelamo.omnivore.api

enum ArticleResponse {
  case ArticleSuccess(id: String, title: String, slug: String, content: String)
  case ArticleError(errors: Seq[String])
}
