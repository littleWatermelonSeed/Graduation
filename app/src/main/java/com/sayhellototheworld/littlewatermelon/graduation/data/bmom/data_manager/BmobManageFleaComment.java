package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 123 on 2018/5/1.
 */

public class BmobManageFleaComment {

    private static BmobManageFleaComment manager;

    private BmobManageFleaComment() {

    }

    public static BmobManageFleaComment getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageFleaComment();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(FleaCommentBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryMsg(FleaMarketBean fleaMarketBean, int skip, final BmobQueryDone<FleaCommentBean> listener){
        BmobQuery<FleaCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("fleaMarkte",new BmobPointer(fleaMarketBean));
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<FleaCommentBean>() {
            @Override
            public void done(List<FleaCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryCount(FleaMarketBean fleaMarketBean, final QueryCountListener listener){
        BmobQuery<FleaCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("fleaMarkte",fleaMarketBean);
        query.count(FleaCommentBean.class, new CountListener() {
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

}
