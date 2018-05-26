package com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ForumAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForum;
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
 * Created by 123 on 2018/5/24.
 */

public class ControlForum implements OnLoadMoreListener, OnRefreshListener,
        BmobQueryDone<ForumBean> {

    public final static int FORUM_TYPE_ALL_SCHOOL = 0;
    public final static int FORUM_TYPE_LOACL_SCHOOL = 1;
    public final static int FORUM_TYPE_OWN = 2;
    public final static int FORUM_TYPE_MSG = 3;

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private int type;
    private List<ForumBean> forumData;
    private BmobManageForum manager;
    private ForumAdapter adapter;

    private boolean loading = false;
    private int nowSkip = 0;

    public ControlForum(Context context,int type,SmartRefreshLayout refreshLayout,RecyclerView recyclerView) {
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        forumData = new ArrayList<>();
        manager = BmobManageForum.getManager();

        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        adapter = new ForumAdapter(context,forumData,type,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void querySuccess(List<ForumBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            forumData.addAll(data);
        }
        adapter.notifyDataSetChanged();
        Log.i("niyuanjie","查询成功，数据数为：" + data.size());
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
        doQuery();
    }

    private void doQuery() {
        if (type == FORUM_TYPE_LOACL_SCHOOL){
            manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),nowSkip,this);
        }else if (type == FORUM_TYPE_ALL_SCHOOL){
            manager.queryAll(nowSkip,this);
        }else if (type == FORUM_TYPE_OWN){
            manager.queryByUser(BmobManageUser.getCurrentUser(),nowSkip,this);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        forumData.clear();
        doQuery();
    }

    public void notifyAdapter(int position,int num,String likeID){
        forumData.get(position).setLikeUserObjID(likeID);
        forumData.get(position).setLikeNum(num);
        adapter.notifyDataSetChanged();
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
