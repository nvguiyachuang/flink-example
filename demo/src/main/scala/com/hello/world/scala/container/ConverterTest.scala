package com.hello.world.scala.container

import java.util

object ConverterTest {
  def main(args: Array[String]): Unit = {
//    import scala.collection.JavaConverters.collectionAsScalaIterableConverter
//    import scala.collection.JavaConverters.mapAsScalaMapConverter
    import scala.collection.JavaConverters._

    val javaList = new util.ArrayList[String]()
    javaList.add("hello")
    val scalaList = javaList.asScala
    println(scalaList)

    val javaMap = new util.HashMap[String, String]()
    javaMap.put("place", "love")
    val scalaMap = javaMap.asScala
    println(scalaMap)
  }
}
