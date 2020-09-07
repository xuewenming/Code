package com.sam.zk.api.delete;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;

/**
 * @Package: com.sam.zk.api.delete
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:22
 * @Description:
 */
public class DeleteCuratorApi {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();

    }

    private void delete01(CuratorFramework client) throws Exception {
        client.delete()
                .guaranteed()
                .forPath("/create01");
    }

    // 递归删除
    private void delete02(CuratorFramework client) throws Exception {
        client.delete()
                .guaranteed()
                .deletingChildrenIfNeeded()
                .forPath("create02");
    }

    // 异步删除
    private void delete03(CuratorFramework client) {
        client.delete()
                .guaranteed()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("path：" + event.getPath());
                    }
                });
    }


}
