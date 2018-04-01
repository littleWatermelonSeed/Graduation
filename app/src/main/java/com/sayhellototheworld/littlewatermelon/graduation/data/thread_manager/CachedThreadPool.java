package com.sayhellototheworld.littlewatermelon.graduation.data.thread_manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 123 on 2017/9/10.
 */

public class CachedThreadPool {

    private ExecutorService cachedThreadPool;
    private static CachedThreadPool mCachedThreadPool = null;

    private CachedThreadPool() {
        /**
         * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
         * */
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    public static CachedThreadPool getInstance(){
        if(mCachedThreadPool == null){
            mCachedThreadPool = new CachedThreadPool();
        }
        return mCachedThreadPool;
    }

    public void execute(Runnable runnable){
        cachedThreadPool.execute(runnable);
    }

}
