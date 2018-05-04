package com.sayhellototheworld.littlewatermelon.graduation.presenter.home_function;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResource;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/4.
 */

public class ControlWriteResource implements BmobSaveMsgWithImg{

    private Context context;
    private BmobManageResource manager;

    private BaseNiceDialog dialog;
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                MyToastUtil.showToast("发布失败");
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                MyToastUtil.showToast("发布成功");
                ((Activity) context).finish();
            }
        }
    };

    public ControlWriteResource(Context context){
        this.context = context;
        manager = BmobManageResource.getManager();
    }

    public void releaseResource(final ResourceShareBean bean, final List<String> imgPath){
        DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("发布中...");
                        if (imgPath == null || imgPath.size() <= 0){
                            manager.uploadMsgWithoutImg(bean,ControlWriteResource.this);
                        }else {
                            manager.uploadMsgWithImg(bean,imgPath,ControlWriteResource.this);
                        }
                    }
                });
    }

    @Override
    public void msgSuccess(String objectID) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "发布成功", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void msgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(context,e);
    }

    @Override
    public void imgSuccess(List<BmobFile> list, List<String> urls) {

    }

    @Override
    public void imgFailed(int errorCode, String errorMsg) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        MyToastUtil.showToast("图片上传失败,错误信息" + errorMsg);
    }

    @Override
    public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {

    }
}
