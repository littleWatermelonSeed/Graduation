package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

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
import com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage.ControlForgetPassword;
import com.sayhellototheworld.littlewatermelon.graduation.util.CheckFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class ForgetPasswordActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private LinearLayout parentLayout;
    private ImageView imageView_back;
    private ImageView imageView_cancle;
    private EditText editText_phoneNum;
    private Button button_next;

    private String phoneNum;

    private ControlForgetPassword cfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_password);
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        parentLayout = (LinearLayout)findViewById(R.id.activity_forget_password_parent);
        imageView_back = (ImageView)findViewById(R.id.activity_forget_password_imageViewBack);
        imageView_back.setOnClickListener(this);
        imageView_cancle = (ImageView)findViewById(R.id.activity_forget_password_imageViewCancle);
        imageView_cancle.setOnClickListener(this);
        editText_phoneNum = (EditText)findViewById(R.id.activity_forget_password_editTextPhoneNumber);
        button_next = (Button)findViewById(R.id.activity_forget_password_buttonNext);
        button_next.setOnClickListener(this);
    }

    @Override
    public void initParam() {
        if(tintManager != null){
            tintManager.setStatusBarAlpha(0);
        }
        cfp = new ControlForgetPassword(this);
        baseActivityManager.addActivityToList(this);
    }

    @Override
    public void initShow() {
        StatusBarUtils.setLayoutMargin(this,parentLayout);
        LayoutBackgroundUtil.setLayoutBackground(this,parentLayout,R.drawable.register_background1);
    }

    public static void startLoginActivity(final Context context){
        LayoutBackgroundUtil.preloadBackgroundResource(context, R.drawable.register_background1, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                context.startActivity(new Intent(context,ForgetPasswordActivity.class));
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                context.startActivity(new Intent(context,ForgetPasswordActivity.class));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forget_password_buttonNext:
                if (!verifyPhoneNumFormat()){
                    return;
                }
                cfp.verifyUser(phoneNum);
                break;
            case R.id.activity_forget_password_imageViewCancle:
                baseActivityManager.destroyedListActivity();
                break;
            case R.id.activity_forget_password_imageViewBack:
                finish();
                break;
        }
    }

    private boolean verifyPhoneNumFormat(){
        phoneNum = editText_phoneNum.getText().toString();
        if(!CheckFormatUtil.isMobileNO(phoneNum)){
            MyToastUtil.showToast("手机号码格式不正确");
            return false;
        }
        return true;
    }

}
