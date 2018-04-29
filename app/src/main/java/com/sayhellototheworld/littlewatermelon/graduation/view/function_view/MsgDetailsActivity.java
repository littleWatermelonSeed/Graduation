package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.CommentAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.ShowImageAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyListView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostAndFindBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.LostCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostAndFind;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageLostComment;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MsgDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,OnLoadMoreListener,OnRefreshListener,
        BmobQueryDone{

    private TextView txt;
    private TextView txt_back;
    private ImageView img_more;
    private TextView txt_title;
    private CircleImageView img_head;
    private TextView txt_user_name;
    private TextView txt_create_time;
    private TextView txt_content;
    private MyGridView gridView;
    private TextView txt_comment_num;
    private TextView txt_like_num;
    private MyListView comment_list;
    private LinearLayout ll_comment;
    private LinearLayout ll_like;
    private ImageView img_like_icon;
    private TextView txt_like;
    private SmartRefreshLayout refreshLayout;
    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_delete_lost;
    private BaseNiceDialog dialog;

    public final static int MSG_DETAILS_REQUEST_CODE = 0;

    public final static int DETAILS_TYPE_LOST = 0;

    private static int details_type = -1;
    private static BmobObject bmobObject;
    private static boolean stared;

    private boolean loading = false;
    private boolean privateLost;
    private int nowSkip = 0;
    private CommentAdapter adapter;
    private List<CommentBean> commentList;

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
        setContentView(R.layout.activity_msg_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_msg_details_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_msg_details_more);
        img_more.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_msg_details_title);
        img_head = (CircleImageView) findViewById(R.id.activity_msg_details_head_portrait);
        txt_user_name = (TextView) findViewById(R.id.activity_msg_details_name);
        txt_create_time = (TextView) findViewById(R.id.activity_msg_details_create_time);
        txt_content = (TextView) findViewById(R.id.activity_msg_details_content);
        gridView = (MyGridView) findViewById(R.id.activity_msg_details_image);
        txt_comment_num = (TextView) findViewById(R.id.activity_msg_details_comment_num);
        txt_like_num = (TextView) findViewById(R.id.activity_msg_details_likes_num);
        comment_list = (MyListView) findViewById(R.id.activity_msg_details_comment_list);
        ll_comment = (LinearLayout) findViewById(R.id.activity_msg_details_commentLayout);
        ll_comment.setOnClickListener(this);
        ll_like = (LinearLayout) findViewById(R.id.activity_msg_details_likeLayout);
        ll_like.setOnClickListener(this);
        txt_like = (TextView) findViewById(R.id.activity_msg_details_like_txt);
        img_like_icon = (ImageView) findViewById(R.id.activity_msg_details_like_icon);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_msg_details_refreshLayout);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        pop_window_view = LayoutInflater.from(this).inflate(R.layout.pop_window_manage_lost, null, false);
        txt_delete_lost = (TextView) pop_window_view.findViewById(R.id.pop_window_manage_lost_del);
        txt_delete_lost.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        tintManager.setStatusBarTintResource(R.color.white);
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this,commentList);
        privateLost = getIntent().getBooleanExtra("privateLost",false);
    }

    @Override
    protected void initShow() {
        showBody();
        refreshLayout.autoRefresh();
        if (!privateLost){
            img_more.setVisibility(View.GONE);
        }
    }

    private void showBody(){
        switch (details_type){
            case DETAILS_TYPE_LOST:
                showLostBody();
                break;
        }
    }

    private void showLostBody(){
        LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
        txt_title.setText(lostAndFindBean.getTitle());
        txt_content.setText(lostAndFindBean.getContent());
        txt_create_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(lostAndFindBean.getReleaseTime().getDate())));
        txt_user_name.setText(lostAndFindBean.getUser().getNickName());
        txt_comment_num.setText(lostAndFindBean.getCommentNum() + "");
        txt_like_num.setText(lostAndFindBean.getStars() + "");

        if (stared){
            increaseStars();
        }else {
            reduceStars();

        }

        if (lostAndFindBean.getImageUrls() != null && lostAndFindBean.getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(this,lostAndFindBean.getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            gridView.setAdapter(adapter);
        }

        if (lostAndFindBean.getUser().getHeadPortrait() != null && !lostAndFindBean.getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(this)
                    .load(lostAndFindBean.getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(img_head);
        }else {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(img_head);
        }
        comment_list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_msg_details_back:
                finish();
                break;
            case R.id.activity_msg_details_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.activity_msg_details_commentLayout:
                goToComment();
                break;
            case R.id.activity_msg_details_likeLayout:
                stars();
                break;
            case R.id.pop_window_manage_lost_del:
                pop_window.dismiss();
                ensureDel();
                break;
        }
    }

    private void ensureDel() {
        DialogConfirm.newInstance("提示", "确定取消修改/完善个人资料?", new DialogConfirm.CancleAndOkDo() {
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
                                delLost();
                            }
                        });
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void goToComment(){
        switch (details_type){
            case DETAILS_TYPE_LOST:
                LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
                WriteCommentActivity.go2Activity(this,lostAndFindBean,WriteCommentActivity.COMMENT_TYPE_LOST_AND_FIND);
                break;
        }
    }

    private void stars(){
        switch (details_type){
            case DETAILS_TYPE_LOST:
                LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
                if (!stared){
                    lostAndFindBean.increment("stars");
                    txt_like_num.setText(Integer.parseInt(txt_like_num.getText().toString()) + 1 + "");
                    lostAndFindBean.setStars(lostAndFindBean.getStars() + 1);
                    increaseStars();
                    stared = true;
                }else {
                    lostAndFindBean.increment("stars",-1);
                    txt_like_num.setText(Integer.parseInt(txt_like_num.getText().toString()) - 1 + "");
                    lostAndFindBean.setStars(lostAndFindBean.getStars() - 1);
                    reduceStars();
                    stared = false;
                }
                lostAndFindBean.update();

                break;
        }

    }

    private void increaseStars(){
        img_like_icon.setImageDrawable(getResources().getDrawable(R.drawable.liked));
        txt_like_num.setTextColor(getResources().getColor(R.color.liked));
        txt_like.setTextColor(getResources().getColor(R.color.liked));
    }

    private void reduceStars(){
        img_like_icon.setImageDrawable(getResources().getDrawable(R.drawable.like));
        txt_like_num.setTextColor(getResources().getColor(R.color.black1));
        txt_like.setTextColor(getResources().getColor(R.color.black1));
    }

    private void delLost(){
        switch (details_type){
            case DETAILS_TYPE_LOST:
                LostAndFindBean lostAndFindBean = (LostAndFindBean) bmobObject;
                BmobManageLostAndFind.getManager().delMsg(lostAndFindBean, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            DialogLoading.dismissLoadingDialog(handler, dialog, "删除成功", DialogLoading.MSG_SUCCESS);
                        }else {
                            DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
                            BmobExceptionUtil.dealWithException(MsgDetailsActivity.this,e);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        switch (details_type){
            case DETAILS_TYPE_LOST:
                loading = true;
                BmobManageLostComment.getManager().queryMsg((LostAndFindBean) bmobObject,nowSkip,this);
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        switch (details_type){
            case DETAILS_TYPE_LOST:
                nowSkip = 0;
                loading = false;
                commentList.clear();
                BmobManageLostComment.getManager().queryMsg((LostAndFindBean) bmobObject,nowSkip,this);
                break;
        }
    }

    private void finishSmart(boolean success){
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    @Override
    public void querySuccess(List data) {
        if (data == null || data.size() <= 0){
            MyToastUtil.showToast("已经到底啦,没数据啦~");
            finishSmart(true);
            return;
        }
        switch (details_type){
            case DETAILS_TYPE_LOST:
                List<LostCommentBean> commList = data;
                CommentBean bean;
                for (LostCommentBean l:commList){
                    bean = new CommentBean();
                    bean.setContent(l.getContent());
                    bean.setUser(l.getUser());
                    bean.setCreateDate(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(l.getReleaseTime().getDate())));
                    commentList.add(bean);
                }
                adapter.notifyDataSetChanged();
                nowSkip++;
                finishSmart(true);
                break;
        }
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
        finishSmart(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmobObject = null;
        details_type = -1;
        stared = false;

    }

    public static void go2Activity(Context context, boolean star,BmobObject bmobObj, int type,boolean privateLost) {
        Intent intent = new Intent(context, MsgDetailsActivity.class);
        bmobObject = bmobObj;
        details_type = type;
        stared = star;
        intent.putExtra("privateLost",privateLost);
        ((Activity)context).startActivityForResult(intent,MSG_DETAILS_REQUEST_CODE);
    }

}
