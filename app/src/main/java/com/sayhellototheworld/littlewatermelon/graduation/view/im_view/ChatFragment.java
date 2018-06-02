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
import com.sayhellototheworld.littlewatermelon.graduation.adapter.FriendChatAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.FriendChatBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.im.IMMessageHandler;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

public class ChatFragment extends Fragment implements OnRefreshListener {

    private View mView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<BmobIMConversation> conversations;
    private List<FriendChatBean> friendChatData;

    private FriendChatAdapter adapter;
    private int conCount;
    private int nowComplete = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_chat, container, false);
            init();
        }
        return mView;
    }

    private void init() {
        initWidget();
        initParam();
        initShow();
    }

    private void initWidget() {
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.fragment_chat_refresh);

        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);

        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_chat_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }

    private void initParam() {
        friendChatData = new ArrayList<>();
        adapter = new FriendChatAdapter(getContext(), friendChatData);
        recyclerView.setAdapter(adapter);
        IMMessageHandler.bindChatFriendList(this);
    }

    private void initShow() {
        refreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(final RefreshLayout refreshLayout) {
        friendChatData.clear();
        nowComplete = 0;
        conversations = BmobIM.getInstance().loadAllConversation();
        if (conversations == null ||conversations.size() <= 0) {
            refreshLayout.finishRefresh(true);
            return;
        }

        conCount = conversations.size();
        queryCon(refreshLayout);
    }

    private void queryCon(final RefreshLayout refreshLayout) {
        for (BmobIMConversation b : conversations) {
            final FriendChatBean f = new FriendChatBean();
            final BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), b);
            queryLastMsg(b,refreshLayout, f, messageManager);
        }
    }

    private void queryLastMsg(final BmobIMConversation conversation,final RefreshLayout refreshLayout,final FriendChatBean f,final BmobIMConversation messageManager) {
        messageManager.queryMessages(null, 1, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        f.setNoReadNum(messageManager.getUnreadCount());
                        f.setLastMsg(list.get(0).getContent());
                        f.setFriendID(messageManager.getConversationId());

                        final Date date = new Date();
                        date.setTime(list.get(0).getCreateTime());
                        f.setTime(TimeFormatUtil.DateToRealTime(date));

                        queryUserMsg(messageManager.getConversationId(), f,refreshLayout);
                    }else {
                        addNowComplete();
                        if (getNowComplete() == conCount){
                            refreshLayout.finishRefresh(true);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    addNowComplete();
                    if (getNowComplete() == conCount){
                        refreshLayout.finishRefresh(true);
                        adapter.notifyDataSetChanged();
                    }
                    Log.i("niyuanjie","最后一条消息查询出错");
                }
            }
        });
    }

    private void queryUserMsg(String frientID, final FriendChatBean f,final RefreshLayout refreshLayout){
        BmobManageUser manageUser = new BmobManageUser(getContext());
        manageUser.queryByID(frientID, new BmobQueryDone<MyUserBean>() {
            @Override
            public void querySuccess(List<MyUserBean> data) {
                queryFriendMsg(data.get(0),f,refreshLayout);
            }

            @Override
            public void queryFailed(BmobException e) {
                addNowComplete();
                if (getNowComplete() == conCount){
                    refreshLayout.finishRefresh(true);
                    adapter.notifyDataSetChanged();
                }
                Log.i("niyuanjie","用户查询出错");
            }
        });
    }

    private void queryFriendMsg(MyUserBean friend, final FriendChatBean f,final RefreshLayout refreshLayout) {
        BmobManageFriend.getManager().queryFriend(friend, new BmobQueryDone<FriendBean>() {
            @Override
            public void querySuccess(List<FriendBean> data) {
                if (data.get(0).getFriend().getHeadPortrait() != null &&
                        !data.get(0).getFriend().getHeadPortrait().getUrl().equals("")) {
                    f.setFriendHeadUrl(data.get(0).getFriend().getHeadPortrait().getUrl());
                } else {
                    f.setFriendHeadUrl("");
                }

                if (data.get(0).getRemarkName() != null && !data.get(0).getRemarkName().equals("")) {
                    f.setFriendName(data.get(0).getRemarkName());
                } else {
                    f.setFriendName(data.get(0).getFriend().getNickName());
                }

                friendChatData.add(f);

                addNowComplete();
                if (getNowComplete() == conCount){
                    refreshLayout.finishRefresh(true);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                addNowComplete();
                if (getNowComplete() == conCount){
                    refreshLayout.finishRefresh(true);
                    adapter.notifyDataSetChanged();
                }
                Log.i("niyuanjie","好友查询出错");
            }
        });
    }

    private void addNowComplete(){
        synchronized (ChatFragment.class){
            nowComplete++;
        }
    }

    private int getNowComplete(){
        synchronized (ChatFragment.class){
            return nowComplete;
        }
    }

    public void asyncChatList(MessageEvent event) {
        BmobIMUserInfo bmobIMUserInfo = event.getFromUserInfo();
        boolean newChat = true;
        for (FriendChatBean f : friendChatData) {
            if (f.getFriendID().equals(bmobIMUserInfo.getUserId())) {
                f.setLastMsg(event.getMessage().getContent());
                final Date date = new Date();
                date.setTime(event.getMessage().getCreateTime());
                f.setTime(TimeFormatUtil.DateToRealTime(date));
                if (ChatActivity.getNowChatFriendID() == null ||
                        (ChatActivity.getNowChatFriendID() != null && !ChatActivity.getNowChatFriendID().equals(f.getFriendID()))) {
                    f.setNoReadNum(f.getNoReadNum() + 1);
                    CenterActivity.setAddChatNoRead(1);
                }
                FriendChatBean temp = f;
                friendChatData.remove(f);
                friendChatData.add(0, temp);
                adapter.notifyDataSetChanged();
                newChat = false;
                break;
            }
        }

        if (!newChat)
            return;

        asyncNewChatItem(event);
    }

    private void asyncNewChatItem(MessageEvent event) {
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setAvatar(event.getFromUserInfo().getAvatar());
        info.setName(event.getFromUserInfo().getName());
        info.setUserId(event.getFromUserInfo().getUserId());
        BmobIM.getInstance().startPrivateConversation(info, null);

        FriendChatBean f = new FriendChatBean();
        BmobIMUserInfo bmobIMUserInfo = event.getFromUserInfo();
        f.setFriendHeadUrl(bmobIMUserInfo.getAvatar());
        f.setFriendID(bmobIMUserInfo.getUserId());
        f.setNoReadNum(1);
        f.setLastMsg(event.getMessage().getContent());
        final Date date = new Date();
        date.setTime(event.getMessage().getCreateTime());
        f.setTime(TimeFormatUtil.DateToRealTime(date));
        f.setFriendName(bmobIMUserInfo.getName());
        friendChatData.add(0, f);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IMMessageHandler.unBindChatFriendList();
    }

}
