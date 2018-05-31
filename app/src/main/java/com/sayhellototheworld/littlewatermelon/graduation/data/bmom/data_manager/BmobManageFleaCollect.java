package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCollectBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
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
 * Created by 123 on 2018/5/1.
 */

public class BmobManageFleaCollect {

    private static BmobManageFleaCollect manager;

    private BmobManageFleaCollect() {

    }

    public static BmobManageFleaCollect getManager() {
        if (manager == null) {
            synchronized (BmobManageFleaCollect.class) {
                if (manager == null) {
                    manager = new BmobManageFleaCollect();
                }
            }
        }
        return manager;
    }

    public void uploadMsg(FleaCollectBean bean, final BmobSaveMsgWithoutImg listener) {
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

    public void queryMsgByUser(MyUserBean user, int skip, final BmobQueryDone<FleaCollectBean> listener){
        BmobQuery<FleaCollectBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",user);
        query.include("fleaMarket,fleaMarket.user");
        query.setLimit(10);
        query.setSkip(10*skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<FleaCollectBean>() {
            @Override
            public void done(List<FleaCollectBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryExist(MyUserBean user, FleaMarketBean fleaMarketBean,final QueryCountListener listener){
        BmobQuery<FleaCollectBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",user);
        BmobQuery<FleaCollectBean> query2 = new BmobQuery<>();
        query1.addWhereEqualTo("fleaMarket",fleaMarketBean);

        List<BmobQuery<FleaCollectBean>> andQuerys = new ArrayList<BmobQuery<FleaCollectBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FleaCollectBean> query = new BmobQuery<FleaCollectBean>();
        query.and(andQuerys);
        query.count(FleaCollectBean.class, new CountListener() {
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

    public void delMsg(MyUserBean user, FleaMarketBean fleaMarketBean, final BmobDeletMsgDone listener){
        BmobQuery<FleaCollectBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",user);
        BmobQuery<FleaCollectBean> query2 = new BmobQuery<>();
        query1.addWhereEqualTo("fleaMarket",fleaMarketBean);

        List<BmobQuery<FleaCollectBean>> andQuerys = new ArrayList<BmobQuery<FleaCollectBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FleaCollectBean> query = new BmobQuery<FleaCollectBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<FleaCollectBean>() {
            @Override
            public void done(List<FleaCollectBean> list, BmobException e) {
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
