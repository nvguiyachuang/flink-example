package com.hello.world.flink13.scala.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/*
topic flink_test_5

{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2020-01-05 11:12:12"}
{"username":"ngyc","click_url":"https://www.infoq.cn/video/BYSSg4hGR5oZmUFsL8Kb","ts":"2020-01-05 11:12:15"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:12:18"}
{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2020-01-05 11:12:55"}
{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2020-01-05 11:13:25"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:13:25"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:13:26"}

{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:14:26"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:15:26"}

kafka-console-producer --broker-list devcdh1:9092 --topic flink_test_5
kafka-console-consumer --bootstrap-server devcdh1:9092 --from-beginning --topic flink_test_5
kafka-consumer-groups --bootstrap-server devcdh1:9092 --list
kafka-consumer-groups --bootstrap-server devcdh1:9092 --delete --group test100

sink mysql 创建语句

CREATE TABLE `sync_test_hop_output` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `window_start` datetime DEFAULT NULL,
  `window_end` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `clicks` bigint(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

 */
object Demo5 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    env.setParallelism(4)
    println(env.getParallelism)

    val conf = tableEnv.getConfig.getConfiguration
//    conf.setString("table.exec.mini-batch.enabled", "true")
//    conf.setString("table.exec.mini-batch.allow-latency", "6s")
//    conf.setString("table.exec.mini-batch.size", "5")

    conf.setString("table.exec.source.idle-timeout", "10")

    val source =
      """
        |create table user_clicks (
        |  username varchar,
        |  click_url varchar,
        |  ts timestamp(0),
        |  WATERMARK FOR ts AS ts - INTERVAL '5' SECOND
        |)
        |with (
        |    'connector' = 'kafka',
        |    'topic' = 'flink_test_5',
        |    'properties.bootstrap.servers' = 'devcdh1:9092',
        |    'properties.group.id' = 'zzz',
        |    'scan.startup.mode' = 'earliest-offset',
        |    'format' = 'json',
        |    'json.fail-on-missing-field' = 'false',
        |    'json.ignore-parse-errors' = 'true'
        |  )
        |""".stripMargin

    val sink =
      """
        |CREATE TABLE sync_test_hop_output (
        |				  window_start TIMESTAMP(3),
        |				  window_end TIMESTAMP(3),
        |				  username VARCHAR,
        |				  clicks BIGINT
        | ) WITH (
        |    'connector' = 'jdbc',
        |    'url' = 'jdbc:mysql://devcdh1:3306/test?characterEncoding=UTF-8',
        |    'table-name' = 'sync_test_hop_output',
        |    'username' = 'root',
        |    'password' = 'root'
        |  )
        |""".stripMargin

    val exec =
      """
        | --统计每个用户过去1分钟的单击次数，每30秒更新1次，即1分钟的窗口，30秒滑动1次
        | INSERT INTO sync_test_hop_output
        | SELECT
        |  HOP_START (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE) as window_start,
        |  HOP_END (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE) as window_end,
        | username,
        | COUNT(click_url)
        | FROM user_clicks
        | GROUP BY HOP (ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE), username
        |""".stripMargin

    tableEnv.executeSql(source)
    //    tableEnv.executeSql(sink)
    //    tableEnv.executeSql(exec)

    //    tableEnv.executeSql("select * from user_clicks").print()

    tableEnv.executeSql(
      """
        |SELECT
        |  HOP_START(ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE) as window_start,
        |  HOP_END(ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE) as window_end,
        | username,
        | COUNT(click_url)
        | FROM user_clicks
        | GROUP BY HOP(ts, INTERVAL '30' SECOND, INTERVAL '1' MINUTE), username
        |""".stripMargin)
      .print()

  }
}
