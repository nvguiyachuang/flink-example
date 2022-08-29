//package com.hello.world.scala
//
//import java.util
//
//import com.alibaba.fastjson.{JSON, JSONObject, TypeReference}
//import com.alibaba.fastjson.serializer.SerializerFeature
//import junit.framework.TestCase
//
//import scala.beans.BeanProperty
//
//class FastjsonDemo extends TestCase {
//  def test1(): Unit = {
//    val record = new PvRecord()
//    record.id = "iddddddddddd"
//
//    // object to jsonString
//    val str = JSON.toJSONString(record, SerializerFeature.PrettyFormat)
//    println(str)
//
//    // object to jsonObj to map
//    val jsonObj = JSON.toJSON(record).asInstanceOf[JSONObject]
//    val map = jsonObj.getInnerMap
//    println(map)
//
//    // str to jsonObj
//    val nObject = JSON.parseObject(str)
//    println(nObject)
//
//    // str to sortedJsonObj
//    val sortedJsonObj = JSON.parseObject(str, new TypeReference[util.LinkedHashMap[String, Object]]() {})
//    println(sortedJsonObj)
//  }
//
//  def test2(): Unit = {
//    val map = Map("1" -> "2", "3" -> "4")
//    println(map)
//  }
//}
//
//class PvRecord {
//  @BeanProperty var id: String = _
//  @BeanProperty var indicator_code: String = _
//  @BeanProperty var metric_code: String = _
//  @BeanProperty var dimension_code: String = _
//  @BeanProperty var indicator_date: String = _
//  @BeanProperty var indicator_cycle: String = _
//  @BeanProperty var group_id: String = _
//  @BeanProperty var group_type: String = _
//  @BeanProperty var utm_source_id: String = _
//  @BeanProperty var utm_content_id: String = _
//  @BeanProperty var utm_service_type_id: String = _
//  @BeanProperty var invite_user_id: String = _
//  @BeanProperty var activity_id: String = _
//  @BeanProperty var product_id: String = _
//  @BeanProperty var page_id: String = _
//  @BeanProperty var task_id: String = _
//  @BeanProperty var value: Long = _
//  @BeanProperty var created_time: Long = _
//  @BeanProperty var updated_time: Long = _
//}
