package com.alhuelamo.omnivore.exporter.config

import munit.*

class AppConfSpec extends FunSuite {

  private val validArgs = Array("--api-token", "token", "--input-label", "label")

  testConf("should process valid api token", validArgs) { conf =>
    assertEquals(conf.apiToken, "token")
  }

  testConf("should process valid input label", validArgs) { conf =>
    assertEquals(conf.inputLabel, Some("label"))
  }

  test("should fail with empty args") {
    intercept[IllegalArgumentException] {
      AppConf(Array.empty)
    }
  }

  private val onlyRequiredArgs = Array("--api-token", "token")

  testConf("should process required API token", onlyRequiredArgs) { conf =>
    assertEquals(conf.apiToken, "token")
  }

  testConf("should set inputLabel to None on empty args", onlyRequiredArgs) { conf =>
    assertEquals(conf.inputLabel, None)
  }

  private def testConf(testName: String, args: Array[String])(testCode: AppConf => Unit): Unit = {
    test(testName) {
      val conf = AppConf(args)
      testCode(conf)
    }
  }

}
