package com.hello.world.scala.sql2

import com.hello.world.scala.function.SplitUdtf
import com.hello.world.scala.function.udaf.WeightedAvg
import junit.framework.TestCase
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

class Demo6Udf extends TestCase{

  def test1(): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    tableEnv.createTemporarySystemFunction("ss", classOf[SplitUdtf])

    tableEnv.executeSql(
      """
        |SELECT
        |    supplier_id, word
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2,a,s,d', 'product4', 4))
        |AS Products(supplier_id, product_id, rating),
        |lateral table(ss(supplier_id))
        |""".stripMargin)
      .print()
  }

  def test2(): Unit ={
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

//    tableEnv.createTemporarySystemFunction("ss", classOf[StringLengthUdf])
    tableEnv.executeSql("create function ss as 'com.hello.world.function.StringLengthUdf' ")

    tableEnv.executeSql(
      """
        |SELECT
        |    supplier_id, ss(supplier_id)
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2,a,s,d', 'product4', 4))
        |AS Products(supplier_id, product_id, rating)
        |""".stripMargin)
      .print()
  }

  def test3(): Unit ={
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    tableEnv.createTemporarySystemFunction("ss", classOf[WeightedAvg])

    tableEnv.executeSql(
      """
        |SELECT
        |    supplier_id,
        |    ss(product_id, rating)
        |FROM (VALUES
        |    ('supplier1', 1, 4),
        |    ('supplier1', 2, 3),
        |    ('supplier2', 3, 3),
        |    ('supplier2,a,s,d', 3, 4))
        |AS Products(supplier_id, product_id, rating)
        |group by supplier_id
        |""".stripMargin)
      .print()
  }

}
