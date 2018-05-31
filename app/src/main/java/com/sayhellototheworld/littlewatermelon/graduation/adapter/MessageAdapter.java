package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment.MessageFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.FriendRequestMsgActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.IMActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.teacher_view.BindTeacherMsgActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.CommonMessageActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.message_function_view.ForumCommentMessageActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.utils.AutoUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2018/5/14.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private MyUserBean user;
    private int type;
    private int[] noReadNum;
    private SmartRefreshLayout refreshLayout;
    private String[] nowMsg;
    private int[] nowIcon;
    private int[] nowType;
    private MessageItemClick listener;

    public final static int MSG_TYPE_LOST = 0;
    public final static int MSG_TYPE_FLEA = 1;
    public final static int MSG_TYPE_SHARE = 2;
    public final static int MSG_TYPE_FORUM = 3;
    public final static int MSG_TYPE_FRIEND_REQUEST = 4;
    public final static int MSG_TYPE_MY_FRIEND_REQUEST = 5;
    public final static int MSG_TYPE_FRIEND = 6;
    public final static int MSG_TYPE_STRANGER = 7;
    public final static int MSG_TYPE_STUDENT_BIND = 8;
    public final static int MSG_TYPE_TEACHER_BIND = 9;

    private String[] studentMsg = {"失物招领   的消息", "跳蚤市场   的消息", "资源共享   的消息", "同学圈   的消息", "新朋友请求   的消息","我的朋友申请   的消息",
            "好友私聊   的消息", "来自陌生人   的消息","教师绑定   的消息"};
    private int[] studentType = {MSG_TYPE_LOST, MSG_TYPE_FLEA, MSG_TYPE_SHARE, MSG_TYPE_FORUM,MSG_TYPE_FRIEND_REQUEST, MSG_TYPE_MY_FRIEND_REQUEST,
            MSG_TYPE_FRIEND,MSG_TYPE_STRANGER,MSG_TYPE_STUDENT_BIND};
    private int[] studentIcon = {R.drawable.lost_and_find, R.drawable.flea_market, R.drawable.resource_sharing, R.drawable.forum,
            R.drawable.friend_request_icon,R.drawable.my_request_friend_icon,R.drawable.friend,R.drawable.stranger,  R.drawable.teacher};

    private String[] teacherMsg = {"失物招领   的消息", "跳蚤市场   的消息", "资源共享   的消息", "同学圈   的消息", "新朋友请求   的消息",
            "我的朋友申请   的消息","好友私聊   的消息", "来自陌生人   的消息","学生绑定请求   的消息"};
    private int[] teacherType = {MSG_TYPE_LOST, MSG_TYPE_FLEA, MSG_TYPE_SHARE, MSG_TYPE_FORUM, MSG_TYPE_FRIEND_REQUEST,MSG_TYPE_MY_FRIEND_REQUEST,
            MSG_TYPE_FRIEND,MSG_TYPE_STRANGER,MSG_TYPE_TEACHER_BIND};
    private int[] teacherIcon = {R.drawable.lost_and_find, R.drawable.flea_market, R.drawable.resource_sharing, R.drawable.forum,
            R.drawable.friend_request_icon, R.drawable.my_request_friend_icon,R.drawable.friend,R.drawable.stranger, R.drawable.student};

    private String[] repairsMsg = {"失物招领   的消息", "跳蚤市场   的消息", "资源共享   的消息", "同学圈   的消息", "新朋友请求   的消息","我的朋友申请   的消息",
            "好友私聊   的消息", "来自陌生人   的消息"};
    private int[] repairsType = {MSG_TYPE_LOST, MSG_TYPE_FLEA, MSG_TYPE_SHARE, MSG_TYPE_FORUM, MSG_TYPE_FRIEND_REQUEST,MSG_TYPE_MY_FRIEND_REQUEST,
            MSG_TYPE_FRIEND, MSG_TYPE_STRANGER};
    private int[] repairsIcon = {R.drawable.lost_and_find, R.drawable.flea_market, R.drawable.resource_sharing,
            R.drawable.forum,R.drawable.friend_request_icon, R.drawable.my_request_friend_icon,R.drawable.friend,R.drawable.stranger};


    public MessageAdapter(Context context, int type, int[] noReadNum, SmartRefreshLayout refreshLayout) {
        this.context = context;
        this.type = type;
        this.noReadNum = noReadNum;
        this.refreshLayout = refreshLayout;
        user = BmobManageUser.getCurrentUser();
        if (type == MessageFragment.MESSAGE_TYPE_STUDENT) {
            nowMsg = studentMsg;
            nowIcon = studentIcon;
            nowType = studentType;
        } else if (type == MessageFragment.MESSAGE_TYPE_TEACHER) {
            nowMsg = teacherMsg;
            nowIcon = teacherIcon;
            nowType = teacherType;
        } else if (type == MessageFragment.MESSAGE_TYPE_REPAIRS) {
            nowMsg = repairsMsg;
            nowIcon = repairsIcon;
            nowType = repairsType;
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        AutoUtils.autoSize(view);
        MessageViewHolder viewHolder = new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.icon.setImageResource(nowIcon[position]);
        holder.txt_msg.setText(nowMsg[position]);
        if (noReadNum[position] <= 0) {
            holder.txt_no_read_num.setVisibility(View.GONE);
        } else {
            holder.txt_no_read_num.setVisibility(View.VISIBLE);
            holder.txt_no_read_num.setText(noReadNum[position] + "");
        }
        listener = new MessageItemClick(position);
        holder.rl_body.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return nowMsg.length;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView icon;
        public TextView txt_msg;
        public TextView txt_no_read_num;
        private RelativeLayout rl_body;

        public MessageViewHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.item_message_icon);
            txt_msg = (TextView) itemView.findViewById(R.id.item_message_msg);
            txt_no_read_num = (TextView) itemView.findViewById(R.id.item_message_no_read_num);
            rl_body = (RelativeLayout) itemView.findViewById(R.id.item_message_body);
        }
    }

    class MessageItemClick implements View.OnClickListener {

        private int position;

        MessageItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (type == MessageFragment.MESSAGE_TYPE_STUDENT) {
                studentClick(position);
            } else if (type == MessageFragment.MESSAGE_TYPE_TEACHER) {
                teacherClick(position);
            } else if (type == MessageFragment.MESSAGE_TYPE_REPAIRS) {
                repairClick(position);
            }
        }
    }

    private void studentClick(int position) {
        switch (nowType[position]) {
            case MSG_TYPE_LOST:
            case MSG_TYPE_FLEA:
            case MSG_TYPE_SHARE:
                CommonMessageActivity.go2Activity(context, nowType[position]);
                break;
            case MSG_TYPE_FORUM:
                ForumCommentMessageActivity.go2Activity(context);
                break;
            case MSG_TYPE_STUDENT_BIND:
                BindTeacherMsgActivity.go2Activity(context,BindTeacherMsgActivity.BIND_TEACHER_TYPE_STUDENT);
                break;
            case MSG_TYPE_FRIEND:
                IMActivity.go2Activity(context);
                break;
            case MSG_TYPE_STRANGER:
                break;
            case MSG_TYPE_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER);
                break;
            case MSG_TYPE_MY_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN);
                break;
        }
        noReadNum[position] = 0;
        notifyDataSetChanged();
    }

    private void teacherClick(int position) {
        switch (nowType[position]) {
            case MSG_TYPE_LOST:
            case MSG_TYPE_FLEA:
            case MSG_TYPE_SHARE:
                CommonMessageActivity.go2Activity(context, nowType[position]);
                break;
            case MSG_TYPE_TEACHER_BIND:
                BindTeacherMsgActivity.go2Activity(context,BindTeacherMsgActivity.BIND_TEACHER_TYPE_TEACHER);
                break;
            case MSG_TYPE_FORUM:
                ForumCommentMessageActivity.go2Activity(context);
                break;
            case MSG_TYPE_FRIEND:
                IMActivity.go2Activity(context);
                break;
            case MSG_TYPE_STRANGER:
                break;
            case MSG_TYPE_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER);
                break;
            case MSG_TYPE_MY_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN);
                break;
        }
        noReadNum[position] = 0;
        notifyDataSetChanged();
    }

    private void repairClick(int position) {
        switch (nowType[position]) {
            case MSG_TYPE_LOST:
            case MSG_TYPE_FLEA:
            case MSG_TYPE_SHARE:
                CommonMessageActivity.go2Activity(context, nowType[position]);
                break;
            case MSG_TYPE_FORUM:
                ForumCommentMessageActivity.go2Activity(context);
                break;
            case MSG_TYPE_FRIEND:
                IMActivity.go2Activity(context);
                break;
            case MSG_TYPE_STRANGER:
                break;
            case MSG_TYPE_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OTHER);
                break;
            case MSG_TYPE_MY_FRIEND_REQUEST:
                FriendRequestMsgActivity.go2Activity(context,FriendRequestMsgActivity.FRIEND_REQUEST_TYPE_OWN);
                break;
        }
        noReadNum[position] = 0;
        notifyDataSetChanged();
    }

}
