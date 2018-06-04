package com.sayhellototheworld.littlewatermelon.graduation.view.im_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.im.IMMessageHandler;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.im.ControlChat;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class ChatActivity extends BaseStatusActivity implements View.OnClickListener{

    public final static int CHAT_REQUEST_CODE = 100;
    public final static int CHAT_DELETE_FRIEND_CODE = 101;

    private ImageView img_back;
    private TextView txt_title;
    private ImageView img_people;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EditText edt_content;
    private Button btn_send;

    private String userName;
    private String friendHeadUrl;
    private String friendID;

    private ControlChat controlChat;
    private static String nowChatFriendID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        img_back = (ImageView) findViewById(R.id.activity_chat_back);
        img_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_chat_title);
        img_people = (ImageView) findViewById(R.id.activity_chat_more);
        img_people.setOnClickListener(this);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_chat_refreshLayout);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setEnableLoadMore(false);

        recyclerView = (RecyclerView) findViewById(R.id.activity_chat_recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        edt_content = (EditText) findViewById(R.id.activity_chat_edt_content);
        btn_send = (Button) findViewById(R.id.activity_chat_send_msg);
        btn_send.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        IMMessageHandler.bindChatActivity(this);
        friendHeadUrl = getIntent().getStringExtra("friendHeadUrl");
        friendID = getIntent().getStringExtra("friendID");
        userName = getIntent().getStringExtra("userName");
        nowChatFriendID = friendID;

        controlChat = new ControlChat(this,friendID,friendHeadUrl,userName,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        txt_title.setText(userName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_chat_back:
                finish();
                break;
            case R.id.activity_chat_more:
                UserDetailsActivity.go2Activity(this,friendID,true);
                break;
            case R.id.activity_chat_send_msg:
                String msg = edt_content.getText().toString();
                edt_content.setText("");
                controlChat.sendMessage(msg);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMMessageHandler.unBindChatActivity();
        controlChat.updateLocalCache();
        controlChat.unBindAdapter();
        nowChatFriendID = null;
        controlChat.updateMsgStatue();
    }

    public static void go2Activity(Context context,String friendID,String friendHeadUrl,String userName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("friendID",friendID);
        intent.putExtra("friendHeadUrl",friendHeadUrl);
        intent.putExtra("userName",userName);
        context.startActivity(intent);
    }

    public static void go2ActivityForResult(Context context,String friendID,String friendHeadUrl,String userName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("friendID",friendID);
        intent.putExtra("friendHeadUrl",friendHeadUrl);
        intent.putExtra("userName",userName);
        ((FragmentActivity)context).startActivityForResult(intent,CHAT_REQUEST_CODE);
    }

    public static String getNowChatFriendID(){
        return nowChatFriendID;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserDetailsActivity.USER_DETAILS_FOR_RESULT_CODE){
            if (resultCode == UserDetailsActivity.USER_DETAILS_DELETE_FRIEND_CODE){
                Intent intent = new Intent();
                intent.putExtra("friendID",friendID);
                setResult(CHAT_DELETE_FRIEND_CODE,intent);
                controlChat.delConversation();
                finish();
            }
        }
    }

    public void delFriend(){
        finish();
        MyToastUtil.showToast("该好友删除了你,不能再进行聊天");
    }

}
