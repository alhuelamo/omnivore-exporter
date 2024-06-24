package com.alhuelamo.omnivore.exporter.config

import com.alhuelamo.omnivore.exporter.config.cli.entrypoint

case class AppConf(
    apiToken: String,
    inputLabel: Option[String],
)

object AppConf {

  def apply(args: Array[String]): AppConf =
    entrypoint.parse(args) match {
      case Right(conf)   => conf
      case Left(invalid) => throw new IllegalArgumentException(invalid.toString)
    }

}
