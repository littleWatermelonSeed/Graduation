package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.LiTopBar;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class UserSettingActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener {

    private LiTopBar mLiTopBar;
    private Button button_login;
    private RelativeLayout ll_change_ps;

    private boolean loginOutIng = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_setting);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initWidget() {
        button_login = (Button) findViewById(R.id.activity_user_setting_loginOutButton);
        button_login.setOnClickListener(this);
        mLiTopBar = (LiTopBar) findViewById(R.id.activity_user_setting_topbar);
        mLiTopBar.setLeftContainerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_change_ps = (RelativeLayout) findViewById(R.id.activity_user_setting_change_ps);
        ll_change_ps.setOnClickListener(this);
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {

    }

    @Override
    public void onClick(View v) {
        if (loginOutIng)
            return;
        switch (v.getId()) {
            case R.id.activity_user_setting_loginOutButton:
                loginOut();
                break;
            case R.id.activity_user_setting_change_ps:
                ForgetPasswordActivity.startLoginActivity(this);
                break;
        }
    }

    private void loginOut() {
        DialogConfirm.newInstance("提示", "确定退出登录?", new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {

            }

            @Override
            public void ok() {
                loginOutIng = true;
                BmobManageUser.loginOutUser();

//                ControlUserFragment.syncUserFragment();
//                HomePageFragment.syncHomePageFragment();
                LoginActivity.startLoginActivityWithLoginOut(UserSettingActivity.this);
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public static void startLoginActivity(final Context context) {
        context.startActivity(new Intent(context, UserSettingActivity.class));
    }

}
