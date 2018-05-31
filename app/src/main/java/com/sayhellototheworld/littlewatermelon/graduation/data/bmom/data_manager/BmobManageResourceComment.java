package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
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
 * Created by 123 on 2018/5/6.
 */

public class BmobManageResourceComment {

    private static BmobManageResourceComment manager;

    private BmobManageResourceComment() {

    }

    public static BmobManageResourceComment getManager() {
        if (manager == null) {
            synchronized (BmobManageResourceComment.class) {
                if (manager == null) {
                    manager = new BmobManageResourceComment();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(ResourceCommentBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryMsg(ResourceShareBean resourceBean, int skip, final BmobQueryDone<ResourceCommentBean> listener){
        BmobQuery<ResourceCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("resource",new BmobPointer(resourceBean));
        query.include("user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ResourceCommentBean>() {
            @Override
            public void done(List<ResourceCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryToMsg(int skip,final BmobQueryDone<ResourceCommentBean> listener){
        BmobQuery<ResourceCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<ResourceCommentBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<ResourceCommentBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<ResourceCommentBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.include("user,resource,resource.user,publishUser");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<ResourceCommentBean>() {
            @Override
            public void done(List<ResourceCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryCount(ResourceShareBean resourceBean, final QueryCountListener listener){
        BmobQuery<ResourceCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("resource",resourceBean);
        query.count(ResourceCommentBean.class, new CountListener() {
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
        BmobQuery<ResourceCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",user);
        BmobQuery<ResourceCommentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("read",false);
        BmobQuery<ResourceCommentBean> query3 = new BmobQuery<>();
        query3.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<ResourceCommentBean>> andQuerys = new ArrayList<BmobQuery<ResourceCommentBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        andQuerys.add(query3);

        BmobQuery<ResourceCommentBean> query = new BmobQuery<ResourceCommentBean>();
        query.and(andQuerys);

        query.count(ResourceCommentBean.class, new CountListener() {
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

    public void updateReadBatch(List<ResourceCommentBean> data){
        List<BmobObject> obj = new ArrayList<>();
        for (ResourceCommentBean l:data){
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
