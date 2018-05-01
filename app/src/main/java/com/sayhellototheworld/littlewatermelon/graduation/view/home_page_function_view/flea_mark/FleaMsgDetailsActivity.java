package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark;

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
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaMarketBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FleaCollectBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaCollect;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaComment;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageFleaMark;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobDeletMsgDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.QueryCountListener;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity.COMMENT_TYPE_FLEA_MARKET;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_COLLECT;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_HOME;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OWN;

public class FleaMsgDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,OnLoadMoreListener,OnRefreshListener,
        BmobQueryDone<FleaCommentBean>,QueryCountListener,BmobDeletMsgDone{

    private TextView txt_back;
    private ImageView img_more;
    private TextView txt_title;
    private TextView txt_user_name;
    private TextView txt_create_time;
    private TextView txt_price_type;
    private TextView txt_price;
    private TextView txt_content;
    private TextView txt_leave_word;
    private MyGridView gridView;
    private MyListView commentListView;
    private LinearLayout ll_leave_word;
    private LinearLayout ll_collect;
    private CircleImageView img_head;
    private ImageView img_collect_icon;
    private TextView txt_collect;
    private SmartRefreshLayout refreshLayout;
    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_delete_lost;
    private BaseNiceDialog dialog;

    public final static int FLEA_MARKER_DEL_CODE = 0;

    private int fleaMarkType;
    private static FleaMarketBean fleaMarkBean;
    private int nowSkip = 0;
    private CommentAdapter adapter;
    private BmobManageFleaCollect collectManager;
    private BmobManageFleaComment commentManager;
    private boolean loading = false;
    private boolean isLoading = false;
    private boolean isCollectIng = false;
    private boolean collect = false;
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
        setContentView(R.layout.activity_flea_msg_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_flea_msg_details_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_flea_msg_details_more);
        img_more.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.activity_flea_msg_details_title);
        txt_user_name = (TextView) findViewById(R.id.activity_flea_msg_details_user_name);
        txt_create_time = (TextView) findViewById(R.id.activity_flea_msg_details_create_time);
        txt_price_type = (TextView) findViewById(R.id.activity_flea_msg_details_price_type);
        txt_price = (TextView) findViewById(R.id.activity_flea_msg_details_price);
        txt_content = (TextView) findViewById(R.id.activity_flea_msg_details_content);
        txt_leave_word = (TextView) findViewById(R.id.activity_flea_msg_details_leave_word_num);
        gridView = (MyGridView) findViewById(R.id.activity_flea_msg_details_image);
        commentListView = (MyListView) findViewById(R.id.activity_flea_msg_details_comment_list);
        ll_leave_word = (LinearLayout) findViewById(R.id.activity_flea_msg_details_commentLayout);
        ll_leave_word.setOnClickListener(this);
        ll_collect = (LinearLayout) findViewById(R.id.activity_flea_msg_details_collectLayout);
        ll_collect.setOnClickListener(this);
        img_head = (CircleImageView) findViewById(R.id.activity_flea_msg_details_head_portrait);
        img_collect_icon = (ImageView) findViewById(R.id.activity_flea_msg_details_collect_icon);
        txt_collect = (TextView) findViewById(R.id.activity_flea_msg_details_collect_txt);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_flea_msg_details_refreshLayout);
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
        collectManager = BmobManageFleaCollect.getManager();
        commentManager = BmobManageFleaComment.getManager();

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this,commentList);
        commentListView.setAdapter(adapter);

        fleaMarkType = getIntent().getIntExtra("fleaMarkType",-1);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    protected void initShow() {
        showMsg();
        showMoreIcon();
        refreshLayout.autoRefresh();
    }

    private void showMsg(){
        txt_user_name.setText(fleaMarkBean.getUser().getNickName());
        txt_title.setText(fleaMarkBean.getTitle());
        txt_create_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(fleaMarkBean.getReleaseTime().getDate())));
        txt_price.setText(fleaMarkBean.getPrice());
        if (fleaMarkBean.getPriceType() == 0){
            txt_price_type.setText("一口价");
        }else if (fleaMarkBean.getPriceType() == 1){
            txt_price_type.setText("区间价");
        }
        txt_content.setText(fleaMarkBean.getContent());
        txt_leave_word.setText("留言 0");
        
        if (fleaMarkBean.getImageUrls() != null && fleaMarkBean.getImageUrls().size() > 0){
            ShowImageAdapter adapter = new ShowImageAdapter(this,fleaMarkBean.getImageUrls(),ShowImageAdapter.TYPE_READ_PLAN);
            gridView.setAdapter(adapter);
        }

        if (fleaMarkBean.getUser().getHeadPortrait() != null && !fleaMarkBean.getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(this)
                    .load(fleaMarkBean.getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(img_head);
        }else {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(img_head);
        }
    }

    private void showMoreIcon(){
        switch (fleaMarkType){
            case TYPE_FLEA_MARK_HOME:
            case TYPE_FLEA_MARK_OTHER:
            case TYPE_FLEA_MARK_COLLECT:
                img_more.setVisibility(View.GONE);
                break;
            case TYPE_FLEA_MARK_OWN:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_flea_msg_details_back:
                finish();
                break;
            case R.id.activity_flea_msg_details_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.activity_flea_msg_details_collectLayout:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                if (isCollectIng){
                    return;
                }
                collectChange();
                break;
            case R.id.activity_flea_msg_details_commentLayout:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                WriteCommentActivity.go2Activity(this,fleaMarkBean,COMMENT_TYPE_FLEA_MARKET);
                break;
            case R.id.pop_window_manage_lost_del:
                pop_window.dismiss();
                ensureDel();
                break;
        }
    }

    private void collectChange(){
        isCollectIng = true;
        if (collect){
            collectManager.delMsg(BmobManageUser.getCurrentUser(), fleaMarkBean, new BmobDeletMsgDone() {
                @Override
                public void delMsgSuc() {
                    MyToastUtil.showToast("已取消收藏");
                    queryCollect();
                    isCollectIng = false;
                }

                @Override
                public void delMsgFailed(BmobException e) {
                    MyToastUtil.showToast("取消收藏失败");
                    isCollectIng = false;
                }
            });
        }else {
            FleaCollectBean collectBean = new FleaCollectBean();
            collectBean.setUser(BmobManageUser.getCurrentUser());
            collectBean.setCollectTime(new BmobDate(new Date()));
            collectBean.setFleaMarket(fleaMarkBean);
            collectManager.uploadMsg(collectBean, new BmobSaveMsgWithoutImg() {
                @Override
                public void msgSuccess(String objectID) {
                    queryCollect();
                    MyToastUtil.showToast("收藏成功");
                    isCollectIng = false;
                }

                @Override
                public void msgFailed(BmobException e) {
                    MyToastUtil.showToast("收藏失败");
                    BmobExceptionUtil.dealWithException(FleaMsgDetailsActivity.this,e);
                    isCollectIng = false;
                }
            });
        }
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
                                BmobManageFleaMark.getManager().delMsg(fleaMarkBean,FleaMsgDetailsActivity.this);
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
        commentManager.queryMsg(fleaMarkBean,nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        isLoading = true;
        commentList.clear();
        commentManager.queryMsg(fleaMarkBean,nowSkip,this);
        commentManager.queryCount(fleaMarkBean,this);
        queryCollect();
    }

    private void queryCollect(){
        collectManager.queryExist(BmobManageUser.getCurrentUser(), fleaMarkBean, new QueryCountListener() {
            @Override
            public void queryCountSuc(Integer integer) {
                if (integer > 0){
                    img_collect_icon.setImageDrawable(getResources().getDrawable(R.drawable.collected_icon));
                    txt_collect.setTextColor(getResources().getColor(R.color.liked));
                    txt_collect.setText("已收藏");
                    collect = true;
                }else {
                    img_collect_icon.setImageDrawable(getResources().getDrawable(R.drawable.collect_icon));
                    txt_collect.setTextColor(getResources().getColor(R.color.black1));
                    txt_collect.setText("收藏");
                    collect = false;
                }
            }

            @Override
            public void queryCountFailed(BmobException e) {
                BmobExceptionUtil.dealWithException(FleaMsgDetailsActivity.this,e);
            }
        });
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
    public void querySuccess(List<FleaCommentBean> data) {
        if (data == null || data.size() <= 0){
            MyToastUtil.showToast("已经到底啦,没数据啦~");
            finishSmart(true);
            return;
        }
        CommentBean bean;
        for (FleaCommentBean l:data){
            bean = new CommentBean();
            bean.setContent(l.getContent());
            bean.setUser(l.getUser());
            bean.setCreateDate(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(l.getReleaseTime().getDate())));
            commentList.add(bean);
        }
        adapter.notifyDataSetChanged();
        nowSkip++;
        finishSmart(true);
    }

    @Override
    public void queryFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
        finishSmart(false);
    }

    @Override
    public void queryCountSuc(Integer integer) {
        txt_leave_word.setText("留言 " + integer);
    }

    @Override
    public void queryCountFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
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

    public static void go2Activity(Context context, int fleaMarkType, FleaMarketBean data){
        Intent intent = new Intent(context,FleaMsgDetailsActivity.class);
        intent.putExtra("fleaMarkType",fleaMarkType);
        fleaMarkBean = data;
        context.startActivity(intent);
    }

    public static void go2Activity(Context context, int fleaMarkType, FleaMarketBean data,int code){
        Intent intent = new Intent(context,FleaMsgDetailsActivity.class);
        intent.putExtra("fleaMarkType",fleaMarkType);
        fleaMarkBean = data;
        ((Activity)context).startActivityForResult(intent,code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fleaMarkBean = null;
    }

}
