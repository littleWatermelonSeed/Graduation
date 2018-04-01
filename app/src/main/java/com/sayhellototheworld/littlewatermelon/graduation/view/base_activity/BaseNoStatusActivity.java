package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

/**
 * Created by 123 on 2017/8/23.
 */

public class BaseNoStatusActivity extends AutoLayoutActivity {

    protected boolean isActive = true;
    protected MyActivityManager baseActivityManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseInit();
    }

    private void baseInit(){
        //禁止屏幕横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //禁止自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        baseActivityManager = MyActivityManager.getDestoryed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()){
            isActive = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isActive){
            isActive = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        baseActivityManager.setTopActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseActivityManager.removeActivityFromUserMap(getClass().getSimpleName());
    }

    protected boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
