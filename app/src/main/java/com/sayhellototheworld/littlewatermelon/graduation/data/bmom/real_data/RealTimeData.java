package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.real_data;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.othershe.nicedialog.BaseNiceDialog;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserLoginDo;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.ForgetPasswordActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by 123 on 2017/9/19.
 */

public class RealTimeData implements UserLoginDo {

    private static RealTimeData mRealTimeData;
    private static BmobRealTimeData dataUserLogin;
    private static Context mContext;
    private BaseNiceDialog dialog;
    private Handler handler;

    public static RealTimeData getInstance() {
        if (mRealTimeData == null) {
            mRealTimeData = new RealTimeData();
        }
        return mRealTimeData;
    }

    private RealTimeData() {
        mContext = SchoolApp.getAppContext();
        dataUserLogin = new BmobRealTimeData();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("登录成功");
                }
            }
        };
    }

    public void subUserLoginDeviceId(final MyUserBean currentUser) {
        dataUserLogin.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if (dataUserLogin.isConnected()) {
                    dataUserLogin.subRowUpdate("_User", currentUser.getObjectId());
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                JSONObject data = jsonObject.optJSONObject("data");
                if (!currentUser.getLoginDeviceId().equals(data.optString("loginDeviceId"))){
                    userLoginChange();
                    Log.i("niyuanjie","登录手机变化 新ID为：" + data.optString("loginDeviceId"));
                }else {
                    Log.i("niyuanjie","登录手机没变化变化");
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
        final MyActivityManager activityManager = MyActivityManager.getDestoryed();
        final Activity topActivity = activityManager.getTopActivity();
        mContext = topActivity;
        String activityName = topActivity.getClass().getSimpleName();
        boolean topUserActivity = false;
        if (activityManager.isExistInUserMap(activityName)){
            topUserActivity = true;
            Log.i("niyuanjie","top activity存在于userMap");
        }else {
            topUserActivity = false;
            Log.i("niyuanjie","top activity不存在于userMap");
        }
        DialogConfirm.newInstance("警告", "你的账号被异地登录,将关闭所有用户相关界面,如不是本人操作建议修改密码,你可以修改密码/重新登录", R.layout.nicedialog_remind_loginout_layout, new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {
                BmobManageUser.loginOutUser();
                ForgetPasswordActivity.startLoginActivity(topActivity);
//                ControlUserFragment.syncUserFragment();
//                activityManager.clearUserMap();
            }

            @Override
            public void ok() {
                BmobManageUser.loginOutUser();
//                DialogLoading.showLoadingDialog(((FragmentActivity)topActivity).getSupportFragmentManager(),
//                        new DialogLoading.ShowLoadingDone() {
//                            @Override
//                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
//                                dialog = baseNiceDialog;
//                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
//                                textView.setText("登录中...");
//                                BmobManageUser bmobManageUser = new BmobManageUser(topActivity);
//                                bmobManageUser.loginAndSyncUser(MySharedPreferences.getInstance().getStringMessage(MySharedPreferences.KEY_USER_ID),
//                                        MySharedPreferences.getInstance().getStringMessage(MySharedPreferences.KEY_USER_PASSWORD),
//                                        RealTimeData.this);
//                            }
//                        });
                LoginActivity.startLoginActivity(topActivity);
//                ControlUserFragment.syncUserFragment();
//                activityManager.clearUserMap();
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(((FragmentActivity)topActivity).getSupportFragmentManager());
    }

    @Override
    public void loginSuccess(MyUserBean myUserBean) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"登录成功", DialogLoading.MSG_SUCCESS);
//        ControlUserFragment.syncUserFragment();
    }

    @Override
    public void loginFail(BmobException ex) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext,ex);
    }

}
