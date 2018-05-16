package com.sayhellototheworld.littlewatermelon.graduation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.json.College;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.GsonHelper;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class SDriverActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        button = (Button) findViewById(R.id.activity_driver_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        testUserMoudle();
//        testGson();
        testQuery();
    }

    private void testQuery(){
        BmobManageLostComment.getManager().queryToMsg(0, new BmobQueryDone<LostCommentBean>() {
            @Override
            public void querySuccess(List<LostCommentBean> data) {
                Log.i("niyuanjie","查询成功 数据量：" + data.size());
            }

            @Override
            public void queryFailed(BmobException e) {
                Log.i("niyuanjie","查询出错");
            }
        });
    }

    private void testUserMoudle() {
        startActivity(new Intent(SDriverActivity.this, LoginActivity.class));
    }

    private void testGson() {
        long beginTime = System.currentTimeMillis();
        College college = GsonHelper.getCollege();
        Log.i("niyuanjie", "解析时间为：" + (System.currentTimeMillis() - beginTime) + "毫秒");
        if (college == null) {
            Log.i("niyuanjie", "college为空");
        } else {
            Log.i("niyuanjie", "大学信息为： " + "省份：" + college.getData().get(23).getDepartName()
                    + "  城市名：" + college.getData().get(23).getCollegeLocations().get(5).getLocationName()
                    + "  大学名：" + college.getData().get(23).getCollegeLocations().get(5).getCollegeNames().get(0).getName());
        }
    }

}
