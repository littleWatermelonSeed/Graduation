package com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForumComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

public class ReplayOtherCommentActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobSaveMsgWithoutImg{

    private TextView textView_back;
    private TextView textView_send;
    private TextView textView_msg;
    private EditText editText;
    private BaseNiceDialog dialog;

    private MyUserBean user;
    private static MyUserBean otherUser;
    private static ForumCommentBean forumCommentBean;
    private String content;
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == DialogLoading.MSG_FAIL){
                dialog.dismiss();
            }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                dialog.dismiss();
                MyToastUtil.showToast("回复成功");
                finish();
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_replay_other_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        textView_back = (TextView)findViewById(R.id.activity_replay_other_comment_back);
        textView_back.setOnClickListener(this);
        textView_send = (TextView)findViewById(R.id.activity_replay_other_comment_send);
        textView_send.setOnClickListener(this);
        textView_msg = (TextView) findViewById(R.id.activity_replay_other_comment_msg);
        editText = (EditText)findViewById(R.id.activity_replay_other_comment_edit);
    }

    @Override
    protected void initParam() {
        user = BmobManageUser.getCurrentUser();
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white1);
        textView_msg.setText("回复 " + otherUser.getNickName());
        editText.setHint("回复 " + otherUser.getNickName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_replay_other_comment_back:
                finish();
                break;
            case R.id.activity_replay_other_comment_send:
                if (!getContent()){
                    return;
                }
                DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                        new DialogLoading.ShowLoadingDone() {
                            @Override
                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                dialog = baseNiceDialog;
                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                textView.setText("回复中...");
                                sendComment();
                            }
                        });
                break;
        }
    }

    private boolean getContent(){
        content = editText.getText().toString().trim();
        if (content == null || content.equals("")){
            MyToastUtil.showToast("回复内容不能为空");
            return false;
        }
        return true;
    }

    private void sendComment(){
        ForumCommentBean temp = new ForumCommentBean();
        temp.setUser(user);
        temp.setType(1);
        temp.setPublishUser(forumCommentBean.getPublishUser());
        temp.setForum(forumCommentBean.getForum());
        temp.setRead(false);
        temp.setContent(content);
        temp.setOtherUser(otherUser);
        temp.setReleaseTime(new BmobDate(new Date()));
        temp.setReplayContent(forumCommentBean.getContent());
        BmobManageForumComment.getManager().uploadMsg(temp,forumCommentBean.getForum().getObjectId(),this);
    }

    @Override
    public void msgSuccess(String objectID) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "回复成功", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void msgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "回复失败", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

    public static void go2Activity(Context context,MyUserBean other,ForumCommentBean f){
        Intent intent = new Intent(context,ReplayOtherCommentActivity.class);
        otherUser = other;
        forumCommentBean = f;
        context.startActivity(intent);
    }

}
