package com.hello.world.flink13.scala.sql2

import junit.framework.TestCase
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

class LogDemo7 extends TestCase {

  def test1(): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    /**
     * 192.168.1.1,100000000
     * 192.168.1.2,100000000
     * 192.168.1.2,100000000
     * 192.168.1.3,100000000
     * 192.168.1.3,100000000
     * 192.168.1.3,100000000
     */
    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |  IP VARCHAR,
        |  `TIME` VARCHAR
        |)WITH(
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
        |CREATE TABLE result_table (
        |  rownum BIGINT,
        |  start_time VARCHAR,
        |  IP VARCHAR,
        |  cc BIGINT,
        |  PRIMARY KEY (start_time, IP) NOT ENFORCED
        |) WITH (
        |   'connector' = 'jdbc',
        |   'url' = 'jdbc:mysql://node1:3306/test',
        |   'table-name' = 'result_table',
        |   'username' = 'root',
        |   'password' = 'root'
        |)
        |""".stripMargin)

    val create_sql =
      """
        |CREATE TABLE `result_table` (
        |  `rownum` int NOT NULL,
        |  `start_time` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
        |  `ip` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
        |  `cc` int DEFAULT NULL,
        |  PRIMARY KEY (`rownum`,`start_time`)
        |) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
        |""".stripMargin

    tableEnv.executeSql(
      """
        |INSERT INTO result_table
        |SELECT rownum,start_time,IP,cc
        |FROM (
        |  SELECT *,
        |     ROW_NUMBER() OVER (PARTITION BY start_time ORDER BY cc DESC) AS rownum
        |  FROM (
        |        SELECT SUBSTRING(`TIME`,1,2) AS start_time,--可以根据真实时间取相应的数值，这里取得是测试数据。
        |        COUNT(IP) AS cc,
        |        IP
        |        FROM  source_table
        |        GROUP BY SUBSTRING(`TIME`,1,2), IP
        |    )a
        |) t
        |WHERE rownum <= 3
        |""".stripMargin)
  }
}
