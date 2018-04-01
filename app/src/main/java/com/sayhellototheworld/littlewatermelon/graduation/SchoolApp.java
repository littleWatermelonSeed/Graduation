package com.sayhellototheworld.littlewatermelon.graduation;

import android.app.Application;
import android.content.Context;

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

    public static Context getAppContext(){
        return appContext;
    }

}
