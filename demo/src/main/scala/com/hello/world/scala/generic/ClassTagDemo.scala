package com.hello.world.scala.generic

import scala.reflect.ClassTag

object ClassTagDemo {
  def main(args: Array[String]): Unit = {
    val map: Map[String, Any] = Map("a" -> 1, "b" -> "val")

    val valOfA: Any = map("a")
    val i: Int = map("a").asInstanceOf[Int]

    val maybeInt: Option[Int] = getValueFromMapForInt("a", map)
    val maybeInt1: Option[String] = getValueFromMapForString("b", map)

    val option: Option[Int] = getValueFromMap3[Int]("b", map)
    println(option)
  }

  // getValueFromMap for the Int, String and Animal
  def getValueFromMapForInt(key: String, dataMap: collection.Map[String, Any]): Option[Int] =
    dataMap.get(key) match {
      case Some(value: Int) => Some(value)
      case _ => None
    }

  def getValueFromMapForString(key: String, dataMap: collection.Map[String, Any]): Option[String] =
    dataMap.get(key) match {
      case Some(value: String) => Some(value)
      case _ => None
    }

  /**
   * 运行时只会检查key有没有对应的value，而不会检查value是否是我们传递的T类型。因为运行时不包含我们传递的任何类型信息，
   * 在运行时，T已经被擦除不存在了。在JVM中被称为类型擦除。
   */
//  def getValueFromMap[T](key: String, dataMap: collection.Map[String, Any]): Option[T] =
//    dataMap.get(key) match {
//      case Some(value: T) => Some(value)
//      case _ => None
//    }

  def getValueFromMap3[T: ClassTag](key: String, dataMap: collection.Map[String, Any]): Option[T] = {
    dataMap.get(key) match {
      case Some(value: T) => Some(value)
      case _ => None
    }
  }

}
