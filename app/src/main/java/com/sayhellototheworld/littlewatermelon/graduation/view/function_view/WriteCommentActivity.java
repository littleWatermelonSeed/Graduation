package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

public class WriteCommentActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobSaveMsgWithoutImg {

    private TextView textView_back;
    private TextView textView_send;
    private TextView textView_msg;
    private EditText editText;
    private BaseNiceDialog dialog;

    private MyUserBean userBean;
    private static BmobObject bmobObject;
    private static int comment_type = -1;

    public final static int COMMENT_TYPE_LOST_AND_FIND = 0;
    public final static int COMMENT_TYPE_FLEA_MARKET = 1;
    public final static int COMMENT_TYPE_FLEA_RRSOURCE_SHARE = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 == DialogLoading.MSG_FAIL){
                dialog.dismiss();
            }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                dialog.dismiss();
                MyToastUtil.showToast("评论发表成功");
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        textView_back = (TextView)findViewById(R.id.activity_write_comment_back);
        textView_back.setOnClickListener(this);
        textView_send = (TextView)findViewById(R.id.activity_write_comment_send);
        textView_send.setOnClickListener(this);
        textView_msg = (TextView) findViewById(R.id.activity_write_comment_msg);
        editText = (EditText)findViewById(R.id.activity_write_comment_edit);

    }

    @Override
    protected void initParam() {
        userBean = BmobManageUser.getCurrentUser();
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    @Override
    protected void initShow() {
        switch (comment_type){
            case COMMENT_TYPE_LOST_AND_FIND:
                textView_msg.setText("写评论");
                break;
            case COMMENT_TYPE_FLEA_MARKET:
                textView_msg.setText("留言");
                break;
            case COMMENT_TYPE_FLEA_RRSOURCE_SHARE:
                textView_msg.setText("写评论");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_comment_back:
                finish();
                break;
            case R.id.activity_write_comment_send:
                sendComment();
                break;
        }
    }

    private void sendComment(){
        final String commentContent = editText.getText().toString();
        commentContent.replaceAll("\\s","");
        if (commentContent.equals("")){
            MyToastUtil.showToast("评论内容不能为空");
            return;
        }

        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("发送中...");
                        beginSend(commentContent);
                    }
                });
    }

    private void beginSend(String commentContent){
        switch (comment_type){
            case COMMENT_TYPE_LOST_AND_FIND:
                sendLostComment(commentContent);
                break;
            case COMMENT_TYPE_FLEA_MARKET:
                sendFleaComment(commentContent);
                break;
            case COMMENT_TYPE_FLEA_RRSOURCE_SHARE:
                sendResourceComment(commentContent);
                break;
        }
    }

    private void sendLostComment(String commentContent) {
        LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
        lostAndFindBean.setCommentNum(lostAndFindBean.getCommentNum() + 1);

        LostCommentBean lostCommentBean = new LostCommentBean();
        lostCommentBean.setUser(userBean);
        lostCommentBean.setReleaseTime(new BmobDate(new Date()));
        lostCommentBean.setContent(commentContent);
        lostCommentBean.setRead(false);
        lostCommentBean.setLost(lostAndFindBean);
        BmobManageLostComment.getManager().uploadMsg(lostCommentBean,this);
    }

    private void sendFleaComment(String commentContent){
        FleaMarketBean fleaMarketBean = (FleaMarketBean) bmobObject;
        FleaCommentBean fleaMarketCommentBean = new FleaCommentBean();

        fleaMarketCommentBean.setUser(userBean);
        fleaMarketCommentBean.setReleaseTime(new BmobDate(new Date()));
        fleaMarketCommentBean.setContent(commentContent);
        fleaMarketCommentBean.setRead(false);
        fleaMarketCommentBean.setFleaMarkte(fleaMarketBean);
        BmobManageFleaComment.getManager().uploadMsg(fleaMarketCommentBean,this);
    }

    private void sendResourceComment(String commentContent){
        ResourceShareBean resourceShareBean = (ResourceShareBean) bmobObject;
        ResourceCommentBean resourceCommentBean = new ResourceCommentBean();

        resourceCommentBean.setUser(userBean);
        resourceCommentBean.setReleaseTime(new BmobDate(new Date()));
        resourceCommentBean.setContent(commentContent);
        resourceCommentBean.setRead(false);
        resourceCommentBean.setResource(resourceShareBean);
        BmobManageResourceComment.getManager().uploadMsg(resourceCommentBean,this);
    }

    @Override
    public void msgSuccess(String objectID) {
        switch (comment_type){
            case COMMENT_TYPE_LOST_AND_FIND:
                LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
                lostAndFindBean.increment("commentNum");
                lostAndFindBean.update();
                break;
        }
        DialogLoading.dismissLoadingDialog(handler, dialog, "发布成功", DialogLoading.MSG_SUCCESS);
        finish();
    }

    @Override
    public void msgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "发布失败", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }


    public static void go2Activity(Context context, BmobObject bmobObj, int type){
        Intent intent = new Intent(context,WriteCommentActivity.class);
        bmobObject = bmobObj;
        comment_type = type;
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comment_type = -1;
        bmobObject = null;
    }
}
