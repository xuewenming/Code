package com.sam.zk;

import com.sam.zk.watcher.MyWatcher;
import com.sun.deploy.security.ValidationState;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Package: com.sam.zk
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/8/12 15:08
 * @Description:
 */
public class CuratorWatcher {
    private final String nodePath = "/dev/sam";


    // watcher事件，当使用usingWatcher的时候，监听只会除触发一次，监听完毕就销毁
    // 触发监听事件的是nodePath上进行操作
    private void watcherNode(CuratorOperator cto) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        cto.client.getData().usingWatcher(new MyWatcher(latch)).forPath(nodePath);
//        cto.client.getData().usingWatcher(new MyCuratorWatcher(latch)).forPath(nodePath);
        latch.await();

    }

    // 为节点添加永久watcher
    private void watcher(CuratorOperator cto) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        // 监听节点数据变更，会触发事件
        NodeCache nodeCache = new NodeCache(cto.client,nodePath);
        nodeCache.start(true);
        if(nodeCache.getCurrentData() != null)
            System.out.println("节点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
        else
            System.out.println("节点初始化数据为空...");

        nodeCache.getListenable().addListener(()->{
            if(nodeCache.getCurrentData() == null){
                System.out.println("空...");
                latch.countDown();
                return;
            }
            String data = new String(nodeCache.getCurrentData().getData());
            System.out.println("节点路径：" + nodeCache.getCurrentData().getPath() + "数据：" + data);
            latch.countDown();
        });
        latch.await();
    }

    // 为子节点添加监听时间
    private void childWatcher(CuratorOperator cto) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        String childNodePath = nodePath;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(cto.client, childNodePath, true);
        /**
         * StartMode：初始化方式
         *  - POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件，一版采用的方式
         *  - NORMAL：异步初始化
         *  - BUILD_INITIAL_CACHE：同步初始化
         */
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> currentData = pathChildrenCache.getCurrentData();
        System.out.println("当前节点的子节点列表");
        currentData.forEach(childData -> {
            System.out.println(new String(childData.getData()));
        });
        // 为子节点添加监听事件
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化完成...");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    String path = event.getData().getPath();
                    if(path.equals("/sam")){
                        System.out.println("添加子节点：" + event.getData().getPath());
                        System.out.println("子节点数据：" + new String(event.getData().getData()));
                    }else{
                        System.out.println("添加子节点路径不正确....");
                    }
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println("删除子节点数据：" + event.getData().getPath());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println("修改子节点路径：" + event.getData().getPath());
                    System.out.println("修改子节点数据：" + new String(event.getData().getData()));

                }
            }
        });
        latch.await();
    }


    public static void main(String[] args) throws Exception {
        CuratorOperator cto = new CuratorOperator();
        CuratorWatcher watcherAndAcl = new CuratorWatcher();
//        watcherAndAcl.watcherNode(cto);
//        watcherAndAcl.watcher(cto);
        watcherAndAcl.childWatcher(cto);
    }





}
