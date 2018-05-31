package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
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
            synchronized (BmobManageFleaComment.class) {
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

    public void queryToMsg(int skip,final BmobQueryDone<FleaCommentBean> listener){
        BmobQuery<FleaCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<FleaCommentBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<FleaCommentBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FleaCommentBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.include("user,fleaMarkte,fleaMarkte.user,publishUser");
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

    public void queryNoReadCount(MyUserBean user, final QueryCountListener listener){
        BmobQuery<FleaCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",user);
        BmobQuery<FleaCommentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("read",false);
        BmobQuery<FleaCommentBean> query3 = new BmobQuery<>();
        query3.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<FleaCommentBean>> andQuerys = new ArrayList<BmobQuery<FleaCommentBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        andQuerys.add(query3);

        BmobQuery<FleaCommentBean> query = new BmobQuery<FleaCommentBean>();
        query.and(andQuerys);

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

    public void updateReadBatch(List<FleaCommentBean> data){
        List<BmobObject> obj = new ArrayList<BmobObject>();
        for (FleaCommentBean l:data){
            if (!l.getRead()){
                l.setRead(true);
                obj.add(l);
            }
        }

        if (obj.size() <= 0){
            return;
        }

        new BmobBatch().updateBatch(obj).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i=0;i<o.size();i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            Log.i("niyuanjie","第"+i+"个数据批量更新成功："+result.getUpdatedAt());
                        }else{
                            Log.i("niyuanjie","第"+i+"个数据批量更新失败："+ex.getMessage()+","+ex.getErrorCode());
                        }
                    }
                }else{
                    Log.i("niyuanjie","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

}
