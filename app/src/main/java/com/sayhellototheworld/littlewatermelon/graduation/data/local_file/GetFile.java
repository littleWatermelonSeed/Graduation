package com.sayhellototheworld.littlewatermelon.graduation.data.local_file;

import java.io.File;

/**
 * Created by 123 on 2017/9/7.
 */

public class GetFile {

    public static File getNewInternalHeadImageFile(){
        File file = new File(FilePath.getInternalCacheDirectory("myCache"),"internalHeadImage");
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f:files){
                f.delete();
            }
        }else {
            file.mkdir();
        }
        return file;
    }

    public static File getInternalHeadImageFile(){
        File file = new File(FilePath.getInternalCacheDirectory("myCache"),"internalHeadImage");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getNewInternalSkinImageFile(){
        File file = new File(FilePath.getInternalCacheDirectory("myCache"),"internalSkinImage");
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f:files){
                f.delete();
            }
        }else {
            file.mkdir();
        }
        return file;
    }

    public static File getInternalSkinImageFile(){
        File file = new File(FilePath.getInternalCacheDirectory("myCache"),"internalSkinImage");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalPlanImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"plan");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalTemporaryImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"temporary");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalCacheDataFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"cache");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"data");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalUserImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"user");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalUserDataFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"user");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"data");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

}
