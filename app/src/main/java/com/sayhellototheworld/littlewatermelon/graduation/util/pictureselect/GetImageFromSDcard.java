package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContentResolverCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/7/11.
 */

public class GetImageFromSDcard {

    private static final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static ImageCallBack myCallBack;
    private static List<Folder> folderList;
    private static boolean finishFolder = false;

    public static void loadImageForSDCard(final Context context, final ImageCallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = ContentResolverCompat.query(contentResolver,uri,new String[]{
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media._ID,},null,null,MediaStore.Images.Media.DATE_ADDED,null);
                final List<Image> imageList = new ArrayList<>();
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    imageList.add(new Image(path,name,time));
                }
                int n = 0;
                for(Image image:imageList){
                    image.setID(n);
                    n++;
                }
                folderList = getFolder(imageList);
                ((FragmentActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(callback != null){
                            callback.onSuccess(folderList,imageList);
                        }else if(myCallBack != null){
                            myCallBack.onSuccess(folderList,imageList);
                        }
                    }
                });
                cursor.close();

            }
        }).start();
    }

    public void addCallBackListener(ImageCallBack callBack){
        this.myCallBack = callBack;
    }

    public interface ImageCallBack{
        void onSuccess(List<Folder> folderList, List<Image> images);
    }

    private static List<Folder> getFolder(List<Image> imageList){
        if(imageList == null || imageList.size() == 0){
            finishFolder = true;
            return null;
        }

        List<Folder> folderList = new ArrayList<>();
        getTotalFolder(folderList,imageList);
        Folder folder;
        for(Image image:imageList){
            String imagePath = image.getPath();
            String [] s = imagePath.split(File.separator);
            if(s.length < 2){
                folder = new Folder("sd卡");
            }else {
                folder = new Folder(s[s.length - 2]);
            }
            parseFolder(folderList,image,folder);
        }
        finishFolder = true;
        return folderList;
    }

    private static void getTotalFolder(List<Folder> folderList,List<Image> imageList){
        Folder folder = new Folder(imageList,"所有图片");
        folderList.add(folder);
    }

    private static void parseFolder(List<Folder> folderList,Image image,Folder folder){
        for(Folder folder1:folderList){
            if(folder1.getName().equals(folder.getName())){
                folder1.addImage(image);
                return;
            }
        }
        folder.addImage(image);
        folderList.add(folder);
    }

    public static List<Folder> getFolder(){
        if(!finishFolder){
            return null;
        }else {
            return folderList;
        }
    }

}
