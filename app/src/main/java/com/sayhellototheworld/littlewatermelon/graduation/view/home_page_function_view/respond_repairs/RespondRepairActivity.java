package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.respond_repairs;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlResponRepair;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class RespondRepairActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    public final static int REPAIRS_TYPE_ALL = 0;
    public final static int REPAIRS_TYPE_OWN = 1;

    private TextView txt_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView txt_school_name;
    private ImageView img_more;
    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_own;

    private int type;
    private ControlResponRepair crr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_answer_repair);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_school_name = (TextView) findViewById(R.id.activity_respond_repairs_school_name);
        img_more = (ImageView) findViewById(R.id.activity_respond_repairs_more);
        img_more.setOnClickListener(this);
        txt_back = (TextView) findViewById(R.id.activity_respond_repairs_back);
        txt_back.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.activity_respond_repairs_recycler_view);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_respond_repairs_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作

        pop_window_view= LayoutInflater.from(this).inflate(R.layout.pop_window_request_repairs_more, null, false);
        txt_own = (TextView) pop_window_view.findViewById(R.id.pop_window_request_repairs_more);
        txt_own.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type",-1);
        crr = new ControlResponRepair(this,refreshLayout,recyclerView,type);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        txt_school_name.setText(BmobManageUser.getCurrentUser().getSchoolName());
        if (type == REPAIRS_TYPE_ALL){
            img_more.setVisibility(View.VISIBLE);
        }else if (type == REPAIRS_TYPE_OWN){
            img_more.setVisibility(View.GONE);
        }
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_respond_repairs_back:
                finish();
                break;
            case R.id.activity_respond_repairs_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.pop_window_request_repairs_more:
                pop_window.dismiss();
                RespondRepairActivity.go2Activity(this,REPAIRS_TYPE_OWN);
                break;
        }
    }

    public static void go2Activity(Context context,int type){
        Intent intent = new Intent(context,RespondRepairActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

}
