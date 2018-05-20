package com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function.ControlBindTeacherMsg;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class BindTeacherMsgActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{
    
    public final static int BIND_TEACHER_TYPE_STUDENT = 0;
    public final static int BIND_TEACHER_TYPE_TEACHER = 1;

    private TextView txt_back;
    private TextView txt_title;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    
    private int type;
    private ControlBindTeacherMsg cbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_teacher_msg);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_bind_teacher_msg_back);
        txt_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_bind_teacher_msg_title);
        img_more = (ImageView) findViewById(R.id.activity_bind_teacher_msg_more);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_bind_teacher_msg_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        recyclerView = (RecyclerView) findViewById(R.id.activity_bind_teacher_msg_recycler_view);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type",-1);
        cbt = new ControlBindTeacherMsg(this,type,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        showTitle();
        refreshLayout.autoRefresh();
    }

    private void showTitle(){
        switch (type){
            case BIND_TEACHER_TYPE_STUDENT:
                txt_title.setText("我的教师绑定消息");
                break;
            case BIND_TEACHER_TYPE_TEACHER:
                txt_title.setText("学生的绑定申请消息");
                break;
        }
    }

    public static void go2Activity(Context context, int type) {
        Intent intent = new Intent(context, BindTeacherMsgActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_bind_teacher_msg_back:
                finish();
                break;
        }
    }
    
}
