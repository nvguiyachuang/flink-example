//package com.hello.world.scala
//
//import junit.framework.TestCase
//import org.json4s
//import org.json4s.{Formats, JValue, JsonAST, NoTypeHints}
//import org.json4s.jackson.Serialization
//import org.json4s.jackson.Serialization.write
//import org.junit.Test
//
//class Json4sDemo extends TestCase {
//
//  @Test
//  def test1(): Unit = {
//    implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)
//
//    val message: String = write(
//      Map(
//        "data" -> "kafka",
//        "pkNames" -> Array("id"),
//        "index" -> "micro_indicator_clue_nums",
//        "is_send" -> 0,
//        "type" -> "UPSERT"
//      )
//    )
//    println(message)
//  }
//
//  /**
//   * JSON String -> JValue
//   */
//  def test2(): Unit = {
//    import org.json4s.jackson.JsonMethods.parse
//
//    // 正常的例子
//    val value: JValue = parse(""" { "numbers" : [1, 2, 3, 4] } """)
//    println(value)
//    // null
//    val value1 = parse(""" { "code": 0, "error": null } """)
//    println(value1)
//  }
//
//  /**
//   * JValue -> JSON String
//   */
//  def test3(): Unit = {
//    import org.json4s.jackson.JsonMethods.render
//    import org.json4s.jackson.JsonMethods.compact
//    import org.json4s.jackson.JsonMethods.pretty
//    import org.json4s.JsonDSL._
//
//    // primitive type 会被隐式转换成 JValue，
//    // 比如这里的 List(1, 2, 3)
//    // 就是 JArray(List(JInt(1), JInt(2), JInt(3)))
//    val value: JValue = render(List(1, 2, 3))
//    val str: String = compact(value)
//    println(str)
//
//    // JValue
//    val joe: JsonAST.JObject = ("name" -> "joe") ~ ("age" -> Some(35))
//    val value1: JValue = render(joe)
//    println(value1)
//    val str1 = pretty(value1)
//    println(str1)
//  }
//
//  case class Person(name: String, age: Option[Int])
//
//  /**
//   * JValue -> Scala对象
//   */
//  def test4(): Unit = {
//    import org.json4s._
//    import org.json4s.JsonDSL._
//
//    // 什么时候需要用隐式转换？
//    // 为什么3.1，3.2不需要用隐式转换？
//    implicit val formats: DefaultFormats.type = DefaultFormats
//
//    val joe: json4s.JObject = ("name" -> "joe") ~ ("age" -> Some(35))
//    val person: Person = joe.extract[Person]
//    println(person)
//  }
//
//  /**
//   * Scala对象 -> JValue
//   */
//  def test5(): Unit = {
//    import org.json4s.Extraction
//    import org.json4s.DefaultFormats
//
//    implicit val formats: DefaultFormats.type = DefaultFormats
//
//    case class Person(name: String, age: Option[Int])
//    Extraction.decompose(Person("joe", Some(11)))
//  }
//
//  case class Person2(firstName: String)
//
//  case class Status(code: Int, error: String)
//
//  /**
//   * 序列化
//   */
//  def test6(): Unit = {
//    import org.json4s._
//    import org.json4s.jackson.Serialization
//    import org.json4s.jackson.Serialization.{read, write}
//
//    implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)
//
//    val str = write(Person2("joe"))
//    println(str)
//
//    val person: Person = read[Person](""" {"firstName": "joe"} """)
//    println(person)
//
//    val status: Status = read[Status](""" {"code": 0, "error": null} """)
//    println(status)
//  }
//
//  /**
//   * Option 和 Either
//   * Option可以用来表示可有可无的字段。Option是合理的。
//   * Either可以用来表示恶心的需求。使用了Either说明api定义不规范。
//   *
//   * Play with JValue
//   *
//   * Snakize and Camelize
//   */
//  def test7(): Unit = {
//    import org.json4s.JsonDSL._
//
//    val camelJoe = ("firstName" -> "joe") ~ ("age" -> 23)
//    val keys: JValue = camelJoe.snakizeKeys
//    println(keys)
//
//    val snakeJoe = ("first_name" -> "joe") ~ ("age" -> 23)
//    val keys1: JValue = snakeJoe.camelizeKeys
//    println(keys1)
//  }
//
//  /**
//   * Reflections
//   */
//  def test8(): Unit = {
//    //    import org.json4s.jackson.JsonMethods.parse
//    //    import org.json4s._
//    //    import org.json4s.JsonDSL._
//    //    import scala.util.Random
//    //
//    //    // 一些常量
//    //    val url = "https://www.v2ex.com/api/replies/show.json?topic_id=493356"
//    //    val numberOfWinners = 6
//    //
//    //    // 从 V2EX 获取数据
//    //    val source = requests.get(url)
//    //
//    //    // 解析 JSON，获取所有参与抽奖的用户
//    //    implicit val formats = DefaultFormats
//    //    val json = parse(source.text)
//    //    case class Profile(username: String, github: String)
//    //    case class Member(member: Profile)
//    //    val members = json.extract[List[Member]].map(_.member.username).distinct
//    //
//    //    // 抽奖
//    //    Random.shuffle(members).take(numberOfWinners)
//
//    val jsonStr = """ {"day_time": "20211009","id": 7,"amount":20} """
//
//    implicit val value1: AnyRef with Formats = Serialization.formats(NoTypeHints)
//    val record = Serialization.read[Record](jsonStr)
//    println(record)
//  }
//
//  case class Record(day_time: String, id: Int, amount: Int)
//
//}
