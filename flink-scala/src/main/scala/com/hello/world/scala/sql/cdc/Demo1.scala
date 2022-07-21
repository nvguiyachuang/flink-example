package com.hello.world.scala.sql.cdc

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/*

1.14 cumulate consume cdc,不支持

 */
object Demo1 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    val conf = tableEnv.getConfig.getConfiguration
    conf.setString("table.dynamic-table-options.enabled", "true")
    println(env.getParallelism)
    env.setParallelism(1)

    val source =
      """
        |CREATE TABLE test_flink_cdc (
        |    db_name STRING METADATA FROM 'database_name' VIRTUAL,
        |    table_name STRING METADATA  FROM 'table_name' VIRTUAL,
        |    operation_ts TIMESTAMP_LTZ(3) METADATA FROM 'op_ts' VIRTUAL,
        |  id INT,
        |  name STRING,
        |  ts as proctime(),
        |  primary key(id)  NOT ENFORCED
        |) WITH (
        |  'connector' = 'mysql-cdc',
        |  'hostname' = 'devcdh1',
        |  'port' = '3306',
        |  'username' = 'root',
        |  'password' = 'root',
        |  'database-name' = 'test',
        |  'table-name' = 'test_cdc'
        |)
        |""".stripMargin

    val query =
      """
        |select
        | window_start, window_end, sum(id)
        |from table(
        | cumulate(table test_flink_cdc, DESCRIPTOR(ts), INTERVAL '2' SECOND, INTERVAL '10' SECOND)
        |)
        |group by window_start, window_end
        |""".stripMargin

    val query2 =
      """
        |select
        | tumble_start(ts, interval '5' second),
        | tumble_end(ts, interval '5' second),
        | sum(id)
        |from test_flink_cdc
        |group by tumble(ts, interval '5' second)
        |""".stripMargin

    val query3 =
      """
        |select * from test_flink_cdc --/*+ OPTIONS('server-id'='5401-5404') */
        |
        |-- select * from test_flink_cdc /*+ OPTIONS('table.exec.resource.default-parallelism'='1') */
        |
        |""".stripMargin

//    tableEnv.executeSql("set").print()

    tableEnv.executeSql(source)
    tableEnv.executeSql(query3).print()
  }
}
