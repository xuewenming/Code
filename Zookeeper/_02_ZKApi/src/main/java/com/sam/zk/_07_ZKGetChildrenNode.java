package com.sam.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author Mr.xuewenming
 * @title: _07_ZKGetChildrenNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2618:12
 */
public class _07_ZKGetChildrenNode {

    /**
     * @description:
     * String path：znode路径
     * boolean watcher：是否注册监听事件
     * callBack back：异步回调接口
     * ctx：上下文参数
     */

    // 同步获取子节点数据
    private void getChildren01(ZooKeeper zk) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        List<String> children = zk.getChildren("/node_01", false, stat);
        children.forEach(c -> System.out.println(c));
    }


    // 异步获取子节点数据
    private void getChildren02(ZooKeeper zk) {
        zk.getChildren("/node_02", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                // 0代表查询成功
                System.out.println(rc);
            }
        },"ctx");
    }

}
