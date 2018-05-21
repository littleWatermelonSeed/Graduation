package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestLeaveBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestLeave;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

public class WriteLeaveActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,BmobSaveMsgWithoutImg{

    private TextView txt_back;
    private TextView txt_release;
    private EditText edt_reason;
    private EditText edt_student_name;
    private EditText edt_class_num;
    private LinearLayout ll_begin_time;
    private LinearLayout ll_end_time;
    private TextView txt_begin_time;
    private TextView txt_end_time;

    private String reason;
    private String studentName;
    private String classNum;
    private String beginTime;
    private String endTime;

    private Date beginDate;
    private Date endDate;
    private RequestLeaveBean bean;
    private static MyUserBean teacher;

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
        setContentView(R.layout.activity_write_leave);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
         txt_back = (TextView) findViewById(R.id.activity_write_leave_back);
         txt_back.setOnClickListener(this);
         txt_release = (TextView) findViewById(R.id.activity_write_leave_release);
         txt_release.setOnClickListener(this);
         edt_reason = (EditText) findViewById(R.id.activity_write_leave_edt_reason);
         edt_student_name = (EditText) findViewById(R.id.activity_write_leave_edt_student_name);
         edt_class_num = (EditText) findViewById(R.id.activity_write_leave_edt_class);
         ll_begin_time = (LinearLayout) findViewById(R.id.activity_write_leave_ll_begin_time);
         ll_begin_time.setOnClickListener(this);
         ll_end_time = (LinearLayout) findViewById(R.id.activity_write_leave_ll_end_time);
         ll_end_time.setOnClickListener(this);
         txt_begin_time = (TextView) findViewById(R.id.activity_write_leave_txt_begin_time);
         txt_end_time = (TextView) findViewById(R.id.activity_write_leave_txt_end_time);
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
            case R.id.activity_write_leave_back:
                finish();
                break;
            case R.id.activity_write_leave_release:
                if (!getData()){
                    return;
                }
                releaseLeave();
                break;
            case R.id.activity_write_leave_ll_begin_time:
                getBeginTime();
                break;
            case R.id.activity_write_leave_ll_end_time:
                getEndTime();
                break;
        }
    }

    private boolean getData(){
        reason = edt_reason.getText().toString().trim();
        studentName = edt_student_name.getText().toString().trim();
        classNum = edt_class_num.getText().toString().trim();
        if (reason == null || reason.equals("")){
            MyToastUtil.showToast("原因不能为空");
            return false;
        }
        if (studentName == null || studentName.equals("")){
            MyToastUtil.showToast("个人姓名不能为空");
            return false;
        }
        if (classNum == null || classNum.equals("")){
            MyToastUtil.showToast("班级不能为空");
            return false;
        }
        if (beginTime == null || beginTime.equals("")){
            MyToastUtil.showToast("开始时间不能为空");
            return false;
        }
        if (endTime == null || endTime.equals("")){
            MyToastUtil.showToast("归假时间不能为空");
            return false;
        }

        bean = new RequestLeaveBean();
        bean.setStatue(0);
        bean.setTeahcer(teacher);
        bean.setReason(reason);
        bean.setStudent(BmobManageUser.getCurrentUser());
        bean.setStudenName(studentName);
        bean.setClassNum(classNum);
        bean.setBeginTime(beginTime);
        bean.setEndTime(endTime);
        bean.setReleaseTime(new BmobDate(new Date()));
        bean.settRead(false);

        return true;
    }

    private void releaseLeave(){
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("发布中...");
                        BmobManageRequestLeave.getManager().uploadMsgWithoutImg(bean,WriteLeaveActivity.this);
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

    private void getBeginTime() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (date.before(new Date()) && !SysUtil.sameDate(date,new Date())){
                    MyToastUtil.showToast("开始时间必须在今天或今天之后，请重新选择开始时间");
                    return;
                }
                beginDate = date;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                beginTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                txt_begin_time.setText(beginTime);
                txt_end_time.setText("");
                endTime = "";
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void getEndTime() {
        if (beginTime == null){
            MyToastUtil.showToast("请先选择开始时间");
            return;
        }
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                endDate = date;
                if (endDate.before(beginDate)){
                    MyToastUtil.showToast("归假时间必须在开始时间之后，请重新选择归假时间");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                endTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                txt_end_time.setText(endTime);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    public static void go2Activity(Context context,MyUserBean t) {
        teacher = t;
        context.startActivity(new Intent(context, WriteLeaveActivity.class));
    }

}
