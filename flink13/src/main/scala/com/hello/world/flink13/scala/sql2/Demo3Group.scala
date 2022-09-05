package com.hello.world.flink13.scala.sql2

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

object Demo3Group {

  def groupWindow(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |    -- 维度数据
        |    dim STRING,
        |    -- 用户 id
        |    user_id BIGINT,
        |    -- 用户
        |    price BIGINT,
        |    -- 事件时间戳
        |    row_time AS cast(CURRENT_TIMESTAMP as timestamp(3)),
        |    -- watermark 设置
        |    WATERMARK FOR row_time AS row_time - INTERVAL '5' SECOND
        |) WITH (
        |  'connector' = 'datagen',
        |  'rows-per-second' = '10',
        |  'fields.dim.length' = '1',
        |  'fields.user_id.min' = '1',
        |  'fields.user_id.max' = '100000',
        |  'fields.price.min' = '1',
        |  'fields.price.max' = '100000'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE sink_table (
        |    dim STRING,
        |    pv BIGINT,
        |    sum_price BIGINT,
        |    max_price BIGINT,
        |    min_price BIGINT,
        |    uv BIGINT,
        |    window_start bigint
        |) WITH (
        |  'connector' = 'print'
        |)
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |insert into sink_table
        |select dim,
        |    count(*) as pv,
        |    sum(price) as sum_price,
        |    max(price) as max_price,
        |    min(price) as min_price,
        |    -- 计算 uv 数
        |    count(distinct user_id) as uv,
        |    UNIX_TIMESTAMP(CAST(tumble_start(row_time, interval '1' minute) AS STRING)) * 1000  as window_start
        |from source_table
        |group by
        |    dim,
        |    -- 按照 Flink SQL tumble 窗口写法划分窗口
        |    tumble(row_time, interval '1' minute)
        |""".stripMargin)
  }

  def group(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |CREATE TABLE source_table (
        |    -- 维度数据
        |    dim STRING,
        |    -- 用户 id
        |    user_id BIGINT,
        |    -- 用户
        |    price BIGINT,
        |    -- 事件时间戳
        |    row_time AS cast(CURRENT_TIMESTAMP as timestamp(3)),
        |    -- watermark 设置
        |    WATERMARK FOR row_time AS row_time - INTERVAL '5' SECOND
        |) WITH (
        |  'connector' = 'datagen',
        |  'rows-per-second' = '10',
        |  'fields.dim.length' = '1',
        |  'fields.user_id.min' = '1',
        |  'fields.user_id.max' = '100000',
        |  'fields.price.min' = '1',
        |  'fields.price.max' = '100000'
        |);
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |CREATE TABLE sink_table (
        |    dim STRING,
        |    pv BIGINT,
        |    sum_price BIGINT,
        |    max_price BIGINT,
        |    min_price BIGINT,
        |    uv BIGINT,
        |    window_start bigint
        |) WITH (
        |  'connector' = 'print'
        |);
        |""".stripMargin)

    tableEnv.executeSql(
      """
        |insert into sink_table
        |select dim,
        |    count(*) as pv,
        |    sum(price) as sum_price,
        |    max(price) as max_price,
        |    min(price) as min_price,
        |    -- 计算 uv 数
        |    count(distinct user_id) as uv,
        |    cast((UNIX_TIMESTAMP(CAST(row_time AS STRING))) / 60 as bigint) as window_start
        |from source_table
        |group by
        |    dim,
        |    -- 将秒级别时间戳 / 60 转化为 1min
        |    cast((UNIX_TIMESTAMP(CAST(row_time AS STRING))) / 60 as bigint)
        |""".stripMargin)
  }

  def groupSet(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |SELECT
        |    supplier_id
        |    , rating
        |    , product_id
        |    , COUNT(*)
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2', 'product4', 4))
        |AS Products(supplier_id, product_id, rating)
        |GROUP BY GROUPING SETS (
        |    ( supplier_id, product_id, rating ),
        |    ( supplier_id, product_id         ),
        |    ( supplier_id,             rating ),
        |    ( supplier_id                     ),
        |    (              product_id, rating ),
        |    (              product_id         ),
        |    (                          rating ),
        |    (                                 )
        |)
        |""".stripMargin)
      .print()
  }

  def rollup(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |SELECT supplier_id, rating, COUNT(*)
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2', 'product4', 4))
        |AS Products(supplier_id, product_id, rating)
        |GROUP BY ROLLUP (supplier_id, rating)
        |""".stripMargin)
      .print()
  }

  def cube(tableEnv: TableEnvironment): Unit = {
    tableEnv.executeSql(
      """
        |SELECT supplier_id, rating, product_id, COUNT(*)
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2', 'product4', 4))
        |AS Products(supplier_id, product_id, rating)
        |GROUP BY CUBE (supplier_id, rating, product_id)
        |
        |SELECT supplier_id, rating, product_id, COUNT(*)
        |FROM (VALUES
        |    ('supplier1', 'product1', 4),
        |    ('supplier1', 'product2', 3),
        |    ('supplier2', 'product3', 3),
        |    ('supplier2', 'product4', 4))
        |AS Products(supplier_id, product_id, rating)
        |GROUP BY GROUPING SET (
        |    ( supplier_id, product_id, rating ),
        |    ( supplier_id, product_id         ),
        |    ( supplier_id,             rating ),
        |    ( supplier_id                     ),
        |    (              product_id, rating ),
        |    (              product_id         ),
        |    (                          rating ),
        |    (                                 )
        |)
        |""".stripMargin)
  }

  def main(args: Array[String]): Unit = {
    val settings = EnvironmentSettings.newInstance.inStreamingMode.build
    val tableEnv = TableEnvironment.create(settings)

//    groupWindow(tableEnv)
//    group(tableEnv)
//    groupSet(tableEnv)
    rollup(tableEnv)
    cube(tableEnv)
  }
}
