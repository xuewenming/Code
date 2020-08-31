package com.sam.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author Mr.xuewenming
 * @title: _05_ZKGetNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2618:00
 */
public class _05_ZKGetNode {

    /**
     * @description:
     * String path：znode路径
     * boolean watch：是否注册监听事件
     * Stat stat：返回znode原数据
     * DataCallback cb：回调函数
     * Object ctx：上下文
     *
     */

    // 同步查看
    private void get01(ZooKeeper zk) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = zk.getData("/node_01", false, stat);
        System.out.println("打印的数据："+ data);
        System.out.println("znode原数据信息：" + stat);
    }

    // 异步查看
    private void get02(ZooKeeper zk) {
        zk.getData("/node_02", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                // 0代表读取成功
                System.out.println(rc);
            }
        },"xxx");
    }
}
