package com.sam.zk.api.exists;

import com.sam.zk.api._01_ConnectionApi;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * @Package: com.sam.zk.api.exists
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/7 18:42
 * @Description:
 */
public class ExistsCuratorApi {

    public static void main(String[] args) {
        _01_ConnectionApi api = new _01_ConnectionApi();
        CuratorFramework client = api.getCurator();


    }

    private void exists01(CuratorFramework cli) throws Exception {
        Stat stat = cli.checkExists()
                .forPath("/create01");
        if (stat == null) {
            System.out.println("空");
        }else{
            System.out.println("非空");
        }
    }

}
