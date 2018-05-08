package com.sayhellototheworld.littlewatermelon.graduation.customwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.AcknowledgeAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 123 on 2018/5/8.
 */

public class DialogAcknowledge {

    private static int nowPosition = 0;
    private static boolean saveIng = false;

    public static void showDialog(final Context context, final FragmentManager fragmentManager, final List<String> imageUrls) {
        NiceDialog.init()
                .setLayoutId(R.layout.pop_acknowledge)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        initView(context, holder, dialog, imageUrls);
                    }
                })
                .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
                .setShowBottom(false)     //是否在底部显示dialog，默认flase
                .setWidth(-1)     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
                .setOutCancel(false)     //点击dialog外是否可取消，默认true
                .show(fragmentManager);     //显示dialog
    }

    private static void initView(final Context context, ViewHolder holder, final BaseNiceDialog dialog, final List<String> imageUrls) {


        ViewPager viewPager = holder.getView(R.id.pop_acknowledge_view_pager);
        TextView saveImage = holder.getView(R.id.pop_acknowledge_save_image);
        TextView cancle = holder.getView(R.id.pop_acknowledge_cancle);
        TextView more = holder.getView(R.id.pop_acknowledge_more);

        if (imageUrls.size() > 1) {
            more.setVisibility(View.VISIBLE);
        }

        AcknowledgeAdapter adapter = new AcknowledgeAdapter(context, imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nowPosition = position;
                Log.i("niyuanjie", "viewPager当前位置为：" + nowPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                nowPosition = 0;
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveIng){
                    MyToastUtil.showToast("图片正在保存,请稍等");
                    return;
                }
                saveIng = true;
                beginSave(context,imageUrls.get(nowPosition));
                Log.i("niyuanjie","图片url为：" + imageUrls.get(nowPosition));
            }
        });
    }

//    private static void savePic2Camera(Context context, Bitmap bitmap, String fileName) {
//        String fileName;
//        File file;
//        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
//        } else {  // Meizu 、Oppo
//            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
//        }
//        file = new File(fileName);
//
//        if (file.exists()) {
//            file.delete();
//        }
//        FileOutputStream out;
//        try {
//            out = new FileOutputStream(file);
//            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
//            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
//                out.flush();
//                out.close();
//// 插入图库
//                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitmap, null);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//        // 发送广播，通知刷新图库的显示
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
//    }

    public static void beginSave(final Context context,String url){
        final String imageName = "bmob" + System.currentTimeMillis();
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        saveBmp2Gallery(context,resource,imageName);
                    }
                });
    }

    public static void saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
        Log.i("niyuanjie","开始保存图片");
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        } catch (Exception e) {
            e.getStackTrace();
            Log.i("niyuanjie","保存图片出错 错误信息：" + e.getMessage());
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("niyuanjie","保存图片final出错 错误信息：" + e.getMessage());
            }
        }
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        MyToastUtil.showToast("图片保存成功");
        saveIng = false;
    }

}
