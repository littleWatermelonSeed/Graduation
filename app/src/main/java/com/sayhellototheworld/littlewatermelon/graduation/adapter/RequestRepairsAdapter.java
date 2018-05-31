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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestRepairBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManagerRepairs;
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
 * Created by 123 on 2018/5/23.
 */

/**
 * -1：取消维修且维修员还没有处理
 * 0:等待处理
 * 1：已接收申请,等待上门维修
 * 2:取消维修且维修员已同意
 * 3：维修完成
 */

public class RequestRepairsAdapter extends RecyclerView.Adapter<RequestRepairsAdapter.RequestRepairsViewHolder> {

    private Context context;
    private List<RequestRepairBean> data;

    private ItemClick listener;

    public RequestRepairsAdapter(Context context, List<RequestRepairBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RequestRepairsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_repairs, parent, false);
        AutoUtils.autoSize(view);
        RequestRepairsViewHolder viewHolder = new RequestRepairsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestRepairsViewHolder holder, int position) {
        listener = new ItemClick(position);

        holder.txt_describe.setText(data.get(position).getDescribe());
        holder.txt_user_name.setText(data.get(position).getUserName());
        holder.txt_place.setText(data.get(position).getPlace());
        holder.txt_contact_num.setText(data.get(position).getContact());
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));

        if (data.get(position).getStatue() == -1){
            holder.txt_statue.setText("你在维修员处理前取消了维修");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue_2));
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.GONE);
            holder.ll_appointment_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 0){
            holder.txt_statue.setText("等待维修员处理中");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue0));
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.GONE);
            holder.ll_appointment_time.setVisibility(View.GONE);
        }else if (data.get(position).getStatue() == 1){
            holder.txt_statue.setText("维修员已处理你的维修申请,等待维修员上门");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.txt_done.setVisibility(View.VISIBLE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position,listener);
        }else if (data.get(position).getStatue() == 2){
            holder.txt_statue.setText("你在维修员维修前取消了维修");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue_2));
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position,listener);
        }else if (data.get(position).getStatue() == 3){
            holder.txt_statue.setText("已完成维修");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position,listener);
        }


        holder.txt_cancle.setOnClickListener(listener);
        holder.txt_done.setOnClickListener(listener);
    }

    private void showRepairsManMsg(RequestRepairsViewHolder holder, int position,ItemClick listener) {
        if (data.get(position).getRepairman().getRealName() != null && !data.get(position).getRepairman().getRealName().equals("")){
            holder.txt_man_name.setText(data.get(position).getRepairman().getRealName());
        }else {
            holder.txt_man_name.setText(data.get(position).getRepairman().getNickName());
        }

        if (data.get(position).getRepairman().getHeadPortrait() != null && !data.get(position).getRepairman().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getRepairman().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }

        holder.head.setOnClickListener(listener);
        holder.txt_appointment_time.setText(data.get(position).getAppointmentTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RequestRepairsViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_man_name;
        private CircleImageView head;
        private LinearLayout ll_man_body;
        private TextView txt_appointment_time;
        private LinearLayout ll_appointment_time;

        private TextView txt_describe;
        private TextView txt_contact_num;
        private TextView txt_place;
        private TextView txt_user_name;
        private TextView txt_statue;
        private TextView txt_release_time;
        private TextView txt_cancle;
        private TextView txt_done;

        public RequestRepairsViewHolder(View itemView) {
            super(itemView);
            txt_man_name = (TextView) itemView.findViewById(R.id.item_request_repairs_man_name);
            head = (CircleImageView) itemView.findViewById(R.id.item_request_repairs_head_portrait);
            ll_man_body = (LinearLayout) itemView.findViewById(R.id.item_request_repairs_man_body);
            txt_appointment_time = (TextView) itemView.findViewById(R.id.item_request_repairs_appointment_time);
            ll_appointment_time = (LinearLayout) itemView.findViewById(R.id.item_request_repairs_appointment_time_body);

            txt_describe = (TextView) itemView.findViewById(R.id.item_request_repairs_describe);
            txt_contact_num = (TextView) itemView.findViewById(R.id.item_request_repairs_contact);
            txt_place = (TextView) itemView.findViewById(R.id.item_request_repairs_place);
            txt_user_name = (TextView) itemView.findViewById(R.id.item_request_repairs_user_name);
            txt_statue = (TextView) itemView.findViewById(R.id.item_request_repairs_statue);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_request_repairs_release_time);
            txt_cancle = (TextView) itemView.findViewById(R.id.item_request_repairs_cancle);
            txt_done = (TextView) itemView.findViewById(R.id.item_request_repairs_done);
        }
    }

    class ItemClick implements View.OnClickListener{

        private int position;

        public ItemClick(int position) {
            this.position = position;
        }

        /**
         * -1：取消维修且维修员还没有处理
         * 0:等待处理
         * 1：已接收申请,等待上门维修
         * 2:取消维修且维修员已同意
         * 3：维修完成
         */
        @Override
        public void onClick(View v) {
            int tempStatue = 0;
            final String objID = data.get(position).getObjectId();
            String tempMsg1 = "";
            String tempMsg2 = "";
            boolean update = true;
            switch (v.getId()){
                case R.id.item_request_repairs_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getRepairman().getObjectId());
                    update = false;
                    break;
                case R.id.item_request_repairs_cancle:
                    tempMsg1 = "确定要取消维修申请?";
                    tempMsg2 = "取消申请中...";
                    if (data.get(position).getStatue() == 0){
                        tempStatue = -1;
                    }else if (data.get(position).getStatue() == 1){
                        tempStatue = 2;
                    }
                    break;
                case R.id.item_request_repairs_done:
                    tempMsg1 = "确定完成维修?";
                    tempMsg2 = "确认中...";
                    tempStatue = 3;
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
                                    BmobManagerRepairs.getManager().userUpdateSatue(objID, statue,new BmobUpdateDone() {
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
