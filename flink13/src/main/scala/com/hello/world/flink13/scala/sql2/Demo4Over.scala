package com.hello.world.flink13.scala.sql2

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment, TableResult}

object Demo4Over {

  def test1(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |    order_id BIGINT,
        |    product BIGINT,
        |    amount BIGINT,
        |    order_time as cast(CURRENT_TIMESTAMP as TIMESTAMP(3)),
        |    WATERMARK FOR order_time AS order_time - INTERVAL '0.001' SECOND
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'test',
        |  'properties.bootstrap.servers' = 'mydev:19092',
        |  'properties.group.id' = 'testGroup',
        |  'scan.startup.mode' = 'latest-offset',
        |  'format' = 'csv',
        |  'csv.ignore-parse-errors' = 'true'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE sink_table (
        |    product BIGINT,
        |    order_time TIMESTAMP(3),
        |    amount BIGINT,
        |    one_hour_prod_amount_sum BIGINT
        |) WITH (
        |  'connector' = 'print'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |INSERT INTO sink_table
        |SELECT product, order_time, amount,
        |  SUM(amount) OVER (
        |    PARTITION BY product
        |    ORDER BY order_time
        |    -- 标识统计范围是一个 product 的最近 1 小时的数据
        |    RANGE BETWEEN INTERVAL '1' HOUR PRECEDING AND CURRENT ROW
        |  ) AS one_hour_prod_amount_sum
        |FROM source_table
        |""".stripMargin)
  }

  def test2(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |    order_id BIGINT,
        |    product BIGINT,
        |    amount BIGINT,
        |    order_time as cast(CURRENT_TIMESTAMP as TIMESTAMP(3)),
        |    WATERMARK FOR order_time AS order_time - INTERVAL '0.001' SECOND
        |) WITH (
        |  'connector' = 'datagen',
        |  'rows-per-second' = '1',
        |  'fields.order_id.min' = '1',
        |  'fields.order_id.max' = '2',
        |  'fields.amount.min' = '1',
        |  'fields.amount.max' = '2',
        |  'fields.product.min' = '1',
        |  'fields.product.max' = '2'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE sink_table (
        |    product BIGINT,
        |    order_time TIMESTAMP(3),
        |    amount BIGINT,
        |    one_hour_prod_amount_sum BIGINT
        |) WITH (
        |  'connector' = 'print'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |INSERT INTO sink_table
        |SELECT product, order_time, amount,
        |  SUM(amount) OVER (
        |    PARTITION BY product
        |    ORDER BY order_time
        |    -- 标识统计范围是一个 product 的最近 5 行数据
        |    ROWS BETWEEN 5 PRECEDING AND CURRENT ROW
        |  ) AS one_hour_prod_amount_sum
        |FROM source_table
        |""".stripMargin)
  }

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

    test1(tableEnv)
//    test2(tableEnv)
  }
}
