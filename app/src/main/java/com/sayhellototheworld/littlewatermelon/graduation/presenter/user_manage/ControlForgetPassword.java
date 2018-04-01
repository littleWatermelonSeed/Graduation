package com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage;

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
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.QueryUserDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ViForgetPasswordCoDo;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.ForgetPasswordNextActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/11.
 */

public class ControlForgetPassword implements ViForgetPasswordCoDo,QueryUserDo {

    private Context mContext;
    private BmobManageUser mBmobManageUser;
    private final Handler mHandler;
    private BaseNiceDialog dialog;


    public ControlForgetPassword(Context context) {
        mContext = context;
        mBmobManageUser = new BmobManageUser(mContext);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                }
            }
        };

    }

    @Override
    public void verifyUser(final String phone) {
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("验证中...");
                        mBmobManageUser.query("username",phone,ControlForgetPassword.this);
                    }
                });
    }

    @Override
    public void querySuccess(List<MyUserBean> object) {
        DialogLoading.dismissLoadingDialog(mHandler,dialog,DialogLoading.MSG_SUCCESS);
        if (object.size() == 0){
            MyToastUtil.showToast("该用户不存在");
            return;
        }
        ForgetPasswordNextActivity.startLoginActivity(mContext,object.get(0).getMobilePhoneNumber());

    }

    @Override
    public void queryFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(mHandler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext, e);
    }

}
