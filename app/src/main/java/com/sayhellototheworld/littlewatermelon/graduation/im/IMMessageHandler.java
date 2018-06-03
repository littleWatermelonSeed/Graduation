package com.sayhellototheworld.littlewatermelon.graduation.im;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ChatMessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/28.
 */

public class IMMessageHandler extends BmobIMMessageHandler {

    private static Map<String, ChatMessageAdapter> chatAdapters = new HashMap<>();
    private static ChatFragment chatFragment;
    private static ChatActivity chatActivity;

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        if (deleteFriendMsg(event))
            return;

        boolean chating = false;

        if (chatAdapters.containsKey(event.getFromUserInfo().getUserId())) {
            chatAdapters.get(event.getFromUserInfo().getUserId()).addToBottomMessages(event.getMessage());
            chating = true;
        }

        if (chatFragment == null) {
            BmobIM.getInstance().startPrivateConversation(event.getFromUserInfo(), null);
            CenterActivity.setAddChatNoRead(1);
        } else {
            chatFragment.asyncChatList(event,chating);
        }
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        Log.i("niyuanjie", "离线接收到消息");
    }

    private boolean deleteFriendMsg(MessageEvent event){
        if (event.getMessage().getContent().equals("delete,you,stupid" + BmobManageUser.getCurrentUser().getObjectId())){
            String friendID = event.getFromUserInfo().getUserId();
            BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), event.getConversation());
            messageManager.clearMessage();

            BmobIM.getInstance().deleteConversation(friendID);
            if (chatFragment != null){
                chatFragment.delFriend(friendID);
            }

            if (chatActivity != null){
                chatActivity.delFriend();
            }
            return true;
        }
        return false;
    }

    public void updateUserInfo(MessageEvent event) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        if (!username.equals(title)) {
            BmobManageUser manageUser = new BmobManageUser();
            manageUser.queryByID(info.getUserId(), new BmobQueryDone<MyUserBean>() {
                @Override
                public void querySuccess(List<MyUserBean> data) {
                    String name = data.get(0).getNickName();
                    if (data.get(0).getHeadPortrait() != null){
                        String avatar = data.get(0).getHeadPortrait().getUrl();
                        conversation.setConversationIcon(avatar);
                        info.setAvatar(avatar);
                    }
                    conversation.setConversationTitle(name);
                    info.setName(name);
                    //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                    BmobIM.getInstance().updateUserInfo(info);
                    //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
                    if (!msg.isTransient()) {
                        BmobIM.getInstance().updateConversation(conversation);
                    }
                }

                @Override
                public void queryFailed(BmobException e) {

                }
            });
        }
    }

    public static void bindChatAdapter(String friendID, ChatMessageAdapter adapter) {
        chatAdapters.put(friendID, adapter);
    }

    public static void unBindChatAdapter(String friendID) {
        chatAdapters.remove(friendID);
    }

    public static void bindChatFriendList(ChatFragment c) {
        chatFragment = c;
    }

    public static void unBindChatFriendList() {
        chatFragment = null;
    }

    public static void bindChatActivity(ChatActivity c){
        chatActivity = c;
    }

    public static void unBindChatActivity(){
        chatActivity = null;
    }

}
