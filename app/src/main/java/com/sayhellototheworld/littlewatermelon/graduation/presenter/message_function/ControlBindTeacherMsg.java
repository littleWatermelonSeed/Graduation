package com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.BindTeacherAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.BindTeacherMsgActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/16.
 */

public class ControlBindTeacherMsg implements OnRefreshListener, OnLoadMoreListener{

    private Context context;
    private int type;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private BindTeacherAdapter adapter;
    private List<TeacherBean> messageData;
    private boolean loading = false;
    private int nowSkip = 0;

    public ControlBindTeacherMsg(Context context, int type, SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        messageData = new ArrayList<>();

        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);

        adapter = new BindTeacherAdapter(context, messageData, type);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(llManager);
        this.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        queryMsg();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loading = false;
        nowSkip = 0;
        messageData.clear();
        queryMsg();
    }

    private void queryMsg(){
        switch (type){
            case BindTeacherMsgActivity.BIND_TEACHER_TYPE_STUDENT:
                queryStudent();
                break;
            case BindTeacherMsgActivity.BIND_TEACHER_TYPE_TEACHER:
                queryTeacher();
                break;
        }
    }

    private void queryStudent(){
        BmobManageTeacher.getManager().queryStudentMsg(nowSkip, new BmobQueryDone<TeacherBean>() {
            @Override
            public void querySuccess(List<TeacherBean> data) {
                if (data.size() <= 0){
                    MyToastUtil.showToast("已经到底啦~");
                }else {
                    messageData.addAll(data);
                }
                adapter.notifyDataSetChanged();
                nowSkip++;
                finishSmart(true);
                BmobManageTeacher.getManager().updateSReadBatch(data);
            }

            @Override
            public void queryFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void queryTeacher(){
        BmobManageTeacher.getManager().queryTeacherMsg(nowSkip, new BmobQueryDone<TeacherBean>() {
            @Override
            public void querySuccess(List<TeacherBean> data) {
                if (data.size() <= 0){
                    MyToastUtil.showToast("已经到底啦~");
                }else {
                    messageData.addAll(data);
                }
                adapter.notifyDataSetChanged();
                nowSkip++;
                finishSmart(true);
                BmobManageTeacher.getManager().updateTReadBatch(data);
            }

            @Override
            public void queryFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void finishSmart(boolean success) {
        if (!loading) {
            refreshLayout.finishRefresh(success);
        } else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
