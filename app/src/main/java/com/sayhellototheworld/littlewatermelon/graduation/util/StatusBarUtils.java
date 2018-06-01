package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 123 on 2017/8/23.
 */

public class StatusBarUtils {

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public static void setLayoutMargin(Activity activity, ViewGroup parentLayout) {
        View firstChild = parentLayout.getChildAt(0);
        if (firstChild != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(firstChild.getLayoutParams());
            params.setMargins(0, StatusBarUtils.getStatusHeight(activity), 0, 0);
            firstChild.setLayoutParams(params);
        }
    }

    public static void setRelativeLayoutMargin(Activity activity, ViewGroup parentLayout) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(parentLayout.getLayoutParams());
        params.setMargins(0, StatusBarUtils.getStatusHeight(activity), 0, 0);
        parentLayout.setLayoutParams(params);

    }

}
