package com.hello.world.flink13.scala.sql2

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

/**
 * SET sql-client.execution.result-mode=tableau;
 * CREATE VIEW MyView1 AS SELECT LOCALTIME, LOCALTIMESTAMP, CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_ROW_TIMESTAMP(), NOW(), PROCTIME();
 * DESC MyView1;
 * SET table.local-time-zone=UTC;
 * SELECT * FROM MyView1;
 * SET table.local-time-zone=Asia/Shanghai;
 * SELECT * FROM MyView1;
 * CREATE VIEW MyView2 AS SELECT TO_TIMESTAMP_LTZ(4001, 3) AS ltz, TIMESTAMP '1970-01-01 00:00:01.001'  AS ntz;
 * DESC MyView2;
 * SET table.local-time-zone=UTC;
 * SELECT * FROM MyView2;
 * SET table.local-time-zone=Asia/Shanghai;
 * SELECT * FROM MyView2;
 * CREATE VIEW MyView3 AS SELECT ltz, CAST(ltz AS TIMESTAMP(3)), CAST(ltz AS STRING), ntz, CAST(ntz AS TIMESTAMP_LTZ(3)) FROM MyView2;
 * DESC MyView3;
 * SELECT * FROM MyView3;
 */
object Demo2TimeZone {


  def main(args: Array[String]): Unit = {
    val settings = EnvironmentSettings.newInstance.inStreamingMode.build
    val tableEnv = TableEnvironment.create(settings)

    withoutLtz(tableEnv)
//    withLtz(tableEnv)
  }

  def withLtz(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE MyTable3 (
        |                  item STRING,
        |                  price DOUBLE,
        |                  ts BIGINT, -- long 类型的时间戳
        |                  ts_ltz AS TO_TIMESTAMP_LTZ(ts, 3), -- 转为 TIMESTAMP_LTZ 类型的时间戳
        |                  WATERMARK FOR ts_ltz AS ts_ltz - INTERVAL '10' SECOND
        |            ) WITH (
        |                'connector' = 'socket',
        |                'hostname' = '127.0.0.1',
        |                'port' = '9999',
        |                'format' = 'csv'
        |           );
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE VIEW MyView5 AS
        |            SELECT
        |                TUMBLE_START(ts_ltz, INTERVAL '10' MINUTES) AS window_start,
        |                TUMBLE_END(ts_ltz, INTERVAL '10' MINUTES) AS window_end,
        |                TUMBLE_ROWTIME(ts_ltz, INTERVAL '10' MINUTES) as window_rowtime,
        |                item,
        |                MAX(price) as max_price
        |            FROM MyTable3
        |                GROUP BY TUMBLE(ts_ltz, INTERVAL '10' MINUTES), item;
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |select * from MyView5
        |""".stripMargin)
      .print()
  }

  private def withoutLtz(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE MyTable2 (
        |                  item STRING,
        |                  price DOUBLE,
        |                  ts TIMESTAMP(3), -- TIMESTAMP 类型的时间戳
        |                  WATERMARK FOR ts AS ts - INTERVAL '10' SECOND
        |            ) WITH (
        |                'connector' = 'socket',
        |                'hostname' = '127.0.0.1',
        |                'port' = '9999',
        |                'format' = 'csv'
        |           );
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE VIEW MyView4 AS
        |            SELECT
        |                TUMBLE_START(ts, INTERVAL '10' MINUTES) AS window_start,
        |                TUMBLE_END(ts, INTERVAL '10' MINUTES) AS window_end,
        |                TUMBLE_ROWTIME(ts, INTERVAL '10' MINUTES) as window_rowtime,
        |                item,
        |                MAX(price) as max_price
        |            FROM MyTable2
        |                GROUP BY TUMBLE(ts, INTERVAL '10' MINUTES), item;
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |SELECT * FROM MyView4
        |""".stripMargin)
      .print()
  }
}
