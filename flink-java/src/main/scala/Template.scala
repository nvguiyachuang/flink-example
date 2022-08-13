import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

object Template {
  // private val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val settings = EnvironmentSettings.newInstance().inStreamingMode().build()
    val tEnv = StreamTableEnvironment.create(env, settings)

    // 定义数据源表
    tEnv.executeSql(
      """
        |CREATE TABLE datagen (
        | f_sequence INT,
        | f_random INT,
        | f_random_str STRING,
        | ts AS localtimestamp,
        | WATERMARK FOR ts AS ts
        |) WITH (
        | 'connector' = 'datagen',
        | -- optional options --
        | 'rows-per-second'='1',
        | 'fields.f_sequence.kind'='sequence',
        | 'fields.f_sequence.start'='1',
        | 'fields.f_sequence.end'='20',
        | 'fields.f_random.min'='1',
        | 'fields.f_random.max'='1000',
        | 'fields.f_random_str.length'='10'
        |)
        |""".stripMargin)

    // 定义 redis 表
    tEnv.executeSql(
      """
        |create table redis_sink (
        |f1 STRING,
        |f2 INT
        |) WITH (
        |'connector' = 'redis',
        |'host' = 'mydev',
        |'port' = '16379',
        |'password' = 'Redis6379',
        |'expire' = '100'
        |)
        |""".stripMargin)

    // 执行插入 SQL
    tEnv.executeSql(
      """
        |insert into redis_sink
        |select f_random_str,f_random
        |from datagen
        |""".stripMargin)
  }

}
