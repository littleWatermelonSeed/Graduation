package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.RequestRepairBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManagerRepairs;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.respond_repairs.RespondRepairActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/23.
 */

public class ResponRepairsAdapter extends RecyclerView.Adapter<ResponRepairsAdapter.ResponRepairsViewHolder> {

    private Context context;
    private List<RequestRepairBean> data;
    private int type;

    private ItemClick listener;
//    private static String appointmentTime;

    public ResponRepairsAdapter(Context context, List<RequestRepairBean> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    public ResponRepairsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_respond_repairs, parent, false);
        AutoUtils.autoSize(view);
        ResponRepairsViewHolder viewHolder = new ResponRepairsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResponRepairsViewHolder holder, int position) {
        listener = new ItemClick(position);

        holder.txt_describe.setText(data.get(position).getDescribe());
        holder.txt_user_name.setText(data.get(position).getUserName());
        holder.txt_place.setText(data.get(position).getPlace());
        holder.txt_contact_num.setText(data.get(position).getContact());
        holder.txt_contact_num.setOnClickListener(listener);
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));

        if (type == RespondRepairActivity.REPAIRS_TYPE_ALL) {
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.VISIBLE);
        } else if (type == RespondRepairActivity.REPAIRS_TYPE_OWN) {
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.txt_done.setVisibility(View.GONE);
        }

        if (data.get(position).getStatue() == 0) {
            holder.txt_statue.setText("等待维修员处理中");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue0));
            holder.ll_man_body.setVisibility(View.GONE);
            holder.ll_appointment_time.setVisibility(View.GONE);
        } else if (data.get(position).getStatue() == 1) {
            holder.txt_statue.setText("你已接受申请,请及时上门维修");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_cancle.setVisibility(View.VISIBLE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position, listener);
        } else if (data.get(position).getStatue() == 2) {
            holder.txt_statue.setText("申请人取消了维修申请");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue_2));
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position, listener);
        } else if (data.get(position).getStatue() == 3) {
            holder.txt_statue.setText("申请人已确定完成维修");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
            holder.txt_cancle.setVisibility(View.GONE);
            holder.txt_done.setVisibility(View.GONE);
            holder.ll_man_body.setVisibility(View.VISIBLE);
            holder.ll_appointment_time.setVisibility(View.VISIBLE);

            showRepairsManMsg(holder, position, listener);
        }


        holder.txt_cancle.setOnClickListener(listener);
        holder.txt_done.setOnClickListener(listener);
    }

    private void showRepairsManMsg(ResponRepairsViewHolder holder, int position, ItemClick listener) {
        if (data.get(position).getUser().getRealName() != null && !data.get(position).getUser().getRealName().equals("")) {
            holder.txt_people_name.setText(data.get(position).getUser().getRealName());
        } else {
            holder.txt_people_name.setText(data.get(position).getUser().getNickName());
        }

        if (data.get(position).getUser().getHeadPortrait() != null && !data.get(position).getUser().getHeadPortrait().getUrl().equals("")) {
            Glide.with(context)
                    .load(data.get(position).getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        } else {
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

    class ResponRepairsViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_people_name;
        private CircleImageView head;
        private LinearLayout ll_man_body;
        private TextView txt_appointment_time;
        private LinearLayout ll_appointment_time;

        private LinearLayout ll_statue_body;
        private TextView txt_describe;
        private TextView txt_contact_num;
        private TextView txt_place;
        private TextView txt_user_name;
        private TextView txt_statue;
        private TextView txt_release_time;
        private TextView txt_cancle;
        private TextView txt_done;

        public ResponRepairsViewHolder(View itemView) {
            super(itemView);
            txt_people_name = (TextView) itemView.findViewById(R.id.item_respond_repairs_people_name);
            head = (CircleImageView) itemView.findViewById(R.id.item_respond_repairs_head_portrait);
            ll_man_body = (LinearLayout) itemView.findViewById(R.id.item_respond_repairs_man_body);
            txt_appointment_time = (TextView) itemView.findViewById(R.id.item_respond_repairs_appointment_time);
            ll_appointment_time = (LinearLayout) itemView.findViewById(R.id.item_respond_repairs_appointment_time_body);

            txt_describe = (TextView) itemView.findViewById(R.id.item_respond_repairs_describe);
            txt_contact_num = (TextView) itemView.findViewById(R.id.item_respond_repairs_contact);
            txt_place = (TextView) itemView.findViewById(R.id.item_respond_repairs_place);
            txt_user_name = (TextView) itemView.findViewById(R.id.item_respond_repairs_user_name);
            txt_statue = (TextView) itemView.findViewById(R.id.item_respond_repairs_statue);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_respond_repairs_release_time);
            txt_cancle = (TextView) itemView.findViewById(R.id.item_respond_repairs_cancle);
            txt_done = (TextView) itemView.findViewById(R.id.item_respond_repairs_done);
            ll_statue_body = (LinearLayout) itemView.findViewById(R.id.item_respond_repairs_statue_body);
        }
    }

    class ItemClick implements View.OnClickListener {

        private int position;
        private String appointmentTime = "";

        public ItemClick(int position) {
            this.position = position;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public void setAppointmentTime(String appointmentTime) {
            this.appointmentTime = appointmentTime;
        }

        @Override
        public void onClick(View v) {
            int tempStatue = 0;
            final String objID = data.get(position).getObjectId();
            String tempMsg1 = "";
            String tempMsg2 = "";
            boolean update = true;
            boolean tempCancle = true;
            switch (v.getId()) {
                case R.id.item_respond_repairs_head_portrait:
                    UserDetailsActivity.go2Activity(context, data.get(position).getUser().getObjectId());
                    update = false;
                    break;
                case R.id.item_respond_repairs_contact:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri uri = Uri.parse("tel:" + data.get(position).getContact());
                    intent.setData(uri);
                    context.startActivity(intent);
                    update = false;
                    break;
                case R.id.item_respond_repairs_cancle:
                    tempStatue = 0;
                    tempMsg1 = "你确定取消维修?";
                    tempMsg2 = "取消中...";
                    tempCancle = true;
                    break;
                case R.id.item_respond_repairs_done:
                    tempStatue = 1;
                    tempMsg1 = "你确定接受此维修申请?";
                    tempMsg2 = "接受中...";
                    tempCancle = false;
                    break;
            }

            if (!update) {
                return;
            }

            updateStatue(tempStatue, objID, tempMsg1, tempMsg2, tempCancle);

        }

        private void updateStatue(int tempStatue, final String objID, String tempMsg1, String tempMsg2, boolean tempCancle) {
            final int statue = tempStatue;
            final String msg = tempMsg2;
            final boolean cancle = tempCancle;

            DialogConfirm.newInstance("提示", tempMsg1, new DialogConfirm.CancleAndOkDo() {
                @Override
                public void cancle() {

                }

                @Override
                public void ok() {
                    if (cancle) {
                        cancleRepairs(msg, objID, statue);
                    } else {
                        dealRepairs(msg, objID, statue);
                    }

                }
            }).setMargin(60)
                    .setOutCancel(false)
                    .show(((FragmentActivity) context).getSupportFragmentManager());
        }

        private void dealRepairs(final String msg, final String objID, final int statue) {
            NiceDialog.init()
                    .setLayoutId(R.layout.pop_chose_time)
                    .setConvertListener(new ViewConvertListener() {
                        @Override

                        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                            final TextView txt_appointment_time = holder.getView(R.id.pop_chose_time_appointment);
                            TextView txt_cancle = holder.getView(R.id.pop_chose_time_cancle);
                            TextView txt_ensure = holder.getView(R.id.pop_chose_time_ensure);

                            txt_appointment_time.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerView pvTime = new
                                            TimePickerView.Builder(dialog.getContext(), new TimePickerView.OnTimeSelectListener() {
                                        @Override
                                        public void onTimeSelect(Date date,
                                                                 View v) {
                                            if (date.before(new Date()) && !SysUtil.sameDate(date, new Date())) {
                                                MyToastUtil.showToast("维修时间必须在今天或今天之后，请重新选择开始时间");
                                                return;
                                            }
                                            setAppointmentTime(TimeFormatUtil.DateToRealTime(date));
                                            txt_appointment_time.setText(TimeFormatUtil.DateToRealTime(date));
                                        }
                                    }).setType(new boolean[]{true, true, true,
                                            true, true, false}).isDialog(true).build();
                                    pvTime.setDate(Calendar.getInstance());
                                    pvTime.show();
                                }
                            });

                            txt_cancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            txt_ensure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (getAppointmentTime().equals("")) {
                                        MyToastUtil.showToast("请先选择上门维修时间");
                                        return;
                                    }
                                    doDealRepairs(msg, objID, statue,dialog);

                                }
                            });
                        }
                    })
                    .setDimAmount(0.5f)
                    .setShowBottom(false)
                    .setWidth(-1)
                    .setOutCancel(false)
                    .show(((FragmentActivity) context).getSupportFragmentManager());
        }

        private void doDealRepairs(final String msg, final String objID, final int statue, final BaseNiceDialog dialog) {
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText(msg);
                            BmobManagerRepairs.getManager().ensureStatueAndUpdateStatue(objID, statue, getAppointmentTime(), baseNiceDialog, new BmobUpdateDone() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        data.remove(position);
                                        notifyDataSetChanged();
                                        baseNiceDialog.dismiss();
                                        dialog.dismiss();
                                        MyToastUtil.showToast("处理成功,请到我的处理中查看维修详细信息");
                                    } else {
                                        BmobExceptionUtil.dealWithException(context, e);
                                        baseNiceDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
        }

        private void cancleRepairs(final String msg, final String objID, final int statue) {
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText(msg);
                            BmobManagerRepairs.getManager().repairManUpdateSatueWithoutTime(objID, statue, new BmobUpdateDone() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        data.remove(position);
                                        notifyDataSetChanged();
                                        baseNiceDialog.dismiss();
                                        MyToastUtil.showToast("操作成功");
                                    } else {
                                        BmobExceptionUtil.dealWithException(context, e);
                                        baseNiceDialog.dismiss();
                                    }
                                }
                            });
                        }
                    });
        }

    }

}
