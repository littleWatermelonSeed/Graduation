package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity;

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
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.MessageFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.UserFragment;

public class CenterActivity extends BaseStatusActivity implements TabHost.OnTabChangeListener{

    private FragmentTabHost mTabHost;

    private Class fragmentArr[] = {HomePageFragment.class, ForumFragment.class, MessageFragment.class, UserFragment.class};
    private String txtArr[] = {"首页","同学圈","消息","我的"};
    private int imageArr[] = {R.drawable.tab_home_page_selector,R.drawable.tab_forum_selector,
                              R.drawable.tab_message_selector,R.drawable.tab_user_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_center);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.activity_center_body);
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
        return view;
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {

    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId){
            case "首页":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "同学圈":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.white1);
                break;
            case "消息":
                tintManager.setStatusBarAlpha(1f);
                tintManager.setStatusBarTintResource(R.color.home_page_top_bar);
                break;
            case "我的":
                tintManager.setStatusBarAlpha(0);
                break;
        }
    }

}
