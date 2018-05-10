package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherMessageActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener, OnRefreshListener, BmobQueryDone<TeacherBean>,
        BmobDeletMsgDone {

    private SmartRefreshLayout refreshLayout;
    private LinearLayout ll_msg_body;
    private TextView txt_back;
    private TextView txt_un_green;
    private TextView txt_green;
    private ImageView img_more;
    private TextView txt_no_msg;
    private CircleImageView img_head;
    private TextView txt_teacher_name;
    private TextView txt_teacher_nick_name;
    private TextView txt_teacher_sex;
    private TextView txt_teacher_school_name;
    private PopupWindow pop_window;
    private TextView txt_bind_teacher;
    private TextView txt_unbind_teacher;
    private TextView txt_change_teahcer;
    private View pop_window_view;
    private BaseNiceDialog dialog;

    private MyUserBean student;
    private MyUserBean nowTeacher;
    private TeacherBean mTeacherBean;
    private boolean refreshFinish = false;
    private boolean bindTeacher = false;
    private boolean refreshIng = false;
    private int bindType = -1;
    private BmobManageTeacher teacherManager;
    private BmobManageUser userManager;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                if (bindType == 0) {
                    MyToastUtil.showToast("绑定失败");
                } else if (bindType == 1) {
                    MyToastUtil.showToast("解绑失败");
                } else if (bindType == 2) {
                    MyToastUtil.showToast("更换辅导员失败");
                }
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                if (bindType == 0) {
                    MyToastUtil.showToast("绑定成功");
                } else if (bindType == 1) {
                    MyToastUtil.showToast("解绑成功");
                } else if (bindType == 2) {
                    MyToastUtil.showToast("更换辅导员成功");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_teacher_message);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        ll_msg_body = (LinearLayout) findViewById(R.id.activity_teacher_message_msg_body);
        txt_back = (TextView) findViewById(R.id.activity_teacher_message_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_teacher_message_more);
        img_more.setOnClickListener(this);
        txt_un_green = (TextView) findViewById(R.id.activity_teacher_message_un_agree);
        txt_green = (TextView) findViewById(R.id.activity_teacher_message_agree);
        txt_no_msg = (TextView) findViewById(R.id.activity_teacher_message_no_msg);
        img_head = (CircleImageView) findViewById(R.id.activity_teacher_message_headPortrait);
        img_head.setOnClickListener(this);
        txt_teacher_name = (TextView) findViewById(R.id.activity_teacher_message_realName);
        txt_teacher_nick_name = (TextView) findViewById(R.id.activity_teacher_message_nickName);
        txt_teacher_sex = (TextView) findViewById(R.id.activity_teacher_message_sex);
        txt_teacher_school_name = (TextView) findViewById(R.id.activity_teacher_message_txtSchool);
        pop_window_view = LayoutInflater.from(this).inflate(R.layout.pop_window_teacher_msg, null, false);
        txt_bind_teacher = (TextView) pop_window_view.findViewById(R.id.pop_window_teacher_msg_bind);
        txt_bind_teacher.setOnClickListener(this);
        txt_unbind_teacher = (TextView) pop_window_view.findViewById(R.id.pop_window_teacher_msg_unbind);
        txt_unbind_teacher.setOnClickListener(this);
        txt_change_teahcer = (TextView) pop_window_view.findViewById(R.id.pop_window_teacher_msg_change);
        txt_change_teahcer.setOnClickListener(this);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_teacher_message_smart_refresh);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initParam() {
        student = BmobManageUser.getCurrentUser();
        teacherManager = BmobManageTeacher.getManager();
        userManager = new BmobManageUser(this);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        txt_change_teahcer.setVisibility(View.GONE);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_teacher_message_back:
                finish();
                break;
            case R.id.activity_teacher_message_more:
                if (!refreshFinish) {
                    if (refreshIng) {
                        MyToastUtil.showToast("数据加载出错,请刷新界面后重试");
                    } else {
                        MyToastUtil.showToast("数据加载中,请稍后...");
                    }
                    return;
                }
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more, pop_window_view);
                showPop();
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START, arr[0] - 50, arr[1] - 50);
                break;
            case R.id.activity_teacher_message_headPortrait:
                if (nowTeacher != null) {
                    UserDetailsActivity.go2Activity(this, nowTeacher.getObjectId());
                }
                break;
            case R.id.pop_window_teacher_msg_bind:
                pop_window.dismiss();
                showBindDialog();
                break;
            case R.id.pop_window_teacher_msg_unbind:
                pop_window.dismiss();
                BaseNiceDialog b = DialogConfirm.newInstance("解除辅导员绑定", "你确定要解除辅导员的绑定吗?",
                        new DialogConfirm.CancleAndOkDo() {
                            @Override
                            public void cancle() {

                            }

                            @Override
                            public void ok() {
                                DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                                        new DialogLoading.ShowLoadingDone() {
                                            @Override
                                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                                dialog = baseNiceDialog;
                                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                                textView.setText("解除绑定...");
                                                bindType = 1;
                                                teacherManager.delMsg(mTeacherBean, TeacherMessageActivity.this);
                                            }
                                        });
                            }
                        }).setMargin(60)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
                break;
            case R.id.pop_window_teacher_msg_change:
                pop_window.dismiss();
                showBindDialog();
                break;
        }
    }

    private void showBindDialog() {
        NiceDialog.init()
                .setLayoutId(R.layout.pop_bind_teacher_one)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        initBindOneWidget(holder, dialog);
                    }
                })
                .setDimAmount(0.5f)
                .setShowBottom(false)
                .setWidth(-1)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void initBindOneWidget(ViewHolder holder, final BaseNiceDialog dialog) {
        final EditText edt_code = holder.getView(R.id.pop_bind_teacher_one_invitation_code);
        TextView txt_cancle = holder.getView(R.id.pop_bind_teacher_one_cancle);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txt_bind = holder.getView(R.id.pop_bind_teacher_one_bind);
        txt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code = edt_code.getText().toString();
                if (code == null || code.equals("")) {
                    MyToastUtil.showToast("邀请码不能为空");
                    return;
                }
                DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                        new DialogLoading.ShowLoadingDone() {
                            @Override
                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                textView.setText("信息确认中...");
                                String tempCode = code;
                                tempCode = tempCode.trim();
                                userManager.queryByID(tempCode, new BmobQueryDone<MyUserBean>() {
                                    @Override
                                    public void querySuccess(List<MyUserBean> data) {
                                        if (data.size() > 0) {
                                            dialog.dismiss();
                                            showBindTwoWidget(data.get(0));
                                            baseNiceDialog.dismiss();
                                        } else {
                                            MyToastUtil.showToast("邀请码错误,没有该辅导员");
                                            baseNiceDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void queryFailed(BmobException e) {
                                        if (e.getErrorCode() == 101) {
                                            MyToastUtil.showToast("邀请码错误,没有该辅导员");
                                        } else {
                                            BmobExceptionUtil.dealWithException(TeacherMessageActivity.this, e);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });
    }

    private void showBindTwoWidget(final MyUserBean teacher) {
        NiceDialog.init()
                .setLayoutId(R.layout.pop_bind_teacher_two)     //设置dialog布局文件
                .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        initBindTwoWidget(teacher, holder, dialog);
                    }
                })
                .setDimAmount(0.5f)
                .setShowBottom(false)
                .setWidth(-1)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void initBindTwoWidget(final MyUserBean teacher, ViewHolder holder, final BaseNiceDialog dialog) {
        TextView txt_real_name = holder.getView(R.id.pop_bind_teacher_two_teacher_name);
        TextView txt_school_name = holder.getView(R.id.pop_bind_teacher_two_school_name);
        TextView txt_cancle = holder.getView(R.id.pop_bind_teacher_two_cancle);
        TextView txt_bind = holder.getView(R.id.pop_bind_teacher_two_bind);
        txt_real_name.setText(teacher.getRealName());
        txt_school_name.setText(teacher.getSchoolName());

        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                        new DialogLoading.ShowLoadingDone() {
                            @Override
                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                textView.setText("绑定中...");
                                TeacherBean teacherBean = new TeacherBean();
                                teacherBean.setStudent(BmobManageUser.getCurrentUser());
                                teacherBean.setTeacher(teacher);
                                teacherBean.setAgreen(false);
                                teacherManager.uploadMsg(teacherBean, new BmobSaveMsgWithoutImg() {
                                    @Override
                                    public void msgSuccess(String objectID) {
                                        MyToastUtil.showToast("绑定成功");
                                        dialog.dismiss();
                                        baseNiceDialog.dismiss();
                                        refreshLayout.autoRefresh();
                                        nowTeacher = teacher;
                                    }

                                    @Override
                                    public void msgFailed(BmobException e) {
                                        BmobExceptionUtil.dealWithException(TeacherMessageActivity.this, e);
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        });
            }
        });
    }

    private void showPop() {
        if (bindTeacher) {
            txt_bind_teacher.setVisibility(View.GONE);
            txt_unbind_teacher.setVisibility(View.VISIBLE);
            txt_change_teahcer.setVisibility(View.GONE);
        } else {
            txt_bind_teacher.setVisibility(View.VISIBLE);
            txt_unbind_teacher.setVisibility(View.GONE);
            txt_change_teahcer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refreshIng = true;
        refreshFinish = false;
        teacherManager.queryByStudent(student, this);
    }

    @Override
    public void querySuccess(List<TeacherBean> data) {
        if (data.size() > 0) {
            if (!data.get(0).getAgreen()){
                txt_un_green.setVisibility(View.VISIBLE);
                txt_green.setVisibility(View.GONE);
            }else {
                txt_un_green.setVisibility(View.GONE);
                txt_green.setVisibility(View.VISIBLE);
            }
            txt_no_msg.setVisibility(View.GONE);
            txt_teacher_name.setText(data.get(0).getTeacher().getRealName());
            txt_teacher_nick_name.setText(data.get(0).getTeacher().getNickName());
            txt_teacher_sex.setText(data.get(0).getTeacher().getSex());
            txt_teacher_school_name.setText(data.get(0).getTeacher().getSchoolName());
            if (data.get(0).getTeacher().getHeadPortrait() == null || data.get(0).getTeacher().getHeadPortrait().getUrl() == null) {
                Glide.with(this)
                        .load(R.drawable.head_log1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(img_head);
            } else {
                Glide.with(this)
                        .load(ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(data.get(0).getTeacher().getHeadPortrait().getUrl())))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.head_log1)
                        .dontAnimate()
                        .into(img_head);
            }
            bindTeacher = true;
            txt_no_msg.setVisibility(View.GONE);
            ll_msg_body.setVisibility(View.VISIBLE);
            nowTeacher = data.get(0).getTeacher();
            mTeacherBean = data.get(0);
        } else {
            txt_un_green.setVisibility(View.GONE);
            txt_green.setVisibility(View.GONE);
            txt_no_msg.setVisibility(View.VISIBLE);
            ll_msg_body.setVisibility(View.GONE);
            bindTeacher = false;
        }
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this, e);
        finishSmart(false);
    }


    @Override
    public void delMsgSuc() {
        refreshLayout.autoRefresh();
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void delMsgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this, e);
    }

    private void finishSmart(boolean success) {
        if (success) {
            refreshFinish = true;
        }
        refreshIng = false;
        refreshLayout.finishRefresh(success);
    }

    public static void go2Activity(Context context) {
        context.startActivity(new Intent(context, TeacherMessageActivity.class));
    }

}
