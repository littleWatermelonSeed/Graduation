package com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class OwnForumActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private ControlForum cf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_own_forum);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_own_forum_back);
        txt_back.setOnClickListener(this);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_own_forum_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);

        recyclerView = (RecyclerView) findViewById(R.id.activity_own_forum_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initParam() {
        cf = new ControlForum(this,ControlForum.FORUM_TYPE_OWN,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,OwnForumActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_own_forum_back:
                finish();
                break;
        }
    }

}
