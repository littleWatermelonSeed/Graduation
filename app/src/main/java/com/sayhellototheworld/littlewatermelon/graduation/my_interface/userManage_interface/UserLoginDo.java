package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/7.
 */

public interface UserLoginDo {

    void loginSuccess(MyUserBean myUserBean);
    void loginFail(BmobException ex);

}
