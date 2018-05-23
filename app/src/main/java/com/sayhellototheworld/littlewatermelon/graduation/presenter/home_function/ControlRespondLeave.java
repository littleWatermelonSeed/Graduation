package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.RespondLeaveAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestLeaveBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestLeave;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/21.
 */

public class ControlRespondLeave implements OnLoadMoreListener,OnRefreshListener,BmobQueryDone<RequestLeaveBean> {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private BmobManageRequestLeave manager;
    private RespondLeaveAdapter adapter;
    private List<RequestLeaveBean> requestData;

    private boolean loading = false;
    private int nowSkip = 0;

    public ControlRespondLeave(Context context, SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
        this.context = context;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);

        manager = BmobManageRequestLeave.getManager();
        requestData = new ArrayList<>();
        adapter = new RespondLeaveAdapter(context,requestData);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void querySuccess(List<RequestLeaveBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            requestData.addAll(data);
            nowSkip++;
            adapter.notifyDataSetChanged();
        }
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(context,e);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        manager.queryByTeacher(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        requestData.clear();
        manager.queryByTeacher(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
