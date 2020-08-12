package com.sam.zk.watcher;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @Package: com.sam.zk.watcher
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/8/12 15:24
 * @Description:
 */
public class MyWatcher implements Watcher {
    private CountDownLatch latch;

    public MyWatcher(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.KeeperState state = watchedEvent.getState();
        String path = watchedEvent.getPath();
        Event.EventType type = watchedEvent.getType();
        int intValue = type.getIntValue();
        System.out.println(intValue);
        System.err.println("stat：" + state + ",path：" + path + ",type：" + type);
        latch.countDown();
    }
}
