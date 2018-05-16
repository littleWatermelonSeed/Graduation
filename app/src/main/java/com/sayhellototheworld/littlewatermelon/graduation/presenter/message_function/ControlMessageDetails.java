package com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.MessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.MessageDeatilsAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.MessageDetailsBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceComment;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/15.
 */

public class ControlMessageDetails implements OnRefreshListener, OnLoadMoreListener {

    private Context context;
    private int type;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private MessageDeatilsAdapter adapter;
    private List<MessageDetailsBean> messageData;
    private boolean loading = false;
    private int nowSkip = 0;


    public ControlMessageDetails(Context context, int type, SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        messageData = new ArrayList<MessageDetailsBean>();

        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);

        adapter = new MessageDeatilsAdapter(context, messageData, type);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.recyclerView.setLayoutManager(llManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loading = false;
        nowSkip = 0;
        messageData.clear();
        queryMsg();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        queryMsg();
    }


    private void queryMsg() {
        switch (type) {
            case MessageAdapter.MSG_TYPE_LOST:
                queryLost();
                break;
            case MessageAdapter.MSG_TYPE_FLEA:
                queryFlea();
                break;
            case MessageAdapter.MSG_TYPE_SHARE:
                queryShare();
                break;
            case MessageAdapter.MSG_TYPE_FORUM:
                break;
        }
    }

    private void queryLost() {
        BmobManageLostComment.getManager().queryToMsg(nowSkip, new BmobQueryDone<LostCommentBean>() {
            @Override
            public void querySuccess(List<LostCommentBean> data) {
                finishSmart(true);
                if (data.size() <= 0){
                    MyToastUtil.showToast("已经到底啦~");
                }else {
                    MessageDetailsBean bean;
                    for (LostCommentBean l:data){
                        bean = new MessageDetailsBean();
                        bean.setRead(l.getRead());
                        bean.setBmobObject(l.getLost());
                        bean.setComment(l.getContent());
                        bean.setCommentUser(l.getUser());
                        bean.setContent(l.getLost().getContent());
                        bean.setCreateTime(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(l.getReleaseTime().getDate())));
                        bean.setReleaseUserName(l.getPublishUser().getNickName());
                        if (l.getLost().getImageUrls() != null && l.getLost().getImageUrls().size() > 0) {
                            bean.setFirstImgUrl(l.getLost().getImageUrls().get(0));
                        }
                        messageData.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    nowSkip++;
                    BmobManageLostComment.getManager().updateReadBatch(data);
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void queryFlea() {
        BmobManageFleaComment.getManager().queryToMsg(nowSkip, new BmobQueryDone<FleaCommentBean>() {
            @Override
            public void querySuccess(List<FleaCommentBean> data) {
                finishSmart(true);
                if (data.size() <= 0){
                    MyToastUtil.showToast("没有更多消息啦,已经到底啦~");
                }else {
                    MessageDetailsBean bean;
                    for (FleaCommentBean l:data){
                        bean = new MessageDetailsBean();
                        bean.setRead(l.getRead());
                        bean.setBmobObject(l.getFleaMarkte());
                        bean.setComment(l.getContent());
                        bean.setCommentUser(l.getUser());
                        bean.setContent(l.getFleaMarkte().getContent());
                        bean.setCreateTime(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(l.getReleaseTime().getDate())));
                        bean.setReleaseUserName(l.getPublishUser().getNickName());
                        if (l.getFleaMarkte().getImageUrls() != null && l.getFleaMarkte().getImageUrls().size() > 0) {
                            bean.setFirstImgUrl(l.getFleaMarkte().getImageUrls().get(0));
                        }
                        messageData.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    nowSkip++;
                    BmobManageFleaComment.getManager().updateReadBatch(data);
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void queryShare() {
        BmobManageResourceComment.getManager().queryToMsg(nowSkip, new BmobQueryDone<ResourceCommentBean>() {
            @Override
            public void querySuccess(List<ResourceCommentBean> data) {
                finishSmart(true);
                if (data.size() <= 0){
                    MyToastUtil.showToast("已经到底啦~");
                }else {
                    MessageDetailsBean bean;
                    for (ResourceCommentBean l:data){
                        bean = new MessageDetailsBean();
                        bean.setRead(l.getRead());
                        bean.setBmobObject(l.getResource());
                        bean.setComment(l.getContent());
                        bean.setCommentUser(l.getUser());
                        bean.setContent(l.getResource().getContent());
                        bean.setCreateTime(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(l.getReleaseTime().getDate())));
                        bean.setReleaseUserName(l.getPublishUser().getNickName());
                        if (l.getResource().getImageUrls() != null && l.getResource().getImageUrls().size() > 0) {
                            bean.setFirstImgUrl(l.getResource().getImageUrls().get(0));
                        }
                        messageData.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    nowSkip++;
                    BmobManageResourceComment.getManager().updateReadBatch(data);
                }
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
