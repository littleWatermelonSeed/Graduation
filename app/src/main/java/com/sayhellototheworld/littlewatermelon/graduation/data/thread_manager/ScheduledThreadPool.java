package com.sayhellototheworld.littlewatermelon.graduation.data.thread_manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 123 on 2017/9/10.
 */

public class ScheduledThreadPool {

    private ScheduledExecutorService scheduledThreadPool;
    private static ScheduledThreadPool mScheduledThreadPool = null;

    private ScheduledThreadPool() {
        /**
         * 创建一个定长线程池，支持定时及周期性任务执行
         * */
        scheduledThreadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static ScheduledThreadPool getInstance(){
        if(mScheduledThreadPool == null){
            mScheduledThreadPool = new ScheduledThreadPool();
        }
        return mScheduledThreadPool;
    }

    public void execute(Runnable runnable,int delay){
        /**
         * 表示延迟 delay 秒执行
         * */
        scheduledThreadPool.schedule(runnable,delay, TimeUnit.SECONDS);
    }

    public void execute(Runnable runnable,int delay,int excute){
        /**
         * 表示延迟 delay 秒后每 excute 秒执行一次。
         * */
        scheduledThreadPool.scheduleAtFixedRate(runnable,delay, excute,TimeUnit.SECONDS);
    }

}
