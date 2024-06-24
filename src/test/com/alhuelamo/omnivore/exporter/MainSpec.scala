package com.alhuelamo.omnivore.exporter

import munit.*

class MainSpec extends FunSuite {

  test("greet should start with H") {
    assert(greet.startsWith("H"))
  }

  test("greet should end with d") {
    assert(greet.endsWith("d"))
  }

  test("Hello should start with H") {
    assert("Hello".startsWith("H"))
  }

  test("Hello should end with o") {
    assert("Hello".endsWith("o"))
  }

}
