package com.sayhellototheworld.littlewatermelon.graduation.presenter.message_function;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.adapter.MessageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForumComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.MessageFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/15.
 */

public class ControlMessage implements OnRefreshListener{

    private int userType;
    private Context context;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private MyUserBean user;
    private int[] noReadNum;
    private int nowNum = 0;
    private int num;
    private MessageAdapter adapter;
    private boolean refreshIng = false;
    private int totalNoReadNum = 0;

    public ControlMessage(Context context,int userType,SmartRefreshLayout refreshLayout,RecyclerView recyclerView){
        this.context = context;
        this.userType = userType;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;
        refreshLayout.setOnRefreshListener(this);
        user = BmobManageUser.getCurrentUser();
        if (userType == MessageFragment.MESSAGE_TYPE_STUDENT){
            noReadNum = new int[7];
            num = 7;
        }else if (userType == MessageFragment.MESSAGE_TYPE_TEACHER){
            noReadNum = new int[7];
            num = 7;
        }else if (userType == MessageFragment.MESSAGE_TYPE_REPAIRS){
            noReadNum = new int[6];
            num = 6;
        }
    }

    public void beginNoReadNum(){
        switch (userType){
            case MessageFragment.MESSAGE_TYPE_STUDENT:
                getStudentNoReadNum();
                break;
            case MessageFragment.MESSAGE_TYPE_TEACHER:
                getTeacherNoReadNum();
                break;
            case MessageFragment.MESSAGE_TYPE_REPAIRS:
                getRepairNoReadNum();
                break;
        }
    }

    private void getStudentNoReadNum(){
        commonNoReadNum();
        getSBindNoReadNum();
    }

    private void getTeacherNoReadNum(){
        commonNoReadNum();
        getTBindNoReadNum();
    }

    private void getRepairNoReadNum(){
        commonNoReadNum();
    }

    private void commonNoReadNum(){
        getLostNoReadNum();
        getFleaNoReadNum();
        getShareNoReadNum();
        getFourmNoReadNum();
        getFriendRequestNoReadNum();
        getMyFriendRequestNoReadNum();
    }

    private void getLostNoReadNum(){
        BmobManageLostComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[0] = integer;
                addNum(true);
                Log.i("niyuanjie","失物招领消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getFleaNoReadNum(){
        BmobManageFleaComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[1] = integer;
                addNum(true);
                Log.i("niyuanjie","跳蚤市场消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getShareNoReadNum(){
        BmobManageResourceComment.getManager().queryNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[2] = integer;
                addNum(true);
                Log.i("niyuanjie","资源共享消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getFourmNoReadNum(){
        BmobManageForumComment.getManager().queryNoReadCount(new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[3] = integer;
                addNum(true);
                Log.i("niyuanjie","同学圈消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getFriendRequestNoReadNum(){
        BmobManageRequestFriend.getManager().queryFriendNoReadCount(BmobManageUser.getCurrentUser(),new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[4] = integer;
                addNum(true);
                Log.i("niyuanjie","同学圈消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getMyFriendRequestNoReadNum(){
        BmobManageRequestFriend.getManager().queryUserNoReadCount(BmobManageUser.getCurrentUser(),new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[5] = integer;
                addNum(true);
                Log.i("niyuanjie","同学圈消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getSBindNoReadNum(){
        BmobManageTeacher.getManager().querySNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[6] = integer;
                addNum(true);
                Log.i("niyuanjie","学生绑定消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void getTBindNoReadNum(){
        BmobManageTeacher.getManager().queryTNoReadCount(user, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                totalNoReadNum = totalNoReadNum + integer;
                noReadNum[6] = integer;
                addNum(true);
                Log.i("niyuanjie","老师绑定消息数量 = " + integer);
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(context,e);
                finishSmart(false);
            }
        });
    }

    private void finishSmart(boolean success) {
        refreshIng = false;
        refreshLayout.finishRefresh(success);
        nowNum = 0;
    }

    private void addNum(boolean success){
        synchronized (this){
            if (!refreshIng){
                return;
            }
            nowNum++;
            Log.i("niyuanjie","当前数量 = " + nowNum);
            if (nowNum == num){
                finishSmart(success);
                adapter = new MessageAdapter(context,userType,noReadNum, refreshLayout);
                recyclerView.setAdapter(adapter);
                CenterActivity.setNewMsgNoRead(totalNoReadNum);
                Log.i("niyuanjie","所有信息查询完成 当前数量 = " + nowNum);
            }
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshIng = true;
        beginNoReadNum();
    }

}
