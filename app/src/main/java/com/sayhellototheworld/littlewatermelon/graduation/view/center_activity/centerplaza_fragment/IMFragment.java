package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.friend_view.QueryFriendActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.ChatFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.im_view.FriendFragment;

public class IMFragment extends Fragment implements View.OnClickListener{

    private static View mView;
    private LinearLayout parentLayout;
    private TextView txt_title;
    private ImageView img_more;
    private LinearLayout ll_chat_body;
    private LinearLayout ll_friend_body;
    private ImageView img_chat_icon;
    private ImageView img_friend_icon;
    private TextView txt_chat;
    private TextView txt_friend;
    
    private ChatFragment chatFragment;
    private FriendFragment friendFragment;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction mTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_im, container, false);
            init();
        }
        return mView;
    }

    private void init(){
        initWidget();
        initParam();
        initShow();
    }

    private void initWidget(){
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_im_ll_body);
        txt_title = (TextView) mView.findViewById(R.id.fragment_im_title);
        img_more = (ImageView) mView.findViewById(R.id.fragment_im_more);
        img_more.setOnClickListener(this);
        ll_chat_body = (LinearLayout) mView.findViewById(R.id.fragment_im_chat_body);
        ll_chat_body.setOnClickListener(this);
        ll_friend_body = (LinearLayout) mView.findViewById(R.id.fragment_im_friend_body);
        ll_friend_body.setOnClickListener(this);
        img_chat_icon = (ImageView) mView.findViewById(R.id.fragment_im_chat_icon);
        img_friend_icon = (ImageView) mView.findViewById(R.id.fragment_im_friend_icon);
        txt_chat = (TextView) mView.findViewById(R.id.fragment_im_chat_txt);
        txt_friend = (TextView) mView.findViewById(R.id.fragment_im_friend_txt);
    }

    private void initParam(){
        fm = getFragmentManager();
    }

    private void initShow(){
//        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
        setFragment(R.id.fragment_im_chat_body);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_im_more:
                QueryFriendActivity.go2Activity(getContext());
                break;
            case R.id.fragment_im_chat_body:
                setFragment(R.id.fragment_im_chat_body);
                break;
            case R.id.fragment_im_friend_body:
                setFragment(R.id.fragment_im_friend_body);
                break;

        }
    }

    private void setFragment(int id){
        mTransaction = fm.beginTransaction();
        hideFragment();
        switch (id){
            case R.id.fragment_im_chat_body:
                img_chat_icon.setImageResource(R.drawable.chat_checked1);
                img_friend_icon.setImageResource(R.drawable.friend_unchecked);
                txt_title.setText("聊天");
                txt_chat.setTextColor(getResources().getColor(R.color.statue2));
                txt_friend.setTextColor(getResources().getColor(R.color.white4));
                if(chatFragment == null){
                    chatFragment = new ChatFragment();
                    mTransaction.add(R.id.fragment_im_body,chatFragment);
                }else {
                    mTransaction.show(chatFragment);
                }
                mTransaction.commit();
                break;
            case R.id.fragment_im_friend_body:
                img_chat_icon.setImageResource(R.drawable.chat_unchecked);
                img_friend_icon.setImageResource(R.drawable.friend_checked);
                txt_title.setText("我的好友");
                txt_chat.setTextColor(getResources().getColor(R.color.white4));
                txt_friend.setTextColor(getResources().getColor(R.color.statue2));
                if(friendFragment == null){
                    friendFragment = new FriendFragment();
                    friendFragment.setImFragment(this);
                    mTransaction.add(R.id.fragment_im_body,friendFragment);
                }else {
                    mTransaction.show(friendFragment);
                    friendFragment.asyncFriendList();
                }
                mTransaction.commit();
                break;
        }
    }

    private void hideFragment(){
        if(friendFragment != null){
            mTransaction.hide(friendFragment);
        }
        if(chatFragment != null){
            mTransaction.hide(chatFragment);
        }
    }

    public void showChatFragment(){
        setFragment(R.id.fragment_im_chat_body);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }

    public void onParentActivityResult(String friendID){
        if (chatFragment != null){
            chatFragment.onParentActivityResult(friendID);
        }
    }

}
