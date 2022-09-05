package com.hello.world.flink13.scala.sql

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/**
 * 不能有 ;
 */
object Demo3 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tableEnv = StreamTableEnvironment.create(env, settings)

    tableEnv.getConfig.getConfiguration.setString("execution.checkpointing.interval", "60s")

    tableEnv.executeSql(
      """
        |CREATE TABLE orders (
        |order_id INT,
        |order_date TIMESTAMP(0),
        |customer_name STRING,
        |price DECIMAL(10, 5),
        |product_id INT,
        |order_status BOOLEAN,
        |PRIMARY KEY(order_id) NOT ENFORCED
        |) WITH (
        |'connector' = 'mysql-cdc',
        |'hostname' = 'node',
        |'port' = '3306',
        |'username' = 'root',
        |'password' = '000000',
        |'database-name' = 'mydb',
        |'table-name' = 'orders')
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |select * from orders
        |""".stripMargin)
      .print()
  }
}
