package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.FriendRequestBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageRequestFriend;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobSaveMsgWithoutImg;
import com.sayhellototheworld.littlewatermelon.graduation.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.UserDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/28.
 */

public class QueryFriendtAdapter extends RecyclerView.Adapter<QueryFriendtAdapter.QueryFriendtHolder> {

    private Context context;
    private List<MyUserBean> data;

    private QueryFriendtClick listener;

    public QueryFriendtAdapter(Context context, List<MyUserBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public QueryFriendtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_query_friend, parent, false);
        AutoUtils.autoSize(view);
        QueryFriendtHolder viewHolder = new QueryFriendtHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QueryFriendtHolder holder, int position) {

        listener = new QueryFriendtClick(position);
        if (data.get(position).getHeadPortrait() != null && !data.get(position).getHeadPortrait().getUrl().equals("")) {
            Glide.with(context)
                    .load(data.get(position).getHeadPortrait().getUrl())
                    .dontAnimate()
                    .into(holder.head);
        } else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(holder.head);
        }

        if (data.get(position).getRealName() != null && !data.get(position).getRealName().equals("")) {
            holder.txt_student_name.setText(data.get(position).getRealName());
        } else {
            holder.txt_student_name.setText(data.get(position).getNickName());
        }

        holder.rl_body.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class QueryFriendtHolder extends RecyclerView.ViewHolder {

        public CircleImageView head;
        public TextView txt_student_name;
        public RelativeLayout rl_body;

        public QueryFriendtHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.item_query_friend_head_portrait);
            txt_student_name = (TextView) itemView.findViewById(R.id.item_query_friend_student_name);
            rl_body = (RelativeLayout) itemView.findViewById(R.id.item_query_friend_body);
        }
    }

    class QueryFriendtClick implements View.OnClickListener {

        private int position;

        QueryFriendtClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_query_friend_body:
                    UserDetailsActivity.go2Activity(context, data.get(position).getObjectId());
                    break;
            }
        }

        private void requestFriend() {
            NiceDialog.init()
                    .setLayoutId(R.layout.pop_request_friend)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                            requestFriend(holder, dialog);
                        }
                    })
                    .setDimAmount(0.5f)
                    .setShowBottom(false)
                    .setWidth(-1)
                    .setOutCancel(false)
                    .show(((FragmentActivity) context).getSupportFragmentManager());
        }

        private void requestFriend(ViewHolder holder, final BaseNiceDialog dialog) {
            final EditText edt_remark = holder.getView(R.id.pop_request_friend_remark);
            TextView txt_cancle = holder.getView(R.id.pop_request_friend_cancle);
            TextView txt_ensure = holder.getView(R.id.pop_request_friend_ensure);
            txt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            txt_ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doRequest(dialog, edt_remark);
                }
            });
        }

        private void doRequest(BaseNiceDialog dialog, final EditText edt_remark) {
            dialog.dismiss();
            DialogLoading.showLoadingDialog(((FragmentActivity) context).getSupportFragmentManager(),
                    new DialogLoading.ShowLoadingDone() {
                        @Override
                        public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                            TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                            textView.setText("申请中...");
                            FriendRequestBean bean = new FriendRequestBean();
                            bean.setFriendRead(false);
                            bean.setFriend(data.get(position));
                            bean.setUser(BmobManageUser.getCurrentUser());
                            bean.setRemark(edt_remark.getText().toString());
                            bean.setStatue(0);
                            BmobManageRequestFriend.getManager().uploadMsg(bean, new BmobSaveMsgWithoutImg() {
                                @Override
                                public void msgSuccess(String objectID) {
                                    baseNiceDialog.dismiss();
                                    MyToastUtil.showToast("好友申请成功");
                                }

                                @Override
                                public void msgFailed(BmobException e) {
                                    baseNiceDialog.dismiss();
                                    BmobExceptionUtil.dealWithException(context, e);
                                }
                            });
                        }
                    });
        }
    }

}
