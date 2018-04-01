package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage.ControlForgetPasswordNext;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class ForgetPasswordNextActivity extends BaseSlideBcakStatusActivity implements BaseActivityDo,View.OnClickListener{

    private LinearLayout parentLayout;
    private ImageView imageView_back;
    private ImageView imageView_cancle;
    private EditText editText_keyCode;
    private EditText editText_passwordOne;
    private EditText editText_passwordTwo;
    private Button button_reset;
    private Button button_getSmsCode;

    private String phoneNum;
    private String smsCode;
    private String psOne;
    private String psTwo;

    private ControlForgetPasswordNext cfpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_next);
        init();
    }


    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        parentLayout = (LinearLayout)findViewById(R.id.activity_forget_password_next_parent);
        imageView_back = (ImageView)findViewById(R.id.activity_forget_password_next_imageViewBack);
        imageView_back.setOnClickListener(this);
        imageView_cancle = (ImageView)findViewById(R.id.activity_forget_password_next_imageViewCancle);
        imageView_cancle.setOnClickListener(this);
        editText_keyCode = (EditText)findViewById(R.id.activity_forget_password_next_editTextKeyCode);
        editText_passwordOne = (EditText)findViewById(R.id.activity_forget_password_next_editTextPasswordOne);
        editText_passwordTwo = (EditText)findViewById(R.id.activity_forget_password_next_editTextPasswordTwo);
        button_reset = (Button)findViewById(R.id.activity_forget_password_next_buttonReset);
        button_reset.setOnClickListener(this);
        button_getSmsCode = (Button)findViewById(R.id.activity_forget_password_next_buttonGetKeyCode);
        button_getSmsCode.setOnClickListener(this);
    }

    @Override
    public void initParam() {
        if(tintManager != null){
            tintManager.setStatusBarAlpha(0);
        }
        baseActivityManager.addActivityToList(this);
        phoneNum = getIntent().getStringExtra("phoneNum");
        cfpn = new ControlForgetPasswordNext(this,button_getSmsCode,phoneNum);
    }

    @Override
    public void initShow() {
        StatusBarUtils.setLayoutMargin(this,parentLayout);
        LayoutBackgroundUtil.setLayoutBackground(this,parentLayout,R.drawable.register_background1);
    }

    public static void startLoginActivity(final Context context, final String phoneNum){
        LayoutBackgroundUtil.preloadBackgroundResource(context, R.drawable.register_background1, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                Intent intent = new Intent(context,ForgetPasswordNextActivity.class);
                intent.putExtra("phoneNum",phoneNum);
                ((Activity)context).startActivity(intent);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                Intent intent = new Intent(context,ForgetPasswordNextActivity.class);
                intent.putExtra("phoneNum",phoneNum);
                ((Activity)context).startActivity(intent);
                return false;
            }
        });
    }

    private boolean getSMessage(){
        smsCode = editText_keyCode.getText().toString();
        if(smsCode.equals("") || smsCode == null){
            MyToastUtil.showToast("验证码不能为空");
            return false;
        }

        psOne = editText_passwordOne.getText().toString();
        psTwo = editText_passwordTwo.getText().toString();
        if (psTwo.equals("") || psTwo.equals("")){
            MyToastUtil.showToast("密码不能为空");
            return false;
        }
        if (psTwo.length() < 7 || psTwo.length() < 7){
            MyToastUtil.showToast("密码长度不能小于7位字符");
            return false;
        }
        if (!psTwo.equals(psOne)){
            MyToastUtil.showToast("两个密码不相同");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forget_password_next_imageViewBack:
                finish();
                break;
            case R.id.activity_forget_password_next_imageViewCancle:
                baseActivityManager.destroyedListActivity();
                break;
            case R.id.activity_forget_password_next_buttonGetKeyCode:
                cfpn.sentSmsCode(phoneNum);
                break;
            case R.id.activity_forget_password_next_buttonReset:
                if (!getSMessage()){
                    return;
                }
                cfpn.resetPassWord(smsCode,psOne);
                break;
        }
    }

}
