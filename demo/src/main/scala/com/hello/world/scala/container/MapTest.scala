package com.hello.world.scala.container

import junit.framework.TestCase

import scala.collection.mutable

/**
 * @author zihang on 2020/3/1 19:46
 */
class MapTest extends TestCase{

  /**
   * mutable map
   */
  def test3(): Unit ={
    val mutableMap = mutable.Map[String, Int]()
    mutableMap.put("1", 1)
    mutableMap.put("2", 2)
    mutableMap.put("3", 3)

    val stringToInt = mutableMap.drop(0)
    println(stringToInt)
  }

  val map: Map[String, Int] = Map("a" -> 111, "b" -> 33, "c" -> 22, "d" -> 99)
  val map2: Map[String, Int] = Map("a" -> 11, "f" -> 33, "g" -> 22, "h" -> 99)

  def test(): Unit = {
    var map1 = map + ("e" -> 11)
    map1 = map - "e"
    map1 = map.updated("a", 88)

    println(map1)
  }

  def test1(): Unit = {
    // Option ...
    val maybeInt: Option[Int] = map.get("b")
    val i: Int = maybeInt.getOrElse(0)
    println(i)

    if (maybeInt.isDefined) println(maybeInt.get)
    if (maybeInt.isEmpty) throw new RuntimeException("empty  ...")
    if (maybeInt.contains(33)) println(maybeInt.get)

    // 遍历
    for (elem <- map) print(elem._1, "\t" + elem._2)
  }

  /**
   * map之间合并，相同key取后面的元素
   */
  def test2(): Unit = {
    val stringToInt: Map[String, Int] = map ++ map2
    stringToInt.foreach(println)
  }

  def test33(): Unit ={
    println('型'.toInt)
  }
}
