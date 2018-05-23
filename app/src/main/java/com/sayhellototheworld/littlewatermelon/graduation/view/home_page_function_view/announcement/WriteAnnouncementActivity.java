package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.announcement;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.AnnouncementBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageAnnouncement;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

public class WriteAnnouncementActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobSaveMsgWithoutImg{

    private TextView txt_back;
    private TextView txt_release;
    private EditText edt_title;
    private EditText edt_content;

    private String title;
    private String content;

    private BaseNiceDialog dialog;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                MyToastUtil.showToast("发布失败");
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                MyToastUtil.showToast("发布成功");
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_announcement);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_write_announcement_back);
        txt_back.setOnClickListener(this);
        txt_release = (TextView) findViewById(R.id.activity_write_announcement_release);
        txt_release.setOnClickListener(this);
        edt_title = (EditText) findViewById(R.id.activity_write_announcement_edt_title);
        edt_content = (EditText) findViewById(R.id.activity_write_announcement_edt_content);
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_announcement_back:
                finish();
                break;
            case R.id.activity_write_announcement_release:
                if (!getMsg()){
                    return;
                }
                releaseMsg();
                break;
        }
    }

    private boolean getMsg(){
        title = edt_title.getText().toString().trim();
        content = edt_content.getText().toString().trim();

        if (title == null || title.equals("")){
            MyToastUtil.showToast("标题不能为空");
            return false;
        }

        if (content == null || content.equals("")){
            MyToastUtil.showToast("内容不能为空");
            return false;
        }

        return true;
    }

    private void releaseMsg(){
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("发布中...");
                        AnnouncementBean bean = new AnnouncementBean();
                        bean.setTitle(title);
                        bean.setContent(content);
                        bean.setReleaseTime(new BmobDate(new Date()));
                        bean.setTeacher(BmobManageUser.getCurrentUser());
                        BmobManageAnnouncement.getManager().uploadMsgWithoutImg(bean,WriteAnnouncementActivity.this);
                    }
                });
    }

    @Override
    public void msgSuccess(String objectID) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "发布成功", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void msgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

    public static void go2Activity(Context context){
        Intent intent = new Intent(context,WriteAnnouncementActivity.class);
        context.startActivity(intent);
    }

}
