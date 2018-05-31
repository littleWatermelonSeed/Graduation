package com.sayhellototheworld.littlewatermelon.graduation.view.im_view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.FriendAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFriend;
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

public class FriendFragment extends Fragment implements OnLoadMoreListener,OnRefreshListener, BmobQueryDone<FriendBean> {

    private View mView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private boolean loading = false;
    private int nowSkip = 0;
    private List<FriendBean> friendData;
    private BmobManageFriend manager;
    private FriendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_friend, container, false);
            init();
        }
        return mView;
    }

    private void init(){
        initWidget();
        initParam();
        initShow();
    }

    private void initWidget(){
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.fragment_friend_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_friend_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }

    private void initParam(){
        friendData = new ArrayList<>();
        manager = BmobManageFriend.getManager();
        adapter = new FriendAdapter(getContext(),friendData);
        recyclerView.setAdapter(adapter);
    }

    private void initShow(){
        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        manager.queryByUser(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        friendData.clear();
        manager.queryByUser(BmobManageUser.getCurrentUser(),nowSkip,this);
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    @Override
    public void querySuccess(List<FriendBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多好友啦~");
        }else {
            friendData.addAll(data);
        }
        adapter.notifyDataSetChanged();
        Log.i("niyuanjie","查询成功，数据数为：" + data.size());
        finishSmart(true);
        nowSkip++;
    }

    @Override
    public void queryFailed(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(getContext(),e);
    }
}
