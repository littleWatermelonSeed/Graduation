package com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.MessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function.ControlMessageDetails;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class CommonMessageActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private TextView txt_title;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private int type;
    private ControlMessageDetails cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_common_message);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_common_message_back);
        txt_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_common_message_title);
        img_more = (ImageView) findViewById(R.id.activity_common_message_more);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_common_message_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        recyclerView = (RecyclerView) findViewById(R.id.activity_common_message_recycler_view);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type", -1);
        cmd = new ControlMessageDetails(this,type,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        refreshLayout.autoRefresh();
        tintManager.setStatusBarTintResource(R.color.white);
        showTitle();
    }

    private void showTitle(){
        switch (type){
            case MessageAdapter.MSG_TYPE_LOST:
                txt_title.setText("失物招领消息");
                break;
            case MessageAdapter.MSG_TYPE_FLEA:
                txt_title.setText("跳蚤市场消息");
                break;
            case MessageAdapter.MSG_TYPE_SHARE:
                txt_title.setText("资源共享消息");
                break;
            case MessageAdapter.MSG_TYPE_FORUM:
                txt_title.setText("同学圈消息");
                break;
        }
    }

    public static void go2Activity(Context context, int type) {
        Intent intent = new Intent(context, CommonMessageActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_common_message_back:
                finish();
                break;
        }
    }
}
