package com.sam.example;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.sam.example
 * @ClassName: Xuewm
 * @Author: huayu
 * @CreateTime: 2020/9/1 11:02
 * @Description:
 */
public class TicketSeller implements Runnable{
    // 测试zk分布式锁

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new TicketSeller());
    }

    // 售票
    private static void sell() throws InterruptedException {

            System.out.println("开始售票");
            TimeUnit.SECONDS.sleep(2);
            System.out.println("结束售票");
    }

    @Override
    public void run() {

    }
}
