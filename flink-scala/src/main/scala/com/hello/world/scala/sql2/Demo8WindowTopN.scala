package com.hello.world.scala.sql2

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object Demo8WindowTopN {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |    name BIGINT NOT NULL,
        |    search_cnt BIGINT NOT NULL,
        |    key BIGINT NOT NULL,
        |    row_time AS cast(CURRENT_TIMESTAMP as timestamp(3)),
        |    WATERMARK FOR row_time AS row_time
        |) WITH (
        |  ...
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE sink_table (
        |    key BIGINT,
        |    name BIGINT,
        |    search_cnt BIGINT,
        |    window_start TIMESTAMP(3),
        |    window_end TIMESTAMP(3)
        |) WITH (
        |  ...
        |);
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |INSERT INTO sink_table
        |SELECT key, name, search_cnt, window_start, window_end
        |FROM (
        |   SELECT key, name, search_cnt, window_start, window_end,
        |     ROW_NUMBER() OVER (PARTITION BY window_start, window_end, key
        |       ORDER BY search_cnt desc) AS rownum
        |   FROM (
        |      SELECT window_start, window_end, key, name, max(search_cnt) as search_cnt
        |      -- window tvf 写法
        |      FROM TABLE(TUMBLE(TABLE source_table, DESCRIPTOR(row_time), INTERVAL '1' MINUTES))
        |      GROUP BY window_start, window_end, key, name
        |   )
        |)
        |WHERE rownum <= 100
        |""".stripMargin)
  }
}
