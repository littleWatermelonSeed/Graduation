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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendBean;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/31.
 */

public class FriendChatAdapter extends RecyclerView.Adapter<FriendChatAdapter.FriendChatViewHolder>  {

    private Context context;
    private List<FriendBean> data;

    private FriendClick listener;

    public FriendChatAdapter(Context context, List<FriendBean> data) {
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

        String userName;
        if (data.get(position).getRemarkName() != null && data.get(position).getRemarkName() != null && !data.get(position).getRemarkName().equals("")){
            userName = data.get(position).getRemarkName();
        }else {
            userName = data.get(position).getFriend().getNickName();
        }
        holder.txt_user_name.setText(userName);

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
                    String userName;
                    if (data.get(position).getRemarkName() != null && data.get(position).getRemarkName() != null && !data.get(position).getRemarkName().equals("")){
                        userName = data.get(position).getRemarkName();
                    }else {
                        userName = data.get(position).getFriend().getNickName();
                    }
                    ChatActivity.go2Activity(context,data.get(position).getFriend(),userName);
                    break;
            }
        }
    }

}
