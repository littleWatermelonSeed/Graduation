package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.real_data;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.othershe.nicedialog.BaseNiceDialog;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.ForgetPasswordActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by 123 on 2017/9/19.
 */

public class RealTimeData{

    private static RealTimeData mRealTimeData;
    private static BmobRealTimeData dataUserLogin;
    private static Context mContext;
    private BaseNiceDialog dialog;

    public static RealTimeData getInstance() {
        if (mRealTimeData == null) {
            mRealTimeData = new RealTimeData();
        }
        return mRealTimeData;
    }

    private RealTimeData() {
        mContext = SchoolApp.getAppContext();
        dataUserLogin = new BmobRealTimeData();
    }

    public void subUserLoginDeviceId(final MyUserBean currentUser) {
        dataUserLogin.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                Log.i("123","实时连接完成 连接状态为：" + dataUserLogin.isConnected());
                if (dataUserLogin.isConnected()) {
                    Log.i("123","监听行ID为：" + currentUser.getObjectId());
                    dataUserLogin.subRowUpdate("_User", currentUser.getObjectId());
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                Log.i("123","用户异地登录");
                JSONObject data = jsonObject.optJSONObject("data");
                if (!currentUser.getLoginDeviceId().equals(data.optString("loginDeviceId"))){
                    userLoginChange();
                    Log.i("123","登录手机变化 新ID为：" + data.optString("loginDeviceId"));
                }else {
                    Log.i("123","登录手机没变化变化");
                }
            }
        });
    }

    public void unsubUserLoginDeviceId(final MyUserBean currentUser) {
        if (dataUserLogin != null && dataUserLogin.isConnected()) {
            dataUserLogin.unsubRowUpdate("_User", currentUser.getObjectId());
        }
    }

    private void userLoginChange(){
        BmobManageUser.loginOutUser();
        final MyActivityManager activityManager = MyActivityManager.getDestoryed();
        final Activity topActivity = activityManager.getTopActivity();
        mContext = topActivity;
//        String activityName = topActivity.getClass().getSimpleName();
//        boolean topUserActivity = false;
//        if (activityManager.isExistInUserMap(activityName)){
//            topUserActivity = true;
//            Log.i("niyuanjie","top activity存在于userMap");
//        }else {
//            topUserActivity = false;
//            Log.i("niyuanjie","top activity不存在于userMap");
//        }
        DialogConfirm.newInstance("警告", "你的账号被异地登录,将关闭所有用户相关界面,如不是本人操作建议修改密码,你可以修改密码/重新登录", R.layout.nicedialog_remind_loginout_layout, new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {
                activityManager.destroyedListActivity();
                ForgetPasswordActivity.startLoginActivity(topActivity);
                activityManager.clearUserMap();
            }

            @Override
            public void ok() {
                LoginActivity.startLoginActivityWithLoginOut(topActivity);
                activityManager.clearUserMap();
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(((FragmentActivity)topActivity).getSupportFragmentManager());
    }

}
