package com.hello.world.scala.container

import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * 推荐
 * 不可变数组用符号方法
 * 可变数组用英文方法
 *
 * @author xx on 2020/5/23 20:19
 */
class ArrayTest {

  val array: Array[Int] = Array(1, 2, 3, 4)

  val arrayBuffer: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3, 4)

  @Test
  def test3(): Unit = {
    val ints1 = array :+ 5
    println(ints1.mkString(","))

    val ints2 = 5 +: array
    println(ints2.mkString(","))
  }

  /**
   * 可变数组
   */
  @Test
  def test4(): Unit = {

    // add elem with index
    arrayBuffer.insert(0, 5)
    println(arrayBuffer)

    // add elem
    arrayBuffer.append(6)
    println(arrayBuffer)

    // index count
    arrayBuffer.remove(0, 4)
    println(arrayBuffer)

    arrayBuffer.update(0, 999)

    for (elem <- arrayBuffer) {
      println(elem)
    }
  }

  /**
   * transfer between mutable and immutable
   */
  @Test
  def test5(): Unit = {
    val i: mutable.Buffer[Int] = array.toBuffer

    val array1: Array[Int] = arrayBuffer.toArray
  }
}
