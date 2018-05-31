package com.sayhellototheworld.littlewatermelon.graduation.presenter.friend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.FriendQuestAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.FriendRequestMsgActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/30.
 */

public class ControlFriendRequest implements OnLoadMoreListener,OnRefreshListener,BmobQueryDone<FriendRequestBean> {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int type;

    private List<FriendRequestBean> requestData;
    private boolean loading = false;
    private int nowSkip = 0;
    private FriendQuestAdapter adapter;

    private BmobManageRequestFriend manager;

    public ControlFriendRequest(Context context, SmartRefreshLayout refreshLayout, RecyclerView recyclerView, int type) {
        this.context = context;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        this.type = type;

        this.refreshLayout.setOnLoadMoreListener(this);
        this.refreshLayout.setOnRefreshListener(this);

        manager = BmobManageRequestFriend.getManager();
        requestData = new ArrayList<>();

        adapter = new FriendQuestAdapter(context,requestData,this.type);
        this.recyclerView.setAdapter(adapter);
        if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){

        }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){

        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){
            manager.queryOwnToMsg(nowSkip,this);
        }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){
            manager.queryOtherToMsg(nowSkip,this);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loading = false;
        nowSkip = 0;
        requestData.clear();
        if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){
            manager.queryOwnToMsg(nowSkip,this);
        }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){
            manager.queryOtherToMsg(nowSkip,this);
        }
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    @Override
    public void querySuccess(List<FriendRequestBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            requestData.addAll(data);
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

    public void updateRead(){
        if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){
            manager.updateUserReadBatch(requestData);
        }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){
            manager.updateFriendReadBatch(requestData);
        }
    }

}
