package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.sayhellototheworld.littlewatermelon.graduation.adapter.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogAcknowledge;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.MyListView;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCollectBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceCommentBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.ResourceShareBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResource;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceCollect;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageResourceComment;
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
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
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

import static com.sayhellototheworld.littlewatermelon.graduation.view.function_view.WriteCommentActivity.COMMENT_TYPE_FLEA_RRSOURCE_SHARE;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_COLLECT;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_HOME;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity.TYPE_RESOURCE_SHARE_OWN;

public class ResourceShareDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener,OnLoadMoreListener,OnRefreshListener,
        BmobDeletMsgDone,BmobQueryDone<ResourceCommentBean>,QueryCountListener {
    
    private TextView txt_back;
    private ImageView img_more;
    private TextView txt_content;
    private TextView txt_user_name;
    private CircleImageView head;
    private TextView txt_release_time;
    private TextView txt_link;
    private TextView txt_link_type;
    private TextView txt_link_ps;
    private TextView txt_link_copy;
    private TextView txt_comment_num;
    private MyListView comment_listview;
    private LinearLayout ll_comment;
    private LinearLayout ll_collect;
    private ImageView img_collect_icon;
    private TextView txt_collect;
    private LinearLayout ll_acknowledge;
    private SmartRefreshLayout refreshLayout;
    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_delete_resource;
    private BaseNiceDialog dialog;

    private int resourceType;
    private static ResourceShareBean resourceShareBean;
    private int nowSkip = 0;
    private CommentAdapter adapter;
    private BmobManageResourceComment commentManager;
    private BmobManageResourceCollect collectManager;
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
        setContentView(R.layout.activity_resource_share_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_resource_share_details_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_resource_share_details_more);
        img_more.setOnClickListener(this);
        txt_content = (TextView) findViewById(R.id.activity_resource_share_details_content);
        txt_user_name = (TextView) findViewById(R.id.activity_resource_share_details_user_name);
        head = (CircleImageView) findViewById(R.id.activity_resource_share_details_head_portrait);
        head.setOnClickListener(this);
        txt_release_time = (TextView) findViewById(R.id.activity_resource_share_details_create_time);
        txt_link = (TextView) findViewById(R.id.activity_resource_share_details_link);
        txt_link_type = (TextView) findViewById(R.id.activity_resource_share_details_link_type);
        txt_link_ps = (TextView) findViewById(R.id.activity_resource_share_details_link_password);
        txt_link_copy = (TextView) findViewById(R.id.activity_resource_share_details_copy);
        txt_link_copy.setOnClickListener(this);
        txt_comment_num = (TextView) findViewById(R.id.activity_resource_share_details_comment_num);
        comment_listview = (MyListView) findViewById(R.id.activity_resource_share_details_comment_list);
        ll_comment = (LinearLayout) findViewById(R.id.activity_resource_share_details_commentLayout);
        ll_comment.setOnClickListener(this);
        ll_collect = (LinearLayout) findViewById(R.id.activity_resource_share_details_collectLayout);
        ll_collect.setOnClickListener(this);
        img_collect_icon = (ImageView) findViewById(R.id.activity_resource_share_details_collect_icon);
        txt_collect = (TextView) findViewById(R.id.activity_resource_share_details_collect_txt);
        ll_acknowledge = (LinearLayout) findViewById(R.id.activity_resource_share_details_acknowledgeLayout);
        ll_acknowledge.setOnClickListener(this);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_resource_share_details_refreshLayout);
        refreshLayout.setEnableScrollContentWhenRefreshed(true);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        pop_window_view = LayoutInflater.from(this).inflate(R.layout.pop_window_manage_lost, null, false);
        txt_delete_resource = (TextView) pop_window_view.findViewById(R.id.pop_window_manage_lost_del);
        txt_delete_resource.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this,commentList);
        comment_listview.setAdapter(adapter);

        collectManager = BmobManageResourceCollect.getManager();
        commentManager = BmobManageResourceComment.getManager();
        resourceType = getIntent().getIntExtra("resourceType",-1);
    }

    @Override
    protected void initShow() {
        showMsg();
        showMoreIcon();
        tintManager.setStatusBarTintResource(R.color.white);
        refreshLayout.autoRefresh();
    }

    private void showMsg(){
        txt_user_name.setText(resourceShareBean.getUser().getNickName());
        txt_release_time.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(resourceShareBean.getReleaseTime().getDate())));
        txt_link.setText(resourceShareBean.getLink());
        txt_link_type.setText(resourceShareBean.getLinkType());
        txt_link_ps.setText(resourceShareBean.getLinkPassword());
        txt_content.setText(resourceShareBean.getContent());
        txt_comment_num.setText("评论 0");

        if (resourceShareBean.getUser().getHeadPortrait() != null && !resourceShareBean.getUser().getHeadPortrait().getUrl().equals("")){
            Glide.with(this)
                    .load(resourceShareBean.getUser().getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(head);
        }else {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(head);
        }
    }

    private void showMoreIcon(){
        switch (resourceType){
            case TYPE_RESOURCE_SHARE_HOME:
            case TYPE_RESOURCE_SHARE_OTHER:
            case TYPE_RESOURCE_SHARE_COLLECT:
                img_more.setVisibility(View.GONE);
                break;
            case TYPE_RESOURCE_SHARE_OWN:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_resource_share_details_back:
                finish();
                break;
            case R.id.activity_resource_share_details_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.activity_resource_share_details_commentLayout:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                WriteCommentActivity.go2Activity(this,resourceShareBean,COMMENT_TYPE_FLEA_RRSOURCE_SHARE);
                break;
            case R.id.activity_resource_share_details_acknowledgeLayout:
                if (resourceShareBean.getImageUrls() == null || resourceShareBean.getImageUrls().size() <= 0){
                    MyToastUtil.showToast("分享人似乎不在乎这点小钱,Ta并没有上传收款码");
                }else {
                    DialogAcknowledge.showDialog(this,getSupportFragmentManager(),resourceShareBean.getImageUrls());
                }
                break;
            case R.id.activity_resource_share_details_collectLayout:
                if (isLoading){
                    MyToastUtil.showToast("正在加载数据,请稍等");
                    return;
                }
                if (isCollectIng){
                    return;
                }
                collectChange();
                break;
            case R.id.pop_window_manage_lost_del:
                pop_window.dismiss();
                ensureDel();
                break;
            case R.id.activity_resource_share_details_head_portrait:
                UserDetailsActivity.go2Activity(this,resourceShareBean.getUser().getObjectId());
                break;
            case R.id.activity_resource_share_details_copy:
                ClipboardManager cm = (ClipboardManager) (getSystemService(Context.CLIPBOARD_SERVICE));// 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("text", resourceShareBean.getLink());// 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                MyToastUtil.showToast("链接复制成功,快去云盘下载吧");
                break;

        }
    }

    private void collectChange(){
        isCollectIng = true;
        if (collect){
            collectManager.delMsg(BmobManageUser.getCurrentUser(), resourceShareBean, new BmobDeletMsgDone() {
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
            ResourceCollectBean collectBean = new ResourceCollectBean();
            collectBean.setUser(BmobManageUser.getCurrentUser());
            collectBean.setCollectTime(new BmobDate(new Date()));
            collectBean.setResourceShareBean(resourceShareBean);
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
                    BmobExceptionUtil.dealWithException(ResourceShareDetailsActivity.this,e);
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
                                BmobManageResource.getManager().delMsg(resourceShareBean,ResourceShareDetailsActivity.this);
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
        commentManager.queryMsg(resourceShareBean,nowSkip,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        nowSkip = 0;
        loading = false;
        isLoading = true;
        commentList.clear();
        commentManager.queryMsg(resourceShareBean,nowSkip,this);
        commentManager.queryCount(resourceShareBean,this);
        queryCollect();
    }

    private void queryCollect(){
        collectManager.queryExist(BmobManageUser.getCurrentUser(), resourceShareBean, new QueryCountListener() {
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
                BmobExceptionUtil.dealWithException(ResourceShareDetailsActivity.this,e);
            }
        });
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
    public void querySuccess(List<ResourceCommentBean> data) {
        if (data == null || data.size() <= 0){
            MyToastUtil.showToast("已经到底啦,没数据啦~");
            finishSmart(true);
            return;
        }
        CommentBean bean;
        for (ResourceCommentBean l:data){
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
        txt_comment_num.setText("评论 " + integer);
    }

    @Override
    public void queryCountFailed(BmobException e) {
        BmobExceptionUtil.dealWithException(this,e);
    }


    private void finishSmart(boolean success){
        isLoading = false;
        if (!loading){
            refreshLayout.finishRefresh(success);
        }else {
            refreshLayout.finishLoadMore(success);
        }
    }

    public static void go2Activity(Context context,int resourceType, ResourceShareBean data){
        Intent intent = new Intent(context,ResourceShareDetailsActivity.class);
        intent.putExtra("resourceType",resourceType);
        resourceShareBean = data;
        context.startActivity(intent);
    }

    public static void go2Activity(Context context, int fleaMarkType, ResourceShareBean data, int code){
        Intent intent = new Intent(context,ResourceShareDetailsActivity.class);
        intent.putExtra("fleaMarkType",fleaMarkType);
        resourceShareBean = data;
        ((Activity)context).startActivityForResult(intent,code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        resourceShareBean = null;
    }

}
