package com.alhuelamo.omnivore.exporter

import com.alhuelamo.omnivore.exporter.config.AppConf

object Main {

  def main(args: Array[String]): Unit = {
    val conf = AppConf(args)
  }

}
