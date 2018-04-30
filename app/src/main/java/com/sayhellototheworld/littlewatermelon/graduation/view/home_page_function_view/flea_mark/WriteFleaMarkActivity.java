package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarkBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function.ControlWriteFlea;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

import static com.sayhellototheworld.littlewatermelon.graduation.R.id.activity_write_flea_back;
import static com.sayhellototheworld.littlewatermelon.graduation.R.id.activity_write_flea_release;

public class WriteFleaMarkActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView txt_back;
    private TextView txt_release;
    private EditText edt_title;
    private EditText edt_content;
    private MyGridView grid_view;
    private RadioGroup radioGroup;
    private LinearLayout ll_single_price;
    private LinearLayout ll_section_price;
    private EditText edt_single_price;
    private EditText edt_section_price1;
    private EditText edt_section_price2;

    private ShowImageAdapter mShowImageAdapter;
    private List<String> imagePath;
    private String price = "";
    private String title;
    private String content;
    private boolean single_price = true;
    private ControlWriteFlea clf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_write_flea_mark);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(activity_write_flea_back);
        txt_back.setOnClickListener(this);
        txt_release = (TextView) findViewById(activity_write_flea_release);
        txt_release.setOnClickListener(this);
        edt_title = (EditText) findViewById(R.id.activity_write_flea_edt_title);
        edt_content = (EditText) findViewById(R.id.activity_write_flea_edt_details);
        grid_view = (MyGridView) findViewById(R.id.activity_write_flea_card_view);
        radioGroup = (RadioGroup) findViewById(R.id.activity_write_flea_radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        ll_single_price = (LinearLayout) findViewById(R.id.activity_write_flea_ll_single_price);
        ll_section_price = (LinearLayout) findViewById(R.id.activity_write_flea_ll_section_price);
        edt_single_price = (EditText) findViewById(R.id.activity_write_flea_edt_single_price);
        edt_section_price1 = (EditText) findViewById(R.id.activity_write_flea_edt_section_price_one);
        edt_section_price2 = (EditText) findViewById(R.id.activity_write_flea_edt_section_price_two);
    }

    @Override
    protected void initParam() {
        imagePath = new ArrayList<>();
        clf = new ControlWriteFlea(this);
        mShowImageAdapter = new ShowImageAdapter(this, imagePath, ShowImageAdapter.TYPE_WRITE_PLAN);
        grid_view.setAdapter(mShowImageAdapter);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    protected void initShow() {

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
        context.startActivity(new Intent(context, WriteFleaMarkActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case activity_write_flea_back:
                finish();
                break;
            case activity_write_flea_release:
                releaseFlea();
                break;
        }
    }

    private void releaseFlea(){
        if (!checkMsg())
            return;
        FleaMarkBean bean = new FleaMarkBean();
        bean.setUser(BmobManageUser.getCurrentUser());
        bean.setTitle(title);
        bean.setCommentNum(0);
        bean.setContent(content);
        bean.setSchoolKey(BmobManageUser.getCurrentUser().getSchooleKey());
        bean.setReleaseTime(new BmobDate(new Date()));
        bean.setPrice(price);
        if (single_price){
            bean.setPriceType(0);
        }else {
            bean.setPriceType(1);
        }
        clf.releaseFlea(bean,imagePath);
    }

    private boolean checkMsg(){
        title = edt_title.getText().toString();
        title = title.trim();
        content = edt_content.getText().toString();
        content = content.trim();

        String p1 = "";
        String p2 = "";

        if (single_price){
            p1 = edt_single_price.getText().toString();
            p1 = p1.trim();
            price = p1;
        }else {
            p1 = edt_section_price1.getText().toString();
            p2 = edt_section_price2.getText().toString();
            p1 = p1.trim();
            p2 = p2.trim();
            price = p1 + "ㅡ" + p2;
        }

        if (title == null || title.equals("")){
            MyToastUtil.showToast("标题不能为空");
            return false;
        }

        if (content == null || content.equals("")){
            MyToastUtil.showToast("内容不能为空");
            return false;
        }

        if ((single_price && (p1 == null || p1.equals(""))) || (!single_price && (p2 == null || p2.equals("") || p1 == null || p1.equals("")))){
            MyToastUtil.showToast("价格不能为空");
            return false;
        }

        if (!single_price && Integer.parseInt(p1) >= Integer.parseInt(p2)){
            MyToastUtil.showToast("价格1不能比价格2高不能为空");
            return false;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.activity_write_flea_radio_single_price) {
            ll_single_price.setVisibility(View.VISIBLE);
            ll_section_price.setVisibility(View.GONE);
            single_price = true;
        } else if (checkedId == R.id.activity_write_flea_radio_section_price) {
            ll_single_price.setVisibility(View.GONE);
            ll_section_price.setVisibility(View.VISIBLE);
            single_price = false;
        }
    }
}
