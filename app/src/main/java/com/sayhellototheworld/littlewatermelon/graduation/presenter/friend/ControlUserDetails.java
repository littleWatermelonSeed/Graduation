package com.sayhellototheworld.littlewatermelon.graduation.presenter.friend;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/29.
 */

public class ControlUserDetails implements BmobQueryDone<MyUserBean>{

    private Context context;
    private String userID;
    private UserDetailsActivity uda;
    private LinearLayout ll_bottom_body;

    private MyUserBean user;
    private BmobManageUser manager;

    private FriendBean friendBean;

    public ControlUserDetails(Context context, String userID,UserDetailsActivity uda,LinearLayout ll_bottom_body) {
        this.context = context;
        this.userID = userID;
        this.uda = uda;
        this.ll_bottom_body = ll_bottom_body;
        manager = new BmobManageUser(context);
        queryUser();
    }

    public void queryUser(){
        manager.queryByID(userID,this);
    }

    public void queryFriend(){
        BmobManageFriend.getManager().queryFriend(user, new BmobQueryDone<FriendBean>() {
            @Override
            public void querySuccess(List<FriendBean> data) {
                if (data.size() > 0){
                    uda.setFriendOperationStatue(1,data.get(0).getRemarkName());
                    friendBean = data.get(0);
                }else {
                    uda.setFriendOperationStatue(-1,"");
                }
                ll_bottom_body.setVisibility(View.VISIBLE);
            }

            @Override
            public void queryFailed(BmobException e) {

            }
        });
    }

    @Override
    public void querySuccess(List<MyUserBean> data) {
        uda.showMsg(data.get(0));
        uda.setUser(data.get(0));
        uda.setDataStatue(1);
        user = data.get(0);
        queryFriend();
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(context,e);
        uda.setDataStatue(-1);
    }

    public void requestFriend(){
        NiceDialog.init()
                .setLayoutId(R.layout.pop_request_friend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        ControlUserDetails.this.requestFriend(holder, dialog);
                    }
                })
                .setDimAmount(0.5f)
                .setShowBottom(false)
                .setWidth(-1)
                .setOutCancel(false)
                .show(((FragmentActivity)context).getSupportFragmentManager());
    }

    private void requestFriend(ViewHolder holder, final BaseNiceDialog dialog) {
        final EditText edt_remark = holder.getView(R.id.pop_request_friend_remark);
        TextView txt_cancle = holder.getView(R.id.pop_request_friend_cancle);
        TextView txt_ensure = holder.getView(R.id.pop_request_friend_ensure);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest(dialog, edt_remark);
            }
        });
    }

    private void doRequest(BaseNiceDialog dialog, final EditText edt_remark) {
        dialog.dismiss();
        DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("申请中...");
                        FriendRequestBean bean = new FriendRequestBean();
                        bean.setFriendRead(false);
                        bean.setFriend(user);
                        bean.setUser(BmobManageUser.getCurrentUser());
                        bean.setRemark(edt_remark.getText().toString());
                        bean.setStatue(0);
                        BmobManageRequestFriend.getManager().uploadMsg(bean, new BmobSaveMsgWithoutImg() {
                            @Override
                            public void msgSuccess(String objectID) {
                                baseNiceDialog.dismiss();
                                MyToastUtil.showToast("好友申请成功");
                            }

                            @Override
                            public void msgFailed(BmobException e) {
                                baseNiceDialog.dismiss();
                                BmobExceptionUtil.dealWithException(context,e);
                            }
                        });
                    }
                });
    }

    public void cancleFriend(){
        NiceDialog.init()
                .setLayoutId(R.layout.pop_cancle_friend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        doCancle(holder, dialog);
                    }
                })
                .setDimAmount(0.5f)
                .setShowBottom(false)
                .setWidth(-1)
                .setOutCancel(false)
                .show(((FragmentActivity)context).getSupportFragmentManager());
    }

    private void doCancle(ViewHolder holder, final BaseNiceDialog dialog) {
        TextView txt_cancle = holder.getView(R.id.pop_cancle_friend_cancle);
        TextView txt_ensure = holder.getView(R.id.pop_cancle_friend_ensure);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                        new DialogLoading.ShowLoadingDone() {
                            @Override
                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                textView.setText("删除好友中...");
                                BmobManageFriend.getManager().deleteFriend(BmobManageUser.getCurrentUser(), user, new BmobDeletMsgDone() {
                                    @Override
                                    public void delMsgSuc() {
                                        baseNiceDialog.dismiss();
                                        MyToastUtil.showToast("好友删除成功");
                                        uda.setFriendOperationStatue(-1,"");
                                        uda.setResult();
                                    }

                                    @Override
                                    public void delMsgFailed(BmobException e) {
                                        baseNiceDialog.dismiss();
                                        BmobExceptionUtil.dealWithException(context,e);
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void writeRemark(final TextView txt_remark_name){
        NiceDialog.init()
                .setLayoutId(R.layout.pop_write_remark_name)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final EditText edt_remark_name = holder.getView(R.id.pop_write_remark_name_remark);

                        TextView txt_cancle = holder.getView(R.id.pop_write_remark_name_cancle);
                        txt_cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        TextView txt_ensure = holder.getView(R.id.pop_write_remark_name_ensure);
                        txt_ensure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String remarkName = edt_remark_name.getText().toString().trim();
//                                if (remarkName == null || remarkName.equals("")){
//                                    MyToastUtil.showToast("备注名不能为空");
//                                    return;
//                                }
                                BmobManageFriend.getManager().updateRemarkName(friendBean.getObjectId(), remarkName, new BmobUpdateDone() {
                                    @Override
                                    public void done(BmobException e) {
                                        dialog.dismiss();
                                        if (e == null){
                                            txt_remark_name.setText(remarkName);
                                            MyToastUtil.showToast("备注名更新成功");
                                        }else {
                                            MyToastUtil.showToast("备注名更新失败");
                                            BmobExceptionUtil.dealWithException(context,e);
                                        }
                                    }
                                });
                            }
                        });

                    }
                })
                .setDimAmount(0.5f)
                .setShowBottom(false)
                .setWidth(-1)
                .setOutCancel(false)
                .show(((FragmentActivity)context).getSupportFragmentManager());
    }

    public void goChat(){
        String userName;
        if (friendBean != null && friendBean.getRemarkName() != null && !friendBean.getRemarkName().equals("")){
            userName = friendBean.getRemarkName();
        }else {
            userName = user.getNickName();
        }
        String h = "";
        if (user.getHeadPortrait() != null && !user.getHeadPortrait().getUrl().equals("")){
            h = user.getHeadPortrait().getUrl();
        }
        ChatActivity.go2Activity(context,user.getObjectId(),h,userName);
    }

}
