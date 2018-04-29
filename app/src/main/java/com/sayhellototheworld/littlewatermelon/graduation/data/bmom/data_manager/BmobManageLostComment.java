package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 123 on 2018/4/27.
 */

public class BmobManageLostComment {

    private static BmobManageLostComment manager;

    private BmobManageLostComment() {

    }

    public static BmobManageLostComment getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageLostComment();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(LostCommentBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryMsg(LostAndFindBean lostAndFindBean, int skip, final BmobQueryDone<LostCommentBean> listener){
        BmobQuery<LostCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("lost",new BmobPointer(lostAndFindBean));
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<LostCommentBean>() {
            @Override
            public void done(List<LostCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void delComment(){

    }

}
