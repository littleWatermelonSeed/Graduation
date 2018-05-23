package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.AnnouncementBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageAnnouncement;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.announcement.AnnouncementActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2018/5/22.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{

     private Context context;
     private List<AnnouncementBean> data;
     int type;

     private ItemClick listener;

    public AnnouncementAdapter(Context context, List<AnnouncementBean> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_announcement, parent, false);
        AutoUtils.autoSize(view);
        AnnouncementViewHolder viewHolder = new AnnouncementViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
        holder.txt_title.setText(data.get(position).getTitle());
        holder.txt_content.setText(data.get(position).getContent());
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));

        if (type == AnnouncementActivity.ANNOUNCEMENT_TYPE_STUDENT){
            holder.ll_teacher_body.setVisibility(View.VISIBLE);
            if (data.get(position).getTeacher().getRealName() != null && !data.get(position).getTeacher().getRealName().equals("")){
                holder.txt_teacher_name.setText(data.get(position).getTeacher().getRealName());
            }else {
                holder.txt_teacher_name.setText(data.get(position).getTeacher().getNickName());
            }
            holder.ll_delete.setVisibility(View.GONE);
        }else if (type == AnnouncementActivity.ANNOUNCEMENT_TYPE_TEACHER){
            holder.ll_teacher_body.setVisibility(View.GONE);
            holder.ll_delete.setVisibility(View.VISIBLE);
            listener = new ItemClick(position);
            holder.ll_delete.setOnClickListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AnnouncementViewHolder extends RecyclerView.ViewHolder {

         TextView txt_title;
         TextView txt_content;
         TextView txt_teacher_name;
         TextView txt_release_time;
         LinearLayout ll_teacher_body;
         LinearLayout ll_delete;

        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.item_announcement_title);
            txt_content = (TextView) itemView.findViewById(R.id.item_announcement_content);
            txt_teacher_name = (TextView) itemView.findViewById(R.id.item_announcement_teacher_name);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_announcement_release_time);
            ll_teacher_body = (LinearLayout) itemView.findViewById(R.id.item_announcement_teacher_body);
            ll_delete = (LinearLayout) itemView.findViewById(R.id.item_announcement_delete);
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
                case R.id.item_announcement_delete:
                    DialogConfirm.newInstance("提示", "确定删除本条公告?", new DialogConfirm.CancleAndOkDo() {
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
                                            textView.setText("删除中...");
                                            BmobManageAnnouncement.getManager().delMsg(data.get(position), new BmobDeletMsgDone() {
                                                @Override
                                                public void delMsgSuc() {
                                                    data.remove(position);
                                                    notifyDataSetChanged();
                                                    baseNiceDialog.dismiss();
                                                }

                                                @Override
                                                public void delMsgFailed(BmobException e) {
                                                    baseNiceDialog.dismiss();
                                                    BmobExceptionUtil.dealWithException(context,e);
                                                }
                                            });
                                        }
                                    });
                        }
                    }).setMargin(60)
                            .setOutCancel(false)
                            .show(((FragmentActivity)context).getSupportFragmentManager());
                    break;
            }
        }
    }
    
}
