package com.hello.world.ck;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

import static com.hello.world.ck.MockData.*;

public class AppTest {
    public static void main(String[] args) {
        //配置文件
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:clickhouse://node1:18123");
        hikariConfig.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        hikariConfig.setUsername("default");
        hikariConfig.setPassword("Ck18123");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        long start = System.currentTimeMillis();

        try (HikariDataSource ds = new HikariDataSource(hikariConfig);
             Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    " insert into flink_metric values (?,?,?,?,?,?,?,?) ");

            BigDecimal x = BigDecimal.ZERO;
            for (int i = 0; i < 10000000; i++) {
                ps.setString(1, "");// applicationId
                String metricType = getValueByArr(metricTypeArr);
                ps.setString(7, metricType);// metricType

                ps.setString(2, getValueByArr(instanceIdArr));// instanceId
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis() / 1000 / 15 * 15000));// time
                ps.setString(4, getValueByArr(nameArr));// name
                ps.setInt(6, getValueByArr(partitionArr));// partition
                ps.setString(8, getValueByArr(clusterIdArr));// clusterId

                // value
                x = BigDecimal.valueOf(new Random().nextInt(5)).add(x);
                if ("sum".equals(metricType)) {
                    ps.setBigDecimal(5, x);
                } else {
                    ps.setBigDecimal(5, BigDecimal.valueOf(new Random().nextInt(20)));
                }

                ps.addBatch();

                if (i % 100000 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }

            ps.executeBatch();
            ps.clearBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start) + "【单位：m秒】");
    }

    @Test
    public void test1() throws SQLException {
        //配置文件
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:clickhouse://node1:18123");
        hikariConfig.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        hikariConfig.setUsername("default");
        hikariConfig.setPassword("Ck18123");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(hikariConfig);

        long start = System.currentTimeMillis();
        Connection conn = ds.getConnection();
        String sql = "insert into a(id, name) VALUES (?,null)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);//取消自动提交
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, i);
                ps.addBatch();
                if (i % 500 == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
            ps.clearBatch();
            conn.commit();//所有语句都执行完毕后才手动提交sql语句
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ds.close();
        }
        System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start) + "【单位：m秒】");
    }

    @Test
    public void test2() {
        //配置文件
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:clickhouse://node1:18123");
        hikariConfig.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        hikariConfig.setUsername("default");
        hikariConfig.setPassword("Ck18123");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        long start = System.currentTimeMillis();

        try (HikariDataSource ds = new HikariDataSource(hikariConfig);
             Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    " insert into flink_metric values (?,?,?,?,?,?,?,?) ");


            ps.setString(1, "");
            ps.setString(7, "sum");

            ps.setString(2, "instanceId");
            // DateTime
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, "name");
            ps.setBigDecimal(5, BigDecimal.valueOf(99L));
            ps.setInt(6, 88);
            ps.setString(8, "clusterId2");

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("百万条数据插入用时：" + (System.currentTimeMillis() - start) + "【单位：m秒】");

    }
}
