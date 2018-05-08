package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.ResourceShareAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCollectBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResource;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceCollect;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
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

import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_COLLECT;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_HOME;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OWN;

/**
 * Created by 123 on 2018/5/6.
 */

public class ControlResourceShare extends FindListener<ResourceShareBean> implements OnLoadMoreListener, OnRefreshListener,
        BmobQueryDone<ResourceCollectBean>,QueryCountListener {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private MyUserBean other_user;
    private TextView txt_other_resource_num;

    private BmobManageResource manager;
    private ResourceShareAdapter adapter;
    private List<ResourceShareBean> resourceData;

    private boolean loading = false;
    private int nowSkip = 0;
    private int resourceType;

    public ControlResourceShare(Context context, MyUserBean other_user, SmartRefreshLayout refreshLayout,
                                RecyclerView mRecyclerView,int resourceType,TextView txt_other_resource_num){
        this.context = context;
        this.other_user = other_user;
        this.resourceType = resourceType;
        this.refreshLayout = refreshLayout;
        this.mRecyclerView = mRecyclerView;
        this.txt_other_resource_num = txt_other_resource_num;
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setOnLoadMoreListener(this);
        manager = BmobManageResource.getManager();
        resourceData = new ArrayList<>();
        adapter = new ResourceShareAdapter(context,resourceData,resourceType);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        this.mRecyclerView.setLayoutManager(llManager);
        this.mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        switch (resourceType){
            case TYPE_RESOURCE_SHARE_HOME:
                manager.queryAll(this,nowSkip);
                break;
            case TYPE_RESOURCE_SHARE_OWN:
                manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
                break;
            case TYPE_RESOURCE_SHARE_OTHER:
                manager.queryByUser(other_user,this,nowSkip);
                break;
            case TYPE_RESOURCE_SHARE_COLLECT:
                BmobManageResourceCollect.getManager().queryMsgByUser(BmobManageUser.getCurrentUser(),nowSkip,this);
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        switch (resourceType){
            case TYPE_RESOURCE_SHARE_HOME:
                manager.queryAll(this,nowSkip);
                break;
            case TYPE_RESOURCE_SHARE_OWN:
                manager.queryByUser(BmobManageUser.getCurrentUser(),this,nowSkip);
                break;
            case TYPE_RESOURCE_SHARE_OTHER:
                manager.queryByUser(other_user,this,nowSkip);
                manager.queryCount(other_user,this);
                break;
            case TYPE_RESOURCE_SHARE_COLLECT:
                BmobManageResourceCollect.getManager().queryMsgByUser(BmobManageUser.getCurrentUser(),nowSkip,this);
                break;
        }
    }

    @Override
    public void querySuccess(List<ResourceCollectBean> data) {
        if (!loading){
            resourceData.clear();
            for (ResourceCollectBean f:data){
                resourceData.add(f.getResourceShareBean());
            }
        }else {
            for (ResourceCollectBean f:data){
                resourceData.add(f.getResourceShareBean());
            }
        }

        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }
        adapter.notifyDataSetChanged();
        finishSmart(true);
        nowSkip++;
    }

    @Override
    public void queryFailed(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(context,e);
    }

    @Override
    public void done(List<ResourceShareBean> list, BmobException e) {
        if (e == null){
            if (!loading){
                resourceData.clear();
                resourceData.addAll(list);
            }else {
                resourceData.addAll(list);
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
        txt_other_resource_num.setText("Ta贡献的资源 " + integer);
    }

    @Override
    public void queryCountFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(context,e);
    }

}
