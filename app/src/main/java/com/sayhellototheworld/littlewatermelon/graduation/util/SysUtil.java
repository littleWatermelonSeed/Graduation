package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.net.Uri;
import android.util.DisplayMetrics;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;


/**
 * Created by 123 on 2017/9/12.
 */

public class SysUtil {

    public static int getDisplayHeight(){
        DisplayMetrics dm = SchoolApp.getAppContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getDisplayWidth(){
        DisplayMetrics dm = SchoolApp.getAppContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static Uri getResourceUri(int resId)
    {
        return Uri.parse("android.resource://"+SchoolApp.getAppContext().getPackageName()+"/"+resId);
    }

}
