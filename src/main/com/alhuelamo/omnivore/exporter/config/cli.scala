package com.alhuelamo.omnivore.exporter.config.cli

import com.monovore.decline.Opts

object cli {
  val apiToken: Opts[String] = Opts.option[String]("api-token", help = "The Omnivore API token")
  val inputLabel: Opts[Option[String]] = Opts.option[String]("input-label", help = "The input label").orNone
}
