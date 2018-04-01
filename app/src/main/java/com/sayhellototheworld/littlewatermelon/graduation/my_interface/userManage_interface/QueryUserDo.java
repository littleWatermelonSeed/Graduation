package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/11.
 */

public interface QueryUserDo {

    void querySuccess(List<MyUserBean> object);
    void queryFail(BmobException e);

}
