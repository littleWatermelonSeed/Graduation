package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.AnnouncementAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.AnnouncementBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageAnnouncement;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/22.
 */

public class ControlAnnouncement  implements OnLoadMoreListener,OnRefreshListener,BmobQueryDone<AnnouncementBean> {

    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private int type;
    private MyUserBean teacher;

    private BmobManageAnnouncement manager;
    private AnnouncementAdapter adapter;
    private List<AnnouncementBean> annData;

    private boolean loading = false;
    private int nowSkip = 0;

    public ControlAnnouncement(Context context, int type, MyUserBean teacher, SmartRefreshLayout refreshLayout, RecyclerView recyclerView) {
        this.context = context;
        this.type = type;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        this.teacher = teacher;

        this.refreshLayout.setOnLoadMoreListener(this);
        this.refreshLayout.setOnRefreshListener(this);

        manager = BmobManageAnnouncement.getManager();
        annData = new ArrayList<>();
        adapter = new AnnouncementAdapter(context,annData,this.type);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    public void querySuccess(List<AnnouncementBean> data) {
        if (data.size() == 0){
            MyToastUtil.showToast("到底啦,没有更多数据啦~");
        }else {
            Log.i("niyuanjie","查询到" + data.size() + "条数据  " + "第一条数据为：" + data.get(0).getTitle());
            annData.addAll(data);
            adapter.notifyDataSetChanged();
            nowSkip++;
        }
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(context,e);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        manager.queryByUser(teacher,nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        annData.clear();
        manager.queryByUser(teacher,nowSkip,this);
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

}
