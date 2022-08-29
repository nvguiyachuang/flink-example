package com.hello.world.scala.oop

/**
 * @author zihang on 2020/2/29 17:29
 */
object Trait {
    def main(args: Array[String]): Unit = {
        // with 动态混入特质
        val snake = new Snake() with NewTrait
        
        snake.fight = "修改trait属性"
        println(snake.fight)
        
        snake.eat()
        
        // println(snake.getMsg)
    }
}

class SuperClass {
    println("SuperClass body...")
}

trait Chicken {
    println("Chicken body...")
    
    var fight: String = "斗鸡"
    
    def color(): String = {
        "red chicken"
    }
    
    def eat(): Unit ={
        println("chicken eat...")
    }
}

trait Bear {
    println("white ice bear never talk with someone...")
    
    def eat(): Unit ={
        println("bear eat...")
    }
}

trait NewTrait /*extends RuntimeException*/ {
}

// 连接（混入）特质
// 同一个包下类不能重名
class Snake extends SuperClass with Chicken with Bear with Cloneable {
    println("Snake body...")

    // 需要重写相同的方法
    override def eat(): Unit = {
        println("snake eat...")
        super[Chicken].eat();
    }
}
