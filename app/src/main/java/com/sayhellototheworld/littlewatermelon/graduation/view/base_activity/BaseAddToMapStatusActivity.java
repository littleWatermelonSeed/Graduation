package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by 123 on 2017/8/21.
 */

public class BaseAddToMapStatusActivity extends BaseStatusActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityManager myActivityManager = MyActivityManager.getDestoryed();
        myActivityManager.addActivityToMap(getLocalClassName(),this);
    }
}
