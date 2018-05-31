package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.StudentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageStudent;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/28.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentBean> data;

    private StudentItemClick listener;

    public StudentAdapter(Context context, List<StudentBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        AutoUtils.autoSize(view);
        StudentViewHolder viewHolder = new StudentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {

        listener = new StudentItemClick(position);
        if (data.get(position).getStudent().getHeadPortrait() != null && !data.get(position).getStudent().getHeadPortrait().getUrl().equals("")) {
            Glide.with(context)
                    .load(data.get(position).getStudent().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        } else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }
        holder.head.setOnClickListener(listener);

        if (data.get(position).getStudent().getRealName() != null && !data.get(position).getStudent().getRealName().equals("")) {
            holder.txt_student_name.setText(data.get(position).getStudent().getRealName());
        } else {
            holder.txt_student_name.setText(data.get(position).getStudent().getNickName());
        }

        holder.btn_unbind.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView head;
        public TextView txt_student_name;
        public Button btn_unbind;

        public StudentViewHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.item_student_head_portrait);
            txt_student_name = (TextView) itemView.findViewById(R.id.item_student_student_name);
            btn_unbind = (Button) itemView.findViewById(R.id.item_student_unbind);
        }
    }

    class StudentItemClick implements View.OnClickListener {

        private int position;

        StudentItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_student_head_portrait:
                    UserDetailsActivity.go2Activity(context, data.get(position).getStudent().getObjectId());
                    break;
                case R.id.item_student_unbind:
                    DialogConfirm.newInstance("提示", "确定要取消与该同学的绑定?", new DialogConfirm.CancleAndOkDo() {
                        @Override
                        public void cancle() {

                        }

                        @Override
                        public void ok() {
                            unBind();

                        }
                    }).setMargin(60)
                            .setOutCancel(false)
                            .show(((FragmentActivity) context).getSupportFragmentManager());
                    break;
            }
        }

        private void unBind() {
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText("取消绑定中...");
                            BmobManageStudent.getManager().delMsg(data.get(position), new BmobDeletMsgDone() {
                                @Override
                                public void delMsgSuc() {
                                    BmobManageTeacher.getManager().teacherUnBindStudent(data.get(position).getStudent(), data.get(position).getTeacher());
                                    data.remove(position);
                                    notifyDataSetChanged();
                                    baseNiceDialog.dismiss();
                                    Log.i("niyuanjie", "学生删除成功");
                                }

                                @Override
                                public void delMsgFailed(BmobException e) {
                                    BmobExceptionUtil.dealWithException(context, e);
                                    Log.i("niyuanjie", "学生删除失败");
                                    baseNiceDialog.dismiss();
                                }
                            });
                        }
                    });
        }
    }

}
