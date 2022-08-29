package com.hello.world.scala.oop

import junit.framework.TestCase

/**
 * Open Close Principle
 * 隐式转换与入参类型和出参类型有关
 *
 * 隐式方法， 隐式参数， 隐式类
 *
 * @author zihang on 2020/2/29 19:54
 */
class ImplicitDemo extends TestCase {

  /**
   * 隐式函数
   */
  implicit def transfer(d: Double): Int = {
    d.toInt
  }

  var num: Int = 3.99D
  println(num) // 3

  /**
   * 隐式对象
   */
  class Operate {
    def delete(): Unit = {
      println("拿起手术刀，一顿操作")
    }
  }

  class Doctor {
    def cure(): Unit = {
      println("学医救不了中国人")
    }
  }

  // 添加其他类的功能
  implicit def addFunction(doctor: Doctor): Operate = {
    new Operate
  }

  val doctor = new Doctor()
  doctor.cure()
  doctor.delete()

  /**
   * 隐式值
   */
  def test(): Unit = {
    implicit val value: String = "yyyy"

    def xxx(implicit name: String = "zhangsan"): Unit = {
      println("name--" + name)
    }

    xxx
    xxx()
    xxx("new arg")
  }

}
