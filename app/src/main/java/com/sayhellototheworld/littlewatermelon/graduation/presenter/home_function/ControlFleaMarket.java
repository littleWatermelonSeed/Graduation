package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.FleaMarketAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarkBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaMark;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_HOME;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OWN;

/**
 * Created by 123 on 2018/5/1.
 */

public class ControlFleaMarket extends FindListener<FleaMarkBean> implements OnLoadMoreListener, OnRefreshListener ,QueryCountListener{

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private TextView txt_other_flea_num;
    private MyUserBean other_user;

    private BmobManageFleaMark manager;
    private FleaMarketAdapter adapter;
    private List<FleaMarkBean> fleaData;

    private boolean loading = false;
    private int nowSkip = 0;
    private int fleaMarkType;

    public ControlFleaMarket(Context context,MyUserBean other_user, SmartRefreshLayout refreshLayout, RecyclerView mRecyclerView,TextView txt_other_flea_num,int fleaMarkType){
        this.context = context;
        this.other_user = other_user;
        this.fleaMarkType = fleaMarkType;
        this.txt_other_flea_num =txt_other_flea_num;
        this.refreshLayout = refreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        manager = BmobManageFleaMark.getManager();
        fleaData = new ArrayList<>();
        adapter = new FleaMarketAdapter(context,fleaData,fleaMarkType);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.mRecyclerView.setLayoutManager(llManager);
        this.mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        switch (fleaMarkType){
            case TYPE_FLEA_MARK_HOME:
                manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
                break;
            case TYPE_FLEA_MARK_OWN:
                manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
                break;
            case TYPE_FLEA_MARK_OTHER:
                manager.queryByUser(other_user,this,nowSkip);
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        switch (fleaMarkType){
            case TYPE_FLEA_MARK_HOME:
                manager.queryBySchoolKey(BmobManageUser.getCurrentUser().getSchooleKey(),this,nowSkip);
                break;
            case TYPE_FLEA_MARK_OWN:
                manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
                break;
            case TYPE_FLEA_MARK_OTHER:
                manager.queryByUser(other_user,this,nowSkip);
                manager.queryCount(other_user,this);
                break;
        }
    }

    @Override
    public void done(List<FleaMarkBean> list, BmobException e) {
        if (e == null){
            if (!loading){
                fleaData.clear();
                fleaData.addAll(list);
            }else {
                fleaData.addAll(list);
            }

            if (list.size() == 0){
                MyToastUtil.showToast("到底啦,没有更多数据啦~");
            }
            adapter.notifyDataSetChanged();
            finishSmart(true);
            nowSkip++;
        }else {
            finishSmart(false);
            BmobExceptionUtil.dealWithException(context,e);
        }
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    @Override
    public void queryCountSuc(Integer integer) {
        txt_other_flea_num.setText("Ta共有" + integer + "件商品");
    }

    @Override
    public void queryCountFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(context,e);
    }
}
