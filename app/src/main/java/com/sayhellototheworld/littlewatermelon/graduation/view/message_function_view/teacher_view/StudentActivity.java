package com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.teacher_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.StudentAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.StudentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageStudent;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class StudentActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,OnLoadMoreListener,
        OnRefreshListener,BmobQueryDone<StudentBean> {

    private TextView txt_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private boolean loading = false;
    private int nowSkip = 0;
    private List<StudentBean> studentData;
    private StudentAdapter adapter;
    private BmobManageStudent manager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_student);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_student_back);
        txt_back.setOnClickListener(this);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_student_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.activity_student_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initParam() {
        manager = BmobManageStudent.getManager();
        studentData = new ArrayList<>();
        adapter = new StudentAdapter(this,studentData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_student_back:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        manager.queryByteacher(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        studentData.clear();
        manager.queryByteacher(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    @Override
    public void querySuccess(List<StudentBean> data) {

        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            studentData.addAll(data);
        }

        adapter.notifyDataSetChanged();
        nowSkip++;
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
        finishSmart(false);
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    public static void go2Activity(Context context) {
        Intent intent = new Intent(context, StudentActivity.class);
        context.startActivity(intent);
    }

}
