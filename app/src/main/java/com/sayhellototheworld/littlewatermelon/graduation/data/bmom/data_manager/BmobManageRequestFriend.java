package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/30.
 */

public class BmobManageRequestFriend {

    private static BmobManageRequestFriend manager;

    private BmobManageRequestFriend() {

    }

    public static BmobManageRequestFriend getManager() {
        if (manager == null) {
            synchronized (BmobManageRequestFriend.class) {
                if (manager == null) {
                    manager = new BmobManageRequestFriend();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(FriendRequestBean bean, final BmobSaveMsgWithoutImg listener){
        queryExist(bean,listener);
    }

    public void uploadMsgWithoutImg(FriendRequestBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryExist(final FriendRequestBean bean, final BmobSaveMsgWithoutImg listener){
        BmobQuery<FriendRequestBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",bean.getUser());
        BmobQuery<FriendRequestBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("friend",bean.getFriend());
        BmobQuery<FriendRequestBean> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("statue",0);

        List<BmobQuery<FriendRequestBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        andQuerys.add(query3);

        BmobQuery<FriendRequestBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.count(FriendRequestBean.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
                    if (integer > 0){
                        listener.msgSuccess("");
                    }else {
                        uploadMsgWithoutImg(bean,listener);
                    }
                }else {
                    listener.msgFailed(e);
                }
            }
        });
    }

    public void queryOtherToMsg(int skip,final BmobQueryDone<FriendRequestBean> listener){
        BmobQuery<FriendRequestBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("friend",BmobManageUser.getCurrentUser());

        List<BmobQuery<FriendRequestBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);

        BmobQuery<FriendRequestBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.include("user,friend");
        query.setLimit(20);
        query.setSkip(20*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<FriendRequestBean>() {
            @Override
            public void done(List<FriendRequestBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryOwnToMsg(int skip,final BmobQueryDone<FriendRequestBean> listener){
        BmobQuery<FriendRequestBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",BmobManageUser.getCurrentUser());

        List<BmobQuery<FriendRequestBean>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);

        BmobQuery<FriendRequestBean> query = new BmobQuery<>();
        query.and(andQuerys);
        query.include("user,friend");
        query.setLimit(20);
        query.setSkip(20*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<FriendRequestBean>() {
            @Override
            public void done(List<FriendRequestBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryUserNoReadCount(MyUserBean user, final QueryCountListener listener){
        BmobQuery<FriendRequestBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",user);
        BmobQuery<FriendRequestBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("userRead",false);

        List<BmobQuery<FriendRequestBean>> andQuerys = new ArrayList<BmobQuery<FriendRequestBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FriendRequestBean> query = new BmobQuery<FriendRequestBean>();
        query.and(andQuerys);

        query.count(FriendRequestBean.class, new CountListener() {
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

    public void queryFriendNoReadCount(MyUserBean user, final QueryCountListener listener){
        BmobQuery<FriendRequestBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("friend",user);
        BmobQuery<FriendRequestBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("friendRead",false);

        List<BmobQuery<FriendRequestBean>> andQuerys = new ArrayList<BmobQuery<FriendRequestBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FriendRequestBean> query = new BmobQuery<FriendRequestBean>();
        query.and(andQuerys);

        query.count(FriendRequestBean.class, new CountListener() {
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

    public void updateUserReadBatch(List<FriendRequestBean> data){
        List<BmobObject> obj = new ArrayList<>();
        for (FriendRequestBean l:data){
            if (l.getUserRead() != null && !l.getUserRead()){
                l.setUserRead(true);
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

    public void updateFriendReadBatch(List<FriendRequestBean> data){
        List<BmobObject> obj = new ArrayList<>();
        for (FriendRequestBean l:data){
            if (!l.getFriendRead()){
                l.setFriendRead(true);
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

    public void updateStatue(String objID,int statue){
        FriendRequestBean t = new FriendRequestBean();
        t.setStatue(statue);
        t.setUserRead(false);
        t.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Log.i("niyuanjie","好友请求状态修改成功");
                }else {
                    Log.i("niyuanjie","好友请求状态修改失败");
                }
            }
        });
    }

    public void updateStatue(String objID, int statue, final BmobUpdateDone listener){
        FriendRequestBean t = new FriendRequestBean();
        t.setStatue(statue);
        t.setUserRead(false);
        t.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

}
