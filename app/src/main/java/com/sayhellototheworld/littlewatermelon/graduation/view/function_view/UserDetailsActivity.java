package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlUserDetails;
import com.sayhellototheworld.littlewatermelon.graduation.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.OwnForumActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.lost_and_find.LostAndFindActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener {

    private LinearLayout ll_body;
    private FrameLayout fl_body;
    private ImageView img_back;
    private TextView txt_title;
    private ImageView img_more;
    private CircleImageView head;
    private TextView txt_user_name;
    private ImageView img_user_type;
    private ImageView img_user_sex;
    private TextView txt_introduction;
    private ImageView img_forum;
    private ImageView img_lost;
    private ImageView img_flea;
    private ImageView img_share;
    private TextView txt_school_name;
    private TextView txt_real_name;
    private TextView txt_nick_name;
    private TextView txt_birthday;
    private TextView txt_local;
    private TextView txt_home;
    private TextView txt_email;
    private TextView txt_id;
    private Button btn_operation_friend;
    private Button btn_chat;
    private LinearLayout ll_bottom_body;
    private LinearLayout ll_function_body;

    private String userID;
    private int dataStatue = 0;
    private MyUserBean user;

    private ControlUserDetails cud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        ll_body = (LinearLayout) findViewById(R.id.activity_user_details_informationBackground);
        fl_body = (FrameLayout) findViewById(R.id.activity_user_details_body);
        img_back = (ImageView) findViewById(R.id.activity_user_details_back);
        img_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_user_details_title);
        img_more = (ImageView) findViewById(R.id.activity_user_details_more);
        img_more.setOnClickListener(this);
        head = (CircleImageView) findViewById(R.id.activity_user_details_head_portrait);
        txt_user_name = (TextView) findViewById(R.id.activity_user_details_user_name);
        img_user_type = (ImageView) findViewById(R.id.activity_user_details_user_type_icon);
        img_user_sex = (ImageView) findViewById(R.id.activity_user_details_sex_icon);
        txt_introduction = (TextView) findViewById(R.id.activity_user_details_introduction_content);
        img_forum = (ImageView) findViewById(R.id.activity_user_details_forum_icon);
        img_forum.setOnClickListener(this);
        img_lost = (ImageView) findViewById(R.id.activity_user_details_lost_and_find_icon);
        img_lost.setOnClickListener(this);
        img_flea = (ImageView) findViewById(R.id.activity_user_details_flea_icon);
        img_flea.setOnClickListener(this);
        img_share = (ImageView) findViewById(R.id.activity_user_details_share_icon);
        img_share.setOnClickListener(this);
        txt_school_name = (TextView) findViewById(R.id.activity_user_details_school_name);
        txt_real_name = (TextView) findViewById(R.id.activity_user_details_real_name);
        txt_nick_name = (TextView) findViewById(R.id.activity_user_details_nickname);
        txt_birthday = (TextView) findViewById(R.id.activity_user_details_birthday);
        txt_local = (TextView) findViewById(R.id.activity_user_details_location);
        txt_home = (TextView) findViewById(R.id.activity_user_details_home);
        txt_email = (TextView) findViewById(R.id.activity_user_details_email);
        txt_id = (TextView) findViewById(R.id.activity_user_details_id);
        btn_operation_friend = (Button) findViewById(R.id.activity_user_details_operation_friend);
        btn_operation_friend.setOnClickListener(this);
        btn_chat = (Button) findViewById(R.id.activity_user_details_chat);
        btn_chat.setOnClickListener(this);
        ll_bottom_body = (LinearLayout) findViewById(R.id.activity_user_details_bottom_body);
        ll_function_body = (LinearLayout) findViewById(R.id.activity_user_details_function_body);
    }

    @Override
    protected void initParam() {
        userID = getIntent().getStringExtra("userID");
        cud = new ControlUserDetails(this,userID,this);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarAlpha(0);
    }

    public void showMsg(MyUserBean userBean){
        txt_user_name.setText(userBean.getNickName());
        txt_title.setText(userBean.getNickName() + "的资料");

        if (userBean.getRole().equalsIgnoreCase("s")){
            img_user_type.setImageResource(R.drawable.student_small_icon);
        }else if (userBean.getRole().equalsIgnoreCase("t")){
            img_user_type.setImageResource(R.drawable.teacher_small_icon);
        }else if (userBean.getRole().equalsIgnoreCase("r")){
            img_user_type.setImageResource(R.drawable.repairs_small_icon);
        }

        if (userBean.getSex().equals("男")){
            img_user_sex.setImageResource(R.drawable.man_log);
        }else  if (userBean.getSex().equals("女")){
            img_user_sex.setImageResource(R.drawable.woman_log);
        }

        txt_school_name.setText(userBean.getSchoolName());
        txt_real_name.setText(userBean.getRealName());
        txt_nick_name.setText(userBean.getNickName());
        txt_birthday.setText(userBean.getBirthday());
        txt_local.setText(userBean.getLocation());
        txt_home.setText(userBean.getHometown());
        txt_email.setText(userBean.getMyEmail());
        txt_id.setText(userBean.getObjectId());

        if (userBean.getIntroduction() == null || userBean.getIntroduction().equals("")){
            txt_introduction.setText("他很懒,什么都没留下");
            txt_introduction.setTextColor(getResources().getColor(R.color.statue2));
        }else {
            txt_introduction.setText(userBean.getIntroduction());
        }

        if (userBean.getSkin() == null || userBean.getSkin().getUrl() == null) {
            LayoutBackgroundUtil.setLayoutBackground(this,ll_body,R.drawable.user_background);
        } else {
            Glide.with(this)
                    .load(userBean.getSkin().getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .placeholder(R.color.green)
                    .error(R.color.green)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            ll_body.setBackground(resource);
                        }
                    });
        }

        if (userBean.getHeadPortrait() == null || userBean.getHeadPortrait().getUrl() == null) {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(head);
        } else {
            Glide.with(this)
                    .load(userBean.getHeadPortrait().getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .error(R.drawable.head_log1)
                    .into(head);
        }

        if (userBean.getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
            ll_bottom_body.setVisibility(View.GONE);
            ll_function_body.setVisibility(View.GONE);
        }
    }

    public static void go2Activity(Context context, String userID) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_user_details_back:
                finish();
                break;
            case R.id.activity_user_details_more:
                if (!clickJudge())
                    return;
                break;
            case R.id.activity_user_details_forum_icon:
                if (!clickJudge())
                    return;
                OwnForumActivity.go2Activity(this, ControlForum.FORUM_TYPE_OTHER,userID);
                break;
            case R.id.activity_user_details_lost_and_find_icon:
                if (!clickJudge())
                    return;
                LostAndFindActivity.go2Activity(this,LostAndFindActivity.LOST_AND_FIND_TYPE_OTHER,userID);
                break;
            case R.id.activity_user_details_flea_icon:
                if (!clickJudge())
                    return;
                FleaMarketActivity.go2Activity(this,user);
                break;
            case R.id.activity_user_details_share_icon:
                if (!clickJudge())
                    return;
                ResourceSharingActivity.go2Activity(this,user);
                break;
            case R.id.activity_user_details_operation_friend:
                if (!clickJudge())
                    return;
                break;
            case R.id.activity_user_details_chat:
                if (!clickJudge())
                    return;
                break;
        }
    }

    private boolean clickJudge(){
        if (dataStatue == 0){
            MyToastUtil.showToast("数据加载中,请稍后");
            return false;
        }else if (dataStatue == -1){
            MyToastUtil.showToast("数据加载出错,请退出本界面重试");
            return false;
        }
        return true;
    }

    public void setDataStatue(int statue){
        dataStatue = statue;
    }

    public void setUser(MyUserBean user){
        this.user = user;
    }
}
