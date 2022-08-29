package com.hello.world.scala.oop

/**
 * @author zihang on 2020/2/29 16:09
 */
object Constructor {
    def main(args: Array[String]): Unit = {
        val major = new Major()
        println(major)
        
        val demo = new Demo1("简化属性")
        println(demo.name1)
    }
}

// 主构造方法
class Major(username: String) {
    // 这里是类体， 也是构造方法体
    println("主构造方法-------")
    println(username)
    
    // 辅助构造方法， 它必须调用主构造方法
    def this() {
        this("yehongyu")
    }
}

// var 修饰, 将参数也添加为成员变量
class Demo1(var name1: String) {

}