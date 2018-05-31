package com.sayhellototheworld.littlewatermelon.graduation.view.im_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.QueryFriendActivity;

public class IMActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{
    
     private ImageView img_back;
     private TextView txt_title;
     private ImageView img_more;
     private LinearLayout ll_chat_body;
     private LinearLayout ll_friend_body;
     private ImageView img_chat_icon;
     private ImageView img_friend_icon;
     private TextView txt_chat;
     private TextView txt_friend;

     private int nowType = -1;

    private ChatFragment chatFragment;
    private FriendFragment friendFragment;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_im);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        img_back = (ImageView) findViewById(R.id.activity_im_back);
        img_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_im_title);
        img_more = (ImageView) findViewById(R.id.activity_im_more);
        img_more.setOnClickListener(this);
        ll_chat_body = (LinearLayout) findViewById(R.id.activity_im_chat_body);
        ll_chat_body.setOnClickListener(this);
        ll_friend_body = (LinearLayout) findViewById(R.id.activity_im_friend_body);
        ll_friend_body.setOnClickListener(this);
        img_chat_icon = (ImageView) findViewById(R.id.activity_im_chat_icon);
        img_friend_icon = (ImageView) findViewById(R.id.activity_im_friend_icon);
        txt_chat = (TextView) findViewById(R.id.activity_im_chat_txt);
        txt_friend = (TextView) findViewById(R.id.activity_im_friend_txt);
    }

    @Override
    protected void initParam() {
        fm = getSupportFragmentManager();
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        setFragment(R.id.activity_im_chat_body);
    }

    public static void go2Activity(Context context) {
        Intent intent = new Intent(context, IMActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_im_back:
                finish();
                break;
            case R.id.activity_im_more:
                QueryFriendActivity.go2Activity(this);
                break;
            case R.id.activity_im_chat_body:
                nowType = -1;
                img_chat_icon.setImageResource(R.drawable.chat_checked);
                img_friend_icon.setImageResource(R.drawable.friend_unchecked);
                txt_title.setText("聊天");
                txt_chat.setTextColor(getResources().getColor(R.color.statue2));
                txt_friend.setTextColor(getResources().getColor(R.color.white4));
                setFragment(R.id.activity_im_chat_body);
                break;
            case R.id.activity_im_friend_body:
                nowType = 1;
                img_chat_icon.setImageResource(R.drawable.chat_unchecked);
                img_friend_icon.setImageResource(R.drawable.friend_checked);
                txt_title.setText("我的好友");
                txt_chat.setTextColor(getResources().getColor(R.color.white4));
                txt_friend.setTextColor(getResources().getColor(R.color.statue2));
                setFragment(R.id.activity_im_friend_body);
                break;

        }
    }

    private void setFragment(int id){
        mTransaction = fm.beginTransaction();
        hideFragment();
        switch (id){
            case R.id.activity_im_chat_body:
                if(chatFragment == null){
                    chatFragment = new ChatFragment();
                    mTransaction.add(R.id.activity_im_body,chatFragment);
                }else {
                    mTransaction.show(chatFragment);
                }
                mTransaction.commit();
                break;
            case R.id.activity_im_friend_body:
                if(friendFragment == null){
                    friendFragment = new FriendFragment();
                    mTransaction.add(R.id.activity_im_body,friendFragment);
                }else {
                    mTransaction.show(friendFragment);
                }
                mTransaction.commit();
                break;
        }
    }

    private void hideFragment(){
        if(friendFragment != null){
            mTransaction.hide(friendFragment);
        }
        if(chatFragment != null){
            mTransaction.hide(chatFragment);
        }
    }

}
