package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.MessageDetailsBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.MsgDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMsgDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceShareDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_HOME;

/**
 * Created by 123 on 2018/5/15.
 */

public class MessageDeatilsAdapter extends RecyclerView.Adapter<MessageDeatilsAdapter.MessageDetailsViewHolder> {

    private Context context;
    private List<MessageDetailsBean> data;
    private int type;

    private ItemClick listener;

    public MessageDeatilsAdapter(Context context, List<MessageDetailsBean> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }

    @Override
    public MessageDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_details, parent, false);
        AutoUtils.autoSize(view);
        MessageDetailsViewHolder viewHolder = new MessageDetailsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageDetailsViewHolder holder, int position) {
        listener = new ItemClick(position);
        holder.txt_comment.setText(data.get(position).getComment());
        holder.txt_comment_time.setText(data.get(position).getCreateTime());
        holder.txt_comment_user_name.setText(data.get(position).getCommentUser().getNickName());
        holder.txt_release_user_name.setText(data.get(position).getReleaseUserName());
        holder.txt_release_content.setText(data.get(position).getContent());

        if (data.get(position).getRead()){
            holder.txt_msg_type.setTextColor(context.getResources().getColor(R.color.green));
            holder.txt_msg_type.setText("历史消息");
        }else {
            holder.txt_msg_type.setTextColor(context.getResources().getColor(R.color.red1));
            holder.txt_msg_type.setText("新消息");
        }

        if (data.get(position).getFirstImgUrl() != null){
            holder.img_first.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(data.get(position).getFirstImgUrl())
                    .into(holder.img_first);
        }else {
            holder.img_first.setVisibility(View.GONE);
        }

        if (data.get(position).getCommentUser().getHeadPortrait() != null && !data.get(position).getCommentUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getCommentUser().getHeadPortrait().getUrl())
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


        public MessageDetailsViewHolder(View itemView) {
            super(itemView);
            txt_msg_type = (TextView) itemView.findViewById(R.id.item_message_details_msg_type);
            head = (CircleImageView) itemView.findViewById(R.id.item_message_details_head_portrait);
            txt_comment_user_name = (TextView) itemView.findViewById(R.id.item_message_details_name);
            txt_comment_time = (TextView) itemView.findViewById(R.id.item_message_details_create_time);
            txt_reply = (TextView) itemView.findViewById(R.id.item_message_details_reply);
            txt_comment = (TextView) itemView.findViewById(R.id.item_message_details_comment);
            img_first = (ImageView) itemView.findViewById(R.id.item_message_details_image);
            txt_release_user_name = (TextView) itemView.findViewById(R.id.item_message_details_release_user);
            txt_release_content = (TextView) itemView.findViewById(R.id.item_message_details_content);
            ll_enter_body = (LinearLayout) itemView.findViewById(R.id.item_message_details_enter_body);
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
                case R.id.item_message_details_reply:

                    break;
                case R.id.item_message_details_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getCommentUser().getObjectId());
                    break;
                case R.id.item_message_details_enter_body:
                    switch (type){
                        case MessageAdapter.MSG_TYPE_LOST:
                            MsgDetailsActivity.go2Activity(context,false,data.get(position).getBmobObject(),
                                    MsgDetailsActivity.DETAILS_TYPE_LOST,false);
                            break;
                        case MessageAdapter.MSG_TYPE_FLEA:
                            FleaMsgDetailsActivity.go2Activity(context, FleaMarketActivity.TYPE_FLEA_MARK_HOME, (FleaMarketBean) data.get(position).getBmobObject());
                            break;
                        case MessageAdapter.MSG_TYPE_SHARE:
                            ResourceShareDetailsActivity.go2Activity(context,TYPE_RESOURCE_SHARE_HOME, (ResourceShareBean) data.get(position).getBmobObject());
                            break;
                        case MessageAdapter.MSG_TYPE_FORUM:
                            break;
                    }
                    break;
            }
        }
    }

}
