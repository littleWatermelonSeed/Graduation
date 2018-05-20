package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.net.Uri;
import android.util.DisplayMetrics;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    public static boolean sameDate(Date d1, Date d2){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }

}
