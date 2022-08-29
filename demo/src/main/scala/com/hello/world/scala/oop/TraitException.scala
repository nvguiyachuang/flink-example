package com.hello.world.scala.oop

/**
 * @author xx on 2020/5/23 18:07
 */
object TraitException {
    def main(args: Array[String]): Unit = {
        val demo = new Demo2()
        val str = demo.getMessage
        println(str)
    }
}

// 特质继承类
trait ExTrait {
    
    this: Exception =>
    def insert(): Unit ={
        println("insert...")
        val message = this.getMessage
        if (null != message){
            println(message)
        }else{
            println("null ex")
        }
    }
}

class Demo2 extends Exception with ExTrait{

}
