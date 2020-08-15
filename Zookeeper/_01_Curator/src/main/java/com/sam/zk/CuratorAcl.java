package com.sam.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.sam.zk
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/8/12 17:15
 * @Description:
 */
public class CuratorAcl {
    private CuratorFramework client = null;
    private final String zkServerPath = "172.16.14.131:2181";
    private String nodePath = "/acl/dev/a";

    public CuratorAcl() {
        RetryPolicy retryPolicy = new RetryNTimes(5, 5000);
        client = CuratorFrameworkFactory.builder().retryPolicy(retryPolicy)
                .authorization("digest","root2:root2".getBytes()) // 登录验证
                .connectString(zkServerPath)
                .sessionTimeoutMs(5000)
                .namespace("workspace")
                .build();
        client.start();
    }

    private String getDigestUserPwd(String userPwd) {
        String digest = "";
        try {
            digest = DigestAuthenticationProvider.generateDigest(userPwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    // 更新节点操作
    private void updateNode(CuratorAcl acl) throws Exception {
        byte[] updateData = "balabala".getBytes();
        acl.client.setData().forPath(nodePath, updateData);
    }

    private void deleteNode(CuratorAcl acl) throws Exception {
        acl.client.delete().guaranteed().deletingChildrenIfNeeded().forPath(nodePath);
    }

    public void getNode(CuratorAcl acl) throws Exception {
        Stat stat = new Stat();
        byte[] bytes = acl.client.getData().storingStatIn(stat).forPath(nodePath);
        System.out.println(new String(bytes));
    }




    // 设置权限
    private void setAcl(CuratorAcl acl) throws Exception {
        List<ACL> aclList = new ArrayList<>();
        Id user1 = new Id("digest", acl.getDigestUserPwd("root:root"));
        Id user2 = new Id("digest", acl.getDigestUserPwd("root2:root2"));
        aclList.add(new ACL(ZooDefs.Perms.ALL, user1));
        aclList.add(new ACL(ZooDefs.Perms.READ, user2));
        aclList.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, user2));

        acl.client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(aclList)
                .forPath(nodePath, "bbb".getBytes());

        // 节点设置权限
        acl.client.setACL().withACL(aclList).forPath("/curatorNode");
    }

    public static void main(String[] args) throws Exception {
        CuratorAcl acl = new CuratorAcl();
//        acl.setAcl(acl);
//        acl.updateNode(acl);
//        acl.getNode(acl);
        System.out.println(acl.getDigestUserPwd("root:root"));


    }


}
