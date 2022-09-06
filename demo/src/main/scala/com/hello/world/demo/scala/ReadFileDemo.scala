package com.hello.world.demo.scala

import junit.framework.TestCase

import scala.io.{BufferedSource, Source}

/**
 *
 */
class ReadFileDemo extends TestCase {

  def testReadFile(): Unit = {
    var source: BufferedSource = null
    try {
      source = Source.fromFile("pom.xml")
      val list = source.getLines().toList
      list.foreach(println)
    } finally {
      if (source != null) {
        source.close()
      }
    }
  }

}
