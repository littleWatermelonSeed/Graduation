package com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/1.
 */

public interface BmobDeletMsgDone {

    void delMsgSuc();
    void delMsgFailed(BmobException e);

}
