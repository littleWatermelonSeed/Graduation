package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2017/3/16.
 */

public class MyActivityManager {
    private static List<Activity> list;
    private static Map<String, Activity> sMap;
    private static Map<String, Activity> userMap;
    private static Activity topActivity;

    private static MyActivityManager destroyedAllActivity = null;

    private MyActivityManager() {
        list = new ArrayList<Activity>();
        sMap = new HashMap<String, Activity>();
        userMap = new HashMap<String,Activity>();
    }

    public static MyActivityManager getDestoryed() {
        if (destroyedAllActivity == null) {
            destroyedAllActivity = new MyActivityManager();
            return destroyedAllActivity;
        } else {
            return destroyedAllActivity;
        }
    }

    public void addActivityToList(Activity activity) {
        list.add(activity);
    }

    public void destroyedListActivity() {
        for (Activity activity : list) {
            if (activity != null) {
                activity.finish();
            }
        }
        list.clear();
    }

    public void addActivityToMap(String activityName, Activity activity) {
        sMap.put(activityName, activity);
    }

    public void destroyedMapActivity(String activityName) {
        if (sMap.containsKey(activityName)) {
            sMap.remove(activityName);
        }
    }

    public Activity getTopActivity() {
        return topActivity;
    }

    public void setTopActivity(Activity topActivity) {
        MyActivityManager.topActivity = topActivity;
    }

    public void addActivityToUserMap(Activity activity,String name){
        userMap.put(name,activity);
    }

    public void removeActivityFromUserMap(String name){
        if (userMap.containsKey(name)){
            userMap.remove(name);
        }
    }

    public void clearUserMap(){
        if (userMap.size() <= 0){
            return;
        }
        Iterator iter = userMap.entrySet().iterator();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            Activity activity = (Activity) entry.getValue();
            activity.finish();
        }
        userMap.clear();
    }

    public boolean isExistInUserMap(String name){
        if (userMap.containsKey(name)){
            return true;
        }
        return false;
    }

}
