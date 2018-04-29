package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.PreviewPlanPicActivity;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.List;

/**
 * Created by 123 on 2017/9/21.
 */

public class ShowImageAdapter extends BaseAdapter {

    private Context context;
    private List<String> imagePath;
    private LayoutInflater inflater;
    private int type;

    private WritePlanImageListen mListen;

    public final static int TYPE_WRITE_PLAN = 0;
    public final static int TYPE_READ_PLAN = 1;

    public ShowImageAdapter(Context context, List<String> imagePath, int type) {
        this.context = context;
        this.imagePath = imagePath;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (type == TYPE_WRITE_PLAN) {
            return imagePath.size() + 1;
        } else {
            return imagePath.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return imagePath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (type == TYPE_WRITE_PLAN) {
            convertView = getWriteView(position, convertView, parent);
        } else if (type == TYPE_READ_PLAN) {
            convertView = getReadView(position, convertView, parent);
        }
        return convertView;
    }

    @NonNull
    private View getReadView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int displayWidth = SysUtil.getDisplayWidth();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((displayWidth - 400) / 3, (displayWidth - 400) / 3);
            convertView = inflater.inflate(R.layout.adapter_write_plan_image, parent, false);
            viewHolder.imageView_content = (ImageView) convertView.findViewById(R.id.adapter_writer_plan_image_contentImage);
            viewHolder.imageView_deltete = (ImageView) convertView.findViewById(R.id.adapter_writer_plan_image_deleteImage);
//            viewHolder.imageView_content.setLayoutParams(params);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView_deltete.setVisibility(View.GONE);
        if (!imagePath.get(position).startsWith("http://")){
//            Log.i("niyuanjie","加载本地图片");
            Glide.with(context)
                    .load(new File(imagePath.get(position)))
                    .into(viewHolder.imageView_content);
        }else {
//            Log.i("niyuanjie","加载网络图片");
            Glide.with(context)
                    .load(imagePath.get(position))
                    .into(viewHolder.imageView_content);
        }
        mListen = new WritePlanImageListen(position);
        viewHolder.imageView_content.setOnClickListener(mListen);
        return convertView;
    }

    @NonNull
    private View getWriteView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int displayWidth = SysUtil.getDisplayWidth();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((displayWidth - 400) / 3, (displayWidth - 400) / 3);
            convertView = inflater.inflate(R.layout.adapter_write_plan_image, parent, false);
            viewHolder.imageView_content = (ImageView) convertView.findViewById(R.id.adapter_writer_plan_image_contentImage);
            viewHolder.imageView_deltete = (ImageView) convertView.findViewById(R.id.adapter_writer_plan_image_deleteImage);
//            viewHolder.imageView_content.setLayoutParams(params);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.imageView_deltete.setVisibility(View.GONE);
            Glide.with(context)
                    .load(R.drawable.addpicture)
                    .into(viewHolder.imageView_content);
        } else {
            viewHolder.imageView_deltete.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imagePath.get(position - 1))
                    .into(viewHolder.imageView_content);
        }

        mListen = new WritePlanImageListen(position);
        viewHolder.imageView_content.setOnClickListener(mListen);
        viewHolder.imageView_deltete.setOnClickListener(mListen);
        return convertView;
    }

    class WritePlanImageListen implements View.OnClickListener {

        private int position;

        public WritePlanImageListen(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (type == TYPE_WRITE_PLAN) {
                if (position == 0) {
                    if (imagePath.size() >= 5) {
                        MyToastUtil.showToast("最多只能上传5张图片");
                        return;
                    }
                    ShowPictureActivity.startShowPictureActivityForResult(context, ShowPictureActivity.TARGET_PLAN);
                } else {
                    switch (v.getId()) {
                        case R.id.adapter_writer_plan_image_contentImage:
                            PreviewPlanPicActivity.startPreviewPlanPicActivity(context,imagePath,position - 1);
                            break;
                        case R.id.adapter_writer_plan_image_deleteImage:
                            imagePath.remove(position - 1);
                            notifyDataSetChanged();
                            break;
                    }
                }
            } else {
                switch (v.getId()) {
                    case R.id.adapter_writer_plan_image_contentImage:
                        PreviewPlanPicActivity.startPreviewPlanPicActivity(context,imagePath,position);
                        break;
                }
            }
        }
    }

    class ViewHolder {
        ImageView imageView_content;
        ImageView imageView_deltete;
    }

}