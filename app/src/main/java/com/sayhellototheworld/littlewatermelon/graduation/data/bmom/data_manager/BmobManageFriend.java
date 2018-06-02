package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager;

import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2018/5/30.
 */

public class BmobManageFriend {

    private static BmobManageFriend manager;

    private BmobManageFriend() {

    }

    public static BmobManageFriend getManager() {
        if (manager == null) {
            synchronized (BmobManageFriend.class) {
                if (manager == null) {
                    manager = new BmobManageFriend();
                }
            }
        }
        return manager;
    }

//    public void uploadMsgWithoutImg(final FriendRequestBean bean, final BmobSaveMsgWithoutImg listener) {
//        bean.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    FriendBean f = new FriendBean();
//                    f.setUser(bean.getFriend());
//                    f.setFriend(bean.getUser());
//                    f.save();
//                    listener.msgSuccess(s);
//                } else {
//                    listener.msgFailed(e);
//                }
//            }
//        });
//    }

    public void uploadMsgWithoutImg(final FriendRequestBean bean, final BmobSaveMsgWithoutImg listener) {
        final List<BmobObject> l = new ArrayList<BmobObject>();

        final FriendBean friend1 = new FriendBean();
        friend1.setUser(bean.getUser());
        friend1.setFriend(bean.getFriend());

        final FriendBean friend2 = new FriendBean();
        friend2.setUser(bean.getFriend());
        friend2.setFriend(bean.getUser());

        queryByUserAndFriend(bean.getUser(), bean.getFriend(), new BmobQueryDone<FriendBean>() {
            @Override
            public void querySuccess(List<FriendBean> data) {
                if (data.size() <= 0) {
                    l.add(friend1);
                    l.add(friend2);
                    doUpload(listener, l);
                } else {
                    listener.msgSuccess("");
                }
            }

            @Override
            public void queryFailed(BmobException e) {
                listener.msgFailed(e);
            }
        });
    }

    private void doUpload(final BmobSaveMsgWithoutImg listener, List<BmobObject> l) {
        new BmobBatch().insertBatch(l).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < o.size(); i++) {
                        BatchResult result = o.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.i("niyuanjie", "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            Log.i("niyuanjie", "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                        if ((i + 1) == 2) {
                            listener.msgSuccess("");
                        }
                    }
                } else {
                    listener.msgFailed(e);
                }
            }
        });
    }

    public void queryByUserAndFriend(MyUserBean user, MyUserBean friend, final BmobQueryDone<FriendBean> listener) {
        BmobQuery<FriendBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user);
        BmobQuery<FriendBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("friend", friend);

        List<BmobQuery<FriendBean>> andQuerys1 = new ArrayList<BmobQuery<FriendBean>>();
        andQuerys1.add(query1);
        andQuerys1.add(query2);

        BmobQuery<FriendBean> tempQuery1 = new BmobQuery<FriendBean>();
        BmobQuery<FriendBean> and1 = tempQuery1.and(andQuerys1);

        BmobQuery<FriendBean> query3 = new BmobQuery<>();
        query3.addWhereEqualTo("friend", user);
        BmobQuery<FriendBean> query4 = new BmobQuery<>();
        query4.addWhereEqualTo("user", friend);

        List<BmobQuery<FriendBean>> andQuerys2 = new ArrayList<BmobQuery<FriendBean>>();
        andQuerys2.add(query3);
        andQuerys2.add(query4);

        BmobQuery<FriendBean> tempQuery2 = new BmobQuery<FriendBean>();
        BmobQuery<FriendBean> and2 = tempQuery2.and(andQuerys2);

        List<BmobQuery<FriendBean>> andQuerys3 = new ArrayList<BmobQuery<FriendBean>>();
        andQuerys3.add(and1);
        andQuerys3.add(and2);

        BmobQuery<FriendBean> query = new BmobQuery<FriendBean>();
        query.or(andQuerys3);
        query.include("friend,user");
        query.findObjects(new FindListener<FriendBean>() {
            @Override
            public void done(List<FriendBean> list, BmobException e) {
                if (e == null) {
                    listener.querySuccess(list);
                } else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void queryByUser(MyUserBean user, int skip, final BmobQueryDone<FriendBean> listener) {
        BmobQuery<FriendBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user);

        List<BmobQuery<FriendBean>> andQuerys = new ArrayList<BmobQuery<FriendBean>>();
        andQuerys.add(query1);

        BmobQuery<FriendBean> query = new BmobQuery<FriendBean>();
        query.and(andQuerys);

        query.include("user,friend");
        query.setLimit(30);
        query.setSkip(30 * skip);
        query.order("-createdAt");
        query.findObjects(new FindListener<FriendBean>() {
            @Override
            public void done(List<FriendBean> list, BmobException e) {
                if (e == null) {
                    listener.querySuccess(list);
                } else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void deleteBatch(final List<FriendBean> data, final BmobDeletMsgDone listener) {
        if (data.size() <= 0) {
            listener.delMsgSuc();
            return;
        }
        List<BmobObject> tempData = new ArrayList<>();
        for (FriendBean f : data) {
            tempData.add(f);
        }
        new BmobBatch().deleteBatch(tempData).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.i("niyuanjie", "第" + i + "个数据批量删除成功");
                        } else {
                            Log.i("niyuanjie", "第" + i + "个数据批量删除失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                        if ((i + 1) == data.size()) {
                            listener.delMsgSuc();
                        }
                    }
                } else {
                    listener.delMsgFailed(e);
                }
            }
        });
    }

    public void deleteFriend(MyUserBean user, MyUserBean friend, final BmobDeletMsgDone listener) {
        queryByUserAndFriend(user, friend, new BmobQueryDone<FriendBean>() {
            @Override
            public void querySuccess(List<FriendBean> data) {
                Log.i("niyuanjie", "删除查询结果数量：" + data.size());
                deleteBatch(data, listener);
            }

            @Override
            public void queryFailed(BmobException e) {
                listener.delMsgFailed(e);
            }
        });
    }

    public void queryFriend(MyUserBean friend, final BmobQueryDone<FriendBean> listener) {
        BmobQuery<FriendBean> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", BmobManageUser.getCurrentUser());
        BmobQuery<FriendBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("friend", friend);

        List<BmobQuery<FriendBean>> andQuerys = new ArrayList<BmobQuery<FriendBean>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<FriendBean> query = new BmobQuery<>();
        query.include("user,friend");
        query.and(andQuerys);
        query.findObjects(new FindListener<FriendBean>() {
            @Override
            public void done(List<FriendBean> list, BmobException e) {
                if (e == null){
                    listener.querySuccess(list);
                }else {
                    listener.queryFailed(e);
                }
            }
        });
    }

    public void updateRemarkName(String objID, String remarkName, final BmobUpdateDone listener) {
        FriendBean friendBean = new FriendBean();
        friendBean.setRemarkName(remarkName);
        friendBean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                listener.done(e);
            }
        });
    }

}
