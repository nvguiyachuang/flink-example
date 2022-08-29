package com.hello.world.scala

import junit.framework.TestCase

import scala.io.{BufferedSource, Source}

/**
 *
 */
class ReadFileDemo extends TestCase {

  def readFile(): Unit = {
    var source: BufferedSource = null
    try {
      source = Source.fromFile("LICENSE")
      val list = source.getLines().toList
      list.foreach(println)
    } finally {
      if (source != null) {
        source.close()
      }
    }
  }

}
