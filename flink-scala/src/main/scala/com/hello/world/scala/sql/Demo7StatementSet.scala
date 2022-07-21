package com.hello.world.scala.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/*
BEGIN STATEMENT SET;      --写入多个Sink时，必填。
INSERT INTO blackhole_sinkA
  SELECT UPPER(name), sum(score)
  FROM datagen_source
  GROUP BY UPPER(name);
INSERT INTO blackhole_sinkB
  SELECT LOWER(name), max(score)
  FROM datagen_source
  GROUP BY LOWER(name);
END;      --写入多个Sink时，必填。
 */
object Demo7StatementSet {
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

    tableEnv.executeSql(
      """
        |CREATE TABLE datagen_source (
        |  name VARCHAR,
        |  score BIGINT
        |) WITH (
        |  'connector' = 'datagen'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE blackhole_sinkA(
        |  name VARCHAR,
        |  score BIGINT
        |) WITH (
        |  'connector' = 'blackhole'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE blackhole_sinkB(
        |  name VARCHAR,
        |  score BIGINT
        |) WITH (
        |  'connector' = 'blackhole'
        |)
        |""".stripMargin)

    val statementSet = tableEnv.createStatementSet()

    statementSet
      .addInsertSql(
      """
        |INSERT INTO blackhole_sinkA
        |  SELECT UPPER(name), sum(score)
        |  FROM datagen_source
        |  GROUP BY UPPER(name)
        |""".stripMargin)
      .addInsertSql(
        """
          |INSERT INTO blackhole_sinkB
          |  SELECT LOWER(name), max(score)
          |  FROM datagen_source
          |  GROUP BY LOWER(name)
          |""".stripMargin)
      .execute()
  }
}
