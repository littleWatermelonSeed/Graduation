package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_repairs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlRequestRepairs;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.PersonalInformationActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class RequestRepairsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private ControlRequestRepairs crr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_repairs);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_request_repairs_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_request_repairs_more);
        img_more.setOnClickListener(this);
        
        recyclerView = (RecyclerView) findViewById(R.id.activity_request_repairs_recycler_view);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);
        
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_request_repairs_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void initParam() {
        crr = new ControlRequestRepairs(this,refreshLayout,recyclerView);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_request_repairs_back:
                finish();
                break;
            case R.id.activity_request_repairs_more:
                if (BmobManageUser.getCurrentUser().getSchooleKey() == null || BmobManageUser.getCurrentUser().getSchooleKey().equals("")){
                    DialogConfirm.newInstance("提示", "你还没有绑定学校,不能进行维修申请,是否现在个人信息中去绑定学校?",
                            new DialogConfirm.CancleAndOkDo() {
                                @Override
                                public void cancle() {

                                }

                                @Override
                                public void ok() {
                                    PersonalInformationActivity.startPersonalInformationActivity(RequestRepairsActivity.this);
                                }
                            }).setMargin(60)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                }else {
                    WriteRepairsActivity.go2Activity(this);
                }
                break;
        }
    }
    
    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,RequestRepairsActivity.class));
    }


}
