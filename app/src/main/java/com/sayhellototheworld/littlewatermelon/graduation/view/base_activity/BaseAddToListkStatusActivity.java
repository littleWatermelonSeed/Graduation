package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by 123 on 2017/8/21.
 */

public class BaseAddToListkStatusActivity extends BaseStatusActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager myActivityManager = MyActivityManager.getDestoryed();
        myActivityManager.addActivityToList(this);
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initShow() {

    }

}
