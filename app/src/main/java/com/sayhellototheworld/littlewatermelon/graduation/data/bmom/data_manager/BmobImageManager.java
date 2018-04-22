package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 123 on 2018/4/22.
 */

public class BmobImageManager {

    private static BmobImageManager manager;

    private BmobImageManager(){

    }

    public static BmobImageManager getManager(){
        if (manager == null){
            synchronized (BmobImageManager.class){
                if (manager == null){
                    manager = new BmobImageManager();
                }
            }
        }
        return manager;
    }

    public void uploadImageOne(String path, UploadFileListener listener){
        BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(listener);
    }

    public void uploadImageMultiple(List<String> path, UploadBatchListener listener){
        BmobFile.uploadBatch(path.toArray(new String[]{}),listener);
    }

}
