package com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/4/29.
 */

public interface BmobQueryDone<T> {

    void querySuccess(List<T> data);
    void queryFailed(BmobException e);

}
