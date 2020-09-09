package com.sam.zk.example;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @Package: com.sam.zk.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/8 15:44
 * @Description:
 */
public class InterProcessMutexLock {
    private static InterProcessMutex mutex;

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();
    }

    private void lock01(CuratorFramework cli) throws Exception {
        /**
         * args1：客户端
         * args2：路径
         */
        mutex = new InterProcessMutex(cli,"/lock");
        System.out.println("等待获取锁对象");
        // 获取锁
        mutex.acquire();
        // 进行业务逻辑操作
        for (int i = 0; i < 10; i++) {
            Thread.sleep(3000);
            System.out.println(i);
        }
        // 释放锁
        mutex.release();
        System.out.println("等待释放锁");
    }
}
