package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import com.othershe.nicedialog.BaseNiceDialog;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestRepairBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/22.
 */

public class BmobManagerRepairs {

    private static BmobManagerRepairs manager;

    private BmobManagerRepairs() {

    }

    public static BmobManagerRepairs getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManagerRepairs();
                }
            }
        }
        return manager;
    }

    public void uploadMsgWithoutImg(RequestRepairBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryByUser(MyUserBean user, int skip, final BmobQueryDone<RequestRepairBean> listener){
        BmobQuery<RequestRepairBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("repairman");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<RequestRepairBean>() {
            @Override
            public void done(List<RequestRepairBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryByRepairsMan(int skip, final BmobQueryDone<RequestRepairBean> listener){
        BmobQuery<RequestRepairBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("repairman",BmobManageUser.getCurrentUser());
        BmobQuery<RequestRepairBean> query2 = new BmobQuery<>();
        query2.addWhereNotEqualTo("statue",0);

        List<BmobQuery<RequestRepairBean>> andQuerys = new ArrayList<BmobQuery<RequestRepairBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);


        BmobQuery<RequestRepairBean> query = new BmobQuery<RequestRepairBean>();
        query.and(andQuerys);

        query.include("repairman,user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<RequestRepairBean>() {
            @Override
            public void done(List<RequestRepairBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryBySchoolKey(String schoolKey, int skip, final BmobQueryDone<RequestRepairBean> listener){
        BmobQuery<RequestRepairBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("schoolKey",schoolKey);
        BmobQuery<RequestRepairBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("statue",0);

        List<BmobQuery<RequestRepairBean>> andQuerys = new ArrayList<BmobQuery<RequestRepairBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);


        BmobQuery<RequestRepairBean> query = new BmobQuery<RequestRepairBean>();
        query.and(andQuerys);

        query.include("repairman,user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<RequestRepairBean>() {
            @Override
            public void done(List<RequestRepairBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void userUpdateSatue(String objID,int statue,final BmobUpdateDone listener){
        RequestRepairBean bean = new RequestRepairBean();
        bean.setStatue(statue);
        bean.setrRead(false);
        bean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

    public void repairManUpdateSatueWithoutTime(String objID,int statue,final BmobUpdateDone listener){
        RequestRepairBean bean = new RequestRepairBean();
        bean.setStatue(statue);
        bean.setuRead(false);
        bean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

    public void repairManUpdateSatueWithTime(String objID,int statue,String appointmentTimefinal ,final BmobUpdateDone listener){
        RequestRepairBean bean = new RequestRepairBean();
        bean.setStatue(statue);
        bean.setuRead(false);
        bean.setRepairman(BmobManageUser.getCurrentUser());
        bean.setAppointmentTime(appointmentTimefinal);
        bean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

    public void ensureStatueAndUpdateStatue(final String objID, final int statue, final String appointmentTimefinal , final BaseNiceDialog baseNiceDialog,
                                            final BmobUpdateDone listener){
        BmobQuery<RequestRepairBean> query = new BmobQuery<RequestRepairBean>();
        query.getObject(objID, new QueryListener<RequestRepairBean>() {

            @Override
            public void done(RequestRepairBean object, BmobException e) {
                if(e == null){
                    if (object.getStatue() == 0){
                        repairManUpdateSatueWithTime(objID,statue,appointmentTimefinal,listener);
                    }else{
                        MyToastUtil.showToast("已经有维修员处理了该维修申请");
                        baseNiceDialog.dismiss();
                    }
                }else {
                    listener.done(e);
                }
            }

        });
    }

}
