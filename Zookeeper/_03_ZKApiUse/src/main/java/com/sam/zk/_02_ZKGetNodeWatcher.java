package com.sam.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.xuewenming
 * @title: _02_ZKGetNodeWatcher
 * @projectName Code
 * @description: TODO
 * @date 2020/8/3121:29
 */
public class _02_ZKGetNodeWatcher {

    private final String zkServerPath = "172.16.14.131:2181";
    private final int timeout = 5000;
    private final CountDownLatch downLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public _02_ZKGetNodeWatcher() {
        try {
            zooKeeper = new ZooKeeper(zkServerPath, timeout, (WatchedEvent event) -> {
                if (event.getType() == Watcher.Event.EventType.None) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        System.out.println("》》》》》》》》》》创建连接成功");
                        downLatch.countDown();
                    } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
                        System.err.println("》》》》》》》》》》连接失败");
                    } else if (event.getState() == Watcher.Event.KeeperState.Expired) {
                        System.err.println("》》》》》》》》》》连接超时");
                        try {
                            zooKeeper = new ZooKeeper(zkServerPath, timeout, (Watcher) new _01_ZKExistsNodeWatcher());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (event.getState() == Watcher.Event.KeeperState.AuthFailed) {
                        System.err.println("》》》》》》》》》》认证超时");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @description: getNode Watcher
     * 可以监听
     * deleteNode
     * NodeDataChange
     */

    private void getWatcher(){
        try {
            Stat stat = new Stat();
            zooKeeper.getData("/watcher1", true, stat);
            System.out.println(stat.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWatcher02(){
        try {
            Stat stat = new Stat();
            zooKeeper.getData("/watcher1", (WatchedEvent event) -> {
                System.out.println("type：" + event.getType());
                System.out.println("path：" + event.getPath());
                System.out.println("stat：" + stat.toString());
            }, stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        _02_ZKGetNodeWatcher obj = new _02_ZKGetNodeWatcher();
        obj.downLatch.countDown();
        obj.getWatcher();
    }


}
