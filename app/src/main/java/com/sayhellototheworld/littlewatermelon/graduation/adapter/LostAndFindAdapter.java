package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.MsgDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 123 on 2018/4/23.
 */

public class LostAndFindAdapter extends RecyclerView.Adapter<LostAndFindAdapter.MyViewHolder>{

    private List<LostAndFindBean> bean;
    private Context context;
    private boolean privateLost;

    public LostAndFindAdapter(Context context,List<LostAndFindBean> bean,boolean privateLost){
        this.context = context;
        this.bean = bean;
        this.privateLost = privateLost;
    }

    @Override
    public LostAndFindAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lost_and_find,parent,false);
        AutoUtils.autoSize(view);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClickListener listener = new ClickListener(position,holder.txt_like_text,holder.img_star,holder.txt_likeNum);
        holder.txt_title.setText(bean.get(position).getTitle());
//        holder.txt_title.setOnClickListener(listener);
        holder.txt_user_name.setText(bean.get(position).getUser().getNickName());
        holder.txt_create_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(bean.get(position).getReleaseTime().getDate())));
        holder.txt_content.setText(bean.get(position).getContent());
//        holder.txt_content.setOnClickListener(listener);
        holder.txt_commentNum.setText(bean.get(position).getCommentNum() + "");
        holder.txt_likeNum.setText(bean.get(position).getStars() + "");
        holder.mGridView.setAdapter(null);
        holder.ll_comment.setOnClickListener(listener);
        holder.ll_star.setOnClickListener(listener);
        holder.ll_body.setOnClickListener(listener);
        if (bean.get(position).getImageUrls() != null && bean.get(position).getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(context,bean.get(position).getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            holder.mGridView.setAdapter(adapter);
        }
        if (bean.get(position).getUser().getHeadPortrait() != null && !bean.get(position).getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(context)
                    .load(bean.get(position).getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.img_head_portrait);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.img_head_portrait);
        }
        holder.img_head_portrait.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return bean.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title;
        ImageView img_head_portrait;
        TextView txt_user_name;
        TextView txt_create_time;
        TextView txt_content;
        MyGridView mGridView;
        TextView txt_commentNum;
        LinearLayout ll_comment;
        LinearLayout ll_star;
        LinearLayout ll_body;
        ImageView img_star;
        TextView txt_like_text;
        TextView txt_likeNum;
        public MyViewHolder(View view) {
            super(view);
            txt_title = (TextView) view.findViewById(R.id.item_lost_and_find_title);
            img_head_portrait = (ImageView)view.findViewById(R.id.item_lost_and_find_head_portrait);
            txt_user_name = (TextView) view.findViewById(R.id.item_lost_and_find_name);
            txt_create_time = (TextView) view.findViewById(R.id.item_lost_and_find_create_time);
            txt_content = (TextView) view.findViewById(R.id.item_lost_and_find_content);
            mGridView = (MyGridView) view.findViewById(R.id.item_lost_and_find_image);
            txt_commentNum = (TextView) view.findViewById(R.id.item_lost_and_find_commentNum);
            ll_comment = (LinearLayout) view.findViewById(R.id.item_lost_and_find_comment);
            ll_star = (LinearLayout) view.findViewById(R.id.item_lost_and_find_like);
            ll_body = (LinearLayout) view.findViewById(R.id.item_lost_and_find_body);
            img_star = (ImageView) view.findViewById(R.id.item_lost_and_find_like_icon);
            txt_like_text = (TextView) view.findViewById(R.id.item_lost_and_find_like_text);
            txt_likeNum = (TextView) view.findViewById(R.id.item_lost_and_find_likeNum);
        }
    }

    class ClickListener implements View.OnClickListener{

        private int position;
        private TextView likeText;
        private ImageView likeIcon;
        private TextView likeNum;
        private boolean star = false;

        ClickListener(int position,TextView likeText,ImageView likeIcon,TextView likeNum){
            this.position = position;
            this.likeText = likeText;
            this.likeIcon = likeIcon;
            this.likeNum = likeNum;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_lost_and_find_head_portrait:
                    Log.i("niyuanjie","点击头像");
                    break;
                case R.id.item_lost_and_find_body:
                    MsgDetailsActivity.go2Activity(context,star,bean.get(position),MsgDetailsActivity.DETAILS_TYPE_LOST,privateLost);
                    break;
                case R.id.item_lost_and_find_comment:
                    WriteCommentActivity.go2Activity(context,bean.get(position),WriteCommentActivity.COMMENT_TYPE_LOST_AND_FIND);
                    break;
                case R.id.item_lost_and_find_like:
                    if (!star){
                        bean.get(position).increment("stars");
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.liked));
                        likeText.setTextColor(context.getResources().getColor(R.color.liked));
                        likeNum.setTextColor(context.getResources().getColor(R.color.liked));
                        likeNum.setText(Integer.parseInt(likeNum.getText().toString()) + 1 + "");
                        bean.get(position).setStars(bean.get(position).getStars() + 1);
                        star = true;
                    }else {
                        bean.get(position).increment("stars",-1);
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                        likeText.setTextColor(context.getResources().getColor(R.color.black1));
                        likeNum.setTextColor(context.getResources().getColor(R.color.black1));
                        likeNum.setText(Integer.parseInt(likeNum.getText().toString()) - 1 + "");
                        bean.get(position).setStars(bean.get(position).getStars() - 1);
                        star = false;
                    }
                    bean.get(position).update();
                    break;
            }
        }
    }

}
