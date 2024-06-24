package com.alhuelamo.omnivore.exporter.config

import munit.*

class AppConfSpec extends FunSuite {

  private val requiredArgs = Array("--api-token", "token", "--username", "user")
  private val validArgs = requiredArgs ++ Array("--input-label", "label")

  testConf("should process valid api token", validArgs) { conf =>
    assertEquals(conf.apiToken, "token")
  }

  testConf("should process valid username", validArgs) { conf =>
    assertEquals(conf.username, "user")
  }

  testConf("should process valid input label", validArgs) { conf =>
    assertEquals(conf.inputLabel, Some("label"))
  }

  test("should fail with empty args") {
    intercept[IllegalArgumentException] {
      AppConf(Array.empty)
    }
  }

  testConf("should set inputLabel to None on empty args", requiredArgs) { conf =>
    assertEquals(conf.inputLabel, None)
  }

  private def testConf(testName: String, args: Array[String])(testCode: AppConf => Unit): Unit = {
    test(testName) {
      val conf = AppConf(args)
      testCode(conf)
    }
  }

}
