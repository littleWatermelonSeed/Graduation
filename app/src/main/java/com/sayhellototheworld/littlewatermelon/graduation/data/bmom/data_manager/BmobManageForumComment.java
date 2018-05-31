package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
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
 * Created by 123 on 2018/5/25.
 */

public class BmobManageForumComment {

    private static BmobManageForumComment manager;

    private BmobManageForumComment() {

    }

    public static BmobManageForumComment getManager() {
        if (manager == null) {
            synchronized (BmobManageForumComment.class) {
                if (manager == null) {
                    manager = new BmobManageForumComment();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(ForumCommentBean bean, final String forumID, final BmobSaveMsgWithoutImg listener) {
        bean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    listener.msgSuccess(s);
                    BmobManageForum.getManager().updateCommentNum(forumID);
                }else {
                    listener.msgFailed(e);
                }
            }
        });
    }

    public void queryMsg(ForumBean forumBean, int skip, final BmobQueryDone<ForumCommentBean> listener){
        BmobQuery<ForumCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("forum",new BmobPointer(forumBean));
        query.include("user,otherUser,publishUser,forum");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ForumCommentBean>() {
            @Override
            public void done(List<ForumCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryToMsg(int skip,final BmobQueryDone<ForumCommentBean> listener){
        BmobQuery<ForumCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<ForumCommentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("otherUser",BmobManageUser.getCurrentUser());

        List<BmobQuery<ForumCommentBean>> andQuerys1 = new ArrayList<>();
        andQuerys1.add(query1);
        andQuerys1.add(query2);

        BmobQuery<ForumCommentBean> tempQuery = new BmobQuery<>();
        BmobQuery<ForumCommentBean> or = tempQuery.or(andQuerys1);

        BmobQuery<ForumCommentBean> query3 = new BmobQuery<>();
        query3.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<ForumCommentBean>> andQuerys2 = new ArrayList<>();
        andQuerys2.add(query3);
        andQuerys2.add(or);

        BmobQuery<ForumCommentBean> query = new BmobQuery<>();
        query.and(andQuerys2);
        query.include("user,otherUser,publishUser,forum,forum.user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<ForumCommentBean>() {
            @Override
            public void done(List<ForumCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryToMsg1(int skip,final BmobQueryDone<ForumCommentBean> listener){
        BmobQuery<ForumCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<ForumCommentBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("otherUser",null);

        List<BmobQuery<ForumCommentBean>> andQuerys1 = new ArrayList<>();
        andQuerys1.add(query1);
        andQuerys1.add(query2);

        BmobQuery<ForumCommentBean> tempQuery1 = new BmobQuery<>();
        BmobQuery<ForumCommentBean> and = tempQuery1.and(andQuerys1);

        BmobQuery<ForumCommentBean> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("otherUser",BmobManageUser.getCurrentUser());

        List<BmobQuery<ForumCommentBean>> andQuerys2 = new ArrayList<>();
        andQuerys2.add(and);
        andQuerys2.add(query3);

        BmobQuery<ForumCommentBean> tempQuery2 = new BmobQuery<>();
        BmobQuery<ForumCommentBean> or = tempQuery2.or(andQuerys2);

        BmobQuery<ForumCommentBean> query4 = new BmobQuery<>();
        query4.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<ForumCommentBean>> andQuerys3 = new ArrayList<>();
        andQuerys3.add(query4);
        andQuerys3.add(or);

        BmobQuery<ForumCommentBean> query = new BmobQuery<>();
        query.and(andQuerys3);
        query.include("user,otherUser,publishUser,forum,forum.user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<ForumCommentBean>() {
            @Override
            public void done(List<ForumCommentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryNoReadCount(final QueryCountListener listener){
        BmobQuery<ForumCommentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("publishUser",BmobManageUser.getCurrentUser());
        BmobQuery<ForumCommentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("otherUser",BmobManageUser.getCurrentUser());

        List<BmobQuery<ForumCommentBean>> andQuerys1 = new ArrayList<>();
        andQuerys1.add(query1);
        andQuerys1.add(query2);

        BmobQuery<ForumCommentBean> tempQuery = new BmobQuery<>();
        BmobQuery<ForumCommentBean> or = tempQuery.or(andQuerys1);

        BmobQuery<ForumCommentBean> query3 = new BmobQuery<>();
        query3.addWhereNotEqualTo("user",BmobManageUser.getCurrentUser());
        BmobQuery<ForumCommentBean> query4 = new BmobQuery<>();
        query4.addWhereEqualTo("read",false);

        List<BmobQuery<ForumCommentBean>> andQuerys2 = new ArrayList<>();
        andQuerys2.add(query3);
        andQuerys2.add(query4);
        andQuerys2.add(or);

        BmobQuery<ForumCommentBean> query = new BmobQuery<>();
        query.and(andQuerys2);

        query.count(ForumCommentBean.class, new CountListener() {
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

    public void queryCount(ForumBean forumBean, final QueryCountListener listener){
        BmobQuery<ForumCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("forum",forumBean);
        query.count(ForumCommentBean.class, new CountListener() {
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

    public void updateReadBatch(List<ForumCommentBean> data){
        List<BmobObject> obj = new ArrayList<>();
        for (ForumCommentBean l:data){
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
