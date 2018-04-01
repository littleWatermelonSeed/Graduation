package com.sayhellototheworld.littlewatermelon.graduation.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by 123 on 2017/8/31.
 */

public class LayoutBackgroundUtil {

    public static void setLayoutBackground(final Context context, final ViewGroup parentLayout,int resourceID){
        SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                parentLayout.setBackground(resource);
            }
        };
        Glide.with(context)
                .load(resourceID)
                .dontAnimate()
                .into(target);
    }

    public static void setLayoutBackground(final Context context, final View view, int resourceID){
        SimpleTarget<GlideDrawable> target = new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                view.setBackground(resource);
            }
        };
        Glide.with(context)
                .load(resourceID)
                .dontAnimate()
                .into(target);
    }

    public static void preloadBackgroundResource(Context context,int resourceID,RequestListener listener){
        Glide.with(context)
                .load(resourceID)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(listener)
                .preload();
    }

}
