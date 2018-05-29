package com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/29.
 */

public class ControlUserDetails implements BmobQueryDone<MyUserBean>{

    private Context context;
    private String userID;
    private UserDetailsActivity uda;

    private MyUserBean user;
    private BmobManageUser manager;

    public ControlUserDetails(Context context, String userID,UserDetailsActivity uda) {
        this.context = context;
        this.userID = userID;
        this.uda = uda;
        manager = new BmobManageUser(context);
        queryUser();
    }

    public void queryUser(){
        manager.queryByID(userID,this);
    }

    @Override
    public void querySuccess(List<MyUserBean> data) {
        uda.showMsg(data.get(0));
        uda.setUser(data.get(0));
        uda.setDataStatue(1);
        user = data.get(0);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(context,e);
        uda.setDataStatue(-1);
    }

}
