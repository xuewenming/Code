package com.sam.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.xuewenming
 * @title: Connection
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2615:14
 */
public class Connection {

    private final String zkServerPath = "172.16.14.131:2181";
    private ZooKeeper zooKeeper;

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public Connection() {
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            this.zooKeeper = new ZooKeeper(zkServerPath, 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    Event.KeeperState state = event.getState();
                    int intValue = state.getIntValue();
                    System.out.println(">>>>>>>>>>>" + state + ": " + intValue);
                    latch.countDown();
                }
            });
            latch.await();
            System.out.println("zk启动成功");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>SessionId：" + this.zooKeeper.getSessionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void close(){
        if (this.zooKeeper != null) {
            try {
                this.zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
