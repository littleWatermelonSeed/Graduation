package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener {

    private FrameLayout fl_body;
    private ImageView img_back;
    private TextView txt_title;
    private ImageView img_more;
    private CircleImageView head;
    private TextView txt_user_name;
    private ImageView img_user_type;
    private ImageView img_user_sex;
    private TextView txt_introduction;
    private ImageView img_forum;
    private ImageView img_lost;
    private ImageView img_flea;
    private ImageView img_share;
    private TextView txt_school_name;
    private TextView txt_real_name;
    private TextView txt_nick_name;
    private TextView txt_birthday;
    private TextView txt_local;
    private TextView txt_home;
    private TextView txt_email;
    private Button btn_operation_friend;
    private Button btn_chat;
    private LinearLayout ll_bottom_body;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        fl_body = (FrameLayout) findViewById(R.id.activity_user_details_body);
        img_back = (ImageView) findViewById(R.id.activity_user_details_back);
        img_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_user_details_title);
        img_more = (ImageView) findViewById(R.id.activity_user_details_more);
        img_more.setOnClickListener(this);
        head = (CircleImageView) findViewById(R.id.activity_user_details_head_portrait);
        txt_user_name = (TextView) findViewById(R.id.activity_user_details_user_name);
        img_user_type = (ImageView) findViewById(R.id.activity_user_details_user_type_icon);
        img_user_sex = (ImageView) findViewById(R.id.activity_user_details_sex_icon);
        txt_introduction = (TextView) findViewById(R.id.activity_user_details_introduction_content);
        img_forum = (ImageView) findViewById(R.id.activity_user_details_forum_icon);
        img_forum.setOnClickListener(this);
        img_lost = (ImageView) findViewById(R.id.activity_user_details_lost_and_find_icon);
        img_lost.setOnClickListener(this);
        img_flea = (ImageView) findViewById(R.id.activity_user_details_flea_icon);
        img_flea.setOnClickListener(this);
        img_share = (ImageView) findViewById(R.id.activity_user_details_share_icon);
        img_share.setOnClickListener(this);
        txt_school_name = (TextView) findViewById(R.id.activity_user_details_school_name);
        txt_real_name = (TextView) findViewById(R.id.activity_user_details_real_name);
        txt_nick_name = (TextView) findViewById(R.id.activity_user_details_nickname);
        txt_birthday = (TextView) findViewById(R.id.activity_user_details_birthday);
        txt_local = (TextView) findViewById(R.id.activity_user_details_location);
        txt_home = (TextView) findViewById(R.id.activity_user_details_home);
        txt_email = (TextView) findViewById(R.id.activity_user_details_email);
        btn_operation_friend = (Button) findViewById(R.id.activity_user_details_operation_friend);
        btn_operation_friend.setOnClickListener(this);
        btn_chat = (Button) findViewById(R.id.activity_user_details_chat);
        btn_chat.setOnClickListener(this);
        ll_bottom_body = (LinearLayout) findViewById(R.id.activity_user_details_bottom_body);
    }

    @Override
    protected void initParam() {
        userID = getIntent().getStringExtra("userID");
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarAlpha(0);
    }

    public static void go2Activity(Context context, String userID) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_user_details_back:
                finish();
                break;
            case R.id.activity_user_details_more:
                break;
            case R.id.activity_user_details_forum_icon:
                break;
            case R.id.activity_user_details_lost_and_find_icon:
                break;
            case R.id.activity_user_details_flea_icon:
                break;
            case R.id.activity_user_details_share_icon:
                break;
            case R.id.activity_user_details_operation_friend:
                break;
            case R.id.activity_user_details_chat:
                break;
        }
    }
}
