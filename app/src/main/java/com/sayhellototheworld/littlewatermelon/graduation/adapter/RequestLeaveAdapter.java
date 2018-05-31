package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestLeaveBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestLeave;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/21.
 */

/**
 * -2：取消请假且教师还没有处理
 * -1：拒绝申请
 * 0:审核中
 * 1：同意申请
 * 2:取消请假且教师已同意
 * 3：已归假
 */

public class RequestLeaveAdapter extends RecyclerView.Adapter<RequestLeaveAdapter.RequestLeaveViewHolder> {

    private Context context;
    private List<RequestLeaveBean> data;

    private ItemClick listener;

    public RequestLeaveAdapter(Context context, List<RequestLeaveBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RequestLeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_leave_student, parent, false);
        AutoUtils.autoSize(view);
        RequestLeaveViewHolder viewHolder = new RequestLeaveViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestLeaveViewHolder holder, int position) {
        listener = new ItemClick(position);

        holder.txt_student_name.setText(data.get(position).getStudenName());
        holder.txt_class_num.setText(data.get(position).getClassNum());
        holder.txt_reason.setText(data.get(position).getReason());
        holder.txt_begin_time.setText(data.get(position).getBeginTime());
        holder.txt_end_time.setText(data.get(position).getEndTime());
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));

        if (data.get(position).getStatue() == 0){
            holder.txt_title.setText("等待老师处理中");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue0));
            holder.txt_back.setVisibility(View.GONE);
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.ll_real_back_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 1){
            holder.txt_title.setText("教师已同意你的请假申请");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_back.setVisibility(View.VISIBLE);
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.ll_real_back_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == -1){
            holder.txt_title.setText("教师拒绝了你的请假申请");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue_1));
            holder.txt_back.setVisibility(View.GONE);
            holder.txt_cancle.setVisibility(View.GONE);
            holder.ll_real_back_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 2){
            holder.txt_title.setText("在教师同意申请后,你取消了请假");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue2));
            holder.txt_back.setVisibility(View.GONE);
            holder.txt_cancle.setVisibility(View.GONE);
            holder.ll_real_back_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 3){
            holder.txt_title.setText("已归假");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_back.setVisibility(View.GONE);
            holder.txt_cancle.setVisibility(View.GONE);
            holder.ll_real_back_time.setVisibility(View.VISIBLE);
            holder.txt_real_back_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getBackTime().getDate())));
        }else if (data.get(position).getStatue() == -2){
            holder.txt_title.setText("在教师没有处理前，你取消了请假");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue_2));
            holder.txt_back.setVisibility(View.GONE);
            holder.txt_cancle.setVisibility(View.GONE);
            holder.ll_real_back_time.setVisibility(View.GONE);
        }
        holder.txt_back.setOnClickListener(listener);
        holder.txt_cancle.setOnClickListener(listener);

        if (data.get(position).getTeahcer().getRealName() != null && !data.get(position).getTeahcer().getRealName().equals("")){
            holder.txt_teacher_name.setText(data.get(position).getTeahcer().getRealName());
        }else {
            holder.txt_teacher_name.setText(data.get(position).getTeahcer().getNickName());
        }

        if (data.get(position).getTeahcer().getHeadPortrait() != null && !data.get(position).getTeahcer().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getTeahcer().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }
        holder.head.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RequestLeaveViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private TextView txt_student_name;
        private TextView txt_class_num;
        private TextView txt_reason;
        private TextView txt_begin_time;
        private TextView txt_end_time;
        private TextView txt_release_time;
        private TextView txt_cancle;
        private TextView txt_back;
        private CircleImageView head;
        private TextView txt_teacher_name;
        private LinearLayout ll_real_back_time;
        private TextView txt_real_back_time;

        public RequestLeaveViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.item_request_leave_student_title);
            txt_student_name = (TextView) itemView.findViewById(R.id.item_request_leave_student_student_name);
            txt_class_num = (TextView) itemView.findViewById(R.id.item_request_leave_student_class_num);
            txt_reason = (TextView) itemView.findViewById(R.id.item_request_leave_student_reason);
            txt_begin_time = (TextView) itemView.findViewById(R.id.item_request_leave_student_begin_time);
            txt_end_time = (TextView) itemView.findViewById(R.id.item_request_leave_student_end_time);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_request_leave_student_release_time);
            txt_cancle = (TextView) itemView.findViewById(R.id.item_request_leave_student_cancle);
            txt_back = (TextView) itemView.findViewById(R.id.item_request_leave_student_back);
            head = (CircleImageView) itemView.findViewById(R.id.item_request_leave_student_head_portrait);
            txt_teacher_name = (TextView) itemView.findViewById(R.id.item_request_leave_student_teacher_name);
            ll_real_back_time = (LinearLayout) itemView.findViewById(R.id.item_request_leave_student_real_end_time_body);
            txt_real_back_time = (TextView) itemView.findViewById(R.id.item_request_leave_student_real_end_time);
        }
    }

    class ItemClick implements View.OnClickListener {

        int position;

        public ItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int tempStatue = 0;
            final String objID = data.get(position).getObjectId();
            String tempMsg1 = "";
            String tempMsg2 = "";
            boolean update = true;
            boolean tempBack = false;
            switch (v.getId()) {
                case R.id.item_request_leave_student_cancle:
                    tempMsg2 = "取消请假申请中...";
                    tempMsg1 = "确定取消请假申请?";
                    if (data.get(position).getStatue() == 0){
                        tempStatue = -2;
                    }else if (data.get(position).getStatue() == 1){
                        tempStatue = 2;
                    }
                    tempBack = false;
                    break;
                case R.id.item_request_leave_student_back:
                    tempMsg2 = "归假处理中...";
                    tempMsg1 = "确定归假?";
                    tempStatue = 3;
                    tempBack = true;
                    break;
                case R.id.item_request_leave_student_head_portrait:
                    update = false;
                    UserDetailsActivity.go2Activity(context,data.get(position).getTeahcer().getObjectId());
                    break;
            }
            if (!update){
                return;
            }
            final int statue = tempStatue;
            final String msg = tempMsg2;
            final boolean back = tempBack;

            DialogConfirm.newInstance("提示", tempMsg1, new DialogConfirm.CancleAndOkDo() {
                @Override
                public void cancle() {

                }

                @Override
                public void ok() {
                    DialogLoading.showLoadingDialog(((FragmentActivity)context).getSupportFragmentManager(),
                            new DialogLoading.ShowLoadingDone() {
                                @Override
                                public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                    TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                    textView.setText(msg);
                                    BmobManageRequestLeave.getManager().studentUpdateSatue(objID, statue, back,new BmobUpdateDone() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null){
                                                data.get(position).setStatue(statue);
                                                notifyDataSetChanged();
                                                baseNiceDialog.dismiss();
                                                MyToastUtil.showToast("操作成功");
                                            }else {
                                                BmobExceptionUtil.dealWithException(context,e);
                                                baseNiceDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                }
            }).setMargin(60)
                    .setOutCancel(false)
                    .show(((FragmentActivity)context).getSupportFragmentManager());

        }
    }

}
