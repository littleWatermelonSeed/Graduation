package com.sayhellototheworld.littlewatermelon.graduation.view.im_view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class ChatFragment extends Fragment {

    private View mView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_chat, container, false);
            init();
        }
        return mView;
    }

    private void init(){
        initWidget();
        initParam();
        initShow();
    }

    private void initWidget(){
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.fragment_chat_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作

        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_chat_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }

    private void initParam(){

    }

    private void initShow(){
//        refreshLayout.autoRefresh();
    }

}
