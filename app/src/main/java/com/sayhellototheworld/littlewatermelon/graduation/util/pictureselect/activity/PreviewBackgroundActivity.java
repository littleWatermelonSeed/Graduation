package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserUpdateDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.center_plaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseNoStatusActivity;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

public class PreviewBackgroundActivity extends BaseNoStatusActivity implements View.OnClickListener,UserUpdateDo {

    private ImageView imageView;
    private LinearLayout linearLayout_reelectLayout;
    private TextView textView_confirm;
    private RelativeLayout topbar;

    private String imagePath;
    private boolean topbarShow = true;

    private BmobFile skin;
    private Handler handler;
    private BaseNiceDialog dialog;

    public final static int REQUEST_CODE = 10;
    public final static int RESULT_CODE_FINISH = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_background);
        init();
    }

    private void init(){
        imagePath = getIntent().getStringExtra("imgPath");
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
        imageView = (ImageView) findViewById(R.id.activity_preview_background_ImageView);
        imageView.setOnClickListener(this);
        linearLayout_reelectLayout = (LinearLayout) findViewById(R.id.activity_preview_background_reelectLayout);
        linearLayout_reelectLayout.setOnClickListener(this);
        textView_confirm = (TextView) findViewById(R.id.activity_preview_background_confirm);
        textView_confirm.setOnClickListener(this);
        topbar = (RelativeLayout) findViewById(R.id.activity_preview_background_topbar);
        Glide.with(this)
                .load(new File(imagePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                    MyToastUtil.showToast("背景设置失败");
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("背景设置成功");
                    setResult(RESULT_CODE_FINISH);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_preview_background_reelectLayout:
                finish();
                break;
            case R.id.activity_preview_background_confirm:
                setBackground();
                break;
            case R.id.activity_preview_background_ImageView:
                hideTopbar();
                break;
        }
    }

    private void setBackground(){
        skin = new BmobFile(new File(imagePath));
        final BmobManageUser bmobManageUser = new BmobManageUser(this);
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("正在设置背景...");
                        bmobManageUser.asynUpdateUserSkin(skin,PreviewBackgroundActivity.this);
                    }
                });
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

    @Override
    public void updateSuccess() {
        DialogLoading.dismissLoadingDialog(handler,dialog,"背景设置成功", DialogLoading.MSG_SUCCESS);
        ControlUserFragment.syncUserFragment();
        setResult(RESULT_CODE_FINISH);
        finish();
    }

    @Override
    public void updateFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this, e);
    }

    public static void startActivity(Context context, String imgPath){
        Intent intent = new Intent(context,PreviewBackgroundActivity.class);
        intent.putExtra("imgPath",imgPath);
        ((FragmentActivity) context).startActivityForResult(intent,REQUEST_CODE);
    }

}
