package com.sam.zk.api;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Package: com.sam.zk.api.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 16:51
 * @Description:
 */
public class _01_ConnectionApi {

    private final String zkServerPath = "172.16.14.104:2181";
    private final int timeout = 5000;
    private CuratorFramework curator;

    public _01_ConnectionApi() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 5);

        this.curator = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .sessionTimeoutMs(timeout)
                .canBeReadOnly(false)
                .retryPolicy(retryPolicy)
                .build();
    }

    public CuratorFramework getCurator() {
        return curator;
    }
}
