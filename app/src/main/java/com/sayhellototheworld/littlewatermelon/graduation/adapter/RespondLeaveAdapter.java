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
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/21.
 */

public class RespondLeaveAdapter extends RecyclerView.Adapter<RespondLeaveAdapter.RespondLeaveViewHolder>{

    private Context context;
    private List<RequestLeaveBean> data;

    private ItemClick listener;

    public RespondLeaveAdapter(Context context, List<RequestLeaveBean> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public RespondLeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_respond_leave, parent, false);
        AutoUtils.autoSize(view);
        RespondLeaveViewHolder viewHolder = new RespondLeaveViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RespondLeaveViewHolder holder, int position) {
        listener = new ItemClick(position);

        holder.txt_student_name.setText(data.get(position).getStudenName());
        holder.txt_class_num.setText(data.get(position).getClassNum());
        holder.txt_reason.setText(data.get(position).getReason());
        holder.txt_begin_time.setText(data.get(position).getBeginTime());
        holder.txt_end_time.setText(data.get(position).getEndTime());
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));

        if (data.get(position).getStatue() == 0){
            holder.txt_title.setText("等待您的处理中");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue0));
            holder.txt_agree.setVisibility(View.VISIBLE);
            holder.txt_disagree.setVisibility(View.VISIBLE);
            holder.ll_real_back_time_body.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 1){
            holder.txt_title.setText("您已经同意了该同学的请假申请");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_agree.setVisibility(View.GONE);
            holder.txt_disagree.setVisibility(View.GONE);
            holder.ll_real_back_time_body.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == -1){
            holder.txt_title.setText("您已经拒绝了该同学的请假申请");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue_1));
            holder.txt_agree.setVisibility(View.GONE);
            holder.txt_disagree.setVisibility(View.GONE);
            holder.ll_real_back_time_body.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 2){
            holder.txt_title.setText("该同学取消了请假");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue2));
            holder.txt_agree.setVisibility(View.GONE);
            holder.txt_disagree.setVisibility(View.GONE);
            holder.ll_real_back_time_body.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 3){
            holder.txt_title.setText("该同学已归假");
            holder.txt_title.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_agree.setVisibility(View.GONE);
            holder.txt_disagree.setVisibility(View.GONE);
            holder.ll_real_back_time_body.setVisibility(View.VISIBLE);
            holder.txt_real_back_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getBackTime().getDate())));
        }

        holder.txt_agree.setOnClickListener(listener);
        holder.txt_disagree.setOnClickListener(listener);

        if (data.get(position).getStudent().getRealName() != null && !data.get(position).getStudent().getRealName().equals("")){
            holder.txt_user_name.setText(data.get(position).getStudent().getRealName());
        }else {
            holder.txt_user_name.setText(data.get(position).getStudent().getNickName());
        }

        if (data.get(position).getStudent().getHeadPortrait() != null && !data.get(position).getStudent().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getStudent().getHeadPortrait().getUrl())
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

    class RespondLeaveViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_title;
        private TextView txt_student_name;
        private TextView txt_class_num;
        private TextView txt_reason;
        private TextView txt_begin_time;
        private TextView txt_end_time;
        private TextView txt_release_time;
        private TextView txt_disagree;
        private TextView txt_agree;
        private CircleImageView head;
        private TextView txt_user_name;
        private TextView txt_real_back_time;
        private LinearLayout ll_real_back_time_body;

        public RespondLeaveViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.item_respond_leave_title);
            txt_student_name = (TextView) itemView.findViewById(R.id.item_respond_leave_student_name);
            txt_class_num = (TextView) itemView.findViewById(R.id.item_respond_leave_class_num);
            txt_reason = (TextView) itemView.findViewById(R.id.item_respond_leave_reason);
            txt_begin_time = (TextView) itemView.findViewById(R.id.item_respond_leave_begin_time);
            txt_end_time = (TextView) itemView.findViewById(R.id.item_respond_leave_end_time);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_respond_leave_release_time);
            txt_disagree = (TextView) itemView.findViewById(R.id.item_respond_leave_disagree);
            txt_agree = (TextView) itemView.findViewById(R.id.item_respond_leave_agree);
            head = (CircleImageView) itemView.findViewById(R.id.item_respond_leave_head_portrait);
            txt_user_name = (TextView) itemView.findViewById(R.id.item_respond_leave_name);
            txt_real_back_time = (TextView) itemView.findViewById(R.id.item_respond_leave_real_end_time);
            ll_real_back_time_body = (LinearLayout) itemView.findViewById(R.id.item_respond_leave_real_end_time_body);
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
            switch (v.getId()) {
                case R.id.item_respond_leave_disagree:
                    tempMsg2 = "拒绝中...";
                    tempMsg1 = "确定拒绝该同学的请假申请?";
                    tempStatue = -1;
                    break;
                case R.id.item_respond_leave_agree:
                    tempMsg2 = "同意中...";
                    tempMsg1 = "确定同意该同学的请假申请?";
                    tempStatue = 1;

                    break;
                case R.id.item_respond_leave_head_portrait:
                    update = false;
                    UserDetailsActivity.go2Activity(context,data.get(position).getTeahcer().getObjectId());
                    break;
            }

            if (!update){
                return;
            }

            final int statue = tempStatue;
            final String msg = tempMsg2;

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
                                    BmobManageRequestLeave.getManager().teacherUpdateSatue(objID, statue,new BmobUpdateDone() {
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
