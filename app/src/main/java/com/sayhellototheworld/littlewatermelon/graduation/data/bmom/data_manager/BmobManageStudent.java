package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.StudentBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/16.
 */

public class BmobManageStudent {

    private static BmobManageStudent manager;

    private BmobManageStudent() {

    }

    public static BmobManageStudent getManager() {
        if (manager == null) {
            synchronized (BmobManageStudent.class) {
                if (manager == null) {
                    manager = new BmobManageStudent();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(StudentBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryByUser(MyUserBean student, MyUserBean teacher, final BmobQueryDone<StudentBean> listener){
        BmobQuery<StudentBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("student",student);
        BmobQuery<StudentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("teacher",teacher);

        List<BmobQuery<StudentBean>> andQuerys = new ArrayList<BmobQuery<StudentBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<StudentBean> query = new BmobQuery<StudentBean>();
        query.and(andQuerys);

        query.include("teacher,student");
        query.findObjects(new FindListener<StudentBean>() {
            @Override
            public void done(List<StudentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryByteacher(MyUserBean teacher,int skip, final BmobQueryDone<StudentBean> listener){
        BmobQuery<StudentBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("teacher",teacher);

        List<BmobQuery<StudentBean>> andQuerys = new ArrayList<BmobQuery<StudentBean>>();
        andQuerys.add(query2);

        BmobQuery<StudentBean> query = new BmobQuery<StudentBean>();
        query.and(andQuerys);

        query.include("teacher,student");
        query.setLimit(30);
        query.setSkip(30*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<StudentBean>() {
            @Override
            public void done(List<StudentBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryAndDel(MyUserBean student, MyUserBean teacher){
        queryByUser(student, teacher, new BmobQueryDone<StudentBean>() {
            @Override
            public void querySuccess(List<StudentBean> data) {
                if (data.size() > 0){
                    delMsg(data.get(0), new BmobDeletMsgDone() {
                        @Override
                        public void delMsgSuc() {
                            Log.i("niyuanjie","学生删除成功");
                        }

                        @Override
                        public void delMsgFailed(BmobException e) {
                            Log.i("niyuanjie","学生删除出错");
                        }
                    });
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                Log.i("niyuanjie","学生查询出错");
            }
        });
    }

    public void queryAndDel(MyUserBean student, MyUserBean teacher, final BmobDeletMsgDone listener){
        queryByUser(student, teacher, new BmobQueryDone<StudentBean>() {
            @Override
            public void querySuccess(List<StudentBean> data) {
                if (data.size() > 0){
                    delMsg(data.get(0), new BmobDeletMsgDone() {
                        @Override
                        public void delMsgSuc() {
                            listener.delMsgSuc();
                            Log.i("niyuanjie","学生删除成功");
                        }

                        @Override
                        public void delMsgFailed(BmobException e) {
                            listener.delMsgFailed(e);
                            Log.i("niyuanjie","学生删除出错");
                        }
                    });
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                Log.i("niyuanjie","学生查询出错");
            }
        });
    }

    public void delMsg(final StudentBean bean, final BmobDeletMsgDone listener){
        StudentBean s = new StudentBean();
        s.setObjectId(bean.getObjectId());
        s.delete(new UpdateListener() {
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
