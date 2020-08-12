package com.sam.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @Package: com.sam.zk
 * @ClassName:
 * @Author: Xuewm
 * @CreateTime: 2020/8/12 11:09
 * @Description:
 */
public class CuratorOperator {

    private final String zkServerPath = "172.16.14.131:2181";

    protected CuratorFramework client = null;
    protected RetryPolicy retryPolicy = null;
    protected final String nodePath = "/dev/sam";

    protected CuratorOperator() {
        // 重试机制
        /**
         * baseSleepTimeMs：初始化重试等待时间
         * maxRetries：重试次数
         * maxSleepMs：最大重试时间
         */
        retryPolicy = new ExponentialBackoffRetry(5000, 5, 10000);

        /**
         * n：重试次数
         * sleepMsBetweenReties：重试时间间隔
         */
        retryPolicy = new RetryNTimes(5, 5000);


        /**
         * 重试直到经过指定的时间
         * maxElapsedTimeMs：最大重试时间
         * sleepMsBetweenRetries：重试时间间隔
         */
        retryPolicy = new RetryUntilElapsed(60000, 5000);

        client = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .connectionTimeoutMs(20000)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("curator_namespace").build();
        client.start();
    }

    private void closeZk(){
        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorOperator curator = new CuratorOperator();
        System.out.println("》》》》》》》》》》》》》》》》" + curator.client.isStarted());
//        curator.createNode(curator);
//        curator.updateNode(curator);
//        curator.getNode(curator);
        curator.getChildNode(curator);
        curator.existNode(curator);
    }

    // 创建节点
    private void createNode(CuratorOperator cto) throws Exception {
        String data = "123";
        cto.client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT) // 节点的类型
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) // 节点权限
                .forPath(nodePath, data.getBytes());
    }

    // 更新节点
    private void updateNode(CuratorOperator cto) throws Exception {
        byte[] data = "update".getBytes();
        Stat stat = cto.client.setData().forPath(nodePath, data);
        System.out.println(">>>>>>>>>>>>" + stat);
    }

    // 读取节点数据
    private void getNode(CuratorOperator cto) throws Exception {
        Stat stat = new Stat();
        byte[] data = cto.client.getData().storingStatIn(stat).forPath(nodePath);
        System.out.println("data：" + new String(data));
        System.out.println("version：" + stat.getVersion());
    }

    // 读取子节点数据
    private void getChildNode(CuratorOperator cto) throws Exception {
        List<String> childs = cto.client.getChildren().forPath(nodePath);
        childs.forEach(childNode -> {
            System.out.println(childNode);
        });
    }

    // 删除节点
    private void deleteNode(CuratorOperator cto) throws Exception {
        cto.client.delete()
                .guaranteed() // 如果删除失败，后端会继续删除，直到成功为止
                .deletingChildrenIfNeeded() //如果有子节点，就删除
                .forPath(nodePath);
    }

    // 判断节点是否存在
    private void existNode(CuratorOperator cto) throws Exception {
        Stat stat = cto.client.checkExists().forPath(nodePath);
        if (stat == null) System.out.println("节点不存在");
        else System.out.println("节点存在");
    }


}
