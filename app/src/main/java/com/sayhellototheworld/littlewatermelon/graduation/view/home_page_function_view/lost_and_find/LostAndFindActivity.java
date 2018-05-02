package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.lost_and_find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlLostAndFind;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.MsgDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class LostAndFindActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private TextView txt_msg;
    private TextView txt_school_name;
    private ImageView img_more;
    private TextView txt_no_msg;
    private LinearLayout ll_search;
    private SmartRefreshLayout refreshLayout;
    private View pop_window_view;
    private TextView txt_write_lost;
    private TextView txt_own_lost;
    private PopupWindow pop_window;
    private RecyclerView mRecyclerView;
    private ControlLostAndFind cla;

    private boolean privateLost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lost_and_find);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_msg = (TextView) findViewById(R.id.activity_lost_and_find_msg);
        txt_back = (TextView) findViewById(R.id.activity_lost_and_find_back);
        txt_school_name = (TextView) findViewById(R.id.activity_lost_and_find_school_name);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_lost_and_find_more);
        img_more.setOnClickListener(this);
        txt_no_msg = (TextView) findViewById(R.id.activity_lost_and_find_no_msg);
        ll_search = (LinearLayout) findViewById(R.id.activity_lost_and_find_search);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_lost_and_find_smart_refresh);
        ll_search.setOnClickListener(this);
        pop_window_view= LayoutInflater.from(this).inflate(R.layout.pop_window_lost_and_find_view, null, false);
        txt_write_lost = (TextView) pop_window_view.findViewById(R.id.pop_window_lost_and_find_view_write);
        txt_write_lost.setOnClickListener(this);
        txt_own_lost = (TextView) pop_window_view.findViewById(R.id.pop_window_lost_and_find_view_own);
        txt_own_lost.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_lost_and_find_recycler_view);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void initParam() {
        Intent intent = getIntent();
        privateLost = intent.getBooleanExtra("privateLost",false);
        cla = new ControlLostAndFind(this,refreshLayout,mRecyclerView,privateLost);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    protected void initShow() {
        refreshLayout.autoRefresh();
        if (privateLost){
            ll_search.setVisibility(View.GONE);
            img_more.setVisibility(View.GONE);
            txt_msg.setText("我的发布");
        }else {
            txt_school_name.setVisibility(View.VISIBLE);
            txt_school_name.setText(BmobManageUser.getCurrentUser().getSchoolName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_lost_and_find_back:
                finish();
                break;
            case R.id.activity_lost_and_find_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.activity_lost_and_find_search:
                SearchLostActivity.go2Activity(this);
                break;
            case R.id.pop_window_lost_and_find_view_write:
                if (BmobManageUser.getCurrentUser().getSchooleKey() == null ||
                        BmobManageUser.getCurrentUser().getSchooleKey().equals("")){
                    pop_window.dismiss();
                    MyToastUtil.showToast("请先去个人设置中设置自己的学校");
                    return;
                }
                pop_window.dismiss();
                WriteLostActivity.go2Activity(this);
                break;
            case R.id.pop_window_lost_and_find_view_own:
                pop_window.dismiss();
                LostAndFindActivity.go2Activity(this,true);
                break;
        }
    }

    public static void go2Activity(Context context,boolean privateLost){
        Intent intent = new Intent(context,LostAndFindActivity.class);
        intent.putExtra("privateLost",privateLost);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MsgDetailsActivity.MSG_DETAILS_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                refreshLayout.autoRefresh();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cla.notifData();
    }
}
