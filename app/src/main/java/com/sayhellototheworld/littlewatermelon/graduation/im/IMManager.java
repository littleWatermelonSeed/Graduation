package com.sayhellototheworld.littlewatermelon.graduation.im;

import android.text.TextUtils;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobMessageSendListener;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;

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
        }else {
            info.setAvatar("");
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
                    CenterActivity.setChatNoReadNum((int) BmobIM.getInstance().getAllUnReadCount());
                    CenterActivity.setNewChatNoRead((int) BmobIM.getInstance().getAllUnReadCount());
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
                if (listener == null)
                    return;
                listener.done(bmobIMMessage,e);
            }
        });
    }

}
