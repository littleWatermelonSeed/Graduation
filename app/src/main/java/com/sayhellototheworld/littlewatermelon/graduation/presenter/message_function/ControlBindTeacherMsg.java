package com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.BindTeacherAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loading = false;
        nowSkip = 0;
        messageData.clear();
    }

    private void finishSmart(boolean success) {
        if (!loading) {
            refreshLayout.finishRefresh(success);
        } else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
