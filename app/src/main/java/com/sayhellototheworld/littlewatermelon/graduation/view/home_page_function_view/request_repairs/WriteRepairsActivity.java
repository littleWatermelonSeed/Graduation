package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_repairs;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestRepairBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManagerRepairs;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import cn.bmob.v3.exception.BmobException;

public class WriteRepairsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobSaveMsgWithoutImg{
    
    private TextView txt_back;
    private TextView txt_commit;
    private EditText edt_describe;
    private EditText edt_user_name;
    private EditText edt_place;
    private EditText edt_contact;

    private String describe;
    private String user_name;
    private String place;
    private String contact;

    private BaseNiceDialog dialog;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                MyToastUtil.showToast("提交失败");
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                MyToastUtil.showToast("提交成功");
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_repairs);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_write_repairs_back);
        txt_back.setOnClickListener(this);
        txt_commit = (TextView) findViewById(R.id.activity_write_repairs_release);
        txt_commit.setOnClickListener(this);
        edt_describe = (EditText) findViewById(R.id.activity_write_repairs_edt_describe);
        edt_user_name = (EditText) findViewById(R.id.activity_write_repairs_edt_user_name);
        edt_place = (EditText) findViewById(R.id.activity_write_repairs_edt_place);
        edt_contact = (EditText) findViewById(R.id.activity_write_repairs_edt_contact);
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
            case R.id.activity_write_repairs_back:
                finish();
                break;
            case R.id.activity_write_repairs_release:
                if (!getMsg()){
                    return;
                }
                commit();
                break;
        }
    }

    private boolean getMsg(){
        describe = edt_describe.getText().toString().trim();
        user_name = edt_user_name.getText().toString().trim();
        place = edt_place.getText().toString().trim();
        contact = edt_contact.getText().toString().trim();

        if (describe == null || describe.equals("")){
            MyToastUtil.showToast("维修描述不能为空");
            return false;
        }

        if (user_name == null || user_name.equals("")){
            MyToastUtil.showToast("申请人姓名不能为空");
            return false;
        }

        if (place == null || place.equals("")){
            MyToastUtil.showToast("维修地点不能为空");
            return false;
        }

        if (contact == null || contact.equals("")){
            MyToastUtil.showToast("联系电话不能为空");
            return false;
        }

        return true;
    }

    private void commit(){
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("提交中...");
                        RequestRepairBean bean = new RequestRepairBean();
                        bean.setUser(BmobManageUser.getCurrentUser());
                        bean.setUserName(user_name);
                        bean.setDescribe(describe);
                        bean.setPlace(place);
                        bean.setContact(contact);
                        bean.setStatue(0);
                        bean.setrRead(false);
                        bean.setSchoolKey(BmobManageUser.getCurrentUser().getSchooleKey());
                        BmobManagerRepairs.getManager().uploadMsgWithoutImg(bean,WriteRepairsActivity.this);
                    }
                });
    }

    @Override
    public void msgSuccess(String objectID) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "提交成功", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void msgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,WriteRepairsActivity.class));
    }

}
