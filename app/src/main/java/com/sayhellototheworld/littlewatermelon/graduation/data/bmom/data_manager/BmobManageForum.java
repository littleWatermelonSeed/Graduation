package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by 123 on 2018/5/24.
 */

public class BmobManageForum {

    private static BmobManageForum manager;

    private BmobManageForum() {

    }

    public static BmobManageForum getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageForum();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(ForumBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void uploadMsgWithImg(final ForumBean bean, final List<String> path, final BmobSaveMsgWithImg listener) {
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

    public void queryBySchoolKey(String schoolKey,int skip,final BmobQueryDone<ForumBean> listener){
        BmobQuery<ForumBean> query = new BmobQuery<>();
        query.addWhereEqualTo("schoolKey",schoolKey);
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ForumBean>() {
            @Override
            public void done(List<ForumBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryAll(int skip,final BmobQueryDone<ForumBean> listener){
        BmobQuery<ForumBean> query = new BmobQuery<>();
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ForumBean>() {
            @Override
            public void done(List<ForumBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryByUser(MyUserBean user, int skip,final BmobQueryDone<ForumBean> listener){
        BmobQuery<ForumBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ForumBean>() {
            @Override
            public void done(List<ForumBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void updateLike(final String forumID, String likeOgjID, final int num, final BmobUpdateDone listener){
        ForumBean forumBean = new ForumBean();
        forumBean.setLikeUserObjID(likeOgjID);
        forumBean.update(forumID, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null){
                    updateLikeNum(forumID, num);
                }
                listener.done(e);
            }
        });
    }

    public void updateLikeNum(String forumID,int num){
        ForumBean forumBean = new ForumBean();
        forumBean.increment("likeNum",num);
        forumBean.update(forumID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Log.i("niyuanjie","点赞数更新成功");
                }else {
                    Log.i("niyuanjie","点赞数更新失败");
                }
            }
        });
    }

    public void updateCommentNum(String forumID){
        ForumBean forumBean = new ForumBean();
        forumBean.increment("commentNum",1);
        forumBean.update(forumID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Log.i("niyuanjie","评论数更新成功");
                }else {
                    Log.i("niyuanjie","评论数更新失败");
                }
            }
        });
    }

    public void delMsg(ForumBean bean, final BmobDeletMsgDone listener){
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
