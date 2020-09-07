package com.sam.zk.api.create;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.sam.zk.api.create
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 17:57
 * @Description:
 */
public class CreateCuratorApi {

    private byte[] data = "create".getBytes();

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework curator = api.getCurator();
    }


    private void create01(CuratorFramework client) throws Exception {
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/create01", data);

    }

    // 自定义权限列表
    private void create02(CuratorFramework client) throws Exception {
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("ip", "172.16.14.131");
        acls.add(new ACL(ZooDefs.Perms.ALL, id));

        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(acls)
                .forPath("/create02",data);
    }


    // 递归创建节点数据
    private void create03(CuratorFramework client) throws Exception {
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/create03/create03_child", data);
    }

    // 异步创建节点
    private void create04(CuratorFramework client) throws Exception {
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println(event.getPath());
                        System.out.println(event.getType());
                    }
                })
                .forPath("/create_04");
    }

}
