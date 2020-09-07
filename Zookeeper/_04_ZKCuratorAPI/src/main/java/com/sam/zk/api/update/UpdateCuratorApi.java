package com.sam.zk.api.update;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;

/**
 * @Package: com.sam.zk.api.update
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:12
 * @Description:
 */
public class UpdateCuratorApi {

    private byte[] data = "update".getBytes();

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();
    }


    private void update01(CuratorFramework client) throws Exception {
        client.setData().forPath("/create01", data);
    }

    // 指定版本号
    private void update02(CuratorFramework client) throws Exception {
        client.setData()
                .withVersion(0)
                .forPath("/create02", data);
    }

    // 指定版本号，不设置版本
    private void update03(CuratorFramework client) throws Exception {
        client.setData()
                .withVersion(-1)
                .forPath("/create03", data);
    }

    // 异步更新
    public void update04(CuratorFramework client) {
        client.setData()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("type：" + event.getType());
                        System.out.println("path：" +event.getPath());
                    }
                });
    }



}
