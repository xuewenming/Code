package com.sam.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.xuewenming
 * @title: _03_ZKNodeChildrenWatcher
 * @projectName Code
 * @description: TODO
 * @date 2020/8/3121:39
 */
public class _03_ZKNodeChildrenWatcher {

    private final String zkServerPath = "172.16.14.131:2181";
    private final int timeout = 5000;
    private final CountDownLatch downLatch = new CountDownLatch(1);

    private ZooKeeper zooKeeper;

    public _03_ZKNodeChildrenWatcher() {
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

    private void getNodeChildrenWatcher() {
        try {
            zooKeeper.getChildren("/watcher01", true);
            TimeUnit.SECONDS.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNodeChildrenWatcher02() {
        try {
            List<String> childrens = zooKeeper.getChildren("/watcher01", (WatchedEvent event) -> {
                System.out.println("type：" + event.getType());
                System.out.println("path：" + event.getPath());
            });
            TimeUnit.SECONDS.sleep(50);
            childrens.forEach(children ->{
                System.out.println(children);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
