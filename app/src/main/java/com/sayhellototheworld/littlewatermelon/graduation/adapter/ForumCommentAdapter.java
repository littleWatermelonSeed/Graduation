package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.ReplayOtherCommentActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;

import java.util.List;

/**
 * Created by 123 on 2018/5/26.
 */

public class ForumCommentAdapter extends BaseAdapter{

    private Context context;
    private List<ForumCommentBean> data;
    private LayoutInflater inflater;

    private ItemClick listener;

    public ForumCommentAdapter(Context context, List<ForumCommentBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForumCommentViewHolder viewHolder;
        listener = new ItemClick(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_forum_comment,parent,false);
            viewHolder = new ForumCommentViewHolder();
            viewHolder.head = (ImageView) convertView.findViewById(R.id.item_forum_comment_head_portrait);
            viewHolder.txt_user_name = (TextView) convertView.findViewById(R.id.item_forum_comment_name);
            viewHolder.txt_school_name = (TextView) convertView.findViewById(R.id.item_forum_comment_school_name);
            viewHolder.txt_replay = (TextView) convertView.findViewById(R.id.item_forum_comment_replay);
            viewHolder.ll_replay_body = (LinearLayout) convertView.findViewById(R.id.item_forum_comment_replay_body);
            viewHolder.txt_replay_user_name = (TextView) convertView.findViewById(R.id.item_forum_comment_replay_user_name);
            viewHolder.txt_content = (TextView) convertView.findViewById(R.id.item_forum_comment_content);
            viewHolder.txt_create_time = (TextView) convertView.findViewById(R.id.item_forum_comment_create_time);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ForumCommentViewHolder) convertView.getTag();
        }

        if (data.get(position).getUser().getHeadPortrait() != null && !data.get(position).getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(viewHolder.head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(viewHolder.head);
        }
        viewHolder.head.setOnClickListener(listener);

        viewHolder.txt_user_name.setText(data.get(position).getUser().getNickName());
        viewHolder.txt_school_name.setText(data.get(position).getUser().getSchoolName());
        viewHolder.txt_content.setText(data.get(position).getContent());
        viewHolder.txt_create_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));

        if (data.get(position).getType() == 0){
            viewHolder.ll_replay_body.setVisibility(View.GONE);
        }else if (data.get(position).getType() == 1){
            viewHolder.ll_replay_body.setVisibility(View.VISIBLE);
            if (data.get(position).getOtherUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
                viewHolder.txt_replay_user_name.setText("你");
                viewHolder.txt_replay_user_name.setTextColor(context.getResources().getColor(R.color.statue2));
            }else {
                viewHolder.txt_replay_user_name.setText(data.get(position).getOtherUser().getNickName());
                viewHolder.txt_replay_user_name.setTextColor(context.getResources().getColor(R.color.green));
                viewHolder.txt_replay_user_name.setOnClickListener(listener);
            }
        }

        if (data.get(position).getUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
            viewHolder.txt_replay.setVisibility(View.GONE);
        }else {
            viewHolder.txt_replay.setVisibility(View.VISIBLE);
            viewHolder.txt_replay.setOnClickListener(listener);
        }

        return convertView;
    }

    class ForumCommentViewHolder{
        public ImageView head;
        public TextView txt_user_name;
        public TextView txt_school_name;
        public TextView txt_replay;
        public LinearLayout ll_replay_body;
        public TextView txt_replay_user_name;
        public TextView txt_content;
        public TextView txt_create_time;
    }

    class ItemClick implements View.OnClickListener{

        private int position;

        public ItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_forum_comment_head_portrait:
                    if (data.get(position).getUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
                        return;
                    }
                    UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    break;
                case R.id.item_forum_comment_replay:
                    ReplayOtherCommentActivity.go2Activity(context,data.get(position).getUser(),data.get(position));
                    break;
                case R.id.item_forum_comment_replay_user_name:
                    UserDetailsActivity.go2Activity(context,data.get(position).getOtherUser().getObjectId());
                    break;
            }
        }
    }

}
