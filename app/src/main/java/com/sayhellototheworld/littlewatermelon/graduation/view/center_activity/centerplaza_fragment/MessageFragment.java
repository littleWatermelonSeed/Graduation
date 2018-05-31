package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.MessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function.ControlMessage;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.QueryFriendActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class MessageFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private LinearLayout parentLayout;

    private ImageView img_plus;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    public final static int MESSAGE_TYPE_STUDENT = 0;
    public final static int MESSAGE_TYPE_TEACHER = 1;
    public final static int MESSAGE_TYPE_REPAIRS = 2;

    private MessageAdapter adapter;
    private int[] noReadNum;
    private int userType;
    private static boolean create = false;
    private ControlMessage cm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_message, container, false);
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
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_message_parent);
        img_plus = (ImageView) mView.findViewById(R.id.fragment_message_more);
        img_plus.setOnClickListener(this);
        recyclerView = (RecyclerView) mView.findViewById(R.id.fragment_message_recycler_view);
        refreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.fragment_message_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        refreshLayout.setEnableLoadMore(false);

    }

    private void initParam(){
        String userRole = BmobManageUser.getCurrentUser().getRole();
        if (userRole.equalsIgnoreCase("s")){
            userType = MESSAGE_TYPE_STUDENT;
        }else if (userRole.equalsIgnoreCase("t")){
            userType = MESSAGE_TYPE_TEACHER;
        }else if (userRole.equalsIgnoreCase("r")){
            userType = MESSAGE_TYPE_REPAIRS;
        }
        cm = new ControlMessage(getContext(),userType,refreshLayout,recyclerView);
    }

    private void initShow(){
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
        LinearLayoutManager llManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llManager);
        refreshLayout.autoRefresh();
    }

    public static void asyncMessageFragment(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
        create = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_message_more:
                QueryFriendActivity.go2Activity(getContext());
                break;
        }
    }
}
