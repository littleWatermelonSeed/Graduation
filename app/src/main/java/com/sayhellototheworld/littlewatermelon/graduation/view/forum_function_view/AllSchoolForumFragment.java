package com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


public class AllSchoolForumFragment extends Fragment {

    private View mView;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private ControlForum cf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_all_school_forum, container, false);
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
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.fragment_all_school_forum_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);

        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_all_school_forum_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
    }

    private void initParam(){
        cf = new ControlForum(getContext(),ControlForum.FORUM_TYPE_ALL_SCHOOL,refreshLayout,recyclerView);
    }

    private void initShow(){
        refreshLayout.autoRefresh();
    }

}
