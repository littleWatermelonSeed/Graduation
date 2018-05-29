package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.forum_function.ControlForum;
import com.sayhellototheworld.littlewatermelon.graduation.util.ScreenUtils;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.AllSchoolForumFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.LocalSchoolForumFragment;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.OwnForumActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.forum_function_view.WriteForumActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.PersonalInformationActivity;


public class ForumFragment extends Fragment implements View.OnClickListener{

    private static View mView;
    private LinearLayout parentLayout;
    private ImageView img_more;
    private TextView txt_local_school;
    private TextView txt_all_school;

    private PopupWindow pop_window;
    private View pop_window_view;
    private TextView txt_write;
    private TextView txt_own;

    private LocalSchoolForumFragment localFragment;
    private AllSchoolForumFragment allFragment;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction mTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_forum, container, false);
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
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_forum_parent);
        img_more = (ImageView) mView.findViewById(R.id.fragment_forum_more);
        txt_local_school = (TextView) mView.findViewById(R.id.fragment_forum_local_school);
        txt_all_school = (TextView) mView.findViewById(R.id.fragment_forum_all_school);

        pop_window_view= LayoutInflater.from(getContext()).inflate(R.layout.pop_window_forum_more, null, false);
        txt_write = (TextView) pop_window_view.findViewById(R.id.pop_window_forum_more_write_write);
        txt_write.setOnClickListener(this);
        txt_own = (TextView) pop_window_view.findViewById(R.id.pop_window_forum_more_write_own);

        txt_own.setOnClickListener(this);
        txt_write.setOnClickListener(this);
        txt_own.setOnClickListener(this);
        txt_all_school.setOnClickListener(this);
        txt_local_school.setOnClickListener(this);
        img_more.setOnClickListener(this);
    }

    private void initParam(){
        fm = getFragmentManager();
        showTop();
    }

    private void initShow(){
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);

    }

    private void showTop(){
        if (BmobManageUser.getCurrentUser().getSchoolName() == null || BmobManageUser.getCurrentUser().getSchoolName().equals("")){
            txt_local_school.setVisibility(View.GONE);
            txt_all_school.setBackgroundResource(R.drawable.radius_background8);
            setFragment(R.id.fragment_forum_all_school);
        }else {
            txt_local_school.setVisibility(View.VISIBLE);
            txt_local_school.setBackgroundResource(R.drawable.radius_background8);
            txt_all_school.setBackgroundResource(R.drawable.radius_background4);
            txt_local_school.setText(BmobManageUser.getCurrentUser().getSchoolName());
            setFragment(R.id.fragment_forum_local_school);
        }
    }

    private void setFragment(int id){
        mTransaction = fm.beginTransaction();
        hideFragment();
        switch (id){
            case R.id.fragment_forum_local_school:
                if(localFragment == null){
                    localFragment = new LocalSchoolForumFragment();
                    mTransaction.add(R.id.fragment_forum_frame,localFragment);
                }else {
                    mTransaction.show(localFragment);
                }
                mTransaction.commit();
                break;
            case R.id.fragment_forum_all_school:
                if(allFragment == null){
                    allFragment = new AllSchoolForumFragment();
                    mTransaction.add(R.id.fragment_forum_frame,allFragment);
                }else {
                    mTransaction.show(allFragment);
                }
                mTransaction.commit();
                break;
        }
    }

    private void hideFragment(){
        if(allFragment != null){
            mTransaction.hide(allFragment);
        }
        if(localFragment != null){
            mTransaction.hide(localFragment);
        }
    }

    public static void recreateView(){
        mView = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_forum_more:
                pop_window = new PopupWindow(pop_window_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop_window.setOutsideTouchable(true);
                pop_window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int[] arr = ScreenUtils.calculatePopWindowPos(img_more,pop_window_view);
                pop_window.showAtLocation(img_more, Gravity.TOP | Gravity.START,arr[0] - 50,arr[1] - 50);
                break;
            case R.id.fragment_forum_local_school:
                txt_local_school.setBackgroundResource(R.drawable.radius_background8);
                txt_all_school.setBackgroundResource(R.drawable.radius_background4);
                setFragment(R.id.fragment_forum_local_school);
                break;
            case R.id.fragment_forum_all_school:
                txt_local_school.setBackgroundResource(R.drawable.radius_background4);
                txt_all_school.setBackgroundResource(R.drawable.radius_background8);
                setFragment(R.id.fragment_forum_all_school);
                break;
            case R.id.pop_window_forum_more_write_write:
                pop_window.dismiss();
                if (BmobManageUser.getCurrentUser().getSchoolName() == null || BmobManageUser.getCurrentUser().getSchoolName().equals("")){
                    DialogConfirm.newInstance("提示", "你还没有绑定学校,是否前去绑定学校?",
                            new DialogConfirm.CancleAndOkDo() {
                                @Override
                                public void cancle() {

                                }

                                @Override
                                public void ok() {
                                    PersonalInformationActivity.startPersonalInformationActivity(getContext());
                                }
                            }).setMargin(60)
                            .setOutCancel(false)
                            .show(getActivity().getSupportFragmentManager());
                    return;
                }
                WriteForumActivity.go2Activity(getContext());
                break;
            case R.id.pop_window_forum_more_write_own:
                pop_window.dismiss();
                OwnForumActivity.go2Activity(getContext(), ControlForum.FORUM_TYPE_OWN);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
    }
}
