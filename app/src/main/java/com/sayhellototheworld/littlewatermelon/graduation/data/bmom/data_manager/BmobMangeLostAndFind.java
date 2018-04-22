package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.SaveMsgListener;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by 123 on 2018/4/22.
 */

public class BmobMangeLostAndFind {

    private static BmobMangeLostAndFind manager;

    private BmobMangeLostAndFind() {

    }

    public static BmobMangeLostAndFind getManager() {
        if (manager == null) {
            synchronized (BmobMangeLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobMangeLostAndFind();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(LostAndFindBean bean, final SaveMsgListener listener) {
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.msgSuccess(s);
                }else {
                    listener.msgFailed(e);
                }
            }
        });
    }

    public void uploadMsgWithImg(final LostAndFindBean bean, final List<String> path, final SaveMsgListener listener) {
        BmobImageManager.getManager().uploadImageMultiple(path, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                bean.setImageUrls(list1);
                if (list1.size() == path.size()){
                    uploadMsgWithoutImg(bean,listener);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                listener.onProgress(i,i1,i2,i3);
            }

            @Override
            public void onError(int i, String s) {
                listener.imgFailed(i,s);
            }
        });
    }

}
