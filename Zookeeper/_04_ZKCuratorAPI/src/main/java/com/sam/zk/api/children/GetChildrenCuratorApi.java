package com.sam.zk.api.children;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.List;

/**
 * @Package: com.sam.zk.api.children
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:36
 * @Description:
 */
public class GetChildrenCuratorApi {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();

    }

    private void children01(CuratorFramework cli) throws Exception {
        List<String> childrens = cli.getChildren()
                .forPath("/create03");
        childrens.forEach(c -> System.out.println(c));
    }

    private void children02(CuratorFramework cli) throws Exception {
        cli.getChildren()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        List<String> children = event.getChildren();
                        children.forEach(c -> System.out.println(c));
                    }
                })
                .forPath("create03");
    }




}
