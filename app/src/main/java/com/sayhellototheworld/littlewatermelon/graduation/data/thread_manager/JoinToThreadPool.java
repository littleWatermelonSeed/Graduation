package com.sayhellototheworld.littlewatermelon.graduation.data.thread_manager;

/**
 * Created by 123 on 2017/9/10.
 */

public class JoinToThreadPool {

    private static CachedThreadPool sCachedThreadPool;
    private static ScheduledThreadPool sScheduledThreadPool;
    private static FixedThreadPool sFixedThreadPool;
    private static SingleThreadExecutor mSingleThreadExecutor;

    public static void joinToCache(Runnable runnable){
        if(sCachedThreadPool == null){
            sCachedThreadPool = CachedThreadPool.getInstance();
        }
        sCachedThreadPool.execute(runnable);
    }

    public static void joinToScheduled(Runnable runnable,int delay){
        if(sScheduledThreadPool == null){
            sScheduledThreadPool = ScheduledThreadPool.getInstance();
        }
        sScheduledThreadPool.execute(runnable,delay);
    }

    public static void joinToScheduled(Runnable runnable,int delay,int excute){
        if(sScheduledThreadPool == null){
            sScheduledThreadPool = ScheduledThreadPool.getInstance();
        }
        sScheduledThreadPool.execute(runnable,delay,excute);
    }

    public static void joinToFixed(Runnable runnable){
        if(sFixedThreadPool == null){
            sFixedThreadPool = FixedThreadPool.getInstance();
        }
        sFixedThreadPool.execute(runnable);
    }

    public static void joinToSingle(Runnable runnable){
        if(mSingleThreadExecutor == null){
            mSingleThreadExecutor = SingleThreadExecutor.getInstance();
        }
        mSingleThreadExecutor.execute(runnable);
    }

}
