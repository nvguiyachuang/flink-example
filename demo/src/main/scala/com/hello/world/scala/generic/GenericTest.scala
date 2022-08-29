package com.hello.world.scala.generic

import org.junit.Test

/**
 * 泛型类
 *
 * @tparam A 类型
 */
class Stack[A] {
  // List[+A],它是协变的，允许子类
  private var elements: List[A] = Nil

  def push(x: A): Unit = elements = x :: elements

  def peek: A = elements.head

  def pop(): A = {
    val currentTop = peek
    elements = elements.tail
    currentTop
  }

  @Test
  def test(): Unit = {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    println(stack.pop()) // prints 2
    println(stack.pop()) // prints 1
  }

  /**
   * 实例对象 stack 只能接受整型值。然而，如果类型参数有子类型，子类型可以被传入：
   *
   * 类 Apple 和类 Banana 都继承自类 Fruit，所以我们可以把实例对象 apple 和 banana 压入栈 Fruit 中。
   *
   * 注意：泛型类型的子类型是*不可传导*的。这表示如果我们有一个字母类型的栈 Stack[Char]，那它不能被用作一个整型的栈 Stack[Int]。
   * 否则就是不安全的，因为它将使我们能够在字母型的栈中插入真正的整型值。
   * 结论就是，只有当类型 B = A 时， Stack[A] 是 Stack[B] 的子类型才成立。
   * 因为此处可能会有很大的限制，Scala 提供了一种 类型参数注释机制 用以控制泛型类型的子类型的行为。
   */
  @Test
  def test2(): Unit = {
    class Fruit
    class Apple extends Fruit
    class Banana extends Fruit

    val stack = new Stack[Fruit]
    val apple = new Apple
    val banana = new Banana

    stack.push(apple)
    stack.push(banana)
  }
}

/**
 * 协变 -- List[+A]
 *
 * 类型 Cat 和 Dog 都是 Animal 的子类型。 Scala 标准库有一个通用的不可变的类 sealed abstract class List[+A]，其中类型参数 A 是协变的
 * 这意味着 List[Cat] 是 List[Animal]，List[Dog] 也是 List[Animal]。
 * 直观地说，猫的列表和狗的列表都是动物的列表是合理的，你应该能够用它们中的任何一个替换 List[Animal]。
 */
abstract class Animal {
  def name: String
}

case class Cat(name: String) extends Animal

case class Dog(name: String) extends Animal

object CovarianceTest extends App {
  def printAnimalNames(animals: List[Animal]): Unit = {
    animals.foreach { animal =>
      println(animal.name)
    }
  }

  val cats: List[Cat] = List(Cat("Whiskers"), Cat("Tom"))
  val dogs: List[Dog] = List(Dog("Fido"), Dog("Rex"))

  printAnimalNames(cats)
  // Whiskers
  // Tom

  printAnimalNames(dogs)
  // Fido
  // Rex
}

/**
 * 逆变
 * 通过使用注释 -A，可以使一个泛型类的类型参数 A 成为逆变。 与协变类似，这会在类及其类型参数之间创建一个子类型关系，但其作用与协变完全相反。
 * 也就是说，对于某个类 class Writer[-A] ，使 A 逆变意味着对于两种类型 A 和 B，如果 A 是 B 的子类型，
 * 那么 Writer[B] 是 Writer[A] 的子类型。
 */
abstract class Printer[-A] {
  def print(value: A): Unit
}

class AnimalPrinter extends Printer[Animal] {
  def print(animal: Animal): Unit =
    println("The animal's name is: " + animal.name)
}

class CatPrinter extends Printer[Cat] {
  def print(cat: Cat): Unit =
    println("The cat's name is: " + cat.name)
}

object ContravarianceTest extends App {
  val myCat: Cat = Cat("Boots")

  def printMyCat(printer: Printer[Cat]): Unit = {
    printer.print(myCat)
  }

  val catPrinter: Printer[Cat] = new CatPrinter
  val animalPrinter: Printer[Animal] = new AnimalPrinter

  printMyCat(catPrinter)

  /**
   * 如果 Printer[Cat] 知道如何在控制台打印出任意 Cat，并且 Printer[Animal] 知道如何在控制台打印出任意 Animal，
   * 那么 Printer[Animal] 也应该知道如何打印出 Cat 就是合理的。 反向关系不适用，因为 Printer[Cat] 并不知道如何在控制台打印出任意 Animal。
   * 因此，如果我们愿意，我们应该能够用 Printer[Animal] 替换 Printer[Cat]，而使 Printer[A] 逆变允许我们做到这一点。
   */
  printMyCat(animalPrinter)
}

/**
 * 不变
 */
class Container[A](value: A) {
  private var _value: A = value

  def getValue: A = _value

  def setValue(value: A): Unit = {
    _value = value
  }

  @Test
  def test(): Unit = {
    val catContainer: Container[Cat] = new Container(Cat("Felix"))
    //    val animalContainer: Container[Animal] = catContainer
    //    animalContainer.setValue (Dog ("Spot"))
    val cat: Cat = catContainer.getValue
  }
}
