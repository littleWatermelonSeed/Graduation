package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMsgDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OWN;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMsgDetailsActivity.FLEA_MARKER_DEL_CODE;

/**
 * Created by 123 on 2018/4/30.
 */

public class FleaMarketAdapter extends RecyclerView.Adapter<FleaMarketAdapter.FleaMarketViewHolder> {

    private Context context;
    private List<FleaMarketBean> data;
    private int fleaMarkType;

    public FleaMarketAdapter(Context context, List<FleaMarketBean> data, int fleaMarkType){
        this.context = context;
        this.data = data;
        this.fleaMarkType = fleaMarkType;
    }

    @Override
    public FleaMarketAdapter.FleaMarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flea_market,parent,false);
        AutoUtils.autoSize(view);
        FleaMarketViewHolder holder = new FleaMarketViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FleaMarketViewHolder holder, int position) {
        FleaMarketClickListener listener = new FleaMarketClickListener(position);

        holder.txt_title.setText(data.get(position).getTitle());
        holder.txt_title.setOnClickListener(listener);
        holder.txt_content.setText(data.get(position).getContent());
        holder.txt_content.setOnClickListener(listener);
        holder.txt_commentNum.setText("留言 " + data.get(position).getCommentNum());
        holder.txt_price.setText(data.get(position).getPrice());
        holder.txt_create_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(data.get(position).getReleaseTime().getDate())));

        if (fleaMarkType == TYPE_FLEA_MARK_OWN || fleaMarkType == TYPE_FLEA_MARK_OTHER){
            holder.rl_other_page.setVisibility(View.GONE);
        }else {
            holder.txt_user_name.setText(data.get(position).getUser().getNickName());
            holder.ll_go_to_other.setOnClickListener(listener);
            if (data.get(position).getUser().getHeadPortrait() != null && !data.get(position).getUser().getHeadPortrait().getUrl().equals("")){
                Glide.with(context)
                        .load(data.get(position).getUser().getHeadPortrait().getUrl())
                        .dontAnimate()
                        .into(holder.img_head_portrait);
            }else {
                Glide.with(context)
                        .load(R.drawable.head_log1)
                        .dontAnimate()
                        .into(holder.img_head_portrait);
            }
        }

        if (data.get(position).getPriceType() == 0){
            holder.txt_price_type.setText("一口价");
        }else if (data.get(position).getPriceType() == 1){
            holder.txt_price_type.setText("区间价");
        }

        holder.mGridView.setAdapter(null);
        if (data.get(position).getImageUrls() != null && data.get(position).getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(context,data.get(position).getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            holder.mGridView.setAdapter(adapter);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FleaMarketViewHolder extends RecyclerView.ViewHolder {

        ImageView img_head_portrait;
        LinearLayout ll_go_to_other;
        TextView txt_user_name;
        TextView txt_title;
        MyGridView mGridView;
        TextView txt_content;
        TextView txt_price_type;
        TextView txt_price;
        TextView txt_commentNum;
        TextView txt_create_time;
        RelativeLayout rl_other_page;

        public FleaMarketViewHolder(View view) {
            super(view);
            img_head_portrait = (ImageView) view.findViewById(R.id.item_flea_market_head_portrait);
            ll_go_to_other = (LinearLayout) view.findViewById(R.id.item_flea_market_go_to_other);
            txt_user_name = (TextView) view.findViewById(R.id.item_flea_market_user_name);
            txt_title = (TextView) view.findViewById(R.id.item_flea_market_title);
            mGridView = (MyGridView) view.findViewById(R.id.item_flea_market_image);
            txt_content = (TextView) view.findViewById(R.id.item_flea_market_content);
            txt_price_type = (TextView) view.findViewById(R.id.item_flea_market_price_type);
            txt_price = (TextView) view.findViewById(R.id.item_flea_market_price);
            txt_commentNum = (TextView) view.findViewById(R.id.item_flea_market_leave_word_num);
            txt_create_time = (TextView) view.findViewById(R.id.item_flea_market_create_time);
            rl_other_page = (RelativeLayout) view.findViewById(R.id.item_flea_market_other_page);
        }
    }

    class FleaMarketClickListener implements View.OnClickListener{

        private int position;

        public FleaMarketClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_flea_market_go_to_other:
                    FleaMarketActivity.go2Activity(context,data.get(position).getUser());
                    break;
                case R.id.item_flea_market_title:
                case R.id.item_flea_market_content:
                    if (fleaMarkType == TYPE_FLEA_MARK_OWN){
                        FleaMsgDetailsActivity.go2Activity(context,fleaMarkType,data.get(position),FLEA_MARKER_DEL_CODE);
                    }else {
                        FleaMsgDetailsActivity.go2Activity(context,fleaMarkType,data.get(position));
                    }
                    break;
            }
        }
    }

}
