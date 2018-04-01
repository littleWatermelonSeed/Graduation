package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/6.
 */

public class BmobExceptionUtil {

    public static void dealWithException(Context mContext,BmobException e){
        switch (e.getErrorCode()) {
            case 1003:
            case 1004:
            case 1005:
            case 1500:
            case 10011:
                DialogConfirm.newInstance("提示", mContext.getString(R.string.BmobException_note_VIP), new DialogConfirm.CancleAndOkDo() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void ok() {

                    }
                }).setMargin(60)
                        .setOutCancel(false)
                        .show(((FragmentActivity) mContext).getSupportFragmentManager());
                break;
            case 10010:
                MyToastUtil.showToast(mContext.getString(R.string.BmobException_note_overflow), MyToastUtil.LONG);
                break;
            case 209:
            case 202:
                MyToastUtil.showToast("该用户已经存在", MyToastUtil.LONG);
                break;
            case 205:
                MyToastUtil.showToast("该用户不存在", MyToastUtil.LONG);
                break;
            case 207:
                MyToastUtil.showToast("验证码错误", MyToastUtil.LONG);
                break;
            case 9010:
                MyToastUtil.showToast("网络超时,请稍后再试", MyToastUtil.LONG);
                break;
            case 9019:
                MyToastUtil.showToast("手机号码/邮箱地址/验证码格式不正确", MyToastUtil.LONG);
                break;
            case 9016:
                MyToastUtil.showToast("木有网啦，重试下嘛", MyToastUtil.LONG);
                break;
            case 101:
                MyToastUtil.showToast("账号或密码错误", MyToastUtil.LONG);
                break;
            case 500:
                MyToastUtil.showToast("服务器繁忙,请稍后再试", MyToastUtil.LONG);
                break;
            default:
                MyToastUtil.showToast("未知错误：" + e.getErrorCode() + " " + e.getMessage(),MyToastUtil.LONG);
                break;
        }
        Log.i("niyuanjieEx","错误代码：" + e.getErrorCode() + "  错误信息：" + e.getMessage());
    }

    public static void dealWithException(Context mContext, BmobException e, String logMessage){
        switch (e.getErrorCode()) {
            case 1003:
            case 1004:
            case 1005:
            case 1500:
            case 10011:
                DialogConfirm.newInstance("提示", mContext.getString(R.string.BmobException_note_VIP), new DialogConfirm.CancleAndOkDo() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void ok() {

                    }
                }).setMargin(60)
                        .setOutCancel(false)
                        .show(((FragmentActivity) mContext).getSupportFragmentManager());
                break;
            case 10010:
                MyToastUtil.showToast(mContext.getString(R.string.BmobException_note_overflow), MyToastUtil.LONG);
                break;
            case 209:
            case 202:
                MyToastUtil.showToast("该用户已经存在", MyToastUtil.LONG);
                break;
            case 205:
                MyToastUtil.showToast("该用户不存在", MyToastUtil.LONG);
                break;
            case 207:
                MyToastUtil.showToast("验证码错误", MyToastUtil.LONG);
                break;
            case 9010:
                MyToastUtil.showToast("网络超时,请稍后再试", MyToastUtil.LONG);
                break;
            case 9016:
                MyToastUtil.showToast("木有网啦，重试下嘛", MyToastUtil.LONG);
                break;
            case 9019:
                MyToastUtil.showToast("手机号码/邮箱地址/验证码格式不正确", MyToastUtil.LONG);
                break;
            case 101:
                MyToastUtil.showToast("账号或密码错误", MyToastUtil.LONG);
                break;
            case 500:
                MyToastUtil.showToast("服务器繁忙,请稍后再试", MyToastUtil.LONG);
                break;
            default:
                MyToastUtil.showToast("未知错误：" + e.getErrorCode() + " " + e.getMessage(),MyToastUtil.LONG);
                break;
        }
        Log.i("niyuanjieEx","位置：" + logMessage + "  错误代码：" + e.getErrorCode() + "  错误信息：" + e.getMessage());
    }

}
