package com.alhuelamo.omnivore.exporter

import com.alhuelamo.omnivore.exporter.config.AppConf

@main def ovexport(args: String*): Unit = {
  val conf = AppConf(args.toArray)
}
