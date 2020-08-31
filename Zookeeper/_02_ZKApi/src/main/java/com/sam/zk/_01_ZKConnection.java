package com.sam.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.xuewenming
 * @title: _01_ZKConnection
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2522:54
 */
public class _01_ZKConnection {

    private static final String zkConnectionPath = "172.16.14.131:2181";



    /**
     * @description:
     * Zookeeper zk = new Zookeeper(arg1,arg2,arg3,arg4,arg5);
     * 客户端和zk服务器之间是异步过程，当连接成功后会收到一个Watcher事件
     *
     * @params:
     * String connectString：zookeeper服务器地址
     *                      如果是集群用逗号隔开，比如：“172.16.14.131:2181,172.16.14.131:2182,172.16.14.131:2183”
     * int sessionTimeout：会话超时时间，单位是毫秒
     * Watcher watcher：事件监听器，如果不需要刻意设置为null
     * boolean canBeReadOnly：是否只读，当物理机节点断开后，还可以读取数据，不能写入数据。
     *                         -- 此时读取到的旧的数据，一般将此选项设置为false
     * long sessionId：会话Id,用于重新连接
     * byte[] sessionPasswd：会话密码。当会话丢失后，可以凭借sessionId和sessionPasswd重写获取会话。
     */

    public static void main(String[] args) throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(zkConnectionPath, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>创建连接成功>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    latch.countDown();
                }
            }
        });
        latch.await();
        System.out.println(zooKeeper.getSessionId());
        zooKeeper.close();
    }
}
