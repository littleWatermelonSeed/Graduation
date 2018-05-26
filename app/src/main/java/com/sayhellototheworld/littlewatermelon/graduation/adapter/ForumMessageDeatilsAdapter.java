package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.ForumDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.ReplayOtherCommentActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/15.
 */

public class ForumMessageDeatilsAdapter extends RecyclerView.Adapter<ForumMessageDeatilsAdapter.MessageDetailsViewHolder> {

    private Context context;
    private List<ForumCommentBean> data;

    private ItemClick listener;

    public ForumMessageDeatilsAdapter(Context context, List<ForumCommentBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MessageDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_forum_details, parent, false);
        AutoUtils.autoSize(view);
        MessageDetailsViewHolder viewHolder = new MessageDetailsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageDetailsViewHolder holder, int position) {
        listener = new ItemClick(position);

        if (data.get(position).getType() == 1){
            String replayTypeMsg;
            String nickName;
            if (data.get(position).getOtherUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
                replayTypeMsg = "回复 你：";
                nickName = BmobManageUser.getCurrentUser().getNickName();
            }else {
                replayTypeMsg = "回复 " + data.get(position).getOtherUser().getNickName() + ": ";
                nickName = data.get(position).getOtherUser().getNickName();
            }
            holder.txt_comment_type.setText(replayTypeMsg);
            holder.txt_replay_comment.setVisibility(View.VISIBLE);
            String comment = nickName + ": " + data.get(position).getReplayContent();
            setTVColor(comment,0,nickName.length(),holder.txt_replay_comment);
        }else if (data.get(position).getType() == 0){
            holder.txt_comment_type.setText("评论：");
            holder.txt_replay_comment.setVisibility(View.GONE);
        }

        holder.txt_comment.setText(data.get(position).getContent());
        holder.txt_comment_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));
        holder.txt_comment_user_name.setText(data.get(position).getUser().getNickName());
        holder.txt_release_user_name.setText(data.get(position).getPublishUser().getNickName());
        holder.txt_release_content.setText(data.get(position).getForum().getContent());

        if (data.get(position).getRead()){
            holder.txt_msg_type.setTextColor(context.getResources().getColor(R.color.green));
            holder.txt_msg_type.setText("历史消息");
        }else {
            holder.txt_msg_type.setTextColor(context.getResources().getColor(R.color.red1));
            holder.txt_msg_type.setText("新消息");
        }

        if (data.get(position).getForum().getImageUrls() != null && data.get(position).getForum().getImageUrls().size() > 0){
            holder.img_first.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(data.get(position).getForum().getImageUrls().get(0))
                    .into(holder.img_first);
        }else {
            holder.img_first.setVisibility(View.GONE);
        }

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
        holder.ll_enter_body.setOnClickListener(listener);
        holder.txt_reply.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MessageDetailsViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_msg_type;
        public CircleImageView head;
        public TextView txt_comment_user_name;
        public TextView txt_comment_time;
        public TextView txt_reply;
        public TextView txt_comment;
        public ImageView img_first;
        public TextView txt_release_user_name;
        public TextView txt_release_content;
        public LinearLayout ll_enter_body;
        public TextView txt_comment_type;
        public TextView txt_replay_comment;


        public MessageDetailsViewHolder(View itemView) {
            super(itemView);
            txt_msg_type = (TextView) itemView.findViewById(R.id.item_message_forum_details_msg_type);
            head = (CircleImageView) itemView.findViewById(R.id.item_message_forum_details_head_portrait);
            txt_comment_user_name = (TextView) itemView.findViewById(R.id.item_message_forum_details_name);
            txt_comment_time = (TextView) itemView.findViewById(R.id.item_message_forum_details_create_time);
            txt_reply = (TextView) itemView.findViewById(R.id.item_message_forum_details_reply);
            txt_comment = (TextView) itemView.findViewById(R.id.item_message_forum_details_comment);
            img_first = (ImageView) itemView.findViewById(R.id.item_message_forum_details_image);
            txt_release_user_name = (TextView) itemView.findViewById(R.id.item_message_forum_details_release_user);
            txt_release_content = (TextView) itemView.findViewById(R.id.item_message_forum_details_content);
            ll_enter_body = (LinearLayout) itemView.findViewById(R.id.item_message_forum_details_enter_body);
            txt_comment_type = (TextView) itemView.findViewById(R.id.item_message_forum_details_comment_type);
            txt_replay_comment = (TextView) itemView.findViewById(R.id.item_message_forum_details_replay_comment);
        }
    }

    class ItemClick implements View.OnClickListener{

        int position;

        public ItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_message_forum_details_reply:
                    ReplayOtherCommentActivity.go2Activity(context,data.get(position).getUser(),data.get(position));
                    break;
                case R.id.item_message_forum_details_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    break;
                case R.id.item_message_forum_details_enter_body:
                    ForumDetailsActivity.go2Activity(context, ControlForum.FORUM_TYPE_MSG,data.get(position).getForum());
                    break;
            }
        }
    }

    private void setTVColor(String str,int begin,int end,TextView tv){
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.green)), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }

}
