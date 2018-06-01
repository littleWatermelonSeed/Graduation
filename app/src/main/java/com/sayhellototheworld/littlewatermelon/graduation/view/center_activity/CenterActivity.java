package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.ForumFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.HomePageFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.IMFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.MessageFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.UserFragment;

public class CenterActivity extends BaseStatusActivity implements TabHost.OnTabChangeListener {

    private FragmentTabHost mTabHost;
    private static int msgNoReadNum;
    private static int chatNoReadNum;
    private static TextView txt_msg;
    private static TextView txt_chat;

    private Class fragmentArr[] = {HomePageFragment.class, ForumFragment.class, MessageFragment.class, IMFragment.class, UserFragment.class};
    private String txtArr[] = {"首页", "同学圈", "消息", "聊天", "我的"};
    private int imageArr[] = {R.drawable.tab_home_page_selector, R.drawable.tab_forum_selector,
            R.drawable.tab_message_selector, R.drawable.tab_talk_selector, R.drawable.tab_user_selector};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_center);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.activity_center_body);
    }

    @Override
    protected void initParam() {
        msgNoReadNum = getIntent().getIntExtra("msgNoReadNum", 0);
        chatNoReadNum = getIntent().getIntExtra("chatNoReadNum", 0);
    }

    @Override
    protected void initShow() {
        int count = fragmentArr.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(txtArr[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArr[i], null);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.getTabWidget().setBackgroundResource(R.color.white1);
    }

    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_item_view_image_view);
        imageView.setImageResource(imageArr[index]);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_view_txt);
        textView.setText(txtArr[index]);

        if (index == 2) {
            TextView txt_no_read_num = (TextView) view.findViewById(R.id.tab_item_view_no_read_num);
            txt_msg = txt_no_read_num;
            if (msgNoReadNum <= 0){
                txt_no_read_num.setVisibility(View.GONE);
            }else {
                txt_no_read_num.setVisibility(View.VISIBLE);
                txt_no_read_num.setText(msgNoReadNum + "");
            }
        } else if (index == 3) {
            TextView txt_no_read_num = (TextView) view.findViewById(R.id.tab_item_view_no_read_num);
            txt_chat = txt_no_read_num;
            if (chatNoReadNum <= 0){
                txt_no_read_num.setVisibility(View.GONE);
            }else {
                txt_no_read_num.setVisibility(View.VISIBLE);
                txt_no_read_num.setText(chatNoReadNum + "");
            }
        }
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId) {
            case "首页":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "同学圈":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "消息":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "聊天":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "我的":
                tintManager.setStatusBarAlpha(0);
                break;
        }
    }

    public static void go2Activity(Context context, int msgNoReadNum, int chatNoReadNum) {
        Intent intent = new Intent(context, CenterActivity.class);
        intent.putExtra("msgNoReadNum", msgNoReadNum);
        intent.putExtra("chatNoReadNum", chatNoReadNum);
        context.startActivity(intent);
    }

    public static void setReduceMsgNoRead(int n){
        if (txt_msg == null)
            return;

        msgNoReadNum = msgNoReadNum - n;

        if (msgNoReadNum > 0){
            txt_msg.setText(msgNoReadNum + "");
        }else{
            txt_msg.setVisibility(View.GONE);
        }
    }

    public static void setAddMsgNoRead(int n){
        if (txt_msg == null)
            return;

        msgNoReadNum = msgNoReadNum - n;

        if (msgNoReadNum > 0){
            txt_msg.setText(msgNoReadNum + "");
        }else{
            txt_msg.setVisibility(View.GONE);
        }
    }

    public static void setAddChatNoRead(int n){
        if (txt_chat == null)
            return;

        chatNoReadNum = chatNoReadNum - n;

        if (chatNoReadNum > 0){
            txt_chat.setText(chatNoReadNum + "");
        }else{
            txt_chat.setVisibility(View.GONE);
        }
    }

    public static void setReduceChatNoRead(int n){
        synchronized (CenterActivity.class){
            if (txt_chat == null)
                return;

            chatNoReadNum = chatNoReadNum + n;

            if (chatNoReadNum > 0){
                txt_chat.setText(chatNoReadNum + "");
            }else{
                txt_chat.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 每次onRestart后都要查询IM消息
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgNoReadNum = 0;
        chatNoReadNum = 0;
        txt_chat = null;
        txt_msg = null;
    }

}
