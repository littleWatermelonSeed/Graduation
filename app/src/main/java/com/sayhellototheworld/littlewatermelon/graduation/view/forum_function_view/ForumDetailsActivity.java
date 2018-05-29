package com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ForumCommentAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyListView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ForumCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForum;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageForumComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobUpdateDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.UserDetailsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

public class ForumDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,OnLoadMoreListener,OnRefreshListener,
        BmobQueryDone<ForumCommentBean>,QueryCountListener,BmobDeletMsgDone {

    private TextView txt_back;
    private ImageView img_more;
    private CircleImageView head;
    private TextView txt_user_name;
    private TextView txt_school_name;
    private TextView txt_content;
    private MyGridView gridView;
    private TextView txt_release_time;
    private MyListView comment_list_view;
    private ImageView img_like;
    private TextView txt_like_num;
    private ImageView img_comment;
    private TextView txt_comment_num;
    private TextView txt_bottom_comment;
    private ImageView img_bottom_like;
    private SmartRefreshLayout refreshLayout;

    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_del;
    
    private int type;
    private static ForumBean forumBean;
    private String userID;
    private boolean isLikeIng = false;
    private BmobManageForum manageForum;
    private BmobManageForumComment manageForumComment;
    private static ControlForum controlForum;
    private int position;
    private boolean loading = false;
    private boolean isLoading = false;
    private int nowSkip = 0;
    private BaseNiceDialog dialog;

    private ForumCommentAdapter adapter;
    private List<ForumCommentBean> forumCommentData;

    public final static int FORUM_DEL_CODE = 0;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == DialogLoading.MSG_FAIL) {
                dialog.dismiss();
                MyToastUtil.showToast("删除失败");
            } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                dialog.dismiss();
                MyToastUtil.showToast("删除成功");
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_forum_details_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_forum_details_more);
        img_more.setOnClickListener(this);
        head = (CircleImageView) findViewById(R.id.activity_forum_details_head_portrait);
        head.setOnClickListener(this);
        txt_user_name = (TextView) findViewById(R.id.activity_forum_details_user_name);
        txt_school_name = (TextView) findViewById(R.id.activity_forum_details_school_name);
        txt_content = (TextView) findViewById(R.id.activity_forum_details_content);
        gridView = (MyGridView) findViewById(R.id.activity_forum_details_image);
        txt_release_time = (TextView) findViewById(R.id.activity_forum_details_release_time);
        img_like = (ImageView) findViewById(R.id.activity_forum_details_like_icon);
        img_like.setOnClickListener(this);
        txt_like_num = (TextView) findViewById(R.id.activity_forum_details_like_num);
        txt_like_num.setOnClickListener(this);
        img_comment = (ImageView) findViewById(R.id.activity_forum_details_comment_icon);
        img_comment.setOnClickListener(this);
        txt_comment_num = (TextView) findViewById(R.id.activity_forum_details_comment_num);
        txt_comment_num.setOnClickListener(this);
        txt_bottom_comment = (TextView) findViewById(R.id.activity_forum_details_bottom_comment);
        txt_bottom_comment.setOnClickListener(this);
        img_bottom_like = (ImageView) findViewById(R.id.activity_forum_details_bottom_like);
        img_bottom_like.setOnClickListener(this);
        comment_list_view = (MyListView) findViewById(R.id.activity_forum_details_comment_list);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_forum_details_refreshLayout);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);

        pop_window_view = LayoutInflater.from(this).inflate(R.layout.pop_window_manage_lost, null, false);
        txt_del = (TextView) pop_window_view.findViewById(R.id.pop_window_manage_lost_del);
        txt_del.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        type = getIntent().getIntExtra("type",-1);
        position = getIntent().getIntExtra("position",-1);

        userID = BmobManageUser.getCurrentUser().getObjectId();
        manageForum = BmobManageForum.getManager();
        manageForumComment = BmobManageForumComment.getManager();

        forumCommentData = new ArrayList<>();
        adapter = new ForumCommentAdapter(this,forumCommentData);
        comment_list_view.setAdapter(adapter);
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
        if (type == ControlForum.FORUM_TYPE_ALL_SCHOOL || type == ControlForum.FORUM_TYPE_LOACL_SCHOOL ||
                type == ControlForum.FORUM_TYPE_MSG || type == ControlForum.FORUM_TYPE_OTHER){
            img_more.setVisibility(View.GONE);
        }else if (type == ControlForum.FORUM_TYPE_OWN){
            img_more.setVisibility(View.VISIBLE);
        }
        showMsg();
        refreshLayout.autoRefresh();
    }
    
    private void showMsg(){
        txt_user_name.setText(forumBean.getUser().getNickName());
        txt_school_name.setText(forumBean.getUser().getSchoolName());
        txt_content.setText(forumBean.getContent());
        txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(forumBean.getCreatedAt())));
        txt_like_num.setText(forumBean.getLikeNum() + "");
        txt_comment_num.setText(forumBean.getCommentNum() + "");

        if (forumBean.getImageUrls() != null && forumBean.getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(this,forumBean.getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            gridView.setAdapter(adapter);
        }

        if (forumBean.getUser().getHeadPortrait() != null && !forumBean.getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(this)
                    .load(forumBean.getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(head);
        }else {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(head);
        }

        if (forumBean.getLikeUserObjID() != null && forumBean.getLikeUserObjID().contains(userID)){
            img_like.setImageResource(R.drawable.liked);
            img_bottom_like.setImageResource(R.drawable.liked);
        }else {
            img_like.setImageResource(R.drawable.like);
            img_bottom_like.setImageResource(R.drawable.like);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forum_details_back:
                finish();
                break;
            case R.id.activity_forum_details_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.activity_forum_details_head_portrait:
                if (forumBean.getUser().getObjectId().equals(BmobManageUser.getCurrentUser().getObjectId())){
                    return;
                }
                UserDetailsActivity.go2Activity(this,forumBean.getUser().getObjectId());
                break;
            case R.id.activity_forum_details_like_icon:
            case R.id.activity_forum_details_like_num:
            case R.id.activity_forum_details_bottom_like:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                doLike();
                break;
            case R.id.activity_forum_details_comment_icon:
            case R.id.activity_forum_details_comment_num:
            case R.id.activity_forum_details_bottom_comment:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                WriteCommentActivity.go2Activity(this,forumBean,WriteCommentActivity.COMMENT_TYPE_FORUM);
                break;
            case R.id.pop_window_manage_lost_del:
                pop_window.dismiss();
                ensureDel();
                break;
        }
    }

    private void doLike() {
        if (isLikeIng)
            return;
        isLikeIng = true;
        String tempLike = "";
        boolean tempAdd = true;
        int num;
        if (forumBean.getLikeUserObjID() != null && forumBean.getLikeUserObjID().contains(userID)){
            tempLike = forumBean.getLikeUserObjID().replace(userID + ",","");
            tempAdd = false;
            num = -1;
        }else {
            if (forumBean.getLikeUserObjID() == null){
                tempLike = userID + ",";
            }else {
                tempLike = forumBean.getLikeUserObjID() + userID + ",";
            }
            tempAdd = true;
            num = 1;
        }

        final boolean add = tempAdd;
        final String likeObjID = tempLike;
        manageForum.updateLike(forumBean.getObjectId(), tempLike,num, new BmobUpdateDone() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (add){
                        img_like.setImageResource(R.drawable.liked);
                        img_bottom_like.setImageResource(R.drawable.liked);
                        txt_like_num.setText((Integer.parseInt(txt_like_num.getText().toString()) + 1) + "");
                        forumBean.setLikeNum(Integer.parseInt(txt_like_num.getText().toString()));
                    }else {
                        img_like.setImageResource(R.drawable.like);
                        img_bottom_like.setImageResource(R.drawable.like);
                        txt_like_num.setText((Integer.parseInt(txt_like_num.getText().toString()) - 1) + "");
                        forumBean.setLikeNum(Integer.parseInt(txt_like_num.getText().toString()));
                    }
                    forumBean.setLikeUserObjID(likeObjID);
                    if (type != ControlForum.FORUM_TYPE_MSG){
                        controlForum.notifyAdapter(position,Integer.parseInt(txt_like_num.getText().toString()),likeObjID);
                    }
                }
                isLikeIng = false;
            }
        });
    }

    private void ensureDel() {
        DialogConfirm.newInstance("提示", "确定删除此贴?", new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {
            }
            @Override
            public void ok() {
                DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                        new DialogLoading.ShowLoadingDone() {
                            @Override
                            public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                dialog = baseNiceDialog;
                                TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                                textView.setText("删除中...");
                                manageForum.delMsg(forumBean,ForumDetailsActivity.this);
                            }
                        });
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loading = true;
        isLoading = true;
        manageForumComment.queryMsg(forumBean,nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        isLoading = true;
        forumCommentData.clear();
        manageForumComment.queryMsg(forumBean,nowSkip,this);
        manageForumComment.queryCount(forumBean,this);
    }

    private void finishSmart(boolean success){
        isLoading = false;
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    @Override
    public void delMsgSuc() {
        DialogLoading.dismissLoadingDialog(handler, dialog, "删除成功", DialogLoading.MSG_SUCCESS);
    }

    @Override
    public void delMsgFailed(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

    @Override
    public void queryCountSuc(Integer integer) {
        txt_comment_num.setText(integer + "");
    }

    @Override
    public void queryCountFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
    }

    @Override
    public void querySuccess(List<ForumCommentBean> data) {
        if (data == null || data.size() <= 0){
            MyToastUtil.showToast("已经到底啦,没数据啦~");
            finishSmart(true);
            return;
        }
        forumCommentData.addAll(data);
        adapter.notifyDataSetChanged();
        nowSkip++;
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
        finishSmart(false);
    }

    public static void go2Activity(Context context, int type, int position, ControlForum c, ForumBean f){
        Intent intent = new Intent(context,ForumDetailsActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("position",position);
        forumBean = f;
        controlForum = c;
        context.startActivity(intent);
    }

    public static void go2Activity(Context context, int type, ForumBean f){
        Intent intent = new Intent(context,ForumDetailsActivity.class);
        intent.putExtra("type",type);
        forumBean = f;
        context.startActivity(intent);
    }

    public static void go2Activity(Context context, int type, int position, ControlForum c, ForumBean f, int code){
        Intent intent = new Intent(context,ForumDetailsActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("position",position);
        forumBean = f;
        controlForum = c;
        ((Activity)context).startActivityForResult(intent,code);
    }


}
