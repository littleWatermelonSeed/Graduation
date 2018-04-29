package com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/4/27.
 */

public interface BmobSaveMsgWithoutImg {

    void msgSuccess(String objectID);


    void msgFailed(BmobException e);
}
