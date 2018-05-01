package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by 123 on 2018/4/30.
 */

public class BmobManageFleaMark {

    private static BmobManageFleaMark manager;

    private BmobManageFleaMark() {

    }

    public static BmobManageFleaMark getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageFleaMark();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(FleaMarketBean bean, final BmobSaveMsgWithImg listener) {
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

    public void uploadMsgWithImg(final FleaMarketBean bean, final List<String> path, final BmobSaveMsgWithImg listener) {
        BmobImageManager.getManager().uploadImageMultiple(path, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == path.size()){
                    bean.setImageUrls(list1);
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

    public void queryBySchoolKey(String schoolKey, FindListener<FleaMarketBean> listener, int skip){
        BmobQuery<FleaMarketBean> query = new BmobQuery<>();
        query.addWhereEqualTo("schoolKey",schoolKey);
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(listener);
    }

    public void queryByUser(MyUserBean user, FindListener<FleaMarketBean> listener, int skip){
        BmobQuery<FleaMarketBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(listener);
    }

    public void queryCount(MyUserBean user, final QueryCountListener listener){
        BmobQuery<FleaMarketBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.count(FleaMarketBean.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
                    listener.queryCountSuc(integer);
                }else {
                    listener.queryCountFailed(e);
                }
            }
        });
    }

    public void delMsg(FleaMarketBean bean, final BmobDeletMsgDone listener){
        bean.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.delMsgSuc();
                }else {
                    listener.delMsgFailed(e);
                }
            }
        });
    }

}
