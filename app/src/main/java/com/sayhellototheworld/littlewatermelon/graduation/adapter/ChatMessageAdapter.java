package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.im.ControlChat;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/6/1.
 */

public class ChatMessageAdapter  extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    private List<BmobIMMessage> data = new ArrayList<>();
    private Context context;
    private String friendHeadUrl = "";
    private ControlChat controlChat;
    private RecyclerView recyclerView;

    private ChatMessageClick listener;
    private String ownID;
    private String ownHeadUrl = "";

    public ChatMessageAdapter(Context context,String friendHeadUrl,ControlChat controlChat,RecyclerView recyclerView) {
        this.context = context;
        this.friendHeadUrl = friendHeadUrl;
        this.controlChat = controlChat;
        this.recyclerView = recyclerView;
        ownID = BmobManageUser.getCurrentUser().getObjectId();

        if (BmobManageUser.getCurrentUser().getHeadPortrait() != null && !BmobManageUser.getCurrentUser().getHeadPortrait().getUrl().equals("")){
            ownHeadUrl = BmobManageUser.getCurrentUser().getHeadPortrait().getUrl();
        }
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        AutoUtils.autoSize(view);
        ChatMessageViewHolder holder = new ChatMessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        if (data.get(position).getFromId().equals(ownID)){
            holder.ll_left_body.setVisibility(View.GONE);
            holder.ll_right_body.setVisibility(View.VISIBLE);
            rightMsg(holder,position);
        }else {
            holder.ll_left_body.setVisibility(View.VISIBLE);
            holder.ll_right_body.setVisibility(View.GONE);
            leftMsg(holder,position);
        }
    }

    private void leftMsg(ChatMessageViewHolder holder, int position){
        holder.txt_left_content.setText(data.get(position).getContent());

        Date date = new Date();
        date.setTime(data.get(position).getCreateTime());
        holder.txt_left_time.setText(TimeFormatUtil.DateToRealTime(date));

        if (!friendHeadUrl.equals("")){
            Glide.with(context)
                    .load(friendHeadUrl)
                    .dontAnimate()
                    .error(R.drawable.head_log1)
                    .into(holder.left_head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.left_head);
        }
    }

    private void rightMsg(ChatMessageViewHolder holder, final int position){
        holder.txt_right_content.setText(data.get(position).getContent());

        final Date date = new Date();
        date.setTime(data.get(position).getCreateTime());
        holder.txt_right_time.setText(TimeFormatUtil.DateToRealTime(date));

        if (!ownHeadUrl.equals("")){
            Glide.with(context)
                    .load(ownHeadUrl)
                    .dontAnimate()
                    .error(R.drawable.head_log1)
                    .into(holder.right_head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.right_head);
        }

        holder.txt_statue.setVisibility(View.GONE);

        int status =data.get(position).getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            holder.right_pro.setVisibility(View.GONE);
            holder.img_fail_resend.setVisibility(View.VISIBLE);
            holder.img_fail_resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controlChat.sendMessage(data.get(position).getContent());
                    data.remove(position);
                    controlChat.delMsg(data.get(position));
                    notifyDataSetChanged();
                }
            });
        } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
            holder.right_pro.setVisibility(View.VISIBLE);
            holder.img_fail_resend.setVisibility(View.GONE);
        } else {
            holder.right_pro.setVisibility(View.GONE);
            holder.img_fail_resend.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return this.data == null?0:this.data.size();
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView left_head;
        TextView txt_left_time;
        TextView txt_left_content;
        LinearLayout ll_left_body;

        CircleImageView right_head;
        TextView txt_right_time;
        TextView txt_right_content;
        LinearLayout ll_right_body;
        TextView txt_statue;
        ProgressBar right_pro;
        ImageView img_fail_resend;

        public ChatMessageViewHolder(View view) {
            super(view);
            left_head = (CircleImageView) view.findViewById(R.id.item_chat_msg_left_head);
            txt_left_time = (TextView) view.findViewById(R.id.item_chat_msg_left_time);
            txt_left_content = (TextView) view.findViewById(R.id.item_chat_msg_left_content);
            ll_left_body = (LinearLayout) view.findViewById(R.id.item_chat_msg_left_body);

            right_head = (CircleImageView) view.findViewById(R.id.item_chat_msg_right_head);
            txt_right_time = (TextView) view.findViewById(R.id.item_chat_msg_right_time);
            txt_right_content = (TextView) view.findViewById(R.id.item_chat_msg_right_content);
            ll_right_body = (LinearLayout) view.findViewById(R.id.item_chat_msg_right_body);
            txt_statue = (TextView) view.findViewById(R.id.item_chat_msg_right_send_status);
            right_pro = (ProgressBar) view.findViewById(R.id.item_chat_msg_right_progress_load);
            img_fail_resend = (ImageView) view.findViewById(R.id.item_chat_msg_right_fail_resend);
        }
    }

    class ChatMessageClick implements View.OnClickListener{

        private int position;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_chat_msg_right_fail_resend:
                    break;
            }
        }

    }

    public void addToTopMessages(List<BmobIMMessage> list){
        data.addAll(0,list);
        notifyDataSetChanged();
    }

    public void addToBottomMessages(List<BmobIMMessage> list){
        data.addAll(list);
        notifyDataSetChanged();
        scrollToBottom();
    }

    public void addToBottomMessages(BmobIMMessage msg){
        data.add(msg);
        notifyDataSetChanged();
        scrollToBottom();
    }

    private void scrollToBottom(){
        recyclerView.scrollToPosition(data.size() - 1);
    }

}
