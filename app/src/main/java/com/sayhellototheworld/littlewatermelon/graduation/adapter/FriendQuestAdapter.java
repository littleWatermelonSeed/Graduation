package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.FriendRequestMsgActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/30.
 */

public class FriendQuestAdapter extends RecyclerView.Adapter<FriendQuestAdapter.FriendQuestViewHolder> {

    private Context context;
    private List<FriendRequestBean> data;
    private int type;

    private FriendQuestClickListener listener;

    public FriendQuestAdapter(Context context, List<FriendRequestBean> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    public FriendQuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_friend_msg, parent, false);
        AutoUtils.autoSize(view);
        FriendQuestViewHolder holder = new FriendQuestViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendQuestViewHolder holder, int position) {
        listener = new FriendQuestClickListener(position);
        if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){
            doOwn( holder,  position,listener);
        }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){
            doRequest( holder,  position,listener);
        }
    }

    private void doRequest(FriendQuestViewHolder holder, int position,FriendQuestClickListener listener){
        if (data.get(position).getUser().getHeadPortrait() != null && !data.get(position).getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }
        holder.head.setOnClickListener(listener);

        holder.txt_user_name.setText(data.get(position).getUser().getNickName());
        holder.txt_remark.setText(data.get(position).getRemark());
        holder.txt_request_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));
        if (data.get(position).getStatue() == 0){
            holder.txt_statue.setVisibility(View.GONE);
            holder.btn_agree.setVisibility(View.VISIBLE);
            holder.btn_agree.setOnClickListener(listener);
            holder.btn_disagree.setVisibility(View.VISIBLE);
            holder.btn_disagree.setOnClickListener(listener);
        }else if (data.get(position).getStatue() == -1){
            holder.txt_statue.setVisibility(View.VISIBLE);
            holder.btn_agree.setVisibility(View.GONE);
            holder.btn_disagree.setVisibility(View.GONE);
            holder.txt_statue.setText("已拒绝");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue_1));
        }else if (data.get(position).getStatue() == 1){
            holder.txt_statue.setVisibility(View.VISIBLE);
            holder.btn_agree.setVisibility(View.GONE);
            holder.btn_disagree.setVisibility(View.GONE);
            holder.txt_statue.setText("已同意");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
        }
    }

    private void doOwn(FriendQuestViewHolder holder, int position,FriendQuestClickListener listener){
        holder.txt_statue.setVisibility(View.VISIBLE);
        holder.btn_agree.setVisibility(View.GONE);
        holder.btn_disagree.setVisibility(View.GONE);
        if (data.get(position).getFriend().getHeadPortrait() != null && !data.get(position).getFriend().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getFriend().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }
        holder.head.setOnClickListener(listener);

        holder.txt_user_name.setText(data.get(position).getFriend().getNickName());
        holder.txt_remark.setText(data.get(position).getRemark());
        holder.txt_request_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));
        if (data.get(position).getStatue() == 0){
            holder.txt_statue.setText("等待处理...");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue0));
        }else if (data.get(position).getStatue() == -1){
            holder.txt_statue.setText("已拒绝你的申请");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue_1));
        }else if (data.get(position).getStatue() == 1){
            holder.txt_statue.setText("已同意你的申请");
            holder.txt_statue.setTextColor(context.getResources().getColor(R.color.statue1));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FriendQuestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        TextView txt_user_name;
        TextView txt_remark;
        TextView txt_request_time;
        TextView txt_statue;
        Button btn_agree;
        Button btn_disagree;

        public FriendQuestViewHolder(View view) {
            super(view);
            head = (CircleImageView) view.findViewById(R.id.item_request_friend_msg_head_portrait);
            txt_user_name = (TextView) view.findViewById(R.id.item_request_friend_msg_user_name);
            txt_remark = (TextView) view.findViewById(R.id.item_request_friend_msg_remark);
            txt_request_time = (TextView) view.findViewById(R.id.item_request_friend_msg_request_time);
            txt_statue = (TextView) view.findViewById(R.id.item_request_friend_msg_statue);
            btn_agree = (Button) view.findViewById(R.id.item_request_friend_msg_agree);
            btn_disagree = (Button) view.findViewById(R.id.item_request_friend_msg_disagree);
        }
    }

    class FriendQuestClickListener implements View.OnClickListener {

        private int position;

        public FriendQuestClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_request_friend_msg_head_portrait:
                    if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN){
                        UserDetailsActivity.go2Activity(context,data.get(position).getFriend().getObjectId());
                    }else if (type == FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER){
                        UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    }
                    break;
                case R.id.item_request_friend_msg_agree:
                    agree();
                    break;
                case R.id.item_request_friend_msg_disagree:
                    disagree();
                    break;
            }
        }

        private void agree(){
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText("处理中...");
                            BmobManageFriend.getManager().uploadMsgWithoutImg(data.get(position), new BmobSaveMsgWithoutImg() {
                                @Override
                                public void msgSuccess(String objectID) {
                                    baseNiceDialog.dismiss();
                                    data.get(position).setStatue(1);
                                    notifyDataSetChanged();
                                    BmobManageRequestFriend.getManager().updateStatue(data.get(position).getObjectId(),1);
                                    MyToastUtil.showToast("已添加该好友");
                                }

                                @Override
                                public void msgFailed(BmobException e) {
                                    baseNiceDialog.dismiss();
                                    BmobExceptionUtil.dealWithException(context,e);
                                }
                            });
                        }
                    });
        }

        private void disagree(){
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText("处理中...");
                            BmobManageRequestFriend.getManager().updateStatue(data.get(position).getObjectId(), -1, new BmobUpdateDone() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        baseNiceDialog.dismiss();
                                        data.get(position).setStatue(-1);
                                        notifyDataSetChanged();
                                        MyToastUtil.showToast("已拒绝该好友");
                                    }else {
                                        baseNiceDialog.dismiss();
                                        BmobExceptionUtil.dealWithException(context,e);
                                    }
                                }
                            });
                        }
                    });
        }
    }

}
