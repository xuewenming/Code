package com.sam.zk.api.watcher;

import com.sam.zk.api._01_ConnectionApi;
import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import java.nio.file.Path;

/**
 * @Package: com.sam.zk.api.watcher
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:50
 * @Description:
 */
public class WatcherCuratorApi {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();

    }

    // Node Cache：只监听某一个特定的节点，监听节点的新增和修改
    private void watcher01(CuratorFramework cli) throws Exception {
        final NodeCache nodeCache = new NodeCache(cli, "/create01");
        nodeCache.start(true);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                java.lang.String path = nodeCache.getCurrentData().getPath();
                byte[] data = nodeCache.getCurrentData().getData();
            }
        });
    }


    // PathChildren Cache：监控某一个ZNode子节点，当一个子节点增加，更新，删除时。PathChildren会记录它的状态
    private void watcher02(CuratorFramework cli) throws Exception {
        /**
         * args1：curator对象
         * args2：监视的节点路径
         * args3：是否可以获取节点的数据
         */
        PathChildrenCache childrenCache = new PathChildrenCache(cli, "create01", true);
        /**
         * StartMode：初始化方式
         *  - POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件，一版采用的方式
         *  - NORMAL：异步初始化
         *  - BUILD_INITIAL_CACHE：同步初始化
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    System.out.println("节点新增");
                    System.out.println("path：" + pathChildrenCacheEvent.getData().getPath());
                } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                    System.out.println("节点删除");
                } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                    System.out.println("节点更新");
                }
            }
        });
        // 关闭监听
        childrenCache.close();
    }

}
