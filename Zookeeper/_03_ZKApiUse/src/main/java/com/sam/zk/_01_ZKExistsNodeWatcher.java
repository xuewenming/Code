package com.sam.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.Watcher.Event.EventType;

/**
 * @author Mr.xuewenming
 * @title: _01_ZkConnectionExists
 * @projectName Code
 * @description: watcher 事件 exists
 * @date 2020/8/3121:04
 */
public class _01_ZKExistsNodeWatcher {

    private final String zkServerPath = "172.16.14.131:2181";
    private final int timeout = 5000;
    private final CountDownLatch downLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public _01_ZKExistsNodeWatcher() {
        try {
            zooKeeper = new ZooKeeper(zkServerPath, timeout, (WatchedEvent event) -> {
                if (event.getType() == EventType.None) {
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
     * @description: exists()
     * exists可以监听
     * CreateNode
     * DeleteNode
     * NodeDataChange
     *
     * exists(String path,boolean b)
     * exists(String path,Watcher w)
     */

    private void watcherExists(){
        try {
            zooKeeper.create("/watcher1",new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.exists("/watcher1", true);
            TimeUnit.SECONDS.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void watcherExists02(){
        try {
            zooKeeper.exists("/watcher2",(WatchedEvent event)->{
                System.out.println("path：" + event.getPath());
                if (event.getType() == EventType.NodeCreated) {
                    System.out.println("》》》》》》》节点被创建");
                } else if (event.getType() == EventType.NodeDataChanged) {
                    System.out.println("》》》》》》》节点数据被改变");
                } else if (event.getType() == EventType.NodeDeleted) {
                    System.out.println("》》》》》》》节点被删除");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        _01_ZKExistsNodeWatcher obj = new _01_ZKExistsNodeWatcher();
        obj.downLatch.countDown();
        obj.watcherExists();
    }

}
