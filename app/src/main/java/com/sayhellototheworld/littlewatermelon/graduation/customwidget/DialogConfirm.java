package com.sayhellototheworld.littlewatermelon.graduation.customwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;


/**
 * Created by 123 on 2017/9/6.
 */

public class DialogConfirm extends BaseNiceDialog {

    private String title;
    private String message;
    private static int mLayoutID = -1;
    private CancleAndOkDo cb;

    public static DialogConfirm newInstance(String title, String message, CancleAndOkDo cb) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        mLayoutID = -1;
        DialogConfirm dialog = new DialogConfirm();
        dialog.setArguments(bundle);
        dialog.setcb(cb);
        return dialog;
    }

    public static DialogConfirm newInstance(String title, String message,int layoutID, CancleAndOkDo cb) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        mLayoutID = layoutID;
        DialogConfirm dialog = new DialogConfirm();
        dialog.setArguments(bundle);
        dialog.setcb(cb);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        title = bundle.getString("title");
        message = bundle.getString("message");
    }

    @Override
    public int intLayoutId() {
        if (mLayoutID != -1){
            return mLayoutID;
        }
        mLayoutID = R.layout.nicedialog_remind_layout;
        return R.layout.nicedialog_remind_layout;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        int leftButtonID = 0;
        int rightButtonID = 0;
        if (mLayoutID == R.layout.nicedialog_remind_loginout_layout){
            holder.setText(R.id.nicedialog_remind_loginOut_layout_title, title);
            holder.setText(R.id.nicedialog_remind_loginOut_layout_message, message);
            leftButtonID = R.id.nicedialog_remind_loginOut_layout_cancel;
            rightButtonID = R.id.nicedialog_loginOut_remind_layout_ok;
        }else if (mLayoutID == R.layout.nicedialog_remind_layout){
            holder.setText(R.id.nicedialog_remind_layout_title, title);
            holder.setText(R.id.nicedialog_remind_layout_message, message);
            leftButtonID = R.id.nicedialog_remind_layout_cancel;
            rightButtonID = R.id.nicedialog_remind_layout_ok;
        }
        holder.setOnClickListener(leftButtonID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cb.cancle();
            }
        });

        holder.setOnClickListener(rightButtonID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cb.ok();
            }
        });
    }

    public void setcb(CancleAndOkDo cb){
        this.cb = cb;
    }

    public interface CancleAndOkDo{
        void cancle();
        void ok();
    }

}
