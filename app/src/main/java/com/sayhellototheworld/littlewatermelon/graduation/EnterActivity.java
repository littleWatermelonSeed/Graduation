package com.sayhellototheworld.littlewatermelon.graduation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForumComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserLoginDo;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;

public class EnterActivity extends BaseStatusActivity implements View.OnClickListener,UserLoginDo{

    private Button btn_enter;
    private Button btn_test;

    private boolean loginStatue;
    private MySharedPreferences mPreferences;
    private TimerTask task;
    private BmobManageUser manageUser;
    private int nowNum = 0;
    private int num;
    private int totalMsgNoReadNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_enter);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        btn_enter = (Button) findViewById(R.id.activity_enter_enter);
        btn_enter.setOnClickListener(this);
        btn_test = (Button) findViewById(R.id.activity_enter_test);
        btn_test.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        mPreferences = MySharedPreferences.getInstance();
        loginStatue = mPreferences.getBooleanMessage(MySharedPreferences.KEY_USER_LOGIN_STATUS);
        manageUser = new BmobManageUser(this);
    }

    @Override
    protected void initShow() {
        if (!loginStatue){
            goToLogin();
        }else {
            String userID = mPreferences.getStringMessage(MySharedPreferences.KEY_USER_ID);
            String userPassword = mPreferences.getStringMessage(MySharedPreferences.KEY_USER_PASSWORD);
            manageUser.loginAndSyncUser(userID,userPassword,this);
        }
    }

    private void goToLogin() {
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.startLoginActivity(EnterActivity.this);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }

    @Override
    public void loginSuccess(MyUserBean myUserBean) {
        beginNoReadNum(myUserBean.getRole(),myUserBean);
    }

    @Override
    public void loginFail(BmobException ex) {
        LoginActivity.startLoginActivity(EnterActivity.this);
        finish();
    }

    private void beginNoReadNum(String userType,MyUserBean user){
        if (userType.equalsIgnoreCase("s")){
            num = 7;
            getStudentNoReadNum(user);
        }else if (userType.equalsIgnoreCase("t")){
            num = 7;
            getTeacherNoReadNum(user);
        }else if (userType.equalsIgnoreCase("r")){
            num = 6;
            getRepairNoReadNum(user);
        }
        
    }

    private void getStudentNoReadNum(MyUserBean user){
        commonNoReadNum(user);
        getSBindNoReadNum(user);
    }

    private void getTeacherNoReadNum(MyUserBean user){
        commonNoReadNum(user);
        getTBindNoReadNum(user);
    }

    private void getRepairNoReadNum(MyUserBean user){
        commonNoReadNum(user);
    }

    private void commonNoReadNum(MyUserBean user){
        getLostNoReadNum(user);
        getFleaNoReadNum(user);
        getShareNoReadNum(user);
        getFourmNoReadNum();
        getFriendRequestNoReadNum(user);
        getMyFriendRequestNoReadNum(user);
    }

    private void getLostNoReadNum(MyUserBean user){
        BmobManageLostComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","失物招领消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","失物招领未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getFleaNoReadNum(MyUserBean user){
        BmobManageFleaComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","跳蚤市场消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","跳蚤市场未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getShareNoReadNum(MyUserBean user){
        BmobManageResourceComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","资源共享消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","资源共享未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getFourmNoReadNum(){
        BmobManageForumComment.getManager().queryNoReadCount(new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","同学圈消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","同学圈未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getFriendRequestNoReadNum(MyUserBean user){
        BmobManageRequestFriend.getManager().queryFriendNoReadCount(user,new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","加好友申请消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","加好友申请未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getMyFriendRequestNoReadNum(MyUserBean user){
        BmobManageRequestFriend.getManager().queryUserNoReadCount(user,new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","我加好友消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","我加好友申请未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getSBindNoReadNum(MyUserBean user){
        BmobManageTeacher.getManager().querySNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","学生绑定消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","学生绑定未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void getTBindNoReadNum(MyUserBean user){
        BmobManageTeacher.getManager().queryTNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalMsgNoReadNum = totalMsgNoReadNum + integer;
                addNum();
                Log.i("niyuanjie","老师绑定消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                Log.i("niyuanjie","老师绑定未读查询出错");
                Log.i("niyuanjie","错误信息" + e.getMessage());
            }
        });
    }

    private void addNum(){
        synchronized (this){
            nowNum++;
            Log.i("niyuanjie","当前已查询未读的数量 = " + nowNum);
            if (nowNum == num){
                CenterActivity.go2Activity(this,totalMsgNoReadNum);
                finish();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getTintManager().setStatusBarAlpha(0);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_enter_enter:
                startActivity(new Intent(EnterActivity.this, CenterActivity.class));
                break;
            case R.id.activity_enter_test:
                startActivity(new Intent(EnterActivity.this, SDriverActivity.class));
                break;
        }
    }

}
