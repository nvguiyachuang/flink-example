package com.hello.world.scala.oop

import org.junit.Test

import scala.collection.mutable

/**
 * match 并不会匹配泛型， Array[Int]，但是例如这种数组是会匹配的，因为编译成class文件后是java里的数组int[] 、String[]
 *
 * @author xx on 2020/5/24 14:58
 */
class MatchDemo {

  @Test
  def test(): Unit = {
    for (ch: Char <- "asdf") {
      var result = 0
      ch match {
        case 'a' => result = 99
        case 's' => result = 88
        case _ if ch.toString.equals("d") => result = 77
        case _ => result = 999
      }

      println(result)
    }
  }

  @Test
  def test2(): Unit = {
    val num = 3
    val matchResult: Any = num match {
      case 1 => Map[String, String]("X" -> "x")
      case 2 => 99
      case 3 => "fffffffff"
    }

    /**
     * : 匹配类型
     */
    matchResult match {
      case _: Int => println("隐藏变量名")
      case cc: Map[String, String] => println(cc)
      case aaa: String => println(aaa)
      case _ => println("kong kong")
    }
  }

  @Test
  def test3(): Unit = {
    val list = List(Array(0, 1), Array(0, 1, 2), Array(1, 2))

    for (elem <- list) {
      elem match {
        case Array(_, _) => println("xy\t\t" + elem.mkString("|"))
        case Array(0, _*) => println("0_*\t\t" + elem.mkString("|"))
        case _ => println("default")
      }
    }
  }

  @Test
  def test4(): Unit = {
    var map = mutable.HashMap[String, Any]()
    map += ("1" -> 1)
    map += ("2" -> "")
    map += ("3" -> 9.9)
    println(map)

//    val obj = 888.2
//    test(obj)
//    println(obj)
//
//    def test( obj: Any): Any = {
//      obj match {
//        case l: Long =>
//          obj = l
//        case d: Double =>
//          obj = d
//        case _ =>
//          obj = obj.asInstanceOf[Object]
//      }
//      obj
//    }
  }

}
