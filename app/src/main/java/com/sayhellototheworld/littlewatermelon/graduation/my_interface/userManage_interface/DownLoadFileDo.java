package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/16.
 */

public interface DownLoadFileDo {

    void onStart();

    void downLoadSuccess(String savePath);

    void downLoadFail(BmobException e);

    void onProgress(Integer value, long newworkSpeed);

}
