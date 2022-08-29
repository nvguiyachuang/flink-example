package com.hello.world.scala.oop

import org.junit.Test

/**
 * 栈上分配， 逃逸分析
 *
 * @author zihang on 2020/2/28 19:55
 */
class Function {

  @Test
  def testCurry(): Unit = {
    /**
     * 函数柯里化
     * 闭包 函数将一个外部变量引入内部，改变了变量的作用范围
     */
    def curry(a: Int)(b: Int): Int = {
      a * b
    }

    val i: Int = curry(9)(9)
    println(i)
  }

  /**
   * 偏函数  相当于filter + map
   * 可以用模式匹配实现
   */
  @Test
  def testPart(): Unit = {
    val list = List(2, 3, 4, 5, "aaa")

    /**
     * 偏函数
     * 方式一
     */
    val partialFun = new PartialFunction[Any, Int] {

      // 过滤
      override def isDefinedAt(x: Any): Boolean = {
        if (x.isInstanceOf[Int]) {
          return true
        }
        false
      }

      // 逻辑
      override def apply(v1: Any): Int = {
        v1.asInstanceOf[Int] + 1
      }
    }

    val list2 = list.collect(partialFun)
    println(list2)

    // 方式二, 模式匹配实现
    val list3 = list.collect {
      case i: Int => i + 1
    }
    println(list3)
  }

}
