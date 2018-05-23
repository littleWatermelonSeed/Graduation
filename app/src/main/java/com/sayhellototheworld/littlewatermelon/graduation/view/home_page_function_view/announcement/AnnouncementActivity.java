package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.announcement;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlAnnouncement;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class AnnouncementActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    public final static int ANNOUNCEMENT_TYPE_STUDENT = 0;
    public final static int ANNOUNCEMENT_TYPE_TEACHER = 1;

    private TextView txt_back;
    private TextView txt_msg;
    private ImageView img_more;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View pop_window_view;
    private PopupWindow pop_window;
    private TextView txt_write;

    private int type;
    private ControlAnnouncement ca;
    private static MyUserBean teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_announcement);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_msg = (TextView) findViewById(R.id.activity_announcement_msg);
        txt_back = (TextView) findViewById(R.id.activity_announcement_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_announcement_more);
        img_more.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.activity_announcement_recycler_view);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_announcement_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作

        pop_window_view= LayoutInflater.from(this).inflate(R.layout.pop_window_announcement, null, false);
        txt_write = (TextView) pop_window_view.findViewById(R.id.pop_window_announcement_more_write);
        txt_write.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type",-1);
        if (type == ANNOUNCEMENT_TYPE_STUDENT){
            ca = new ControlAnnouncement(this,type,teacher,refreshLayout,recyclerView);
        }else if (type == ANNOUNCEMENT_TYPE_TEACHER){
            ca = new ControlAnnouncement(this,type, BmobManageUser.getCurrentUser(),refreshLayout,recyclerView);
        }
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        if (type == ANNOUNCEMENT_TYPE_STUDENT){
            img_more.setVisibility(View.GONE);
            txt_msg.setText("老师发布的公告");
        }else if (type == ANNOUNCEMENT_TYPE_TEACHER){
            img_more.setVisibility(View.VISIBLE);
            txt_msg.setText("我发布的公告");
        }
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_announcement_back:
                finish();
                break;
            case R.id.activity_announcement_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.pop_window_announcement_more_write:
                pop_window.dismiss();
                WriteAnnouncementActivity.go2Activity(this);
                break;
        }
    }

    public static void go2Activity(Context context,int type){
        Intent intent = new Intent(context,AnnouncementActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void go2Activity(Context context, MyUserBean t){
        Intent intent = new Intent(context,AnnouncementActivity.class);
        intent.putExtra("type",ANNOUNCEMENT_TYPE_STUDENT);
        teacher = t;
        context.startActivity(intent);
    }


}
