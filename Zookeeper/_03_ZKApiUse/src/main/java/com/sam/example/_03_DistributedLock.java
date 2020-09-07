package com.sam.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Package: com.sam.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/1 9:41
 * @Description: 分布式锁
 */
public class _03_DistributedLock {

    /**
     * 流程：
     * 1、创建锁节点
     * 2、尝试获取锁
     * 3、释放锁
     */

    private ZooKeeper zk;
    private final String zkServerPath = "172.16.14.131:2181";
    private final int zkTimeout = 500000;

    private final String LOCK_ROOT_PATH = "/LOCKS";
    private final String SLAVE_LOCK_PATH = "/Lock_";
    private String actualPath;

    private CountDownLatch downLatch = new CountDownLatch(1);

    public _03_DistributedLock(){
        try {
            zk = new ZooKeeper(zkServerPath,zkTimeout,(WatchedEvent event)->{
                if (event.getType() == Watcher.Event.EventType.None) {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        System.out.println("zk连接成功");
                        downLatch.countDown();
                    }
                }
            });
            downLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Watcher watcher = (WatchedEvent event)->{
        if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
            synchronized (this) {
                notifyAll();
            }
        }
    };

    /**
     * 尝试获取锁
     */
   public void acquireLock() throws Exception {
       /**
        * 创建锁
        * 尝试获取锁
         */
       createLock();
       attemptLock();
   }

   // 创建锁
    private void createLock() throws KeeperException, InterruptedException {
        Stat stat = zk.exists(LOCK_ROOT_PATH, false);
        if (stat == null) {
            zk.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        actualPath = zk.create(LOCK_ROOT_PATH + SLAVE_LOCK_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("创建临时节点路径：" + actualPath);
    }

    // 尝试获取锁
    private void attemptLock() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(LOCK_ROOT_PATH, false);
        Collections.sort(children);
        System.out.println(actualPath);
        String i = actualPath.substring(LOCK_ROOT_PATH.length() + 1);
        int index = children.indexOf(i);
        if (index == 0) {
            System.out.println("获取锁成功");
            return;
        }else {
            String path = children.get(index - 1);
            Stat stat = zk.exists(LOCK_ROOT_PATH + "/" + path, watcher);
            if (stat == null) {
                attemptLock();
            }else{
                synchronized (this) {
                    wait();
                }
                attemptLock();
            }
        }
    }

    public void releaseLock() throws KeeperException, InterruptedException {
        zk.delete(this.actualPath, -1);
        zk.close();
        System.out.println("释放锁成功");

    }


}
