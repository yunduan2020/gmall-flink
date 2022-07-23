package com.tom.app;

import com.tom.utils.ThreadPoolUtil;
import lombok.SneakyThrows;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();

        for (int i = 0; i < 10; ++i){
            threadPool.submit(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ":tom");
                    Thread.sleep(2000);
                }
            });
        }
    }
}
