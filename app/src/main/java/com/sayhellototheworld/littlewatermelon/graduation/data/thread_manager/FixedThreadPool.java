package com.sayhellototheworld.littlewatermelon.graduation.data.thread_manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 123 on 2017/9/10.
 */

public class FixedThreadPool {

    private ExecutorService fixedThreadPool;
    private static FixedThreadPool mFixedThreadPool = null;

    private FixedThreadPool() {
        /**
         * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
         * */
        fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static FixedThreadPool getInstance(){
        if(mFixedThreadPool == null){
            mFixedThreadPool = new FixedThreadPool();
        }
        return mFixedThreadPool;
    }

    public void execute(Runnable runnable){
        fixedThreadPool.execute(runnable);
    }

}
