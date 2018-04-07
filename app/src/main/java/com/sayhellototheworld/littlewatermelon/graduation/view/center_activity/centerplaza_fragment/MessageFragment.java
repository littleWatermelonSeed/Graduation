package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;

public class MessageFragment extends Fragment {

    private View mView;
    private LinearLayout parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_message, container, false);
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
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_message_parent);
    }

    private void initParam(){

    }

    private void initShow(){
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }
}
