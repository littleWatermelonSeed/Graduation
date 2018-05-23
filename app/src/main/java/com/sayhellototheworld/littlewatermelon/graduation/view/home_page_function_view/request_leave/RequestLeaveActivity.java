package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_leave;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlRequestLeave;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.TeacherMessageActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class RequestLeaveActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View pop_window_view;
    private TextView txt_write_leave;
    private PopupWindow pop_window;

    private ControlRequestLeave crl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_leave);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_request_leave_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_request_leave_more);
        img_more.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_request_leave_recycler_view);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_request_leave_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作

        pop_window_view= LayoutInflater.from(this).inflate(R.layout.pop_window_request_leave_more, null, false);
        txt_write_leave = (TextView) pop_window_view.findViewById(R.id.pop_window_request_leave_more_write);
        txt_write_leave.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        crl = new ControlRequestLeave(this,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,RequestLeaveActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_request_leave_back:
                finish();
                break;
            case R.id.activity_request_leave_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.pop_window_request_leave_more_write:
                writeLeave();
                break;
        }
    }

    private void writeLeave() {
        pop_window.dismiss();
        BmobManageTeacher.getManager().queryBindedByStudent(BmobManageUser.getCurrentUser(), new BmobQueryDone<TeacherBean>() {
            @Override
            public void querySuccess(List<TeacherBean> data) {
                if (data.size() > 0){
                    WriteLeaveActivity.go2Activity(RequestLeaveActivity.this,data.get(0).getTeacher());
                }else {
                    DialogConfirm.newInstance("提示", "你还没有绑定教师,或者你的绑定申请还没有通过,是否前去绑定教师/查看绑定申请状态?",
                            new DialogConfirm.CancleAndOkDo() {
                                @Override
                                public void cancle() {

                                }

                                @Override
                                public void ok() {
                                    TeacherMessageActivity.go2Activity(RequestLeaveActivity.this);
                                }
                            }).setMargin(60)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(RequestLeaveActivity.this,e);
            }
        });
    }

}
