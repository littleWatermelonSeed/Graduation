package com.sayhellototheworld.littlewatermelon.graduation.presenter.im;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ChatMessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.im.IMManager;
import com.sayhellototheworld.littlewatermelon.graduation.im.IMMessageHandler;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobMessageSendListener;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/6/1.
 */

public class ControlChat implements BmobMessageSendListener,OnRefreshListener{

    private Context context;
    private String friednID;
    private String friendHeadUrl;
    private String userName;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private BmobIMUserInfo info;
    private BmobIMConversation conversationEntrance;
    private BmobIMConversation messageManager;

    private ChatMessageAdapter adapter;
    private BmobIMMessage firstMessage;

    public ControlChat(Context context,String friednID, String friendHeadUrl,String userName,SmartRefreshLayout refreshLayout,RecyclerView recyclerView) {
        this.context = context;
        this.friednID = friednID;
        this.friendHeadUrl = friendHeadUrl;
        this.userName = userName;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        adapter = new ChatMessageAdapter(this.context,friendHeadUrl,this,recyclerView);
        IMMessageHandler.bindChatAdapter(friednID,adapter);
        this.recyclerView.setAdapter(adapter);
        this.refreshLayout.setOnRefreshListener(this);

        info = new BmobIMUserInfo();
        info.setUserId(friednID);
        info.setName(this.userName);
        info.setAvatar(friendHeadUrl);
        conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);

        loadAndQueryMsg();
    }

    private void loadAndQueryMsg(){
        messageManager.queryMessages(null, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        firstMessage = list.get(0);
                        adapter.addToBottomMessages(list);
                        recyclerView.scrollToPosition(list.size() - 1);
                    }
                } else {
                    BmobExceptionUtil.dealWithException(context,e);
                }
            }
        });
    }

    @Override
    public void done(BmobIMMessage msg, BmobException e) {
        if (e == null){
            Log.i("niyuanjie","消息发送成功");
            Log.i("niyuanjie","内容" + msg.getContent());
            adapter.notifyDataSetChanged();
        }else {
            Log.i("niyuanjie","发送出错 错误信息为：" + e.getMessage());
        }
    }

    public void sendMessage(String text){
        if (TextUtils.isEmpty(text.trim())) {
            MyToastUtil.showToast("请输入内容");
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        IMManager.getManager().sendMessage(msg,messageManager,this);
        adapter.addToBottomMessages(msg);
    }

    public void updateLocalCache(){
        messageManager.updateLocalCache();
    }

    @Override
    public void onRefresh(final RefreshLayout refreshLayout) {
        messageManager.queryMessages(firstMessage, 20, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addToTopMessages(list);
                        firstMessage = list.get(0);
                    }else {
                        MyToastUtil.showToast("已经没有更多聊天记录了");
                    }
                    refreshLayout.finishRefresh(true);
                } else {
                    BmobExceptionUtil.dealWithException(context,e);
                    refreshLayout.finishRefresh(false);
                }
            }
        });
    }

    public void unBindAdapter(){
        IMMessageHandler.unBindChatAdapter(friednID);
    }

    public void delMsg(BmobIMMessage msg){
        messageManager.deleteMessage(msg);
    }

    public void delConversation(){
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent("delete,you,stupid" + friednID);
        IMManager.getManager().sendMessage(msg, messageManager, new BmobMessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null){
                    messageManager.clearMessage();
                }
            }
        });

        BmobIM.getInstance().deleteConversation(conversationEntrance);
    }

}
