package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;
import com.sayhellototheworld.littlewatermelon.graduation.data.json.College;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by 123 on 2018/4/7.
 */

public class GsonHelper {

    public static String getJson(String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = SchoolApp.getAppContext().getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("niyuanjie","assets解析完成 数据为：" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static College getCollege(){
        Type type = new TypeToken<College>() {}.getType();
        College college = new Gson().fromJson(getJson("college.json"),type);
        return college;
    }

}
