package com.sayhellototheworld.littlewatermelon.graduation;

import android.app.Application;
import android.content.Context;

import com.sayhellototheworld.littlewatermelon.graduation.im.IMMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 123 on 2018/4/1.
 */

public class SchoolApp extends Application{

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        appContext = getApplicationContext();
        initBmob();
        initRealm();
        initIM();
    }

    private void initBmob(){
        Bmob.initialize(getAppContext(), "4e72a6b302b42f82725c95c9749c624b");
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("AISchool")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initIM(){
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new IMMessageHandler());
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Context getAppContext(){
        return appContext;
    }

}
