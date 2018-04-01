package com.sayhellototheworld.littlewatermelon.graduation.data.local_file;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;
import com.sayhellototheworld.littlewatermelon.graduation.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.SysUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 123 on 2017/9/7.
 */

public class ManageFile {

    private static final Context context = SchoolApp.getAppContext();

    public final static String FILENAME_USER_HEADPORTRAIL = "user_portrail";
    public final static String FILENAME_USER_BACKGROUND_DEFAULT = "user_background_default";
    public final static String FILENAME_USER_BACKGROUND_SELF = "user_background_self";

    public static void saveheadPortrait(int resourceID,String userID) {
        final File headPortrait = new File(GetFile.getInternalHeadImageFile(), FILENAME_USER_HEADPORTRAIL + userID + ".png");
        saveFile(resourceID, context, headPortrait);
    }

    public static void saveHeadPortrait(File headPic,String heapPicUrl) {
        final File headPortrait = new File(GetFile.getNewInternalHeadImageFile(), heapPicUrl + ".png");
        saveImage(headPic, headPortrait);
    }

    public static void saveSelfBackground(File background,String skinPicUrl) {
        final File selfBackground = new File(GetFile.getNewInternalSkinImageFile(), skinPicUrl + ".png");
        saveImage(background, selfBackground);
    }

    public static File getHeadPortrait(String heapPicUrl){
        File file = new File(GetFile.getInternalHeadImageFile(), heapPicUrl + ".png");
        if (!file.exists()){
            return null;
        }
        return file;
    }

    public static File getSelfBackground(String skinPicUrl){
        File file = new File(GetFile.getInternalSkinImageFile(), skinPicUrl + ".png");
        if (!file.exists()){
            return null;
        }
        return file;
    }

    public static String copyImageToPlan(String imagePath,String imageName){
        String targetPath = null;
        File image = new File(imagePath);
        if (!image.exists()){
            return targetPath;
        }

        File targetImage = new File(GetFile.getExternalPlanImageFile(),imageName + ".png");
        if (targetImage.exists()){
            targetImage.delete();
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int index = 0;
        try {
            targetImage.createNewFile();
            inputStream = new FileInputStream(image);
            outputStream = new FileOutputStream(targetImage);
            while ((index = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,index);
            }
            targetPath = targetImage.getAbsolutePath();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetPath;
    }

    private static void saveImage(File headPic, File headPortrait) {
        if (headPortrait.exists()){
            headPortrait.delete();
        }
        try {
            headPortrait.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            inputStream = new FileInputStream(headPic);
            outputStream = new FileOutputStream(headPortrait);
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveFile(int resourceID, Context context, File save) {
        if (save.exists()) {
            save.delete();
        }
        try {
            save.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File saveFile = save;
        Glide.with(context)
                .load(resourceID)
                .dontAnimate()
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (resource != null) {
                            Bitmap bitmap = PictureUtil.drawableToBitmap(resource);
                            OutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream(saveFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                                outputStream.close();
                                bitmap.recycle();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(SysUtil.getDisplayWidth(),SysUtil.getDisplayHeight());
    }

}
