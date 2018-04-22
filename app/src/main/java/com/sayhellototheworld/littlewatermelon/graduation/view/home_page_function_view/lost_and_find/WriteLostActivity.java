package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.lost_and_find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlWriteLost;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

public class WriteLostActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView edt_back;
    private TextView edt_release;
    private EditText edt_title;
    private EditText edt_keyword;
    private EditText edt_details;
    private MyGridView mMyGridView;

    private ShowImageAdapter mShowImageAdapter;
    private List<String> imagePath;
    private ControlWriteLost cwl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_lost);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        edt_back = (TextView) findViewById(R.id.activity_write_lost_back);
        edt_back.setOnClickListener(this);
        edt_release = (TextView) findViewById(R.id.activity_write_lost_release);
        edt_release.setOnClickListener(this);
        edt_title = (EditText) findViewById(R.id.activity_write_lost_edt_title);
        edt_keyword = (EditText) findViewById(R.id.activity_write_lost_edt_keyword);
        edt_details = (EditText) findViewById(R.id.activity_write_lost_edt_details);
        mMyGridView = (MyGridView) findViewById(R.id.activity_write_lost_card_view);
    }

    @Override
    protected void initParam() {
        imagePath = new ArrayList<>();
        mShowImageAdapter = new ShowImageAdapter(this,imagePath,ShowImageAdapter.TYPE_WRITE_PLAN);
        cwl = new ControlWriteLost(this);
    }

    @Override
    protected void initShow() {
        mMyGridView.setAdapter(mShowImageAdapter);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,WriteLostActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_lost_back:
                finish();
                break;
            case R.id.activity_write_lost_release:
                releaseMsg();
                break;
        }
    }

    private void releaseMsg(){
        String title = edt_title.getText().toString();
        title = title.trim();
        String keyword = edt_keyword.getText().toString();
        keyword = keyword.trim();
        String details = edt_details.getText().toString();
        details = details.trim();
        if (title == null || title.equals("")){
            MyToastUtil.showToast("标题不能为空");
            return;
        }
        if (keyword == null || keyword.equals("")){
            MyToastUtil.showToast("关键字不能为空");
            return;
        }
        if (details == null || details.equals("")){
            MyToastUtil.showToast("详细内容不能为空");
            return;
        }
        LostAndFindBean bean = new LostAndFindBean();
        bean.setUser(BmobManageUser.getCurrentUser());
        bean.setTitle(title);
        bean.setKeyWord(keyword);
        bean.setContent(details);
        bean.setReleaseTime(new BmobDate(new Date()));
        cwl.releaseLost(bean,imagePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowPictureActivity.SHOWPICTURE_REQUESTCODE){
            if (resultCode == RESULT_OK){
                List<Image> images = (List<Image>) data.getSerializableExtra(ShowPictureActivity.RESULT_DATA_KEY);
                for (int i = 0;i < images.size();i++){
                    if (imagePath.size() >= 5){
                        break;
                    }
                    imagePath.add(images.get(i).getPath());
                }
                mShowImageAdapter.notifyDataSetChanged();
            }
        }
    }

}
