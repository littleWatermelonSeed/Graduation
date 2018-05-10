package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
        BmobQuery<TeacherBean> query = new BmobQuery<>();
        query.addWhereEqualTo("student",student);
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

}
