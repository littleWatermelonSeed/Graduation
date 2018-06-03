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
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.IMFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/31.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>  {

    private Context context;
    private List<FriendBean> data;
    private IMFragment imFragment;

    private FriendClick listener;

    public FriendAdapter(Context context, List<FriendBean> data,IMFragment imFragment) {
        this.context = context;
        this.data = data;
        this.imFragment = imFragment;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        AutoUtils.autoSize(view);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
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
        holder.head.setOnClickListener(listener);

        String userName;
        if (data.get(position).getRemarkName() != null && data.get(position).getRemarkName() != null && !data.get(position).getRemarkName().equals("")){
            userName = data.get(position).getRemarkName();
        }else {
            userName = data.get(position).getFriend().getNickName();
        }
        holder.txt_user_name.setText(userName);

        String intro;
        if (data.get(position).getFriend().getIntroduction() == null){
            intro = "";
        }else {
            intro = data.get(position).getFriend().getIntroduction();
        }
        holder.txt_introduction.setText("简介：" + intro);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        TextView txt_user_name;
        TextView txt_introduction;
        LinearLayout ll_body;

        public FriendViewHolder(View view) {
            super(view);
            ll_body = (LinearLayout) view.findViewById(R.id.item_friend_body);
            head = (CircleImageView) view.findViewById(R.id.item_friend_head_portrait);
            txt_user_name = (TextView) view.findViewById(R.id.item_friend_user_name);
            txt_introduction = (TextView) view.findViewById(R.id.item_friend_introduction);
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
                case R.id.item_friend_body:
                    String userName;
                    if (data.get(position).getRemarkName() != null && data.get(position).getRemarkName() != null && !data.get(position).getRemarkName().equals("")){
                        userName = data.get(position).getRemarkName();
                    }else {
                        userName = data.get(position).getFriend().getNickName();
                    }
                    String h = "";
                    if (data.get(position).getFriend().getHeadPortrait() != null && !data.get(position).getFriend().getHeadPortrait().getUrl().equals("")){
                        h = data.get(position).getFriend().getHeadPortrait().getUrl();
                    }
                    ChatActivity.go2ActivityForResult(context,data.get(position).getFriend().getObjectId(),h,userName);
                    break;
                case R.id.item_friend_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getFriend().getObjectId());
                    break;
            }
            imFragment.showChatFragment();
        }
    }

}
