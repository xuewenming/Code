package com.sam.zk.api.get;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.data.Stat;

/**
 * @Package: com.sam.zk.api.get
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:28
 * @Description:
 */
public class GetCuratorApi {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();
    }


    private void get01(CuratorFramework cli) throws Exception {
        cli.getData()
                .forPath("/create01");
    }

    // 查看节点状态
    private void get02(CuratorFramework cli) throws Exception {
        Stat stat = new Stat();
        cli.getData()
                .storingStatIn(stat)
                .forPath("/create02");
    }

    // 异步查看节点状态
    private void get03(CuratorFramework cli) {
        cli.getData()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("path：" + event.getPath());
                    }
                });
    }


}
