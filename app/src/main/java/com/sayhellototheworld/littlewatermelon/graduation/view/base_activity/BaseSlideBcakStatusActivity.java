package com.sayhellototheworld.littlewatermelon.graduation.view.base_activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.sayhellototheworld.littlewatermelon.graduation.R;

/**
 * Created by 123 on 2017/8/8.
 */

public abstract class BaseSlideBcakStatusActivity extends BaseStatusActivity {

    private View decorView;
    private boolean isBack = false;
    private int downX;
    private int offsetX;
    private int minSlop;
    private float screenWidth;
    private boolean backAnim = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        decorView = getWindow().getDecorView();
        getWindow().setBackgroundDrawableResource(R.color.wuse);
        minSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    protected void slideBackDo(){

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (backAnim){
            return true;
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)ev.getX();
                if(ev.getX() > 200){
                    isBack = false;
                }else {
                    isBack = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = (int)(ev.getX() - downX);
                if (isBack){
                    decorView.setX(offsetX);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(offsetX > screenWidth/2 && isBack){
                    ObjectAnimator animator = ObjectAnimator.ofFloat(decorView,"translationX",offsetX,(int)screenWidth);
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            backAnim = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            slideBackDo();
                            finish();
                        }
                    });
                    animator.start();
                    return true;
                }else if(isBack && offsetX > minSlop){
                    ObjectAnimator animator = ObjectAnimator.ofFloat(decorView,"translationX",offsetX,0);
                    animator.setDuration(200);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            backAnim = false;
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            backAnim = true;
                        }
                    });
                    animator.start();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
