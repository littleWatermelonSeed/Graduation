package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogAcknowledge;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceShareDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity.COMMENT_TYPE_FLEA_RRSOURCE_SHARE;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.RESOURCE_SHARE_DEL_CODE;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OWN;

/**
 * Created by 123 on 2018/5/6.
 */

public class ResourceShareAdapter extends RecyclerView.Adapter<ResourceShareAdapter.ResoureceShareViewHolder> {

    private Context context;
    private List<ResourceShareBean> data;
    private int resourceType;

    public ResourceShareAdapter(Context context, List<ResourceShareBean> data, int resourceType) {
        this.context = context;
        this.data = data;
        this.resourceType = resourceType;
    }

    @Override
    public ResoureceShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource_share, parent, false);
        AutoUtils.autoSize(view);
        ResoureceShareViewHolder viewHolder = new ResoureceShareViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResoureceShareViewHolder holder, int position) {
        ResourceShareItemClick listener = new ResourceShareItemClick(position);
        holder.txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));
        holder.txt_content.setText(data.get(position).getContent());
        holder.txt_content.setOnClickListener(listener);
        holder.txt_link.setText(data.get(position).getLink());
        holder.txt_link_type.setText(data.get(position).getLinkType());
        holder.txt_ps.setText(data.get(position).getLinkPassword());
        holder.txt_copy.setOnClickListener(listener);

        if (resourceType == TYPE_RESOURCE_SHARE_OWN || resourceType == TYPE_RESOURCE_SHARE_OTHER){
            holder.rl_top.setVisibility(View.GONE);
        }else {
            holder.txt_user_name.setText(data.get(position).getUser().getNickName());
            holder.txt_enter_other.setOnClickListener(listener);
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
            holder.rl_top.setOnClickListener(listener);
        }

        holder.txt_comment.setOnClickListener(listener);
        holder.txt_acknowledge.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ResoureceShareViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView head;
        public TextView txt_user_name;
        public TextView txt_release_time;
        public TextView txt_content;
        public TextView txt_link;
        public TextView txt_copy;
        public TextView txt_link_type;
        public TextView txt_ps;
        public TextView txt_comment;
        public TextView txt_acknowledge;
        public RelativeLayout rl_top;
        public TextView txt_enter_other;

        public ResoureceShareViewHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.item_resource_share_head_portrait);
            txt_user_name = (TextView) itemView.findViewById(R.id.item_resource_share_name);
            txt_release_time = (TextView) itemView.findViewById(R.id.item_resource_share_create_time);
            txt_content = (TextView) itemView.findViewById(R.id.item_resource_share_content);
            txt_link = (TextView) itemView.findViewById(R.id.item_resource_share_link);
            txt_copy = (TextView) itemView.findViewById(R.id.item_resource_share_copy);
            txt_link_type = (TextView) itemView.findViewById(R.id.item_resource_share_link_type);
            txt_ps = (TextView) itemView.findViewById(R.id.item_resource_share_link_password);
            txt_comment = (TextView) itemView.findViewById(R.id.item_resource_share_comment);
            txt_acknowledge = (TextView) itemView.findViewById(R.id.item_resource_share_acknowledge);
            rl_top = (RelativeLayout) itemView.findViewById(R.id.item_resource_share_top);
            txt_enter_other = (TextView) itemView.findViewById(R.id.item_resource_share_enter_other);
        }
    }

    class ResourceShareItemClick implements View.OnClickListener {

        private int position;

        public ResourceShareItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_resource_share_top:
                case R.id.item_resource_share_content:
                    if (resourceType == TYPE_RESOURCE_SHARE_OWN){
                        ResourceShareDetailsActivity.go2Activity(context,resourceType,data.get(position),RESOURCE_SHARE_DEL_CODE);
                    }else {
                        ResourceShareDetailsActivity.go2Activity(context,resourceType,data.get(position));
                    }

                    break;
                case R.id.item_resource_share_copy:
                    ClipboardManager cm = (ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE));// 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("text", data.get(position).getLink());// 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    MyToastUtil.showToast("链接复制成功,快去云盘下载吧");
                    break;
                case R.id.item_resource_share_comment:
                    WriteCommentActivity.go2Activity(context,data.get(position),COMMENT_TYPE_FLEA_RRSOURCE_SHARE);
                    break;
                case R.id.item_resource_share_acknowledge:
                    if (data.get(position).getImageUrls() == null || data.get(position).getImageUrls().size() <= 0){
                        MyToastUtil.showToast("分享人似乎不在乎这点小钱,Ta并没有上传收款码");
                    }else {
                        DialogAcknowledge.showDialog(context,((FragmentActivity)context).getSupportFragmentManager(),data.get(position).getImageUrls());
                    }
                    break;
                case R.id.item_resource_share_enter_other:
                    ResourceSharingActivity.go2Activity(context,data.get(position).getUser());
                    break;
                case R.id.item_resource_share_head_portrait:
                    UserDetailsActivity.go2Activity(context,data.get(position).getUser().getObjectId());
                    break;
            }
        }
    }

}
