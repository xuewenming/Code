package com.sam.zk.watcher;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @Package: com.sam.zk.watcher
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/8/12 15:27
 * @Description:
 */
public class MyCuratorWatcher implements CuratorWatcher {
    private CountDownLatch latch;
    @Override
    public void process(WatchedEvent event) throws Exception {
        Watcher.Event.EventType type = event.getType();
        String path = event.getPath();
        Watcher.Event.KeeperState state = event.getState();

        System.err.println("type：" + type +",path：" + path + ",stat：" + state);
        latch.countDown();
    }
}
