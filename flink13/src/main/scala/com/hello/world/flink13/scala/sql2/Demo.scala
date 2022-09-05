package com.hello.world.flink13.scala.sql2

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

object Demo {
  def main(args: Array[String]): Unit = {
    val settings = EnvironmentSettings.newInstance.inStreamingMode.build
    val tableEnv = TableEnvironment.create(settings)

    tableEnv.executeSql("create function ss as 'com.hello.world.function.udaf.Top' ")

    tableEnv.executeSql(
      """
        |create table source_tb(
        |  item string,
        |  ts TIMESTAMP(3) METADATA FROM 'timestamp'
        |) with (
        |  'connector' = 'kafka',
        |  'topic' = 'test',
        |  'properties.bootstrap.servers' = 'node1:9092',
        |  'properties.group.id' = 'testGroup',
        |  'scan.startup.mode' = 'latest-offset',
        |  'format' = 'csv',
        |  'csv.ignore-parse-errors' = 'true'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |create table sk(
        |  item string,
        |  ts TIMESTAMP(3)
        |) with (
        |  'connector' = 'filesystem',
        |  'path' = './result.txt',
        |  'format' = 'json'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |insert into sk
        |select
        |  item,
        |  ts
        |from source_tb
//        |(
//        |  select
//        |     item,
//        |     cnt,
//        |     ts,
//        |     row_number() over (partition by item order by cnt desc) rn
//        |  from
//        |     (
//        |        SELECT
//        |          item,
//        |          count(1) AS cnt,
//        |          max(ts) AS ts
//        |        FROM
//        |          source_tb
//        |        GROUP BY
//        |          item
//        |      )
//        |)
//        |where
//        |  rn < 100
//        |group
//        |  by 0
        |""".stripMargin)


  }
}
