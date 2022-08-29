package com.hello.world.scala.oop

import org.junit.Test

import scala.collection.{immutable, mutable}

/**
 * reduceLeft  倒数第二 operate 倒数第一
 *
 * @author xx on 2020/5/24 13:04
 */
class ReduceTest {

  val ints = List(8, 2, 2)

  @Test
  def testReduce(): Unit = {
    // 相乘
    val product: Int = ints.product
    println(product)

    // 相加
    val sum: Int = ints.sum
    println(sum)

    // 相除
    val res = ints.reduce((a, s) => {
      a / s
    })
    println(res)
    val i: Int = ints.reduce(_ / _)
    println(i)

    // 相减
    val i2: Int = ints.reduce((a, s) => {
      a - s
    })
    println(i2)
    val i3: Int = ints.reduce(_ - _)
    println(i3)
  }

  @Test
  def reduceRight(): Unit = {

    val res = ints.reduce(_ - _)
    println(res)

    val i: Int = ints.reduceLeft(_ - _)
    println(i)

    // 倒数第二 减去 倒数第一
    val i2: Int = ints.reduceRight(_ - _)
    println(i2)
  }

  /**
   * 折叠-柯里化
   */
  @Test
  def testFold(): Unit = {
    val result: Int = ints.fold(100)(_ - _)
    println(result)

    val i: Int = ints.foldLeft(100)(_ - _)
    println(i)

    val i1: Int = ints.foldRight(100)(_ - _)
    println(i1)
  }

  /**
   * 需要使用 mutable.Map
   */
  @Test
  def foldLeft(): Unit = {
    val map1: immutable.Map[String, Int] = immutable.Map("a" -> 1, "x" -> 2, "c" -> 3)
    val map2: mutable.Map[String, Int] = mutable.Map("a" -> 1, "c" -> 2, "v" -> 3)

    val stringToInt: mutable.Map[String, Int] = map1.foldLeft(map2)((map2, tuple) => {

      println(map2)
      println(tuple)

      map2(tuple._1) = map2.getOrElse(tuple._1, 0) + tuple._2
      map2
    })

    println("-----------------------")
    println(stringToInt)
  }
}
