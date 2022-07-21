package com.hello.world.scala.sql2

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object Demo5 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    env.setParallelism(1)
    val javaEnv = env.getJavaEnv
    val field = classOf[org.apache.flink.streaming.api.environment.StreamExecutionEnvironment]
      .getDeclaredField("configuration")
    field.setAccessible(true)
    import org.apache.flink.configuration.Configuration
    val configuration: Configuration = field.get(javaEnv).asInstanceOf[Configuration]
    configuration.setString("rest.bind-port", "8081")
    println("rest.bind-port: 8081")

    tableEnv.executeSql(
      """
        |CREATE TABLE st (
        |   item STRING,
        |   price string
        | ) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'test',
        |  'properties.bootstrap.servers' = 'mydev:19092',
        |  'properties.group.id' = 'testGroup',
        |  'scan.startup.mode' = 'earliest-offset',
        |  'format' = 'csv',
        |  'csv.ignore-parse-errors' = 'true'
        |  )
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |select item, price
        |from st
        |""".stripMargin)
      .print()
  }
}
