package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.ClipImageView;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.graduation.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseNoStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.MyActivityManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ClipHeadPortraitActivity extends BaseNoStatusActivity implements View.OnClickListener{

    private LinearLayout reelectLayout;
    private TextView textView_confirm;
    private ClipImageView mImage;

    private Handler handler;
    private BaseNiceDialog dialog;

    private String imagePath;

    public final static int REQUEST_CODE = 10;
    public final static int RESULT_CODE_FINISH = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_head_portrait);
        init();
    }

    private void init(){
        imagePath = getIntent().getStringExtra("imgPath");
        reelectLayout = (LinearLayout)findViewById(R.id.activity_clip_head_portrait_reelectLayout);
        reelectLayout.setOnClickListener(this);
        textView_confirm = (TextView)findViewById(R.id.activity_clip_head_portrait_confirm);
        textView_confirm.setOnClickListener(this);
        mImage = (ClipImageView) findViewById(R.id.activity_clip_head_portrait_clipImageView);
        Glide.with(this)
                .load(new File(imagePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mImage);
        MyActivityManager.getDestoryed().addActivityToList(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                setResult(RESULT_CODE_FINISH);
                finish();
            }
        };
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_clip_head_portrait_reelectLayout:
                finish();
                break;
            case R.id.activity_clip_head_portrait_confirm:
                confirm();
                break;
        }
    }

    private void confirm(){
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("正在生成头像...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                clipHeadPortrait();
                                handler.sendEmptyMessage(0);
                            }
                        }).start();
                    }
                });
    }

    private void clipHeadPortrait(){
        Bitmap bgimage = mImage.clipBitmap();
        Matrix matrix = new Matrix();
        matrix.setScale(1/4,1/4);
        Bitmap bitmap = PictureUtil.zoomImage(bgimage,200,200);
        File saveFile = new File(GetFile.getExternalTemporaryImageFile(),"temporary_head.png");
        if (saveFile.exists()){
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);
            outputStream.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            bgimage.recycle();
            bitmap.recycle();
        }
    }

    public static void startActivity(Context context,String imgPath){
        Intent intent = new Intent(context,ClipHeadPortraitActivity.class);
        intent.putExtra("imgPath",imgPath);
        ((FragmentActivity) context).startActivityForResult(intent,REQUEST_CODE);
    }

}
