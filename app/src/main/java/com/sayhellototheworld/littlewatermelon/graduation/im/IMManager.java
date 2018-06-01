package com.sayhellototheworld.littlewatermelon.graduation.im;

import android.text.TextUtils;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobMessageSendListener;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/31.
 */

public class IMManager {

    private static IMManager manager;

    private IMManager(){

    }

    public static IMManager getManager(){
        if (manager == null){
            synchronized (IMManager.class){
                if (manager == null){
                    manager = new IMManager();
                }
            }
        }
        return manager;
    }

    public void connectIM(String userID){
        if (!TextUtils.isEmpty(userID)) {
            BmobIM.connect(userID, new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        Log.i("niyuanjie","IM连接成功");
                    } else {
                        Log.i("niyuanjie","IM连接出错");
                    }
                }
            });
        }
    }

    public void updateIMUser(MyUserBean user){
        BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(user.getObjectId());
        info.setName(user.getNickName());
        if (user.getHeadPortrait() != null && !user.getHeadPortrait().getUrl().equals("")){
            info.setAvatar(user.getHeadPortrait().getUrl());
        }
        BmobIM.getInstance().updateUserInfo(info);
    }

    public void disConnectIM(){
        BmobIM.getInstance().disConnect();
    }

    public void addIMStatueListener(final MyUserBean user){
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                if (status == ConnectionStatus.KICK_ASS){
                    disConnectIM();
                }else if (status == ConnectionStatus.CONNECTED){
                    updateIMUser(user);
                }
            }
        });
    }

    /**
     * 发送文本消息
     */
    public void sendMessage(BmobIMTextMessage msg, BmobIMConversation conversationEntrance, final BmobMessageSendListener listener) {
        conversationEntrance.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                listener.done(bmobIMMessage,e);
            }
        });
    }

//    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
//        final BmobIMConversation conversation = event.getConversation();
//        final BmobIMUserInfo info = event.getFromUserInfo();
//        final BmobIMMessage msg = event.getMessage();
//        String username = info.getName();
//        String title = conversation.getConversationTitle();
//        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
//        if (!username.equals(title)) {
//            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
//                @Override
//                public void done(User s, BmobException e) {
//                    if (e == null) {
//                        String name = s.getUsername();
//                        String avatar = s.getAvatar();
//                        conversation.setConversationIcon(avatar);
//                        conversation.setConversationTitle(name);
//                        info.setName(name);
//                        info.setAvatar(avatar);
//                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
//                        BmobIM.getInstance().updateUserInfo(info);
//                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
//                        if (!msg.isTransient()) {
//                            BmobIM.getInstance().updateConversation(conversation);
//                        }
//                    } else {
//                        Logger.e(e);
//                    }
//                    listener.done(null);
//                }
//            });
//        } else {
//            listener.done(null);
//        }
//    }

}
