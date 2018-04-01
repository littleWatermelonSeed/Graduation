package com.sayhellototheworld.littlewatermelon.graduation.data.local_file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;

import java.io.File;

/**
 * Created by 123 on 2017/9/7.
 */

public class FilePath {

    public static File getCacheDirectory(String type) {
        Context context = SchoolApp.getAppContext();
        File appCacheDir = getExternalCacheDirectory(type);
        if (appCacheDir == null){
            appCacheDir = getInternalCacheDirectory(type);
        }

        if (appCacheDir == null){
            Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        }else {
            if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
                Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }

    public static File getExternalCacheDirectory(String type) {
        Context context = SchoolApp.getAppContext();
        File appCacheDir = null;
        if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            if (TextUtils.isEmpty(type)){
//                appCacheDir = context.getExternalCacheDir();
//            }else {
//                appCacheDir = context.getExternalFilesDir(type);
//            }
            appCacheDir = context.getExternalFilesDir(type);
            if (appCacheDir == null){// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),"Android/data/"+context.getPackageName()+"/cache/"+type);
            }

            if (appCacheDir == null){
                Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard unknown exception !");
            }else {
                if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
                    Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        }else {
            Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    public static File getInternalCacheDirectory(String type) {
        Context context = SchoolApp.getAppContext();
        File appCacheDir = null;
//        appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        appCacheDir = new File(context.getFilesDir(),type);// /data/data/app_package_name/files/type
        if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
            Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

}
