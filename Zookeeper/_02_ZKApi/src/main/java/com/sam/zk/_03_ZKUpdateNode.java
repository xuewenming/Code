package com.sam.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author Mr.xuewenming
 * @title: _03_ZKUpdateNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2617:31
 */
public class _03_ZKUpdateNode {

    /**
     * @description: 更新节点
     * String path：znode路径
     * byte data[]：znode节点数据
     * int version：znode当前版本号,"-1"代表版本号不作为修改条件
     * StatCallback cb：异步回调接口
     * Object ctx：上下文参数
     *
     *
     */


    // 同步更新
    private void update01(ZooKeeper zk) throws KeeperException, InterruptedException {
        Stat stat = zk.setData("/node_01", "nodenode".getBytes(), -1);
    }

    // 异步更新
    private void update02(ZooKeeper zk) {

        zk.setData("/node_02", "nodenode".getBytes(), -1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 0代表成功
                System.out.println(rc);
            }
        }, "ctx");

    }



    public static void main(String[] args) {

    }

}
