package com.hello.world.scala.container

import org.junit.Test

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * List -- ListBuffer
 *
 * @author xx on 2020/5/24 11:14
 */
class ListTest {

  /**
   * 可变list
   */
  @Test
  def test4(): Unit = {
    val listBuffer = ListBuffer[Int]()

    // add
    listBuffer.append(0)
    println(listBuffer)

    listBuffer.insert(1, 1)
    println(listBuffer)

    listBuffer += 1
    println(listBuffer)

    // delete
    listBuffer.drop(2)
    println(listBuffer)

    // update
    listBuffer.update(0, 100)
    println(listBuffer)

    println("head tail init last--")
    println(listBuffer.head)
    println(listBuffer.tail)
    println(listBuffer.init)
    println(listBuffer.last)

    for (elem <- listBuffer) {
      print(elem + " ")
    }
  }

  /**
   * 不可变list
   */
  val list = List(17, 6, 35, 44, 71, 93, 88)
  val list1 = List(111, 222, 333, 88)

  @Test
  def groupBy(): Unit = {
    val intToInts: Map[Int, List[Int]] = list.groupBy(i => {
      if (i > 70) {
        i // key
      } else {
        9 // key
      }
    })

    println(intToInts)
  }

  /**
   * 排序
   * 17, 26, 35, 44, 71, 93, 88
   */
  @Test
  def sort(): Unit = {
    // 自然排序
    val reverse = list.sorted
    println(reverse)

    val ints: List[Int] = list.sortBy(ele => -ele.hashCode()).reverse

    ints.foreach(println)

    val ints2 = list.sortWith((a, s) => a > s).reverse
    ints2.foreach(println)
  }

  /**
   * zip 拉链
   * 超出部分，都会忽略， hw high watermark
   */
  @Test
  def zip(): Unit = {
    val ints = List(1, 2, 3, 4, 5)
    val strings = List("b", "c", "d", "e")

    val tuples: List[(Int, String)] = ints.zip(strings)

    tuples.foreach(println)
  }

  // 并集
  @Test
  def union(): Unit = {
    val ints = list.union(list1)
    ints.foreach(println(_))
  }

  // 交集
  @Test
  def intersect(): Unit = {
    val ints = list.intersect(list1)
    ints.foreach(println(_))
  }

  // 差集，左边减去右边
  @Test
  def diff(): Unit = {
    val ints = list1.diff(list)
    ints.foreach(x => print(x + "\t"))
    println()

    val ints2 = list.diff(list1)
    ints2.foreach(x => print(x + "\t"))
  }


  /**
   * empty list instance
   */
  @Test
  def test2(): Unit = {
    val nil: Nil.type = Nil
    val list: List[Nothing] = List()
  }

  /**
   * 从头开始丢弃几个元素
   */
  @Test
  def test3(): Unit = {
    val ints = List(2, 3, 4, 5)
    val ints1 = ints.drop(2)

    println(ints1)
  }

  /**
   * queue
   */
  @Test
  def test5(): Unit = {
    val queue1 = mutable.Queue(1, 2, 3, 4, 5)
    queue1.enqueue(9)
    //        queue1.foreach(ele => print(ele + "\t"))
    queue1.foreach(println(_))
    println()

    // 会取下第一个元素
    val i = queue1.dequeue()
    println(i)

    queue1.foreach(print(_))
  }

  /**
   * 多线程
   */
  @Test
  def test6(): Unit = {
    val inclusive = 0 to 100
    inclusive.par.foreach(_ => println(Thread.currentThread().getName))
  }

  @Test
  def test7(): Unit = {
    val list = List(1, 2, 3)
    val list2 = List(4, 5)

    // 向头部追加一个元素，无论它是否是一个集合， 与 +: 很类似，:: 可以用于模式匹配，+:不可以
    val list3 = list2 :: list
    println(list3)

    // 向尾部追加一个元素
    val list4 = list :+ list2
    println(list4)

    val list5 = list ++ list2
    println(list5)

    val list6 = list ::: list2
    println(list6)
  }
}
