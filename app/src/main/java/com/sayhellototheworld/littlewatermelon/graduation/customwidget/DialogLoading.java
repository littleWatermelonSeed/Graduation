package com.sayhellototheworld.littlewatermelon.graduation.customwidget;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;

/**
 * Created by 123 on 2017/9/10.
 */

public class DialogLoading {

    public final static int MSG_SUCCESS = 0x100001;
    public final static int MSG_FAIL = 0x100000;

    public static void showLoadingDialog(final FragmentManager fragmentManager, final ShowLoadingDone done){
        NiceDialog.init()
                .setLayoutId(R.layout.nicedialog_loading)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                        done.done(viewHolder,baseNiceDialog);
                    }
                })
                .setWidth(100)
                .setHeight(100)
                .setDimAmount(0)
                .setOutCancel(false)
                .show(fragmentManager);

    }

    public static void dismissLoadingDialog(final Handler handler,final BaseNiceDialog dialog,
                                            String successMessage,final int msg){
        if (dialog.isVisible()){
            dialog.dismiss();
            if(msg == MSG_SUCCESS){
                MyToastUtil.showToast(successMessage);
            }
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!dialog.isVisible()){

                    }
                    Message message = new Message();
                    message.arg1 = msg;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    public static void dismissLoadingDialog(final Handler handler,final BaseNiceDialog dialog,
                                            final int msg){
        if (dialog.isVisible()){
            dialog.dismiss();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!dialog.isVisible()){

                    }
                    Message message = new Message();
                    message.arg1 = msg;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    public interface ShowLoadingDone{
        void done(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog);
    }

}
