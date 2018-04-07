package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserLoginDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.center_plaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import cn.bmob.v3.exception.BmobException;

public class LoginActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,UserLoginDo {

    private LinearLayout parentLayout;
    private EditText editText_userID;
    private EditText editText_password;
    private Button button_login;
    private TextView textView_register;
    private TextView textView_forgetPassword;

    private String userID;
    private String userPassword;
    private BaseNiceDialog dialog;
    private Handler handler;

    private MySharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        parentLayout = (LinearLayout)findViewById(R.id.activity_login_parent);
        editText_userID = (EditText)findViewById(R.id.activity_login_userID);
        editText_password = (EditText)findViewById(R.id.activity_login_userPassword);
        button_login = (Button)findViewById(R.id.activity_login_loginButton);
        button_login.setOnClickListener(this);
        textView_register = (TextView)findViewById(R.id.activity_login_registerUser);
        textView_register.setOnClickListener(this);
        textView_forgetPassword = (TextView)findViewById(R.id.activity_login_forgetPassword);
        textView_forgetPassword.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        if(tintManager != null){
            tintManager.setStatusBarAlpha(0);
        }
        baseActivityManager.addActivityToList(this);
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
        mPreferences = MySharedPreferences.getInstance();

        userID = mPreferences.getStringMessage(MySharedPreferences.KEY_USER_ID);
        userPassword = mPreferences.getStringMessage(MySharedPreferences.KEY_USER_PASSWORD);
    }

    @Override
    protected void initShow() {
        LayoutBackgroundUtil.setLayoutBackground(this,parentLayout,R.drawable.login_background);
        editText_userID.setText(userID);
        editText_password.setText(userPassword);
    }

    public static void startLoginActivity(final Context context){
        LayoutBackgroundUtil.preloadBackgroundResource(context, R.drawable.login_background, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                context.startActivity(new Intent(context,LoginActivity.class));
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                context.startActivity(new Intent(context,LoginActivity.class));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_loginButton:
                login();
                break;
            case R.id.activity_login_registerUser:
                RegisterUserActivity.startLoginActivity(this);
                break;
            case R.id.activity_login_forgetPassword:
                ForgetPasswordActivity.startLoginActivity(this);
                break;
        }
    }

    private void login(){
        userID = editText_userID.getText().toString();
        userPassword = editText_password.getText().toString();
        if(userID.equals("") || userID == null){
            MyToastUtil.showToast("账号不能为空");
            return;
        }
        if (userPassword.equals("") || userPassword == null){
            MyToastUtil.showToast("密码不能为空");
            return;
        }
        if (userPassword.length() < 7){
            MyToastUtil.showToast("密码不能少于8位");
            return;
        }
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("登录中...");
                        BmobManageUser bmobManageUser = new BmobManageUser(LoginActivity.this);
                        bmobManageUser.loginAndSyncUser(userID,userPassword,LoginActivity.this);
                    }
                });
    }

    @Override
    protected void slideBackDo() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseActivityManager.destroyedListActivity();
    }

    @Override
    public void loginSuccess(MyUserBean myUserBean) {
        mPreferences.saveMessage(MySharedPreferences.KEY_USER_ID,userID);
        mPreferences.saveMessage(MySharedPreferences.KEY_USER_PASSWORD,userPassword);
        mPreferences.saveMessage(MySharedPreferences.KEY_USER_LOGIN_STATUS,true);
        DialogLoading.dismissLoadingDialog(handler,dialog,"登录成功", DialogLoading.MSG_SUCCESS);
        ControlUserFragment.syncUserFragment();
        finish();
    }

    @Override
    public void loginFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

}
