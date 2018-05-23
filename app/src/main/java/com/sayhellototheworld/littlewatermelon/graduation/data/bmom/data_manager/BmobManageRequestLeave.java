package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestLeaveBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/21.
 */

public class BmobManageRequestLeave {

    private static BmobManageRequestLeave manager;

    private BmobManageRequestLeave() {

    }

    public static BmobManageRequestLeave getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageRequestLeave();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(RequestLeaveBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryByStudent(MyUserBean user,int skip,final BmobQueryDone<RequestLeaveBean> listener){
        BmobQuery<RequestLeaveBean> query = new BmobQuery<>();
        query.addWhereEqualTo("student",user);
        query.include("teahcer");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<RequestLeaveBean>() {
            @Override
            public void done(List<RequestLeaveBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryByTeacher(MyUserBean user,int skip,final BmobQueryDone<RequestLeaveBean> listener){
        BmobQuery<RequestLeaveBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("teahcer",user);
        BmobQuery<RequestLeaveBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("statue",-2);

        List<BmobQuery<RequestLeaveBean>> andQuerys = new ArrayList<BmobQuery<RequestLeaveBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<RequestLeaveBean> query = new BmobQuery<RequestLeaveBean>();
        query.and(andQuerys);

        query.include("student");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<RequestLeaveBean>() {
            @Override
            public void done(List<RequestLeaveBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void studentUpdateSatue(String objID,int statue,boolean back,final BmobUpdateDone listener){
        RequestLeaveBean bean = new RequestLeaveBean();
        bean.setStatue(statue);
        if (back){
            bean.setBackTime(new BmobDate(new Date()));
        }
        bean.settRead(false);
        bean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

    public void teacherUpdateSatue(String objID,int statue,final BmobUpdateDone listener){
        RequestLeaveBean bean = new RequestLeaveBean();
        bean.setStatue(statue);
        bean.setsRead(false);
        bean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
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
