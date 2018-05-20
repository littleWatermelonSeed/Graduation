package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.SchoolApp;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.StudentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;

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
 * Created by 123 on 2018/5/10.
 */

public class BmobManageTeacher {

    private static BmobManageTeacher manager;

    private BmobManageTeacher() {

    }

    public static BmobManageTeacher getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageTeacher();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(TeacherBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryByStudent(MyUserBean student, final BmobQueryDone<TeacherBean> listener){
        BmobQuery<TeacherBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("student",student);
        BmobQuery<TeacherBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("statue",-1);

        List<BmobQuery<TeacherBean>> andQuerys = new ArrayList<BmobQuery<TeacherBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        query.and(andQuerys);

        query.include("teacher");
        query.findObjects(new FindListener<TeacherBean>() {
            @Override
            public void done(List<TeacherBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryStudentMsg(int skip,final BmobQueryDone<TeacherBean> listener){
        BmobQuery<TeacherBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("student",BmobManageUser.getCurrentUser());

        List<BmobQuery<TeacherBean>> andQuerys = new ArrayList<BmobQuery<TeacherBean>>();
        andQuerys.add(query1);

        BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        query.and(andQuerys);

        query.include("teacher");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<TeacherBean>() {
            @Override
            public void done(List<TeacherBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryTeacherMsg(int skip, final BmobQueryDone<TeacherBean> listener){
        BmobQuery<TeacherBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("teacher",BmobManageUser.getCurrentUser());

        List<BmobQuery<TeacherBean>> andQuerys = new ArrayList<BmobQuery<TeacherBean>>();
        andQuerys.add(query1);

        BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        query.and(andQuerys);

        query.include("student");query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");

        query.findObjects(new FindListener<TeacherBean>() {
            @Override
            public void done(List<TeacherBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void querySNoReadCount(MyUserBean user,final QueryCountListener listener){
        BmobQuery<TeacherBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("student",user);
        BmobQuery<TeacherBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("sRead",false);

        List<BmobQuery<TeacherBean>> andQuerys = new ArrayList<BmobQuery<TeacherBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        query.and(andQuerys);

        query.count(TeacherBean.class, new CountListener() {
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

    public void queryTNoReadCount(MyUserBean user,final QueryCountListener listener){
        BmobQuery<TeacherBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("teacher",user);
        BmobQuery<TeacherBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("tRead",false);

        List<BmobQuery<TeacherBean>> andQuerys = new ArrayList<BmobQuery<TeacherBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        query.and(andQuerys);

        query.count(TeacherBean.class, new CountListener() {
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

    public void delMsg(TeacherBean bean,final BmobDeletMsgDone listener){
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

    public void agreeBind(final TeacherBean teacherBean, final BmobUpdateDone listener){
        TeacherBean t = new TeacherBean();
        t.setStatue(1);
        t.setsRead(false);
        t.update(teacherBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
                if (e == null){
                    StudentBean studentBean = new StudentBean();
                    studentBean.setStatue(0);
                    studentBean.setTeacher(BmobManageUser.getCurrentUser());
                    studentBean.setStudent(teacherBean.getStudent());
                    BmobManageStudent.getManager().uploadMsg(studentBean, new BmobSaveMsgWithoutImg() {
                        @Override
                        public void msgSuccess(String objectID) {
                            Log.i("niyuanjie","学生数据储存成功");
                        }

                        @Override
                        public void msgFailed(BmobException e) {
                            BmobExceptionUtil.dealWithException(SchoolApp.getAppContext(),e);
                        }
                    });
                }
            }
        });
    }

    public void disAgreeBind(TeacherBean teacherBean, final BmobUpdateDone listener){
        TeacherBean t = new TeacherBean();
        t.setStatue(-1);
        t.setsRead(false);
        t.update(teacherBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

    public void updateTReadBatch(List<TeacherBean> data){
        List<BmobObject> obj = new ArrayList<BmobObject>();
        for (TeacherBean l:data){
            if (!l.gettRead()){
                l.settRead(true);
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

    public void updateSReadBatch(List<TeacherBean> data){
        List<BmobObject> obj = new ArrayList<BmobObject>();
        for (TeacherBean l:data){
            if (l.getsRead() != null && !l.getsRead()){
                l.setsRead(true);
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
