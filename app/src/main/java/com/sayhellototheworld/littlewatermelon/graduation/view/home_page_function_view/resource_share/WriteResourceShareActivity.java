package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.WriteFleaMarkActivity;

import java.util.ArrayList;
import java.util.List;

public class WriteResourceShareActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private TextView txt_release;
    private EditText edt_content;
    private EditText edt_link;
    private EditText edt_link_type;
    private MyGridView img;

    private ShowImageAdapter mShowImageAdapter;
    private List<String> imagePath;
    private String link;
    private String linkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_resource_share);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_write_resource_back);
        txt_back.setOnClickListener(this);
        txt_release = (TextView) findViewById(R.id.activity_write_resource_release);
        txt_release.setOnClickListener(this);
        edt_content = (EditText) findViewById(R.id.activity_write_resource_edt_details);
        edt_link = (EditText) findViewById(R.id.activity_write_resource_edt_link);
        edt_link_type = (EditText) findViewById(R.id.activity_write_resource_edt_link_type);
        img = (MyGridView) findViewById(R.id.activity_write_resource_card_view);
    }

    @Override
    protected void initParam() {
        imagePath = new ArrayList<>();
        mShowImageAdapter = new ShowImageAdapter(this, imagePath, ShowImageAdapter.TYPE_WRITE_PLAN);
        img.setAdapter(mShowImageAdapter);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_resource_back:
                finish();
                break;
            case R.id.activity_write_resource_release:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowPictureActivity.SHOWPICTURE_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                List<Image> images = (List<Image>) data.getSerializableExtra(ShowPictureActivity.RESULT_DATA_KEY);
                for (int i = 0; i < images.size(); i++) {
                    if (imagePath.size() >= 5) {
                        break;
                    }
                    imagePath.add(images.get(i).getPath());
                }
                mShowImageAdapter.notifyDataSetChanged();
            }
        }
    }

    public static void go2Activity(Context context) {
        context.startActivity(new Intent(context, WriteResourceShareActivity.class));
    }
}
