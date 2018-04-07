package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.ShowCurUserInfo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.center_plaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.PersonalInformationActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.RegisterUserActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.UserSettingActivity;
import com.zhy.autolayout.AutoLinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment implements BaseActivityDo, View.OnClickListener, ShowCurUserInfo {

    private View mView;
    private AutoLinearLayout parentLayout;
    private CircleImageView mCircleImageView;
    private TextView textView_userName;
    private TextView textView_introductionContent;
    private TextView textView_introduction;
    private ImageView imageView_introductionPen;
    private ImageView imageView_userSex;
    private ScrollView itemScrollView;
    private LinearLayout registerAndLoginLayout;
    private Button button_register;
    private Button button_login;
    private RelativeLayout mRelativeLayout_setting;
    private RelativeLayout mRelativeLayout_setSkin;

    private ControlUserFragment cuf;

    private static boolean login = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_user, null);
            init();
        }
        return mView;
    }

    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        mCircleImageView = (CircleImageView) mView.findViewById(R.id.fragment_user_headPortrait);
        mCircleImageView.setOnClickListener(this);
        textView_userName = (TextView) mView.findViewById(R.id.fragment_user_userName);
        textView_userName.setOnClickListener(this);
        textView_introductionContent = (TextView) mView.findViewById(R.id.fragment_user_introductionContent);
        textView_introductionContent.setOnClickListener(this);
        textView_introduction = (TextView) mView.findViewById(R.id.fragment_user_introduction);
        textView_introduction.setOnClickListener(this);
        imageView_introductionPen = (ImageView) mView.findViewById(R.id.fragment_user_introductionPen);
        imageView_introductionPen.setOnClickListener(this);
        imageView_userSex = (ImageView) mView.findViewById(R.id.fragment_user_sex);
        imageView_userSex.setOnClickListener(this);
        parentLayout = (AutoLinearLayout) mView.findViewById(R.id.fragment_user_informationBackground);
        mRelativeLayout_setting = (RelativeLayout) mView.findViewById(R.id.fragment_user_setting);
        mRelativeLayout_setting.setOnClickListener(this);
        mRelativeLayout_setSkin = (RelativeLayout) mView.findViewById(R.id.fragment_user_setSkin);
        mRelativeLayout_setSkin.setOnClickListener(this);
        itemScrollView = (ScrollView) mView.findViewById(R.id.fragment_user_ItemScrollView);
        registerAndLoginLayout = (LinearLayout) mView.findViewById(R.id.fragment_user_registerAndLoginLayout);
        button_login = (Button) mView.findViewById(R.id.fragment_user_loginButton);
        button_login.setOnClickListener(this);
        button_register = (Button) mView.findViewById(R.id.fragment_user_registerButton);
        button_register.setOnClickListener(this);
    }

    @Override
    public void initParam() {
        cuf = new ControlUserFragment(getActivity(), this);
    }

    @Override
    public void initShow() {
        LayoutBackgroundUtil.setLayoutBackground(getActivity(),parentLayout,R.drawable.user_background);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_headPortrait:
            case R.id.fragment_user_userName:
            case R.id.fragment_user_introduction:
            case R.id.fragment_user_introductionContent:
            case R.id.fragment_user_introductionPen:
            case R.id.fragment_user_sex:
                informationClick();
                break;
            case R.id.fragment_user_setSkin:
                setInforBackground();
                break;
            case R.id.fragment_user_setting:
                UserSettingActivity.startLoginActivity(getActivity());
                break;
            case R.id.fragment_user_loginButton:
                LoginActivity.startLoginActivity(getActivity());
                break;
            case R.id.fragment_user_registerButton:
                RegisterUserActivity.startLoginActivity(getActivity());
                break;

        }
    }

    private void informationClick() {
        if (login) {
            PersonalInformationActivity.startPersonalInformationActivity(getActivity());
        } else {
            LoginActivity.startLoginActivity(getActivity());
        }
    }

    private void setInforBackground() {
        if(!login){
            return;
        }
        NiceDialog.init()
                .setLayoutId(R.layout.nicedialog_head_portrait)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final TextView editText1 = holder.getView(R.id.nicedialog_head_portrait_takePicture);
                        final TextView editText2 = holder.getView(R.id.nicedialog_head_portrait_chooseDFromAlbum);
                        final TextView editText3 = holder.getView(R.id.nicedialog_head_portrait_choosedFromSystem);
                        editText3.setText("使用系统默认背景");
                        editText1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                takePicture();
                                dialog.dismiss();
                            }
                        });
                        editText2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                choosePicture();
                                dialog.dismiss();
                            }
                        });
                        editText3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                useSysHeadSkin();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setMargin(10)
                .setShowBottom(true)
                .show(((FragmentActivity)getActivity()).getSupportFragmentManager());
    }

    private void takePicture(){

    }

    private void choosePicture(){
        ShowPictureActivity.startShowPictureActivityForResult(getActivity(), ShowPictureActivity.TARGET_BACKGROUND);
    }

    private void useSysHeadSkin(){
        cuf.useSysHeadSkin();
    }

    @Override
    public void showUserInformation(MyUserBean userBean) {
        if (userBean == null) {
            mCircleImageView.setImageResource(R.drawable.headportrait_boy);
            textView_userName.setText("登录");
            imageView_userSex.setVisibility(View.GONE);
            textView_introduction.setVisibility(View.GONE);
            imageView_introductionPen.setVisibility(View.GONE);
            textView_introductionContent.setVisibility(View.GONE);
            itemScrollView.setVisibility(View.GONE);
            registerAndLoginLayout.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(R.drawable.user_background)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            parentLayout.setBackground(resource);
                        }
                    });
            login = false;
            return;
        }

        textView_userName.setVisibility(View.VISIBLE);
        imageView_userSex.setVisibility(View.VISIBLE);
        textView_introduction.setVisibility(View.VISIBLE);
        imageView_introductionPen.setVisibility(View.VISIBLE);
        textView_introductionContent.setVisibility(View.VISIBLE);
        itemScrollView.setVisibility(View.VISIBLE);
        registerAndLoginLayout.setVisibility(View.GONE);

        textView_userName.setText(userBean.getNickName());

        if (userBean.getSex().equals("男")) {
            imageView_userSex.setImageResource(R.drawable.man_log);
        } else if (userBean.getSex().equals("女")) {
            imageView_userSex.setImageResource(R.drawable.woman_log);
        }

        if (userBean.getIntroduction() == null || userBean.getIntroduction().equals("")) {
            textView_introduction.setVisibility(View.GONE);
            textView_introductionContent.setText("编辑您的简介");
        } else {
            textView_introduction.setVisibility(View.VISIBLE);
            textView_introductionContent.setText(userBean.getIntroduction());
        }

        if (userBean.getHeadPortrait() == null || userBean.getHeadPortrait().getUrl() == null) {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(mCircleImageView);
        } else {
            Glide.with(this)
                    .load(ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(userBean.getHeadPortrait().getUrl())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .error(R.drawable.head_log1)
                    .into(mCircleImageView);
        }

        if (userBean.getSkin() == null || userBean.getSkin().getUrl() == null) {
            LayoutBackgroundUtil.setLayoutBackground(getActivity(),parentLayout,R.drawable.user_background);
        } else {
            Glide.with(this)
                    .load(ManageFile.getSelfBackground(PictureUtil.getPicNameFromUrl(userBean.getSkin().getUrl())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .error(R.drawable.user_background)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            parentLayout.setBackground(resource);
                        }
                    });
        }
        login = true;
    }

    public static void setLogin(boolean l) {
        login = l;
    }

    public static boolean getLoginStatue() {
        return login;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cuf.userFragmentDestroy();
        login = false;
        mView = null;
    }

}
