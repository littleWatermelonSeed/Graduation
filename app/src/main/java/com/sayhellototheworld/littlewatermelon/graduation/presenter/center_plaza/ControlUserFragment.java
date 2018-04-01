package com.sayhellototheworld.littlewatermelon.graduation.presenter.center_plaza;

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
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.DeleteFileDo;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2017/9/8.
 */

public class ControlUserFragment implements DeleteFileDo {

    private Context mContext;
    private static ShowCurUserInfo showInfo;
    private static BmobManageUser sMBmobManageUser;
    private static MyUserBean mUserBean;
    private static boolean UFshow = false;

    private Handler handler;
    private BaseNiceDialog dialog;

    public ControlUserFragment(Context context, ShowCurUserInfo showInfo) {
        mContext = context;
        this.showInfo = showInfo;
        sMBmobManageUser = new BmobManageUser(mContext);
        mUserBean = sMBmobManageUser.getCurrentUser();

        showInfo.showUserInformation(mUserBean);
        UFshow = true;

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                    MyToastUtil.showToast("背景设置失败");
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("背景设置成功");
                }
            }
        };
    }

    public static boolean syncUserFragment() {
        if (!UFshow) {
            return false;
        }

        mUserBean = sMBmobManageUser.getCurrentUser();
        showInfo.showUserInformation(mUserBean);
        return true;
    }

    public void userFragmentDestroy() {
        showInfo = null;
        sMBmobManageUser = null;
        mUserBean = null;
        UFshow = false;
    }

    public void useSysHeadSkin() {
        if (mUserBean.getSkin() != null && mUserBean.getSkin().getUrl() != null) {
            final BmobFile bmobFile = new BmobFile();
            bmobFile.setUrl(BmobManageUser.getCurrentUser().getSkin().getUrl());
            DialogLoading.showLoadingDialog(((FragmentActivity)mContext).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            dialog = baseNiceDialog;
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText("正在设置背景...");
                            sMBmobManageUser.deleteFile(bmobFile,ControlUserFragment.this);
                        }
                    });
        }
    }

    @Override
    public void deleteSuccess() {
        MyUserBean updateUserBean = new MyUserBean();
        updateUserBean.setObjectId(mUserBean.getObjectId());
        updateUserBean.remove("skin");
        updateUserBean.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    DialogLoading.dismissLoadingDialog(handler,dialog,"背景设置成功", DialogLoading.MSG_SUCCESS);
                    syncUserFragment();
                }else {
                    BmobExceptionUtil.dealWithException(mContext,e);
                }
            }
        });

    }

    @Override
    public void deleteFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext, e);
    }

}
