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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.teacher_view.BindTeacherMsgActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/16.
 */

public class BindTeacherAdapter extends RecyclerView.Adapter<BindTeacherAdapter.BindTeacherViewHolder>{
    
    private Context context;
    private int type;
    private List<TeacherBean> data;
    private ItemClick listener;

    public BindTeacherAdapter(Context context, List<TeacherBean> data, int type) {
        this.context = context;
        this.type = type;
        this.data = data;
    }

    @Override
    public BindTeacherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bind_teacher_msg, parent, false);
        AutoUtils.autoSize(view);
        BindTeacherViewHolder viewHolder = new BindTeacherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BindTeacherViewHolder holder, int position) {
        listener = new ItemClick(position);
        if (type == BindTeacherMsgActivity.BIND_TEACHER_TYPE_STUDENT){
            studentBindViewHolder(holder, position,listener);
        }else if (type == BindTeacherMsgActivity.BIND_TEACHER_TYPE_TEACHER){
            teacherBindViewHolder(holder, position,listener);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void studentBindViewHolder(BindTeacherViewHolder holder, int position,ItemClick listener){
        holder.ll_chos_body.setVisibility(View.GONE);
        holder.txt_statue.setVisibility(View.VISIBLE);
        holder.txt_title.setText("我的申请消息");

        if (data.get(position).getStatue() == 0){
            holder.txt_statue.setText("等待老师处理中...");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen1);
        }else if (data.get(position).getStatue() == -1){
            holder.txt_statue.setText("老师拒绝了你的申请");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen3);
        }else if (data.get(position).getStatue() == 1){
            holder.txt_statue.setText("你的申请已通过");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen4);
        }else if (data.get(position).getStatue() == -2){
            holder.txt_statue.setText("老师取消了与你的绑定");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen3);
        }

        if (data.get(position).getTeacher().getRealName() == null || data.get(position).getTeacher().getRealName().equals("")){
            holder.txt_user_name.setText("老师  " + data.get(position).getTeacher().getNickName());
        }else {
            holder.txt_user_name.setText("老师  " + data.get(position).getTeacher().getRealName());
        }

        holder.txt_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));
        if (data.get(position).getRemark() != null){
            holder.txt_remark.setText(data.get(position).getRemark());
        }

        if (data.get(position).getTeacher().getHeadPortrait() != null && !data.get(position).getTeacher().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getTeacher().getHeadPortrait().getUrl())
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

    private void teacherBindViewHolder(BindTeacherViewHolder holder, int position,ItemClick listener){
        holder.txt_title.setText("学生的申请消息");

        if (data.get(position).getStatue() == 0){
            holder.ll_chos_body.setVisibility(View.VISIBLE);
            holder.txt_statue.setVisibility(View.GONE);
            holder.txt_disagree.setOnClickListener(listener);
            holder.txt_agree.setOnClickListener(listener);
        }else if (data.get(position).getStatue() == -1){
            holder.ll_chos_body.setVisibility(View.GONE);
            holder.txt_statue.setVisibility(View.VISIBLE);
            holder.txt_statue.setText("已拒绝申请");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen3);
        }else if (data.get(position).getStatue() == 1){
            holder.ll_chos_body.setVisibility(View.GONE);
            holder.txt_statue.setVisibility(View.VISIBLE);
            holder.txt_statue.setText("已同意申请");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen4);
        }else if (data.get(position).getStatue() == -2){
            holder.ll_chos_body.setVisibility(View.GONE);
            holder.txt_statue.setVisibility(View.VISIBLE);
            holder.txt_statue.setText("你取消了与该同学的绑定");
            holder.txt_statue.setBackgroundResource(R.drawable.radius_background_buttongreen3);
        }

        if (data.get(position).getStudent().getRealName() == null || data.get(position).getStudent().getRealName().equals("")){
            holder.txt_user_name.setText("学生  " + data.get(position).getStudent().getNickName());
        }else {
            holder.txt_user_name.setText("学生  " + data.get(position).getStudent().getRealName());
        }

        holder.txt_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));
        if (data.get(position).getRemark() != null){
            holder.txt_remark.setText(data.get(position).getRemark());
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

    class BindTeacherViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_title;
        private CircleImageView head;
        private TextView txt_user_name;
        private TextView txt_time;
        private TextView txt_remark;
        private LinearLayout ll_chos_body;
        private TextView txt_disagree;
        private TextView txt_agree;
        private TextView txt_statue;
        
        public BindTeacherViewHolder(View itemView) {
            super(itemView);
             txt_title = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_title);
             head = (CircleImageView) itemView.findViewById(R.id.item_bind_teacher_msg_head_portrait);
             txt_user_name = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_real_name);
             txt_time = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_create_time);
             txt_remark = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_remark);
             ll_chos_body = (LinearLayout) itemView.findViewById(R.id.item_bind_teacher_msg_chose_body);
             txt_disagree = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_disagree);
             txt_agree = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_agree);
             txt_statue = (TextView) itemView.findViewById(R.id.item_bind_teacher_msg_statue);
        }
        
    }
    
    class ItemClick implements View.OnClickListener{
        
        private int position;

        public ItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_bind_teacher_msg_disagree:
                    disAgree();
                    break;
                case R.id.item_bind_teacher_msg_agree:
                    agree();
                    break;
                case R.id.item_bind_teacher_msg_head_portrait:
                    if (type == BindTeacherMsgActivity.BIND_TEACHER_TYPE_STUDENT){
                        UserDetailsActivity.go2Activity(context,data.get(position).getTeacher().getObjectId());
                    }else if (type == BindTeacherMsgActivity.BIND_TEACHER_TYPE_TEACHER){
                        UserDetailsActivity.go2Activity(context,data.get(position).getStudent().getObjectId());
                    }
                    break;
            }
        }

        private void agree() {
            DialogConfirm.newInstance("提示", "确定同意同学的申请?", new DialogConfirm.CancleAndOkDo() {
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
                                    textView.setText("同意绑定中...");
                                    BmobManageTeacher.getManager().agreeBind(data.get(position), new BmobUpdateDone() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null){
                                                data.get(position).setStatue(1);
                                                notifyDataSetChanged();
                                            }else {
                                                BmobExceptionUtil.dealWithException(context,e);
                                            }
                                            baseNiceDialog.dismiss();
                                        }
                                    });
                                }
                            });
                }
            }).setMargin(60)
                    .setOutCancel(false)
                    .show(((FragmentActivity)context).getSupportFragmentManager());
        }

        private void disAgree() {
            DialogConfirm.newInstance("提示", "确定拒绝该同学的申请?", new DialogConfirm.CancleAndOkDo() {
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
                                    textView.setText("拒绝中...");
                                    BmobManageTeacher.getManager().disAgreeBind(data.get(position), new BmobUpdateDone() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null){
                                                data.get(position).setStatue(-1);
                                                notifyDataSetChanged();
                                            }else {
                                                BmobExceptionUtil.dealWithException(context,e);
                                            }
                                            baseNiceDialog.dismiss();
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
