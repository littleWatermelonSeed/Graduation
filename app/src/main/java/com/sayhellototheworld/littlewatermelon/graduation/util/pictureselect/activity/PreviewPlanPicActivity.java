package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.PreviewImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseNoStatusActivity;

import java.util.List;

public class PreviewPlanPicActivity extends BaseNoStatusActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private RelativeLayout topbar;
    private LinearLayout back;
    private TextView page;

    private boolean topbarShow = true;

    private String[] imageUrls;
    private int curPosition;
    private PreviewImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_plan_pic);
        init();
    }

    private void init(){
        initWidget();
        initParams();
    }

    private void initWidget(){
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
        topbar = (RelativeLayout)findViewById(R.id.activity_preview_planPic_topbar);
        back = (LinearLayout)findViewById(R.id.activity_preview_planPic_back);
        back.setOnClickListener(this);
        page = (TextView)findViewById(R.id.activity_preview_plan_pic_page);
        mViewPager = (ViewPager)findViewById(R.id.activity_preview_planPic_viewPager);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initParams(){
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        imageUrls = intent.getStringArrayExtra("imageUrls");
        curPosition = intent.getIntExtra("curPosition",-1);
        mAdapter = new PreviewImageAdapter(this,imageUrls,this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(curPosition);
        page.setText((curPosition + 1 + "/" + imageUrls.length));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_preview_planPic_back:
                finish();
                break;
            default:
                hideTopbar();
                break;
        }
    }

    private void hideTopbar(){
        if (topbarShow){
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(topbar,"alpha", 1f, 0f);
            animator1.setDuration(500);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    topbar.setVisibility(View.GONE);
                    topbarShow = false;
                }
            });
            animatorSet.playTogether(animator1);
            animatorSet.start();
        }else {
            topbar.setVisibility(View.VISIBLE);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(topbar,"alpha", 0f, 1f);
            animator1.setDuration(500);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    topbarShow = true;
                }
            });
            animatorSet.playTogether(animator1);
            animatorSet.start();
        }
    }

    public static void startPreviewPlanPicActivity(Context context,List<String> imageUrls,int curPosition){
        Intent intent = new Intent(context,PreviewPlanPicActivity.class);
        String[] urls = new String[imageUrls.size()];
        for (int i = 0;i < urls.length;i++){
            urls[i] = imageUrls.get(i);
        }
        intent.putExtra("imageUrls",urls);
        intent.putExtra("curPosition",curPosition);
        context.startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        page.setText(position + 1 + "/" + imageUrls.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
