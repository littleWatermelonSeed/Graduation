package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.AnnouncementBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/22.
 */

public class BmobManageAnnouncement {

    private static BmobManageAnnouncement manager;

    private BmobManageAnnouncement() {

    }

    public static BmobManageAnnouncement getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageAnnouncement();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(AnnouncementBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryByUser(MyUserBean user, int skip,final BmobQueryDone<AnnouncementBean> listener){
        BmobQuery<AnnouncementBean> query = new BmobQuery<>();
        query.addWhereEqualTo("teacher",user);
        query.include("teacher");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<AnnouncementBean>() {
            @Override
            public void done(List<AnnouncementBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryOneByUser(MyUserBean user, final BmobQueryDone<AnnouncementBean> listener){
        BmobQuery<AnnouncementBean> query = new BmobQuery<>();
        query.addWhereEqualTo("teacher",user);
        query.include("teacher");
        query.setLimit(1);
        query.setSkip(0);
        query.order("-createdAt");
        query.findObjects(new FindListener<AnnouncementBean>() {
            @Override
            public void done(List<AnnouncementBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void delMsg(AnnouncementBean bean, final BmobDeletMsgDone listener){
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
