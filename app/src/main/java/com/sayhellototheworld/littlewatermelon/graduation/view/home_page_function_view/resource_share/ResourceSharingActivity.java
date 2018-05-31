package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share;

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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlResourceShare;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResourceSharingActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private TextView txt_msg;
    private TextView txt_school_name;
    private ImageView img_more;
    private TextView txt_no_msg;
    private SmartRefreshLayout refreshLayout;
    private View pop_window_view;
    private TextView txt_write_flea;
    private TextView txt_own_flea;
    private TextView txt_own_flea_collect;
    private PopupWindow pop_window;
    private RecyclerView mRecyclerView;
    private RelativeLayout rl_other_page;
    private TextView txt_other_user_name;
    private TextView txt_other_resource_num;
    private CircleImageView img_head;

    public final static int RESOURCE_SHARE_DEL_CODE = 20;
    public final static int TYPE_RESOURCE_SHARE_HOME = 0;
    public final static int TYPE_RESOURCE_SHARE_OWN = 1;
    public final static int TYPE_RESOURCE_SHARE_OTHER = 2;
    public final static int TYPE_RESOURCE_SHARE_COLLECT = 3;

    private int resourceType;
    private static MyUserBean other_user;
    private ControlResourceShare crs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_resource_sharing);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_msg = (TextView) findViewById(R.id.activity_resource_sharing_msg);
        txt_back = (TextView) findViewById(R.id.activity_resource_sharing_back);
        txt_school_name = (TextView) findViewById(R.id.activity_resource_sharing_school_name);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_resource_sharing_more);
        img_more.setOnClickListener(this);
        txt_no_msg = (TextView) findViewById(R.id.activity_resource_sharing_no_msg);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_resource_sharing_smart_refresh);
        pop_window_view= LayoutInflater.from(this).inflate(R.layout.pop_window_resource_sharing_more, null, false);
        txt_write_flea = (TextView) pop_window_view.findViewById(R.id.pop_window_resource_sharing_more_write);
        txt_write_flea.setOnClickListener(this);
        txt_own_flea = (TextView) pop_window_view.findViewById(R.id.pop_window_resource_sharing_more_own);
        txt_own_flea.setOnClickListener(this);
        txt_own_flea_collect = (TextView) pop_window_view.findViewById(R.id.pop_window_resource_sharing_more_collect);
        txt_own_flea_collect.setOnClickListener(this);
        rl_other_page = (RelativeLayout) findViewById(R.id.activity_resource_sharing_other_page);
        txt_other_user_name = (TextView) findViewById(R.id.activity_resource_sharing_name);
        txt_other_resource_num = (TextView) findViewById(R.id.activity_resource_sharing_flea_num);
        img_head = (CircleImageView) findViewById(R.id.activity_resource_sharing_head_portrait);
        img_head.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_resource_sharing_recycler_view);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void initParam() {
        resourceType = getIntent().getIntExtra("resourceType",-1);
        crs = new ControlResourceShare(this,other_user,refreshLayout,mRecyclerView,resourceType,txt_other_resource_num);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        showTop();
        refreshLayout.autoRefresh();
    }

    private void showTop(){
        switch (resourceType){
            case TYPE_RESOURCE_SHARE_HOME:
                txt_school_name.setVisibility(View.VISIBLE);
                txt_school_name.setText("全国");
                break;
            case TYPE_RESOURCE_SHARE_OTHER:
                rl_other_page.setVisibility(View.VISIBLE);
                img_more.setVisibility(View.GONE);
                txt_msg.setText(other_user.getNickName() + "的资源");
                txt_other_resource_num.setText("Ta贡献的资源 0");
                txt_other_user_name.setText(other_user.getNickName());
                if (other_user.getHeadPortrait() != null && !other_user.getHeadPortrait().getUrl().equals("")){
                    Glide.with(this)
                            .load(other_user.getHeadPortrait().getUrl())
                            .dontAnimate()
                            .into(img_head);
                }else {
                    Glide.with(this)
                            .load(R.drawable.head_log1)
                            .dontAnimate()
                            .into(img_head);
                }
                break;
            case TYPE_RESOURCE_SHARE_OWN:
                img_more.setVisibility(View.GONE);
                txt_msg.setText("我共享的资源");
                break;
            case TYPE_RESOURCE_SHARE_COLLECT:
                img_more.setVisibility(View.GONE);
                txt_msg.setText("我收藏的资源");
                break;
        }
    }

    public static void go2Activity(Context context,int resourceType){
        Intent intent = new Intent(context,ResourceSharingActivity.class);
        intent.putExtra("resourceType",resourceType);
        context.startActivity(intent);
    }

    public static void go2Activity(Context context,MyUserBean userBean){
        Intent intent = new Intent(context,ResourceSharingActivity.class);
        intent.putExtra("resourceType",TYPE_RESOURCE_SHARE_OTHER);
        other_user = userBean;
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_resource_sharing_back:
                finish();
                break;
            case R.id.activity_resource_sharing_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.pop_window_resource_sharing_more_write:
                if (BmobManageUser.getCurrentUser().getSchooleKey() == null ||
                        BmobManageUser.getCurrentUser().getSchooleKey().equals("")){
                    pop_window.dismiss();
                    MyToastUtil.showToast("请先去个人设置中设置自己的学校");
                    return;
                }
                pop_window.dismiss();
                WriteResourceShareActivity.go2Activity(this);
                break;
            case R.id.pop_window_resource_sharing_more_own:
                pop_window.dismiss();
                ResourceSharingActivity.go2Activity(this,TYPE_RESOURCE_SHARE_OWN);
                break;
            case R.id.pop_window_resource_sharing_more_collect:
                pop_window.dismiss();
                ResourceSharingActivity.go2Activity(this,TYPE_RESOURCE_SHARE_COLLECT);
                break;
            case R.id.activity_resource_sharing_head_portrait:
                UserDetailsActivity.go2Activity(this,other_user.getObjectId());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOURCE_SHARE_DEL_CODE){
            if (resultCode == RESULT_OK){
                refreshLayout.autoRefresh();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        other_user = null;
    }
    
    
}
