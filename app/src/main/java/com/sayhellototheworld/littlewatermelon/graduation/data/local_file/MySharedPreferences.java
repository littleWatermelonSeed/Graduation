package com.sayhellototheworld.littlewatermelon.graduation.data.local_file;

import android.content.Context;
import android.content.SharedPreferences;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;

/**
 * Created by 123 on 2017/9/10.
 */

public class MySharedPreferences {

    public final static String KEY_USER_ID = "userID";
    public final static String KEY_USER_PASSWORD = "userPS";
    public final static String KEY_USER_LOGIN_STATUS = "loginStatus";

    public final static String KEY_FIRST_LOGIN = "firstLogin";

    private Context mContext;

    private SharedPreferences mSharedPreferences;
    private static MySharedPreferences mPreferences;

    public static MySharedPreferences getInstance(){
        if(mPreferences == null){
            mPreferences = new MySharedPreferences();
        }
        return mPreferences;
    }

    private MySharedPreferences() {
        mContext = SchoolApp.getAppContext();
        mSharedPreferences = mContext.getSharedPreferences("MySharedPreferences",Context.MODE_PRIVATE);
    }

    public boolean getBooleanMessage(String msgKey){
        return mSharedPreferences.getBoolean(msgKey,false);
    }

    public String getStringMessage(String msgKey){
        return mSharedPreferences.getString(msgKey,"");
    }

    public int getIntMessage(String msgKey){
        return mSharedPreferences.getInt(msgKey,-1);
    }

    public void saveMessage(String msgKey,int message){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_USER_PASSWORD,message);
        editor.commit();
    }

    public void saveMessage(String msgKey,String message){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(msgKey,message);
        editor.commit();
    }

    public void saveMessage(String msgKey,boolean message){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(msgKey,message);
        editor.commit();
    }

}
