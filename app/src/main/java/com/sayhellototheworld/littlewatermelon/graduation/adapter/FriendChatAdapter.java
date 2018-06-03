package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.FriendChatBean;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.CenterActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/31.
 */

public class FriendChatAdapter extends RecyclerView.Adapter<FriendChatAdapter.FriendChatViewHolder>  {

    private Context context;
    private List<FriendChatBean> data;

    private FriendClick listener;

    public FriendChatAdapter(Context context, List<FriendChatBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public FriendChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_chat, parent, false);
        AutoUtils.autoSize(view);
        FriendChatViewHolder holder = new FriendChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendChatViewHolder holder, int position) {
        listener = new FriendClick(position);
        holder.ll_body.setOnClickListener(listener);
        if (!data.get(position).getFriendHeadUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getFriendHeadUrl())
                    .dontAnimate()
                    .into(holder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }

        holder.txt_user_name.setText(data.get(position).getFriendName());
        holder.txt_last_msg.setText(data.get(position).getLastMsg());
        holder.txt_last_time.setText(data.get(position).getTime());

        if (data.get(position).getNoReadNum() > 0){
            holder.txt_no_read_num.setVisibility(View.VISIBLE);
            holder.txt_no_read_num.setText(data.get(position).getNoReadNum() + "");
        }else {
            holder.txt_no_read_num.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FriendChatViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        TextView txt_user_name;
        TextView txt_last_msg;
        TextView txt_last_time;
        TextView txt_no_read_num;
        LinearLayout ll_body;

        public FriendChatViewHolder(View view) {
            super(view);
            ll_body = (LinearLayout) view.findViewById(R.id.item_friend_chat_body);
            head = (CircleImageView) view.findViewById(R.id.item_friend_chat_head_portrait);
            txt_user_name = (TextView) view.findViewById(R.id.item_friend_chat_user_name);
            txt_last_msg = (TextView) view.findViewById(R.id.item_friend_chat_last_msg);
            txt_last_time = (TextView) view.findViewById(R.id.item_friend_chat_last_time);
            txt_no_read_num = (TextView) view.findViewById(R.id.item_friend_chat_no_read_num);
        }
    }

    class FriendClick implements View.OnClickListener{

        private int position;

        public FriendClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_friend_chat_body:
                    CenterActivity.setReduceChatNoRead(data.get(position).getNoReadNum());
                    ChatActivity.go2ActivityForResult(context,data.get(position).getFriendID(),data.get(position).getFriendHeadUrl(),
                            data.get(position).getFriendName());
                    data.get(position).setNoReadNum(0);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

}
