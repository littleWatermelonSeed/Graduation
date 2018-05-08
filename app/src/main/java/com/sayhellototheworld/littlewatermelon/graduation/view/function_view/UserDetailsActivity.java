package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class UserDetailsActivity extends BaseSlideBcakStatusActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initParam() {
        userID = getIntent().getStringExtra("userID");
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    public static void go2Activity(Context context,String userID){
        Intent intent = new Intent(context,UserDetailsActivity.class);
        intent.putExtra("userID",userID);
        context.startActivity(intent);
    }

}
