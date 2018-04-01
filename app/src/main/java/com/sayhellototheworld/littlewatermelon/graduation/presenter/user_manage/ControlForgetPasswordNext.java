package com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ResetPasswordBySmsDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserGetKeyCodeDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ViForgetPasswordNextCoDo;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/11.
 */

public class ControlForgetPasswordNext implements UserGetKeyCodeDo,ViForgetPasswordNextCoDo,ResetPasswordBySmsDo {

    private Context mContext;
    private Button keyCodeSend;

    private String phoneNum;
    private String newPS;

    private CountDownTimer timer;
    private BmobManageUser mBmobManageUser;
    private final Handler mHandler;
    private BaseNiceDialog dialog;

    public ControlForgetPasswordNext(Context context, Button keyCodeSend,String phoneNum) {
        mContext = context;
        this.keyCodeSend = keyCodeSend;
        this.phoneNum = phoneNum;
        mBmobManageUser = new BmobManageUser(mContext);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                }
            }
        };
    }

    @Override
    public void sentSmsCode(String phoneNum) {
        timer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                keyCodeSend.setText((millisUntilFinished / 1000) + "秒后重发");
                keyCodeSend.setClickable(false);
                keyCodeSend.setBackgroundResource(R.drawable.radius_background_buttongreen3);
            }

            @Override
            public void onFinish() {
                keyCodeSend.setText("重新发送");
                keyCodeSend.setClickable(true);
                keyCodeSend.setBackgroundResource(R.drawable.state_button1);
            }
        };
        timer.start();
        mBmobManageUser.getKeyCode(phoneNum,this, BmobManageUser.SMS_TEMPLATE_CHANGEPASSWORD);
    }

    @Override
    public void resetPassWord(final String smsCode, final String newPassword) {
        newPS = newPassword;
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("修改中...");
                        mBmobManageUser.resetPasswordBySMSCode(smsCode,newPassword,ControlForgetPasswordNext.this);
                    }
                });
    }

    @Override
    public void sendNoteSuccess() {
        MyToastUtil.showToast("验证码已发送");
    }

    @Override
    public void sendNoteFail(BmobException ex) {
        timer.cancel();
        keyCodeSend.setClickable(true);
        keyCodeSend.setText("重新发送");
        keyCodeSend.setBackgroundResource(R.drawable.state_button1);
        BmobExceptionUtil.dealWithException(mContext, ex);
    }

    @Override
    public void resetSuccess() {
        DialogLoading.dismissLoadingDialog(mHandler,dialog,"密码修改成功",DialogLoading.MSG_SUCCESS);
        MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_USER_ID,phoneNum);
        MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_USER_PASSWORD,newPS);
        MyActivityManager.getDestoryed().destroyedListActivity();
        LoginActivity.startLoginActivity(mContext);
    }

    @Override
    public void resetFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(mHandler,dialog, "",DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext,e);
    }

}
