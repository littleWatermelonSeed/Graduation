package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/11.
 */

public interface ResetPasswordBySmsDo {

    void resetSuccess();
    void resetFail(BmobException e);

}
