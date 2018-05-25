package com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlWriteForum;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.ArrayList;
import java.util.List;

public class WriteForumActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private TextView txt_release;
    private EditText edt_content;
    private MyGridView gridView;

    private ShowImageAdapter showImageAdapter;
    private List<String> imagePath;
    private ControlWriteForum cwf;

    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_forum);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_write_forum_back);
        txt_back.setOnClickListener(this);
        txt_release = (TextView) findViewById(R.id.activity_write_forum_release);
        txt_release.setOnClickListener(this);
        edt_content = (EditText) findViewById(R.id.activity_write_forum_edt_content);
        gridView = (MyGridView) findViewById(R.id.activity_write_forum_card_view);
    }

    @Override
    protected void initParam() {
        imagePath = new ArrayList<>();
        showImageAdapter = new ShowImageAdapter(this,imagePath,ShowImageAdapter.TYPE_WRITE_PLAN);
        gridView.setAdapter(showImageAdapter);
        cwf = new ControlWriteForum(this);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_forum_back:
                finish();
                break;
            case R.id.activity_write_forum_release:
                content = edt_content.getText().toString().trim();
                if (content == null || content.equals("")){
                    MyToastUtil.showToast("内容不能为空");
                    return;
                }
                release();
                break;
        }
    }

    private void release(){
        ForumBean bean = new ForumBean();
        bean.setUser(BmobManageUser.getCurrentUser());
        bean.setContent(content);
        bean.setImageUrls(imagePath);
        bean.setSchoolKey(BmobManageUser.getCurrentUser().getSchooleKey());
        bean.setCommentNum(0);
        bean.setLikeNum(0);
        cwf.releaseLost(bean,imagePath);
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
                showImageAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void go2Activity(Context context){
        context.startActivity(new Intent(context,WriteForumActivity.class));
    }

}
