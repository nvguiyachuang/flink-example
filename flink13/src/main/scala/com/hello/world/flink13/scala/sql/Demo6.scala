package com.hello.world.flink13.scala.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/*
window tvf

 */
object Demo6 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    val javaEnv = env.getJavaEnv
    val field = classOf[org.apache.flink.streaming.api.environment.StreamExecutionEnvironment]
      .getDeclaredField("configuration")
    field.setAccessible(true)
    import org.apache.flink.configuration.Configuration
    val configuration: Configuration = field.get(javaEnv).asInstanceOf[Configuration]
    configuration.setString("rest.bind-port", "8081")

    val source =
      """
        |CREATE TABLE data_gen (
        | f_sequence INT,
        | f_random INT,
        | f_random_str STRING,
        | ts AS localtimestamp,
        | WATERMARK FOR ts AS ts
        |) WITH (
        | 'connector' = 'datagen',
        | 'rows-per-second'='5',
        | 'fields.f_sequence.kind'='sequence',
        | 'fields.f_sequence.start'='1',
        | 'fields.f_sequence.end'='1000',
        | 'fields.f_random.min'='1',
        | 'fields.f_random.max'='1000',
        | 'fields.f_random_str.length'='10'
        |)
        |""".stripMargin

    val queryTumble =
      """
        |SELECT
        |  window_start,
        |  window_end,
        |  max(f_random)
        |FROM TABLE(
        |   TUMBLE(TABLE data_gen, DESCRIPTOR(ts), INTERVAL '10' SECOND)
        |)
        |GROUP BY window_start, window_end
        |""".stripMargin

    val queryHop =
      """
        |SELECT window_start, window_end, SUM(f_random)
        |  FROM TABLE(
        |    HOP(TABLE data_gen, DESCRIPTOR(ts), INTERVAL '5' SECOND, INTERVAL '10' SECOND))
        |  GROUP BY window_start, window_end
        |""".stripMargin

    val queryCumulate =
      """
        |SELECT window_start, window_end, SUM(f_random)
        |  FROM TABLE(
        |    CUMULATE(TABLE data_gen, DESCRIPTOR(ts), INTERVAL '2' SECOND, INTERVAL '10' SECOND))
        |  GROUP BY window_start, window_end
        |""".stripMargin

    val print = "select * from data_gen"

    tableEnv.executeSql(source)

//    tableEnv.createStatementSet()
    tableEnv.executeSql(queryCumulate).print()
    tableEnv.executeSql(print).print()

  }
}
