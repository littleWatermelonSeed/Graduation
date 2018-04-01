package com.sayhellototheworld.littlewatermelon.graduation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;

public class EnterActivity extends BaseStatusActivity implements View.OnClickListener{

    private Button btn_enter;
    private Button btn_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        initView();
    }

    private void initView(){
        btn_enter = (Button) findViewById(R.id.activity_enter_enter);
        btn_enter.setOnClickListener(this);
        btn_test = (Button) findViewById(R.id.activity_enter_test);
        btn_test.setOnClickListener(this);
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
