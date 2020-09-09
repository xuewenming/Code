package com.sam.zk.example;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.Collection;

/**
 * @Package: com.sam.zk.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/8 15:40
 * @Description: 分布式事务
 */
public class DistributedTransaction {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();
        
    }

    private void transaction(CuratorFramework cli) throws Exception {
        Collection<CuratorTransactionResult> res = cli.inTransaction()
                .create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/transaction01")
                .and()
                .setData()
                .forPath("transaction02", "ddd".getBytes())
                .and()
                .commit();
    }
    
}
