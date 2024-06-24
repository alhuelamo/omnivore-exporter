package com.alhuelamo.omnivore.exporter.config

import cats.implicits.*
import com.alhuelamo.omnivore.exporter.config.cli.cli
import com.monovore.decline.Command

case class AppConf(
    apiToken: String,
    inputLabel: Option[String],
)

object AppConf {

  private val configFromCli = (
    cli.apiToken,
    cli.inputLabel,
  ).mapN(AppConf.apply)

  private val entrypoint: Command[AppConf] = Command("omnivore-exporter", "Export Omnivore data")(configFromCli)

  def apply(args: Array[String]): AppConf =
    entrypoint.parse(args) match {
      case Right(conf)   => conf
      case Left(invalid) => throw new IllegalArgumentException(invalid.toString)
    }

}
