package com.hello.world.flink13.scala.sql

import junit.framework.TestCase
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/**
 * 聚合结果无法写入kafka，可以upsert-kafka
 */
class LogDemo1 extends TestCase{

  def test1(): Unit ={
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, bsSettings)

    val sql2 =
      """
        |CREATE TABLE pageviews (
        |  user_id BIGINT,
        |  page_id BIGINT,
        |  viewtime TIMESTAMP(3),
        |  user_region STRING,
        |  WATERMARK FOR viewtime AS viewtime - INTERVAL '2' SECOND
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'test1',
        |  'properties.bootstrap.servers' = '',
        |  'format' = 'json'
        |)
        |""".stripMargin

    tableEnv.executeSql(sql2)

    tableEnv.executeSql(
      """
        |CREATE TABLE pageviews_per_region (
        |  user_region STRING,
        |  pv BIGINT,
        |  uv BIGINT,
        |  PRIMARY KEY (user_region) NOT ENFORCED
        |) WITH (
        |  'connector' = 'upsert-kafka',
        |  'topic' = 'test2',
        |  'properties.bootstrap.servers' = '',
        |  'key.format' = 'json',
        |  'value.format' = 'canal-json'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |INSERT INTO pageviews_per_region
        |SELECT
        |  user_region,
        |  COUNT(*),
        |  COUNT(DISTINCT user_id)
        |FROM pageviews
        |GROUP BY user_region
        |""".stripMargin).print()
  }

}
