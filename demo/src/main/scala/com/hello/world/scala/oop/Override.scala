package com.hello.world.scala.oop

/**
 * @author zihang on 2020/2/29 16:38
 */
object Override {
    def main(args: Array[String]): Unit = {
        val color = new SmallGrass().getColor
        println(color)
    }
}

/**
 * Scala 中属性可以抽象、继承     java中属性不可以
 * Scala 中抽象属性在字节码中是一个getter方法，并不是属性
 */
abstract class Grass {
    var length: Int;
    
    def grow()
    
    def getColor: String = {
        "green"
    }
    
    // var 修饰不可继承， val 修饰的可以
    val grassName: String = "绿色的草"
}

class SmallGrass extends Grass {
    override var length: Int = _
    
    /*override*/ def grow(): Unit = {
        println("自然界的小草茁壮成长")
    }
    
    override def getColor: String = "override method of getColor!!!"
    
    override val grassName: String = "白色的草"
}

// error
//class Snack extends Bird(high: String) {
//}
// error
//class Chicken extends Bird{
//}