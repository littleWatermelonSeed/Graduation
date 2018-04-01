package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by 123 on 2017/10/4.
 */

public class PreviewImageAdapter extends PagerAdapter {

    private String[] imageUrls;
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public PreviewImageAdapter(Context context,String[] imageUrls,View.OnClickListener mOnClickListener) {
        mContext = context;
        this.imageUrls = imageUrls;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                ViewPager.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        imageView.setOnClickListener(mOnClickListener);
        container.addView(imageView);
        if (!imageUrls[position].startsWith("http://")){
            Glide.with(mContext)
                    .load(new File(imageUrls[position]))
                    .into(imageView);
        }else {
            Glide.with(mContext)
                    .load(imageUrls[position])
                    .into(imageView);
        }
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
