package com.sayhellototheworld.littlewatermelon.graduation.customwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.DensityUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * Created by 123 on 2017/5/11.
 */

public class LiTopBar extends RelativeLayout {

    private AutoRelativeLayout relativeLayout_content;
    private AutoRelativeLayout relativeLayout_leftContainer;
    private AutoLinearLayout linearLayout_rightContainer;
    private AutoRelativeLayout relativeLayout_middleContainer;

    private LinearLayout linearLayout_middleDevelopment;
    private LinearLayout linearLayout_rightDevelopment;

    private ImageView imageView_left;
    private ImageView imageView_right;

    private TextView textView_left;
    private TextView textView_right;
    private TextView textView_middle;

    private Drawable bodyBackground;

    private Drawable leftImageViewSrc;
    private Drawable leftImageViewBackground;
    private Drawable leftContainerBackground;
    private float leftImageViewWidth;
    private float leftImageViewHeight;
    private String leftText;
    private int leftTextColor;
    private float leftTextSize;
    private float leftContainerMargin;

    private String middleText;
    private Drawable middleContainerBackground;
    private int middleTextColor;
    private float middleTextSize;

    private String rightText;
    private int rightTextColor;
    private Drawable rightImageViewSrc;
    private Drawable rightImageViewBackground;
    private Drawable rightContainerBackground;
    private float rightImageViewWidth;
    private float rightImageViewHeight;
    private float rightTextSize;
    private float rightContainerMargin;

    private int middleDevelopmentChildNum = 0;
    private int rightDevelopmentChildNum = 0;
    private boolean isRightText = false;
    private boolean isMiddleText = false;

    private TypedArray typedArray;
    private Context context;

    public LiTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.li_top_bar, this);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.LiTopBar);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        initWidget();
        getAttrs();
        setAttrs();
    }

    private void initWidget() {
        relativeLayout_content = (AutoRelativeLayout) findViewById(R.id.li_top_bar_content);
        relativeLayout_leftContainer = (AutoRelativeLayout) findViewById(R.id.li_top_bar_leftContainer);
        linearLayout_rightContainer = (AutoLinearLayout) findViewById(R.id.li_top_bar_rightContainer);
        relativeLayout_middleContainer = (AutoRelativeLayout) findViewById(R.id.li_top_bar_middleContainer);

        linearLayout_middleDevelopment = (LinearLayout) findViewById(R.id.li_top_bar_middleDevelopment);
        linearLayout_rightDevelopment = (LinearLayout) findViewById(R.id.li_top_bar_rightDevelopment);

        imageView_left = (ImageView) findViewById(R.id.li_top_bar_leftImage);
        imageView_right = (ImageView) findViewById(R.id.li_top_bar_rightImage);

        textView_left = (TextView) findViewById(R.id.li_top_bar_leftTextview);
        textView_right = (TextView) findViewById(R.id.li_top_bar_rightTextview);
        textView_middle = (TextView) findViewById(R.id.li_top_bar_middleTextview);

    }

    private void getAttrs() {
        bodyBackground = typedArray.getDrawable(R.styleable.LiTopBar_bodyBackground);

        leftImageViewSrc = typedArray.getDrawable(R.styleable.LiTopBar_leftImageSrc);
        leftImageViewBackground = typedArray.getDrawable(R.styleable.LiTopBar_leftImageBackground);
        leftContainerBackground = typedArray.getDrawable(R.styleable.LiTopBar_leftContainerBackground);
        leftText = typedArray.getString(R.styleable.LiTopBar_leftText);
        leftTextColor = typedArray.getColor(R.styleable.LiTopBar_leftTextColor, getResources().getColor(R.color.black));
        leftImageViewWidth = typedArray.getDimension(R.styleable.LiTopBar_leftImageViewWidth, DensityUtil.dip2px(context,25));
        leftImageViewHeight = typedArray.getDimension(R.styleable.LiTopBar_leftImageViewHeight, DensityUtil.dip2px(context,49));
        leftTextSize = typedArray.getDimension(R.styleable.LiTopBar_lefttextSize,DensityUtil.sp2px(context,4));
        leftContainerMargin = typedArray.getDimension(R.styleable.LiTopBar_leftContainerMargin,0);

        middleTextSize = typedArray.getDimension(R.styleable.LiTopBar_middleTextSize,DensityUtil.sp2px(context,5));
        middleText = typedArray.getString(R.styleable.LiTopBar_middleText);
        middleContainerBackground = typedArray.getDrawable(R.styleable.LiTopBar_middleContainerBackground);
        middleTextColor = typedArray.getColor(R.styleable.LiTopBar_middleTextColor, getResources().getColor(R.color.black));

        rightTextSize = typedArray.getDimension(R.styleable.LiTopBar_rightTextSize,DensityUtil.sp2px(context,4));
        rightText = typedArray.getString(R.styleable.LiTopBar_rightText);
        rightImageViewSrc = typedArray.getDrawable(R.styleable.LiTopBar_rightImageSrc);
        rightImageViewBackground = typedArray.getDrawable(R.styleable.LiTopBar_rightImageViewBackground);
        rightContainerBackground = typedArray.getDrawable(R.styleable.LiTopBar_rightContainerBackgroun);
        rightTextColor = typedArray.getColor(R.styleable.LiTopBar_rightTextColor, getResources().getColor(R.color.black));
        rightImageViewWidth = typedArray.getDimension(R.styleable.LiTopBar_rightImageViewWidth,DensityUtil.dip2px(context,25));
        rightImageViewHeight = typedArray.getDimension(R.styleable.LiTopBar_rightImageViewHeight,DensityUtil.dip2px(context,49));
        rightContainerMargin = typedArray.getDimension(R.styleable.LiTopBar_rightContainerMargin,0);

        typedArray.recycle();
    }

    private void setAttrs() {
        relativeLayout_content.setBackground(bodyBackground);

        imageView_left.setImageDrawable(leftImageViewSrc);
        imageView_left.setBackground(leftImageViewBackground);
        relativeLayout_leftContainer.setBackground(leftContainerBackground);
        textView_left.setText(leftText);
        textView_left.setTextColor(leftTextColor);
        textView_left.setTextSize(leftTextSize);

        textView_middle.setText(middleText);
        relativeLayout_middleContainer.setBackground(middleContainerBackground);
        textView_middle.setTextColor(middleTextColor);
        textView_middle.setTextSize(middleTextSize);

        textView_right.setText(rightText);
        textView_right.setTextColor(rightTextColor);
        imageView_right.setImageDrawable(rightImageViewSrc);
        imageView_right.setBackground(rightImageViewBackground);
        linearLayout_rightContainer.setBackground(rightContainerBackground);
        textView_right.setTextSize(rightTextSize);

//        RelativeLayout.LayoutParams paramsLeft = new LayoutParams(relativeLayout_leftContainer.getLayoutParams());
//        paramsLeft.setMargins((int) leftContainerMargin,0,0,0);
//        relativeLayout_leftContainer.setLayoutParams(paramsLeft);
//
//        LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(linearLayout_rightContainer.getLayoutParams());
//        paramsLeft.setMargins(0,0,(int) rightContainerMargin,0);
//        linearLayout_rightContainer.setLayoutParams(paramsRight);

        if (leftImageViewBackground == null && leftImageViewSrc == null) {
            imageView_left.setVisibility(GONE);
        }

        if (rightImageViewBackground == null && rightImageViewSrc == null) {
            imageView_right.setVisibility(GONE);
        }

        if (rightText != null) {
            isRightText = true;
        }

        if (middleText != null) {
            isMiddleText = true;
        }

        setLeftImageViewSize();
        setRightImageViewSize();
    }

    private void setLeftImageViewSize(){
        LayoutParams params = (LayoutParams) imageView_left.getLayoutParams();
        params.width = (int) leftImageViewWidth;
        params.height = (int) leftImageViewHeight;
        imageView_left.setLayoutParams(params);
    }

    private void setRightImageViewSize(){
        ViewGroup.LayoutParams params = imageView_right.getLayoutParams();
        params.width = (int) rightImageViewWidth;
        params.height = (int) rightImageViewHeight;
        imageView_right.setLayoutParams(params);
    }

    public void setLeftContainerListener(OnClickListener l) {
        relativeLayout_leftContainer.setOnClickListener(l);
    }

    public void setRightContainerListener(OnClickListener l) {
        linearLayout_rightContainer.setOnClickListener(l);
    }

    public void setMiddleeContainerListener(OnClickListener l) {
        relativeLayout_middleContainer.setOnClickListener(l);
    }

    public void setLeftImageViewListener(OnClickListener l) {
        imageView_left.setOnClickListener(l);
    }

    public void setRightImageViewListener(OnClickListener l) {
        imageView_right.setOnClickListener(l);
    }

    public void setLeftTextViewListener(OnClickListener l) {
        textView_left.setOnClickListener(l);
    }

    public void setRightTextViewListener(OnClickListener l) {
        textView_right.setOnClickListener(l);
    }

    public void setMiddleTextViewListener(OnClickListener l) {
        textView_middle.setOnClickListener(l);
    }

    public boolean setMiddleDevelopmentListener(OnClickListener l) {
        if (middleDevelopmentChildNum == 1) {
            linearLayout_middleDevelopment.setOnClickListener(l);
            return true;
        }
        return false;
    }

    public boolean setRightDevelopmentListener(OnClickListener l) {
        if (rightDevelopmentChildNum == 1) {
            linearLayout_rightDevelopment.setOnClickListener(l);
            return true;
        }
        return false;
    }

    public boolean addViewToMiddleDevelopment(View view) {
        if (!isMiddleText && middleDevelopmentChildNum == 0) {
            linearLayout_middleDevelopment.addView(view);
            middleDevelopmentChildNum++;
            return true;
        }
        return false;
    }

    public boolean addViewToMiddleDevelopment(View view, LayoutParams params) {
        if (!isMiddleText && middleDevelopmentChildNum == 0) {
            linearLayout_middleDevelopment.addView(view, params);
            middleDevelopmentChildNum++;
            return true;
        }
        return false;
    }

    public boolean addViewToRightDevelopment(View view) {
        if (!isRightText && rightDevelopmentChildNum == 0) {
            linearLayout_rightDevelopment.addView(view);
            rightDevelopmentChildNum++;
            return true;
        }
        return false;
    }

    public boolean addViewToRightDevelopment(View view, LayoutParams params) {
        if (!isRightText && rightDevelopmentChildNum == 0) {
            linearLayout_rightDevelopment.addView(view, params);
            rightDevelopmentChildNum++;
            return true;
        }
        return false;
    }

    public Drawable getBodyBackground() {
        return bodyBackground;
    }

    public void setBodyBackground(Drawable bodyBackground) {
        this.bodyBackground = bodyBackground;
        relativeLayout_content.setBackground(bodyBackground);
    }

    public Drawable getLeftImageViewSrc() {
        return leftImageViewSrc;
    }

    public void setLeftImageViewSrc(Drawable leftImageViewSrc) {
        this.leftImageViewSrc = leftImageViewSrc;
        imageView_left.setImageDrawable(leftImageViewSrc);
    }

    public Drawable getLeftImageViewBackground() {
        return leftImageViewBackground;
    }

    public void setLeftImageViewBackground(Drawable leftImageViewBackground) {
        this.leftImageViewBackground = leftImageViewBackground;
        imageView_left.setBackground(leftImageViewBackground);
    }

    public Drawable getLeftContainerBackground() {
        return leftContainerBackground;
    }

    public void setLeftContainerBackground(Drawable leftContainerBackground) {
        this.leftContainerBackground = leftContainerBackground;
        relativeLayout_leftContainer.setBackground(leftContainerBackground);
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        textView_left.setText(leftText);
    }

    public int getLeftTextColor() {
        return leftTextColor;
    }

    public void setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        textView_left.setTextColor(leftTextColor);
    }

    public String getMiddleText() {
        return middleText;
    }

    public void setMiddleText(String middleText) {
        if (!middleText.equals("")) {
            isMiddleText = true;
        } else {
            isMiddleText = false;
        }

        if (middleDevelopmentChildNum == 0) {
            this.middleText = middleText;
            textView_middle.setText(middleText);
        }
    }

    public Drawable getMiddleContainerBackground() {
        return middleContainerBackground;
    }

    public void setMiddleContainerBackground(Drawable middleContainerBackground) {
        this.middleContainerBackground = middleContainerBackground;
        relativeLayout_middleContainer.setBackground(middleContainerBackground);
    }

    public int getMiddleTextColor() {
        return middleTextColor;
    }

    public void setMiddleTextColor(int middleTextColor) {
        this.middleTextColor = middleTextColor;
        textView_middle.setTextColor(middleTextColor);
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        if (!rightText.equals("")) {
            isRightText = true;
        } else {
            isRightText = false;
        }

        if (rightDevelopmentChildNum == 0) {
            this.rightText = rightText;
            textView_right.setText(rightText);
        }
    }

    public int getRightTextColor() {
        return rightTextColor;
    }

    public void setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        textView_right.setTextColor(rightTextColor);
    }

    public Drawable getRightImageViewSrc() {
        return rightImageViewSrc;
    }

    public void setRightImageViewSrc(Drawable rightImageViewSrc) {
        this.rightImageViewSrc = rightImageViewSrc;
        imageView_right.setImageDrawable(rightImageViewSrc);
    }

    public Drawable getRightImageViewBackground() {
        return rightImageViewBackground;
    }

    public void setRightImageViewBackground(Drawable rightImageViewBackground) {
        this.rightImageViewBackground = rightImageViewBackground;
        imageView_right.setBackground(rightImageViewBackground);
    }

    public Drawable getRightContainerBackground() {
        return rightContainerBackground;
    }

    public void setRightContainerBackground(Drawable rightContainerBackground) {
        this.rightContainerBackground = rightContainerBackground;
        linearLayout_rightContainer.setBackground(rightContainerBackground);
    }

    public LinearLayout getLinearLayout_middleDevelopment() {
        return linearLayout_middleDevelopment;
    }

    public LinearLayout getLinearLayout_rightDevelopment() {
        return linearLayout_rightDevelopment;
    }

    public ImageView getImageView_left() {
        return imageView_left;
    }

    public ImageView getImageView_right() {
        return imageView_right;
    }

    public TextView getTextView_left() {
        return textView_left;
    }

    public TextView getTextView_right() {
        return textView_right;
    }

    public TextView getTextView_middle() {
        return textView_middle;
    }
}
