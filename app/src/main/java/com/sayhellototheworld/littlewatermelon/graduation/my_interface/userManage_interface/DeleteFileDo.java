package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/20.
 */

public interface DeleteFileDo {

    void deleteSuccess();
    void deleteFail(BmobException e);

}
