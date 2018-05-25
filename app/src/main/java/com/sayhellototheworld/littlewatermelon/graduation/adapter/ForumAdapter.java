package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForum;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.ForumDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/24.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private Context context;
    private List<ForumBean> data;
    private int type;

    private ItemClick listener;
    private String userID;
    private BmobManageForum manageForum;

    public ForumAdapter(Context context, List<ForumBean> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
        userID = BmobManageUser.getCurrentUser().getObjectId();
        manageForum = BmobManageForum.getManager();
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum, parent, false);
        AutoUtils.autoSize(view);
        ForumViewHolder holder = new ForumViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {
        listener = new ItemClick(position,holder);
        holder.txt_user_name.setText(data.get(position).getUser().getNickName());
        holder.txt_school_name.setText(data.get(position).getUser().getSchoolName());
        holder.txt_content.setText(data.get(position).getContent());
        holder.txt_content.setOnClickListener(listener);
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getCreatedAt())));
        holder.txt_comment_num.setText(data.get(position).getCommentNum() + "");
        holder.txt_comment_num.setOnClickListener(listener);
        holder.txt_like_num.setText(data.get(position).getLikeNum() + "");
        holder.txt_like_num.setOnClickListener(listener);

        if (data.get(position).getLikeUserObjID() != null && data.get(position).getLikeUserObjID().contains(userID)){
            holder.img_like_icon.setImageResource(R.drawable.liked);
        }else {
            holder.img_like_icon.setImageResource(R.drawable.like);
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

        holder.gridView.setAdapter(null);
        if (data.get(position).getImageUrls() != null && data.get(position).getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(context,data.get(position).getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            holder.gridView.setAdapter(adapter);
        }

        if (type != ControlForum.FORUM_TYPE_OWN){
            holder.head.setOnClickListener(listener);
        }

        holder.txt_comment.setOnClickListener(listener);
        holder.img_comment_icon.setOnClickListener(listener);
        holder.img_like_icon.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ForumViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView head;
        private TextView txt_user_name;
        private TextView txt_school_name;
        private TextView txt_content;
        private TextView txt_release_time;
        private TextView txt_like_num;
        private TextView txt_comment_num;
        private ImageView img_like_icon;
        private ImageView img_comment_icon;
        private TextView txt_comment;
        private MyGridView gridView;

        public ForumViewHolder(View view) {
            super(view);
            head = (CircleImageView) view.findViewById(R.id.item_forum_head_portrait);
            txt_user_name = (TextView) view.findViewById(R.id.item_forum_user_name);
            txt_school_name = (TextView) view.findViewById(R.id.item_forum_school_name);
            txt_content = (TextView) view.findViewById(R.id.item_forum_content);
            txt_release_time = (TextView) view.findViewById(R.id.item_forum_release_time);
            txt_like_num = (TextView) view.findViewById(R.id.item_forum_like_num);
            txt_comment_num = (TextView) view.findViewById(R.id.item_forum_comment_num);
            img_like_icon = (ImageView) view.findViewById(R.id.item_forum_like_icon);
            img_comment_icon = (ImageView) view.findViewById(R.id.item_forum_comment_icon);
            txt_comment = (TextView) view.findViewById(R.id.item_forum_comment);
            gridView = (MyGridView) view.findViewById(R.id.item_forum_image);
        }
    }

    class ItemClick implements View.OnClickListener {

        private int position;
        private ForumViewHolder holder;
        private boolean isLikeIng = false;

        public ItemClick(int position,ForumViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_forum_head_portrait:
                    if (data.get(position).getUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
                        return;
                    }
                    UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    break;
                case R.id.item_forum_content:
                    ForumDetailsActivity.go2Activity(context,type,data.get(position));
                    break;
                case R.id.item_forum_like_num:
                case R.id.item_forum_like_icon:
                    if (isLikeIng)
                        return;
                    isLikeIng = true;
                    String tempLike = "";
                    boolean tempAdd = true;
                    int num;
                    if (data.get(position).getLikeUserObjID() != null && data.get(position).getLikeUserObjID().contains(userID)){
                        tempLike = data.get(position).getLikeUserObjID().replace(userID + ",","");
                        tempAdd = false;
                        num = -1;
                    }else {
                        if (data.get(position).getLikeUserObjID() == null){
                            tempLike = userID + ",";
                        }else {
                            tempLike = data.get(position).getLikeUserObjID() + userID + ",";
                        }
                        tempAdd = true;
                        num = 1;
                    }
                    final boolean add = tempAdd;
                    final String likeObjID = tempLike;
                    manageForum.updateLike(data.get(position).getObjectId(), tempLike,num, new BmobUpdateDone() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                if (add){
                                    holder.img_like_icon.setImageResource(R.drawable.liked);
                                    holder.txt_like_num.setText((Integer.parseInt(holder.txt_like_num.getText().toString()) + 1) + "");
                                    data.get(position).setLikeNum(Integer.parseInt(holder.txt_like_num.getText().toString()));
                                }else {
                                    holder.img_like_icon.setImageResource(R.drawable.like);
                                    holder.txt_like_num.setText((Integer.parseInt(holder.txt_like_num.getText().toString()) - 1) + "");
                                    data.get(position).setLikeNum(Integer.parseInt(holder.txt_like_num.getText().toString()));
                                }
                                data.get(position).setLikeUserObjID(likeObjID);
                            }
                            isLikeIng = false;
                        }
                    });
                    break;
                case R.id.item_forum_comment_icon:
                case R.id.item_forum_comment_num:
                case R.id.item_forum_comment:
                    WriteCommentActivity.go2Activity(context,data.get(position),WriteCommentActivity.COMMENT_TYPE_FORUM);
                    break;
            }
        }

    }

}
