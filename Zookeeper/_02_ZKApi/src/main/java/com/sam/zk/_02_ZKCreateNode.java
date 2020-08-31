package com.sam.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.xuewenming
 * @title: _02_ZKCreateNode
 * @projectName Code
 * @description: TODO
 * @date 2020/8/2523:13
 */
public class _02_ZKCreateNode {


    private _02_ZKCreateNode(){
        Connection zkConn = new Connection();
    }



    /**
     * @description: 创建节点,两种方式
     *                          同步创建
     *                          异步创建
     * create(final String path,byte data[], List<ACL> acl,
     *             CreateMode createMode)
     * create(final String path,byte data[],List<ACL> acl,
     *             CreateMode createMode,StringCallback cb, Object ctx)
     *
     * path：znode节点路径地址
     * data：znode节点数据
     * acl：znode的数据权限，使用静态接口ZooDefs.Ids获取权限列表
     * createMode：znode节点烈性，枚举类
     * callBack：异步回调接口
     * ctx：传递上下文参数
     *
     */


    // =======================================
    // =======================================

    private static void create01(ZooKeeper zk) throws Exception {
        String path = "/node_01";
        byte[] data = "node".getBytes();
        // Ids.OPEN_ACL_UNSAFE: word:anyone:cdrwa
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    private static void create02(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        zooKeeper.create("/node_02", "node".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // world授权模式
    private static void create03(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // 权限列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权对象
        Id id = new Id("world", "anyone");
        // 设置权限
        acls.add(new ACL(ZooDefs.Perms.READ, id));
        acls.add(new ACL(ZooDefs.Perms.WRITE, id));
        zooKeeper.create("/node_03", "node".getBytes(), acls, CreateMode.PERSISTENT);
    }

    // IP授权模式
    private static void create04(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // 权限列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权对象
        Id id = new Id("ip", "172.16.225.104");
        // 设置权限
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zooKeeper.create("node_04", "node".getBytes(), acls, CreateMode.PERSISTENT);
    }

    // auth授权模式
    private void create05(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 授权用户
        zk.addAuthInfo("auth", "root:root".getBytes());
        zk.create("node_05", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // auth授权模式
    private void create06(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 添加授权用户
        zk.addAuthInfo("auth","root:root".getBytes());
        // 授权列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权用户
        Id id = new Id("auth", "root");
        acls.add(new ACL(ZooDefs.Perms.ALL, id));
        zk.create("node_06", "node".getBytes(), acls, CreateMode.PERSISTENT);
    }

    // digest授权模式
    private void create07(ZooKeeper zk) throws KeeperException, InterruptedException {
        // 授权列表
        List<ACL> acls = new ArrayList<>();
        // 授权模式和授权用户
        Id id = new Id("digest", "root:xxxxx");
        // 权限设置
        acls.add(new ACL(ZooDefs.Perms.ALL, id));

        zk.create("/node_07", "node".getBytes(), acls, CreateMode.PERSISTENT);
    }

    // 异步创建节点
    private void create08(ZooKeeper zk) {
        zk.create("/node_08", "node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                // 0 代表创建成功
                System.out.println(rc);
                System.out.println(ctx);
            }
        }, "xxxxx");
    }


    public static void main(String[] args) throws Exception {


    }


}
