package com.hello.world.scala.util

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object KafkaProducerUtil {
  def main(args: Array[String]): Unit = {

    val topic = "test1"
    val jsonStr = s""" {"id": 1, "day_time": "2020-01-01", "amount": 77} """
    //    val jsonStr = s""" {"user_id": 21, "page_id": 12, "user_region": "sh2"} """
    //    val jsonStr = s""" {"user_id": 3, "page_id": 12, "viewtime": "2021-10-12 15:02:31.706", "user_region": "sh"} """

    val jsonStr1 = """ {"username":"zhp","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:12:12"} """
    val jsonStr2 = """ {"username":"zhp","click_url":"https://www.infoq.cn/video/BYSSg4hGR5oZmUFsL8Kb","ts":"2020-01-05 11:12:15"} """
    val jsonStr3 = """ {"username":"zhp","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:12:18"} """
    val jsonStr4 = """ {"username":"zhp","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:12:55"} """
    val jsonStr5 = """ {"username":"zhp","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:13:25"} """
    val jsonStr6 = """ {"username":"zhp","click_url":"https://www.infoq.cn/talks","ts":"2021-01-05 11:13:25"} """
    val jsonStr7 = """ {"username":"zhp","click_url":"https://www.infoq.cn/talks","ts":"2021-01-05 11:13:26"} """

    sendJsonString(jsonStr, topic)
  }

  def sendJsonString(jsonStr: String, topic: String): Unit = {
    val producer = getProducer
    producer.send(new ProducerRecord[String, String](topic, jsonStr))
    producer.close()
  }

  def getProducer: KafkaProducer[String, String] = {
    val properties: Properties = new Properties()
    properties.put("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092")
    properties.put("acks", "all")
    properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer(properties)
  }
}
