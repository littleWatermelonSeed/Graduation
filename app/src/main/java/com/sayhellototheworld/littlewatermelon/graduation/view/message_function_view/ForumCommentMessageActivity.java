package com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ForumMessageDeatilsAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForumComment;
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

public class ForumCommentMessageActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener, OnRefreshListener,
        OnLoadMoreListener, BmobQueryDone<ForumCommentBean> {

    private TextView txt_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private ForumMessageDeatilsAdapter adapter;
    private List<ForumCommentBean> forumCommentData;
    private boolean loading = false;
    private int nowSkip = 0;
    private BmobManageForumComment manageForumComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_comment_message);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_forum_comment_message_back);
        txt_back.setOnClickListener(this);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_forum_comment_message_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.activity_forum_comment_message_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initParam() {
        forumCommentData = new ArrayList<>();
        manageForumComment = BmobManageForumComment.getManager();
        adapter = new ForumMessageDeatilsAdapter(this,forumCommentData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_forum_comment_message_back:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        manageForumComment.queryToMsg(nowSkip, this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loading = false;
        nowSkip = 0;
        forumCommentData.clear();
        manageForumComment.queryToMsg(nowSkip, this);
    }

    @Override
    public void querySuccess(List<ForumCommentBean> data) {
        int dataNum = 0;
        finishSmart(true);
        Log.i("niyuanjie","查询成功，数量有：" + data.size());
        if (data.size() <= 0) {
            MyToastUtil.showToast("没有更多消息啦,已经到底啦~");
        } else {
            for (ForumCommentBean f:data){
                if (f.getForum().getContent() == null){
                    continue;
                }
                forumCommentData.add(f);
                dataNum++;
            }
        }

        if (dataNum == 0){
            MyToastUtil.showToast("没有更多消息啦,已经到底啦~");
        }else {
            adapter.notifyDataSetChanged();
            nowSkip++;
        }

//        manageForumComment.updateReadBatch(data);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
        finishSmart(false);
    }

    private void finishSmart(boolean success) {
        if (!loading) {
            refreshLayout.finishRefresh(success);
        } else {
            refreshLayout.finishLoadMore(success);
        }
    }

    public static void go2Activity(Context context) {
        Intent intent = new Intent(context, ForumCommentMessageActivity.class);
        context.startActivity(intent);
    }

}
