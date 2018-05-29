package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.LostAndFindAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostAndFind;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.lost_and_find.LostAndFindActivity;
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
    private int type;

    private String otherID;
    private MyUserBean other;
    private boolean getOther = false;

    public ControlLostAndFind(Context context, SmartRefreshLayout refreshLayout, RecyclerView mRecyclerView,int type){
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        manager = BmobManageLostAndFind.getManager();
        lostData = new ArrayList<>();
        adapter = new LostAndFindAdapter(context,lostData,this.type);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.mRecyclerView.setLayoutManager(llManager);
        this.mRecyclerView.setAdapter(adapter);
    }

    public ControlLostAndFind(Context context, SmartRefreshLayout refreshLayout, RecyclerView mRecyclerView,int type,String otherID){
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        this.otherID = otherID;
        manager = BmobManageLostAndFind.getManager();
        lostData = new ArrayList<>();
        adapter = new LostAndFindAdapter(context,lostData,this.type);
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
        if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_OWN){
            manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
        }else if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_PUBLIC){
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
        }else if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_OTHER){
            doOther();
        }
        loading = false;
    }

    private void doOther() {
        if (getOther){
            manager.queryByUser(other,ControlLostAndFind.this,nowSkip);
        }else {
            BmobManageUser manageUser = new BmobManageUser(context);
            manageUser.queryByID(otherID, new BmobQueryDone<MyUserBean>() {
                @Override
                public void querySuccess(List<MyUserBean> data) {
                    getOther = true;
                    other = data.get(0);
                    manager.queryByUser(data.get(0),ControlLostAndFind.this,nowSkip);
                }

                @Override
                public void queryFailed(BmobException e) {
                    getOther = false;
                    finishSmart(false);
                    BmobExceptionUtil.dealWithException(context,e);
                }
            });
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_OWN){
            manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
        }else if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_PUBLIC){
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
        }else if (type == LostAndFindActivity.LOST_AND_FIND_TYPE_OTHER){
            doOther();
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
