package com.sayhellototheworld.littlewatermelon.graduation.view.im_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    public static Map<String,MyUserBean> userMap = new HashMap<>();

    private ImageView img_back;
    private TextView txt_title;
    private ImageView img_people;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EditText edt_content;
    private Button btn_send;

    private String userTag;
    private MyUserBean user;
    private String userName;

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

        recyclerView = (RecyclerView) findViewById(R.id.activity_chat_recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        edt_content = (EditText) findViewById(R.id.activity_chat_edt_content);
        btn_send = (Button) findViewById(R.id.activity_chat_send_msg);
        btn_send.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        userTag = getIntent().getStringExtra("userTag");
        userName = getIntent().getStringExtra("userName");
        user = userMap.get(userTag);
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
                UserDetailsActivity.go2Activity(this,user.getObjectId(),true);
                break;
            case R.id.activity_chat_send_msg:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userMap.remove(userTag);
    }

    public static void go2Activity(Context context, MyUserBean user,String userName) {
        Intent intent = new Intent(context, ChatActivity.class);
        userMap.put(user.getObjectId(),user);
        intent.putExtra("userTag",user.getObjectId());
        intent.putExtra("userName",userName);
        context.startActivity(intent);
    }
}
