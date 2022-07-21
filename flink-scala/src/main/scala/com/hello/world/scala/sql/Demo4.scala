package com.hello.world.scala.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/*

source kafka json 数据格式

topic flink_test_4

{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:12:12"}
{"username":"ngyc","click_url":"https://www.infoq.cn/video/BYSSg4hGR5oZmUFsL8Kb","ts":"2020-01-05 11:12:15"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2020-01-05 11:12:18"}
{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:12:55"}
{"username":"ngyc","click_url":"https://www.infoq.cn/","ts":"2021-01-05 11:13:25"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2021-01-05 11:13:25"}
{"username":"ngyc","click_url":"https://www.infoq.cn/talks","ts":"2021-01-05 11:13:26"}
{"username":"ngyc","click_url":"https://www.baidu.ccom/searach","ts":"2021-01-05 11:33:26"}

./kafka-console-producer.sh --broker-list node:9092 --topic flink_test_4
./kafka-console-consumer.sh --bootstrap-server node:9092 --from-beginning --topic flink_test_4
./kafka-consumer-groups.sh --bootstrap-server node:9092 --list

eg: advertised.host.name,遇到一个大坑，其他机器无法访问kafka，域名 ip 访问问题

sink mysql 创建语句

CREATE TABLE `sync_test_tumble_output` (
`id` bigint(11) NOT NULL AUTO_INCREMENT,
`window_start` datetime DEFAULT NULL,
`window_end` datetime DEFAULT NULL,
`username` varchar(255) DEFAULT NULL,
`clicks` bigint(255) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- -- 开启 mini-batch 指定是否启用小批量优化
SET table.exec.mini-batch.enabled=true;
-- -- mini-batch的时间间隔，即作业需要额外忍受的延迟
SET table.exec.mini-batch.allow-latency=60s;
-- -- 一个 mini-batch 中允许最多缓存的数据
SET table.exec.mini-batch.size=5;

 */
object Demo4 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    val conf = tableEnv.getConfig.getConfiguration
    conf.setString("table.exec.mini-batch.enabled", "true")
    conf.setString("table.exec.mini-batch.allow-latency", "6s")
    conf.setString("table.exec.mini-batch.size", "5")

    val source =
      """
        |create table user_clicks (
        |  username varchar,
        |  click_url varchar,
        |  ts timestamp(3),
        |  WATERMARK FOR ts AS ts - INTERVAL '10' SECOND
        |)
        |with (
        |    'connector' = 'kafka',
        |    'topic' = 'flink_test_4',
        |    'properties.bootstrap.servers' = '192.168.12.12:9092',
        |    'properties.zookeeper.connect' = '192.168.12.12:2181/kafka',
        |    'properties.group.id' = 'flink_gp_test4',
        |    'scan.startup.mode' = 'earliest-offset',
        |    'format' = 'json',
        |    'json.fail-on-missing-field' = 'false',
        |    'json.ignore-parse-errors' = 'true'
        |  )
        |""".stripMargin

    val sink =
      """
        |CREATE TABLE sync_test_tumble_output (
        |				 window_start TIMESTAMP(3),
        |				 window_end TIMESTAMP(3),
        |				 username VARCHAR,
        |				 clicks BIGINT
        | ) WITH (
        |    'connector' = 'jdbc',
        |    'url' = 'jdbc:mysql://node:3306/zz?characterEncoding=UTF-8',
        |    'table-name' = 'sync_test_tumble_output',
        |    'username' = 'root',
        |    'password' = '000000'
        |  )
        |""".stripMargin

    val exec =
      """
        |INSERT INTO sync_test_tumble_output
        | SELECT
        | TUMBLE_START(ts, INTERVAL '60' SECOND) as window_start,
        | TUMBLE_END(ts, INTERVAL '60' SECOND) as window_end,
        | username,
        | COUNT(click_url)
        | FROM user_clicks
        | GROUP BY TUMBLE(ts, INTERVAL '60' SECOND), username
        |""".stripMargin

    tableEnv.executeSql(source)
//    tableEnv.executeSql(sink)
//    tableEnv.executeSql(exec)

    tableEnv.executeSql("select username, click_url, ts from user_clicks").print()

    tableEnv.executeSql(
      """
        | SELECT
        | TUMBLE_START(ts, INTERVAL '30' SECOND) as window_start,
        | TUMBLE_END(ts, INTERVAL '30' SECOND) as window_end,
        | username,
        | COUNT(click_url)
        | FROM user_clicks
        | GROUP BY TUMBLE(ts, INTERVAL '30' SECOND), username
        |""".stripMargin)
      .print()

  }
}
