package com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.ShowCurUserInfo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.UserUpdateDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ViUpdateUserCoDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.center_plaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.ForumFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.HomePageFragment;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/7.
 */

public class ControlUpdateUser implements ViUpdateUserCoDo, UserUpdateDo {

    private Context mContext;
    private ShowCurUserInfo cu;
    private BmobManageUser userManager;
    private MyUserBean userBean = null;
    private BaseNiceDialog dialog;
    private boolean afterRegister;

    private final Handler handler;

    public ControlUpdateUser(Context context, ShowCurUserInfo cu,boolean afterRegister) {
        mContext = context;
        this.cu = cu;
        this.afterRegister = afterRegister;
        userManager = new BmobManageUser(mContext);
        userBean = userManager.getCurrentUser();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("个人资料更新成功");
                }
            }
        };

        cu.showUserInformation(userBean);
    }

    @Override
    public void updateUser(final MyUserBean userBean, final BmobFile headPic) {
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("保存中...");
                        if(headPic == null){
                            userManager.asynUpdateWhitoutHeadPic(userBean,ControlUpdateUser.this);
                        }else {
                            userManager.asynUpdateWhitHeadPic(userBean,headPic,ControlUpdateUser.this);
                        }
                    }
                });
    }

    @Override
    public void updateSuccess() {
        DialogLoading.dismissLoadingDialog(handler,dialog,"个人资料更新成功", DialogLoading.MSG_SUCCESS);

        ControlUserFragment.syncUserFragment();
        ForumFragment.recreateView();
        HomePageFragment.syncHomePageFragment();

        if (afterRegister){
            MyActivityManager.getDestoryed().destroyedListActivity();
            CenterActivity.go2Activity(mContext,0);
        }else {
            ((Activity)mContext).finish();
        }
    }

    @Override
    public void updateFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext, e);
    }

}
