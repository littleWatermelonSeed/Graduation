package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

public class RequestLeaveActivity extends BaseSlideBcakStatusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_leave);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {

    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,RequestLeaveActivity.class));
    }

}
