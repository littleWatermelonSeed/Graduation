package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
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
 * Created by 123 on 2018/4/27.
 */

public class BmobManageLostComment {

    private static BmobManageLostComment manager;

    private BmobManageLostComment() {

    }

    public static BmobManageLostComment getManager() {
        if (manager == null) {
            synchronized (BmobManageLostComment.class) {
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

    public void queryToMsg(int skip,final BmobQueryDone<LostCommentBean> listener){
        BmobQuery<LostCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<LostCommentBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());
//        BmobQuery<LostCommentBean> query3 = new BmobQuery<>();
//        query3.addWhereEqualTo("read",false);

        List<BmobQuery<LostCommentBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);
//        andQuerys.add(query3);

        BmobQuery<LostCommentBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.include("user,lost,lost.user,publishUser");
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

    public void queryNoReadCount(MyUserBean user,final QueryCountListener listener){
        BmobQuery<LostCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",user);
        BmobQuery<LostCommentBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());
        BmobQuery<LostCommentBean> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("read",false);

        List<BmobQuery<LostCommentBean>> andQuerys = new ArrayList<BmobQuery<LostCommentBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        andQuerys.add(query3);

        BmobQuery<LostCommentBean> query = new BmobQuery<LostCommentBean>();
        query.and(andQuerys);

        query.count(LostCommentBean.class, new CountListener() {
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

    public void updateReadBatch(List<LostCommentBean> data){
        List<BmobObject> obj = new ArrayList<BmobObject>();
        for (LostCommentBean l:data){
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

    public void delComment(){

    }

}
