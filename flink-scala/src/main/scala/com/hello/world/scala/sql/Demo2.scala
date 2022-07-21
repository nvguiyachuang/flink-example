package com.hello.world.scala.sql

import junit.framework.TestCase
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

import java.util.concurrent.TimeUnit

object Demo2 extends TestCase {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, bsSettings)

    //    tableEnv.executeSql("""SET table.exec.source.cdc-events-duplicate=true""")
    tableEnv.getConfig.getConfiguration.setBoolean("table.exec.source.cdc-events-duplicate", true)

    val sql1 =
      """
        |CREATE TABLE topic_products (
        |  id BIGINT,
        |  name STRING,
        |  description STRING,
        |  weight DECIMAL(10, 2),
        |  primary key (id) not ENFORCED
        |) WITH (
        | 'connector' = 'kafka',
        | 'topic' = 'test1',
        | 'properties.bootstrap.servers' = 'devcdh1:9092',
        | 'properties.group.id' = 'test1',
        | 'format' = 'canal-json',
        | 'canal-json.ignore-parse-errors' = 'true'
        |)
        |""".stripMargin
    tableEnv.executeSql(sql1)

    val sql2 =
      """
        |CREATE table result_print(
        |	 name string,
        |  cnt bigint,
        |  primary key (name) not ENFORCED
        |) with (
        |  'connector' = 'upsert-kafka',
        |  'topic' = 'test2',
        |  'properties.bootstrap.servers' = 'cdh1.lcbint.cn:9092,cdh2.lcbint.cn:9092,cdh3.lcbint.cn:9092',
        |  'key.format' = 'json',
        |  'value.format' = 'json'
        |)
        |""".stripMargin
    tableEnv.executeSql(sql2)

    val sql3 =
      """
        |INSERT INTO result_print
        |SELECT name, count(id) cnt FROM topic_products GROUP BY name
        |""".stripMargin
    tableEnv.executeSql(sql3)

    TimeUnit.DAYS.sleep(1)
  }

}
