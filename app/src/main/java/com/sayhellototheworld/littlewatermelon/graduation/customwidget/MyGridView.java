package com.sayhellototheworld.littlewatermelon.graduation.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by 123 on 2017/9/21.
 */

public class MyGridView extends GridView {

    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode())
        {
            mHelper.adjustChildren();
        }
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
