package com.hello.world.flink15.hudi

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

object Demo {
    def main(args: Array[String]): Unit = {
        val tEnv = TableEnvironment.create(EnvironmentSettings.newInstance().inStreamingMode().build())

        tEnv.executeSql("create database if not exists db_hudi")
        tEnv.executeSql("use db_hudi")

        tEnv.executeSql(
            """
              |CREATE TABLE db_hudi.t1(
              |  uuid VARCHAR(20) PRIMARY KEY NOT ENFORCED,
              |  name VARCHAR(10),
              |  age INT,
              |  ts TIMESTAMP(3),
              |  `partition` VARCHAR(20)
              |)
              |PARTITIONED BY (`partition`)
              |WITH (
              |  'connector' = 'hudi',
              |  'path' = '/hudi-hdfs',
              |  'table.type' = 'MERGE_ON_READ',
              |  'read.streaming.enabled' = 'true',
              |  'read.start-commit' = '20210316134557',
              |  'read.streaming.check-interval' = '4',
              |  'hive_sync.enable' = 'true',
              |  'hive_sync.mode' = 'hms',
              |  'hive_sync.metastore.uris' = 'thrift://localhost:9083',
              |  'hive_sync.db' = 'default',
              |  'hive_sync.table' = 't1'
              |)
              |""".stripMargin)

//        tEnv.executeSql(
//            """
//              |INSERT INTO t1 VALUES
//              |  ('id1','Danny',23,TIMESTAMP '1970-01-01 00:00:01','par1')
//              |""".stripMargin)

        tEnv.executeSql("select * from t1").print()

    }
}
