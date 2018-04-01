package com.sayhellototheworld.littlewatermelon.graduation.data.thread_manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 123 on 2017/9/10.
 */

public class SingleThreadExecutor {

    private ExecutorService singleThreadExecutor;
    private static SingleThreadExecutor mSingleThreadExecutor = null;

    private SingleThreadExecutor() {
        /**
         * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
         * */
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public static SingleThreadExecutor getInstance(){
        if(mSingleThreadExecutor == null){
            mSingleThreadExecutor = new SingleThreadExecutor();
        }
        return mSingleThreadExecutor;
    }

    public void execute(Runnable runnable){
        singleThreadExecutor.execute(runnable);
    }

}
