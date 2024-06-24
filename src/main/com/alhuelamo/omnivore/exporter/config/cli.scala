package com.alhuelamo.omnivore.exporter.config.cli

import cats.implicits.*
import com.alhuelamo.omnivore.exporter.config.AppConf
import com.monovore.decline.{Command, Opts}

object cli {
  val apiToken: Opts[String] = Opts.option[String]("api-token", help = "The Omnivore API token")
  val inputLabel: Opts[Option[String]] = Opts.option[String]("input-label", help = "The input label").orNone
}

val configFromCli = (
  cli.apiToken,
  cli.inputLabel,
).mapN(AppConf.apply)

val entrypoint: Command[AppConf] = Command("omnivore-exporter", "Export Omnivore data")(configFromCli)
