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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ViRegisterUserCoDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage.ControlRegisterUser;
import com.sayhellototheworld.littlewatermelon.graduation.util.CheckFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class RegisterUserActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener {

    private LinearLayout parentLayout;
    private ImageView imageView_back;
    private ImageView imageView_cancle;
    private EditText editText_phoneNum;
    private EditText editText_keyCode;
    private EditText editText_passwordOne;
    private EditText editText_passwordTwo;
    private Button button_getKeyCode;
    private Button button_registerSubmit;

    private String phoneNum;
    private String keyCode;
    private String passwordOne;
    private String passwordTwo;
    private MyUserBean userBean;
    private ViRegisterUserCoDo rud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_user);
        super.onCreate(savedInstanceState);
//        init();
    }

//    public void init() {
//        initWidget();
//        initParam();
//        initShow();
//    }

    @Override
    protected void initWidget() {
        parentLayout = (LinearLayout) findViewById(R.id.activity_register_user_parent);
        imageView_back = (ImageView) findViewById(R.id.activity_register_user_imageviewBack);
        imageView_back.setOnClickListener(this);
        imageView_cancle = (ImageView) findViewById(R.id.activity_register_user_imageviewCancle);
        imageView_cancle.setOnClickListener(this);
        editText_phoneNum = (EditText) findViewById(R.id.activity_register_user_editTextPhoneNumber);
        editText_keyCode = (EditText) findViewById(R.id.activity_register_user_editTextKeyCode);
        editText_passwordOne = (EditText) findViewById(R.id.activity_register_user_editTextPasswordOne);
        editText_passwordTwo = (EditText) findViewById(R.id.activity_register_user_editTextPasswordTwo);
        button_getKeyCode = (Button) findViewById(R.id.activity_register_user_buttonGetKeyCode);
        button_getKeyCode.setOnClickListener(this);
        button_registerSubmit = (Button) findViewById(R.id.activity_register_user_buttonRegister);
        button_registerSubmit.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        if (tintManager != null) {
            tintManager.setStatusBarAlpha(0);
        }
        rud = new ControlRegisterUser(this,button_getKeyCode);
        baseActivityManager.addActivityToList(this);
    }

    @Override
    protected void initShow() {
        StatusBarUtils.setLayoutMargin(this, parentLayout);
        LayoutBackgroundUtil.setLayoutBackground(this, parentLayout, R.drawable.register_background1);
    }

    public static void startLoginActivity(final Context context) {
        LayoutBackgroundUtil.preloadBackgroundResource(context, R.drawable.register_background1, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                ((Activity) context).startActivity(new Intent(context, RegisterUserActivity.class));
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                ((Activity) context).startActivity(new Intent(context, RegisterUserActivity.class));
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_register_user_imageviewBack:
                finish();
                break;
            case R.id.activity_register_user_imageviewCancle:
                baseActivityManager.destroyedListActivity();
                break;
            case R.id.activity_register_user_buttonGetKeyCode:
                if(!verificationPhoneNum()){
                    return;
                }
                rud.getKeyCode(phoneNum);
                break;
            case R.id.activity_register_user_buttonRegister:
                if(!getUserBean()){
                    return;
                }
                rud.registerSubmit(userBean,keyCode,passwordOne);
                break;
        }
    }

    private boolean verificationPhoneNum(){
        getValues();
        if(phoneNum.length() != 11){
            MyToastUtil.showToast("手机号码长度有误");
            return false;
        }
        if(!CheckFormatUtil.isMobileNO(phoneNum)){
            MyToastUtil.showToast("手机号格式不正确");
            return false;
        }
        return true;
    }

    private boolean getUserBean(){
        getValues();
        if(phoneNum.length() == 0){
            MyToastUtil.showToast("手机号不能为空");
            return false;
        }
        if(phoneNum.length() != 11){
            MyToastUtil.showToast("手机号码长度有误");
            return false;
        }
        if(!CheckFormatUtil.isMobileNO(phoneNum)){
            MyToastUtil.showToast("手机号格式不正确");
            return false;
        }
        if(keyCode.length() == 0){
            MyToastUtil.showToast("验证码不能为空");
            return false;
        }
        if(passwordOne.length() == 0 || passwordTwo.length() == 0){
            MyToastUtil.showToast("密码不能为空");
            return false;
        }
        if(!passwordOne.equals(passwordTwo)){
            MyToastUtil.showToast("两次密码不相同,请重新输入");
            return false;
        }
        if(passwordOne.length() < 8){
            MyToastUtil.showToast("密码太简单,必须大于7个字符");
            return false;
        }
        userBean = new MyUserBean();
        userBean.setUsername(phoneNum);
        userBean.setMobilePhoneNumber(phoneNum);
        userBean.setNickName(phoneNum);
        userBean.setSex("男");
        userBean.setRole("s");
        userBean.setPassword(passwordOne);
        return true;
    }

    private void getValues(){
        phoneNum = editText_phoneNum.getText().toString();
        keyCode = editText_keyCode.getText().toString();
        passwordOne = editText_passwordOne.getText().toString();
        passwordTwo = editText_passwordTwo.getText().toString();
    }

}
