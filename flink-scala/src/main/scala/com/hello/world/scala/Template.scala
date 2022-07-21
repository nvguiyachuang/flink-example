package com.hello.world.scala

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment

/**
 * val value: Class[Boolean] = classOf[Boolean]
 * val value1: TypeInformation[(Long, Long, Long)] = createTypeInformation[(Long, Long, Long)]
 * val value2: TypeInformation[Boolean] = org.apache.flink.api.scala.typeutils.Types.of[Boolean]
 */
object Template {
  // private val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val array = Array[String]()
    executeSql(array)
  }

  def executeSql(array: Array[String]): Unit = {
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
    println("rest.bind-port: 8081")

    for (elem <- array) {
      tableEnv.executeSql(elem)
    }
  }

}
