package com.hello.world.scala

import scala.util.control.Breaks._

object BreakableDemo {
  def main(args: Array[String]): Unit = {
    // breakable在循环外，是break
    breakable{
      for (i <- 0 until 10) {
        println(i)
        if (i == 5) {
          break
        }
      }
    }

    println("-----------------------")

    for (i <- 0 until 10) {
      // breakable在循环内，是continue
      breakable {
        if (i == 3 || i == 6) {
          break
        }
        println(i)
      }
    }
  }
}
