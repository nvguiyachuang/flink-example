package com.hello.world.scala.generic

import org.junit.Test

abstract class Animal2 {
  def name: String
}

abstract class Pet extends Animal2 {}

class Cat2 extends Pet {
  override def name: String = "Cat2"
}

class Dog2 extends Pet {
  override def name: String = "Dog2"
}

class Lion extends Animal2 {
  override def name: String = "Lion"
}

/**
 * 在Scala中，类型参数和抽象类型都可以有一个类型边界约束。这种类型边界在限制类型变量实际取值的同时还能展露类型成员的更多信息。
 * 比如像T <: A这样声明的类型上界表示类型变量T应该是类型A的子类。下面的例子展示了类PetContainer的一个类型参数的类型上界。
 */
//class PetContainer[P <: Pet](p: P) {
//  def pet: P = p
//
//  @Test
//  def test(): Unit ={
//    val Dog2Container = new PetContainer[Dog2](new Dog2)
//    val Cat2Container = new PetContainer[Cat2](new Cat2)
//
//    // this would not compile
//    val lionContainer = new PetContainer[Lion](new Lion)
//  }
//}

class PetContainer[+Pet](p: Pet) {
  def pet: Pet = p
}

object Demo{
  def main(args: Array[String]): Unit = {
    val Dog2Container = new PetContainer[Dog2](new Dog2)
    val Cat2Container = new PetContainer[Cat2](new Cat2)

    // this would not compile
    val lionContainer = new PetContainer[Lion](new Lion)

    println("======")
  }
}

//trait Node[+B] {
//  def prepend(elem: B): Node[B]
//}
//
//case class ListNode[+B](h: B, t: Node[B]) extends Node[B] {
//  def prepend(elem: B): ListNode[B] = ListNode(elem, this)
//  def head: B = h
//  def tail: Node[B] = t
//}
//
//case class Nil[+B]() extends Node[B] {
//  def prepend(elem: B): ListNode[B] = ListNode(elem, this)
//}