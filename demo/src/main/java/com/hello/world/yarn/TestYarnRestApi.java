package com.hello.world.yarn;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TestYarnRestApi extends TestCase {

    // Build http client builder
    private final static HttpClientBuilder httpClientBuilder = HttpClients
            .custom()
            .setRedirectStrategy(new DefaultRedirectStrategy() {
                @Override
                protected boolean isRedirectable(String method) {
                    // If the connection target is FE, you need to deal with 307 redirect。
                    return true;
                }
            });

    /**
     * 返回值字段说明太多不看
     *
     * Item	DataType	Description
     * id	string	应用的application-id
     * user	string	提交任务的用户名
     * name	string	应用程序的名称
     * queue	string	应用程序所属消息队列
     * state	string	应用程序当前状态
     * finalStatus	string	应用程序最终状态
     * progress	double	应用程序进度
     * trackingUI	string	追踪UI显示名称
     * trackingUrl	string	追踪UI的url
     * clusterId	string	集群id
     * applicationType	string	应用程序类型
     * priority	int	应用程序优先级
     * startedTime	long	应用程序开始时间
     * launchTime	long	应用程序加载时间
     * finishedTime	long	应用程序完成时间
     * elapsedTime	long	应用程序消耗时间(finished-start)
     * amContainerLogs	string	am容器日志地址
     * amHostHttpAddress	string	am的主机http地址
     * amRPCAddress	string	am的RPC地址
     * allocatedMB	string	初始化内存大小
     * allocatedVCores	string	初始化核心数
     * reservedMB	string	保留内存
     * reservedVCores	string	保留核心数
     * runningContainers	string	正在运行的容器数
     * memorySeconds	int	所有的container每秒消耗的内存总和
     * vcoreSecond	string	所有的container每秒消耗的核心数总和
     * queueUsagePercentage	double	所属队列的资源使用百分比
     * clusterUsagePercentage	double	所属集群的资源使用百分比
     * logAggregationStatus	string	日志聚合状态
     * unmanagedApplication	boolean	未被管理的应用程序
     */
    private static final String URI = "http://cdh34.zetyun.local:8088/ws/v1/cluster/apps";

    private static final String URI2 =
            "http://cdh34.zetyun.local:8088/ws/v1/cluster/apps/application_1649856039005_5963";

    /**
     * 查看所有任务信息
     */
    public void test1() throws IOException {
        HttpGet httpGet = new HttpGet(URI);

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            CloseableHttpResponse response = client.execute(httpGet);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
        }
    }

    public void test2() throws IOException {
        String str = HttpUtil.get(URI, 3000);
        System.out.println(str);
    }

    public static void main(String[] args) {
        HttpGet httpGet = new HttpGet(URI2);

        try (CloseableHttpClient client = httpClientBuilder.build()) {
            CloseableHttpResponse response = client.execute(httpGet);
            String str = EntityUtils.toString(response.getEntity());
//            System.out.println(str);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(str);
            System.out.println(jsonNode.get("app"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
