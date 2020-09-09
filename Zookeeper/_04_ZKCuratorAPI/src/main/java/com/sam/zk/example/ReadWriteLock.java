package com.sam.zk.example;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @Package: com.sam.zk.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/9 9:36
 * @Description:
 */
public class ReadWriteLock {

    private static InterProcessReadWriteLock readWriteLock;

    public ReadWriteLock() {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();
         readWriteLock = new InterProcessReadWriteLock(client, "/lock");
    }

    // 获取读锁
    public void getReadLock(CuratorFramework cli) throws Exception {
        InterProcessMutex readLock = readWriteLock.readLock();

        // 获取锁
        readLock.acquire();

        // 进行业务逻辑操作
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(2);
        }
        // 释放锁
        readLock.release();
    }

    // 获取写锁
    public void getWriteLock(CuratorFramework cli) throws Exception {
        InterProcessMutex writeLock = readWriteLock.writeLock();
        // 获取写锁
        writeLock.acquire();

        // 进行业务逻辑操作
        for (int i = 0; i < 10; i++) {
            TimeUnit.SECONDS.sleep(1);
        }

        // 释放锁
        writeLock.release();
    }

}
