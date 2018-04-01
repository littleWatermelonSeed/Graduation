package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.widget.Toast;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;

/**
 * Created by 123 on 2017/8/21.
 */

public final class MyToastUtil {

    public final static int LONG = 0;
    public final static int SHORT = 1;

    public static void showToast(String showMessage){
//        Toast toast = null;
//        if (toast == null){
//            toast = new Toast(SPApplication.getAppContext());
//            toast.setText(showMessage);
//        }else {
//            toast.setText(showMessage);
//        }
        Toast.makeText(SchoolApp.getAppContext(),showMessage,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String showMessage,int time){
        if(time == LONG){
            Toast.makeText(SchoolApp.getAppContext(),showMessage,Toast.LENGTH_LONG).show();
        }else if(time == SHORT){
            Toast.makeText(SchoolApp.getAppContext(),showMessage,Toast.LENGTH_SHORT).show();
        }
    }

}
