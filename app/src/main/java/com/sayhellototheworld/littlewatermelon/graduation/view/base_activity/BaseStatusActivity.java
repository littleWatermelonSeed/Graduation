package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

/**
 * Created by 123 on 2017/8/22.
 */

public class BaseStatusActivity extends AutoLayoutActivity {

    protected boolean isActive = true;
    protected SystemBarTintManager tintManager;
    protected MyActivityManager baseActivityManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseInit();
//        else if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

    }

    private void baseInit(){
        //禁止屏幕横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //禁止自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.white1);//通知栏所需颜色
        }
        baseActivityManager = MyActivityManager.getDestoryed();
    }

    protected SystemBarTintManager getTintManager() {
        if (tintManager != null) {
            return tintManager;
        }
        return null;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
