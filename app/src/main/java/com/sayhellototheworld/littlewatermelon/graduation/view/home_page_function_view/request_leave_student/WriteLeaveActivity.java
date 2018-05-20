package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_leave_student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.Calendar;
import java.util.Date;

public class WriteLeaveActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

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
                break;
            case R.id.activity_write_leave_ll_begin_time:
                getBeginTime();
                break;
            case R.id.activity_write_leave_ll_end_time:
                getEndTime();
                break;
        }
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
            MyToastUtil.showToast("请先设计开始时间");
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

    public static void go2Activity(Context context) {
        context.startActivity(new Intent(context, WriteLeaveActivity.class));
    }

}
