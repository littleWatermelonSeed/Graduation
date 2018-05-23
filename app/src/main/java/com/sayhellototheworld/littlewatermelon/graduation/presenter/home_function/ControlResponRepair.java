package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ResponRepairsAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestRepairBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManagerRepairs;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.respond_repairs.RespondRepairActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/23.
 */

public class ControlResponRepair implements OnLoadMoreListener,OnRefreshListener,BmobQueryDone<RequestRepairBean> {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int type;

    private BmobManagerRepairs manager;
    private ResponRepairsAdapter adapter;
    private List<RequestRepairBean> repairsData;

    private boolean loading = false;
    private int nowSkip = 0;

    public ControlResponRepair(Context context, SmartRefreshLayout refreshLayout, RecyclerView recyclerView, int type) {
        this.context = context;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        this.type = type;

        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);

        manager = BmobManagerRepairs.getManager();
        repairsData = new ArrayList<>();
        adapter = new ResponRepairsAdapter(context,repairsData,type);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void querySuccess(List<RequestRepairBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            repairsData.addAll(data);
        }
        adapter.notifyDataSetChanged();
        finishSmart(true);
        nowSkip++;
    }

    @Override
    public void queryFailed(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(context,e);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        if (type == RespondRepairActivity.REPAIRS_TYPE_ALL){
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),nowSkip,this);
        }else if (type == RespondRepairActivity.REPAIRS_TYPE_OWN){
            manager.queryByRepairsMan(nowSkip,this);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        repairsData.clear();
        if (type == RespondRepairActivity.REPAIRS_TYPE_ALL){
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),nowSkip,this);
        }else if (type == RespondRepairActivity.REPAIRS_TYPE_OWN){
            manager.queryByRepairsMan(nowSkip,this);
        }
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
