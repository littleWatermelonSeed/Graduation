package com.sayhellototheworld.littlewatermelon.graduation.view.friend_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.friend.ControlFriendRequest;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class FriendRequestMsgActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    public final static int FRIEND_REQUEST_TYPE_OWN = 0;
    public final static int FRIEND_REQUEST_TYPE_OTHER = 1;

    private TextView txt_back;
    private TextView txt_title;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private int type;
    private ControlFriendRequest cfr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend_request_msg);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_friend_request_msg_back);
        txt_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_friend_request_msg_title);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_friend_request_msg_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);

        recyclerView = (RecyclerView) findViewById(R.id.activity_friend_request_msg_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type",-1);
        cfr = new ControlFriendRequest(this,refreshLayout,recyclerView,type);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        if (type == FRIEND_REQUEST_TYPE_OTHER){
            txt_title.setText("新朋友请求");
        }else if (type == FRIEND_REQUEST_TYPE_OWN){
            txt_title.setText("我的好友申请");
        }
        refreshLayout.autoRefresh();
    }

    public static void go2Activity(Context context,int type) {
        Intent intent = new Intent(context, FriendRequestMsgActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_friend_request_msg_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cfr.updateRead();
    }
}
