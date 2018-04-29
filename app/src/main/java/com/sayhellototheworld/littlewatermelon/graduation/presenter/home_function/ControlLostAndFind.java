package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.LostAndFindAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostAndFind;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 123 on 2018/4/24.
 */

public class ControlLostAndFind extends FindListener<LostAndFindBean> implements OnLoadMoreListener, OnRefreshListener {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;

    private BmobManageLostAndFind manager;
    private LostAndFindAdapter adapter;
    private List<LostAndFindBean> lostData;

    private boolean loading = false;
    private int nowSkip = 0;
    private boolean privateLost;

    public ControlLostAndFind(Context context, SmartRefreshLayout refreshLayout, RecyclerView mRecyclerView,boolean privateLost){
        this.context = context;
        this.privateLost = privateLost;
        this.refreshLayout = refreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        manager = BmobManageLostAndFind.getManager();
        lostData = new ArrayList<>();
        adapter = new LostAndFindAdapter(context,lostData,privateLost);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.mRecyclerView.setLayoutManager(llManager);
        this.mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void done(List<LostAndFindBean> list, BmobException e) {
        if (e == null){
            if (!loading){
                lostData.clear();
                lostData.addAll(list);
            }else {
                lostData.addAll(list);
            }

            if (list.size() == 0){
                MyToastUtil.showToast("到底啦,没有更多数据啦~");
            }

            adapter.notifyDataSetChanged();
            nowSkip++;
            Log.i("niyuanjie","失物招领查询成功 一共" + list.size() + "条数据");
            finishSmart(true);
        }else {
            finishSmart(false);
            BmobExceptionUtil.dealWithException(context,e);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        if (privateLost){
            manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
        }else {
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
        }

        loading = false;
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (privateLost){
            manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
        }else {
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
        }
        loading = true;
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    public void notifData(){
        adapter.notifyDataSetChanged();
    }

}
