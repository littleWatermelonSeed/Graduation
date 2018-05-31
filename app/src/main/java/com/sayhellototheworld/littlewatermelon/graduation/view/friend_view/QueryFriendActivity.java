package com.sayhellototheworld.littlewatermelon.graduation.view.friend_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.QueryFriendtAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class QueryFriendActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobQueryDone<MyUserBean> {

    private ImageView img_back;
    private TextView txt_by_id;
    private TextView txt_by_phone;
    private EditText edt_by;
    private Button btn_query;
    private TextView txt_no_msg;
    private RecyclerView recyclerView;

    private int byType = 0;
    private BmobManageUser manageUser;
    private QueryFriendtAdapter adapter;
    private List<MyUserBean> userData;

    private BaseNiceDialog dialog;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                MyToastUtil.showToast("查询失败");
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                MyToastUtil.showToast("查询成功");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_query_friend);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        img_back = (ImageView) findViewById(R.id.activity_query_friend_back);
        img_back.setOnClickListener(this);
        txt_by_id = (TextView) findViewById(R.id.activity_query_friend_by_id);
        txt_by_id.setOnClickListener(this);
        txt_by_phone = (TextView) findViewById(R.id.activity_query_friend_by_phone);
        txt_by_phone.setOnClickListener(this);
        edt_by = (EditText) findViewById(R.id.activity_query_friend_edt_id);
        btn_query = (Button) findViewById(R.id.activity_query_friend_btn_query);
        btn_query.setOnClickListener(this);
        txt_no_msg = (TextView) findViewById(R.id.activity_query_friend_no_msg);
        recyclerView = (RecyclerView) findViewById(R.id.activity_query_friend_recycle_view);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initParam() {
        manageUser = new BmobManageUser(this);
        userData = new ArrayList<>();
        adapter = new QueryFriendtAdapter(this,userData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    public static void go2Activity(Context context) {
        Intent intent = new Intent(context, QueryFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_query_friend_back:
                finish();
                break;
            case R.id.activity_query_friend_btn_query:
                queryUser();
                break;
            case R.id.activity_query_friend_by_id:
                byType = 0;
                txt_by_id.setBackgroundResource(R.drawable.radius_background8);
                txt_by_phone.setBackgroundResource(R.drawable.radius_background4);
                break;
            case R.id.activity_query_friend_by_phone:
                byType = 1;
                txt_by_id.setBackgroundResource(R.drawable.radius_background4);
                txt_by_phone.setBackgroundResource(R.drawable.radius_background8);
                break;
        }
    }

    private void queryUser(){
        final String by = edt_by.getText().toString().trim();
        if (by == null || by.equals("")){
            MyToastUtil.showToast("好友ID/手机号不能为空");
            return;
        }
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("查询中...");
                        dialog = baseNiceDialog;
                        if (byType == 0){
                            manageUser.queryByID(by,QueryFriendActivity.this);
                        }else if (byType == 1){
                            manageUser.queryByPhone(by,QueryFriendActivity.this);
                        }
                    }
                });
    }

    @Override
    public void querySuccess(List<MyUserBean> data) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "查询成功", DialogLoading.MSG_SUCCESS);
        userData.clear();
        if (data.size() > 0){
            userData.addAll(data);
            txt_no_msg.setVisibility(View.GONE);
        }else {
            txt_no_msg.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void queryFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

}
