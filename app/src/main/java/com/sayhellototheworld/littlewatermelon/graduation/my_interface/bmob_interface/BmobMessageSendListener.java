package com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/6/1.
 */

public interface BmobMessageSendListener {

    void done(BmobIMMessage msg, BmobException e);

}
