package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.respond_leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class RespondLeaveActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_respond_leave);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_respond_leave_back);
        txt_back.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_respond_leave_recycler_view);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_respond_leave_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,RespondLeaveActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_respond_leave_back:
                finish();
                break;
        }
    }
    
}
