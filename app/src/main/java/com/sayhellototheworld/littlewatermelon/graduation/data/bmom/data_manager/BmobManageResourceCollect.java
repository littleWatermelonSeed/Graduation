package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCollectBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/6.
 */

public class BmobManageResourceCollect {

    private static BmobManageResourceCollect manager;

    private BmobManageResourceCollect() {

    }

    public static BmobManageResourceCollect getManager() {
        if (manager == null) {
            synchronized (BmobManageLostAndFind.class) {
                if (manager == null) {
                    manager = new BmobManageResourceCollect();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(ResourceCollectBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryMsgByUser(MyUserBean user, int skip, final BmobQueryDone<ResourceCollectBean> listener){
        BmobQuery<ResourceCollectBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("resourceShareBean,resourceShareBean.user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<ResourceCollectBean>() {
            @Override
            public void done(List<ResourceCollectBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryExist(MyUserBean user, ResourceShareBean resourceShareBean, final QueryCountListener listener){
        BmobQuery<ResourceCollectBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",user);
        BmobQuery<ResourceCollectBean> query2 = new BmobQuery<>();
        query1.addWhereEqualTo("resourceShareBean",resourceShareBean);

        List<BmobQuery<ResourceCollectBean>> andQuerys = new ArrayList<BmobQuery<ResourceCollectBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<ResourceCollectBean> query = new BmobQuery<ResourceCollectBean>();
        query.and(andQuerys);
        query.count(ResourceCollectBean.class, new CountListener() {
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

    public void delMsg(MyUserBean user, ResourceShareBean resourceShareBean, final BmobDeletMsgDone listener){
        BmobQuery<ResourceCollectBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",user);
        BmobQuery<ResourceCollectBean> query2 = new BmobQuery<>();
        query1.addWhereEqualTo("resourceShareBean",resourceShareBean);

        List<BmobQuery<ResourceCollectBean>> andQuerys = new ArrayList<BmobQuery<ResourceCollectBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<ResourceCollectBean> query = new BmobQuery<ResourceCollectBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<ResourceCollectBean>() {
            @Override
            public void done(List<ResourceCollectBean> list, BmobException e) {
                if (e == null){
                    Log.i("niyuanjie","取消收藏时查询到有" + list.size() + "条数据");
                    if (list.size() > 0){
                        list.get(0).delete(new UpdateListener() {
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
                }else {
                    listener.delMsgFailed(e);
                }
            }
        });
    }

}
