package com.sam.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author Mr.xuewenming
 * @title: _08_ZKExistsNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2618:18
 */
public class _08_ZKExistsNode {

    /**
     * @description:
     * String path：znode节点路径
     * boolean watcher：是否为节点注册监听器
     * callBack back：异步回调函数
     * ctx：上下文传递
     */

    private void exists01(ZooKeeper zk) throws KeeperException, InterruptedException {
        Stat node_01 = zk.exists("node_01", false);
        if (node_01 == null) {
            System.out.println("节点不存在");
        }else{
            System.out.println("节点存在");
        }
    }


    private void exists02(ZooKeeper zk) {
        zk.exists("node_02", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                System.out.println(rc);
            }
        }, "ctx");
    }
}
