package com.sam.zk;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author Mr.xuewenming
 * @title: _04_ZKDelNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2617:48
 */
public class _04_ZKDelNode {

    /**
     * @description:
     * String path：znode路径
     * int version：版本号
     * VoidCallback cb：异步回调接口
     * Object ctx：传递上下文参数
     *
     */

    // 同步删除
    private void del01(ZooKeeper zk) throws KeeperException, InterruptedException {
        // version：-1代表删除时不考虑版本信息
        zk.delete("/node_01", -1);
    }

    // 异步删除
    private void del02(ZooKeeper zk) {
        zk.delete("/node_02", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                // 0代表删除成功
                System.out.println(rc);
            }
        },"ctx");
    }

    public static void main(String[] args) {

    }
}
