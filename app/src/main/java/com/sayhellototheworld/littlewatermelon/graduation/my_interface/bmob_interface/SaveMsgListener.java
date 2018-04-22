package com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/4/23.
 */

public interface SaveMsgListener {

    void msgSuccess(String objectID);

    void msgFailed(BmobException e);

    void imgSuccess(List<BmobFile> list, List<String> urls);

    void imgFailed(int errorCode, String errorMsg);

    //1、curIndex--表示当前第几个文件正在上传
    //2、curPercent--表示当前上传文件的进度值（百分比）
    //3、total--表示总的上传文件数
    //4、totalPercent--表示总的上传进度（百分比）
    void onProgress(int curIndex, int curPercent, int total,int totalPercent);

}
