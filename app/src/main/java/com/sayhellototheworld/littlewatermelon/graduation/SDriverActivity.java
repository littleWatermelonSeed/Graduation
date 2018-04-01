package com.sayhellototheworld.littlewatermelon.graduation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

public class SDriverActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        button = (Button) findViewById(R.id.activity_driver_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(SDriverActivity.this, LoginActivity.class));
    }

}
