package com.sayhellototheworld.littlewatermelon.graduation.im;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ChatMessageAdapter;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * Created by 123 on 2018/5/28.
 */

public class IMMessageHandler extends BmobIMMessageHandler {

    private static Map<String,ChatMessageAdapter> chatAdapters = new HashMap<>();

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        if (chatAdapters.containsKey(event.getFromUserInfo().getUserId())){
            chatAdapters.get(event.getFromUserInfo().getUserId()).addToBottomMessages(event.getMessage());
        }else {

        }
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        Log.i("niyuanjie","离线接收到消息");
    }

    public static void bindChatAdapter(String friendID,ChatMessageAdapter adapter){
        chatAdapters.put(friendID,adapter);
    }

    public static void unBindChatAdapter(String friendID){
        chatAdapters.remove(friendID);
    }

}
