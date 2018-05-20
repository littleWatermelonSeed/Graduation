package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_leave_student;

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
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class RequestLeaveActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View pop_window_view;
    private TextView txt_write_leave;
    private PopupWindow pop_window;

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

    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
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
                pop_window.dismiss();
                WriteLeaveActivity.go2Activity(this);
                break;
        }
    }
}
