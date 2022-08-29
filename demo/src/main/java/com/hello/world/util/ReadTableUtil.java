package com.hello.world.util;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ReadTableUtil {

    private static final String connectString = "xx111:2181,xx112:2181,xx113:2181";

    private static final int sessionTimeout = 2000;

    private ZooKeeper zkClient = null;

    @Before
    public void init() throws Exception {

        zkClient = new ZooKeeper(connectString, sessionTimeout, event -> {

            // 收到事件通知后的回调函数（用户的业务逻辑）
            System.out.println("zookeeper回调：：" + event.getType() + "--" + event.getPath());

            // 再次启动监听
            try {
                zkClient.getChildren("/", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 创建子节点
    @Test
    public void create() throws Exception {

        // 参数1：要创建的节点的路径； 参数2：节点数据 ； 参数3：节点权限 ；参数4：节点的类型
        String nodeCreated = zkClient.create(
                "/atguigu", "jinlian".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    // 获取子节点
    @Test
    public void getChildren() throws Exception {

        List<String> children = zkClient.getChildren("/", true);

        for (String child : children) {
            System.out.println(child);
        }

        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }

    // 判断znode是否存在
    @Test
    public void exist() throws Exception {

        Stat stat = zkClient.exists("/eclipse", false);

        System.out.println(stat == null ? "not exist" : "exist");
    }

}
