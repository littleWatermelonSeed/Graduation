package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 123 on 2018/4/29.
 */

public class CommentAdapter extends BaseAdapter{

    private List<CommentBean> data;
    private Context context;
    private LayoutInflater inflater;

    private ItemClick listener;

    public CommentAdapter(Context context,List<CommentBean> data){
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
        CommentViewHolder viewHolder;
        listener = new ItemClick(position);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_comment,parent,false);
            viewHolder = new CommentViewHolder();
            viewHolder.img_head = (ImageView) convertView.findViewById(R.id.item_comment_head_portrait);
            viewHolder.txt_user_name = (TextView) convertView.findViewById(R.id.item_comment_name);
            viewHolder.txt_content = (TextView) convertView.findViewById(R.id.item_comment_content);
            viewHolder.txt_create_time = (TextView) convertView.findViewById(R.id.item_comment_create_time);
            AutoUtils.autoSize(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CommentViewHolder) convertView.getTag();
        }
        viewHolder.txt_user_name.setText(data.get(position).getUser().getNickName());
        viewHolder.txt_content.setText(data.get(position).getContent());
        viewHolder.txt_create_time.setText(data.get(position).getCreateDate());
        if (data.get(position).getUser().getHeadPortrait() != null && !data.get(position).getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(data.get(position).getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(viewHolder.img_head);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(viewHolder.img_head);
        }
        viewHolder.img_head.setOnClickListener(listener);
        return convertView;
    }

    class CommentViewHolder{
        public ImageView img_head;
        public TextView txt_user_name;
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
                case R.id.item_comment_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    break;
            }
        }
    }

}
