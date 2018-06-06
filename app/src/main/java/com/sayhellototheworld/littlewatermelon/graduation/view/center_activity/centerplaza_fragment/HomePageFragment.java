package com.sayhellototheworld.littlewatermelon.graduation.view.center_activity.centerplaza_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.HomePageRecycleViewAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.AnnouncementBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.TeacherBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageAnnouncement;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageTeacher;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.bmob_interface.BmobQueryDone;
import com.sayhellototheworld.littlewatermelon.graduation.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.announcement.AnnouncementActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.user_view.LoginActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;


public class HomePageFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private LinearLayout parentLayout;
    private TextView txt_shcool_name;
    private TextView txt_user_name;
    private RecyclerView mRecyclerView;
    private View alphaView;
    private TextView txt_login;
    private LinearLayout ll_announcement_body;
    private TextView txt_announcement_title;
    private TextView txt_announcement_content;

    private static MyUserBean userBean;
    private static boolean show = false;
    private boolean login = false;
    private boolean bindScool = false;
    private boolean annFirstShow = true;
    private static HomePageFragment homePageFragment;
    private MyUserBean teacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_home_page, container, false);
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
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_home_page_parent);
        txt_shcool_name = (TextView) mView.findViewById(R.id.fragment_home_page_school_name);
        txt_user_name = (TextView) mView.findViewById(R.id.fragment_home_page_user_name);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.fragment_home_page_recycle_view);
        alphaView = mView.findViewById(R.id.fragment_home_page_view);
        txt_login = (TextView) mView.findViewById(R.id.fragment_home_page_login_txt);
        txt_login.setOnClickListener(this);
        ll_announcement_body = (LinearLayout) mView.findViewById(R.id.fragment_home_page_announcement_body);
        ll_announcement_body.setOnClickListener(this);
        txt_announcement_title = (TextView) mView.findViewById(R.id.fragment_home_page_announcement_title);
        txt_announcement_content = (TextView) mView.findViewById(R.id.fragment_home_page_announcement_content);
    }

    private void initParam(){
        show = true;
        homePageFragment = this;
        userBean = BmobManageUser.getCurrentUser();
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(manager);
        login = userBean != null;
    }

    private void initShow(){
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
        showTopbar(login);
        showRecycleView(login);
    }

    private void showTopbar(boolean login){
        if (!login){
            txt_user_name.setVisibility(View.GONE);
            ll_announcement_body.setVisibility(View.GONE);
            txt_shcool_name.setText("手掌校园");
        }else {
            txt_user_name.setVisibility(View.VISIBLE);
            if (userBean.getRole().equalsIgnoreCase("s")){
                txt_user_name.setText("学生  " + userBean.getNickName());
                ll_announcement_body.setVisibility(View.GONE);
                showAnnouncement();
            }else if (userBean.getRole().equalsIgnoreCase("r")){
                txt_user_name.setText("维修员  " + userBean.getNickName());
                ll_announcement_body.setVisibility(View.GONE);
            }else if (userBean.getRole().equalsIgnoreCase("t")){
                txt_user_name.setText("老师  " + userBean.getNickName());
                ll_announcement_body.setVisibility(View.GONE);
            }
            if (userBean.getSchoolName() != null&&!userBean.getSchoolName().equals("")){
                txt_shcool_name.setText(userBean.getSchoolName());
            }else {
                txt_shcool_name.setText("手掌校园");
            }
        }
    }

    public void showAnnouncement(){
        BmobManageTeacher.getManager().queryBindedByStudent(BmobManageUser.getCurrentUser(), new BmobQueryDone<TeacherBean>() {
            @Override
            public void querySuccess(List<TeacherBean> data) {
                if (data.size() > 0){
                    teacher = data.get(0).getTeacher();
                    BmobManageAnnouncement.getManager().queryOneByUser(teacher, new BmobQueryDone<AnnouncementBean>() {
                        @Override
                        public void querySuccess(List<AnnouncementBean> data) {
                            if (data.size() > 0){
                                if (ll_announcement_body.getVisibility() == View.GONE){
                                    ll_announcement_body.setVisibility(View.VISIBLE);
                                }
                                if (annFirstShow){
                                    txt_announcement_title.setText(data.get(0).getTitle());
                                    txt_announcement_content.setText(data.get(0).getContent());
                                    annFirstShow = false;
                                }else if (!data.get(0).getTitle().equalsIgnoreCase(txt_announcement_title.getText().toString()) &&
                                        !data.get(0).getContent().equalsIgnoreCase(txt_announcement_content.getText().toString())){
                                    txt_announcement_title.setText(data.get(0).getTitle());
                                    txt_announcement_content.setText(data.get(0).getContent());
                                }
                            }
                        }

                        @Override
                        public void queryFailed(BmobException e) {

                        }
                    });
                }else {

                }
            }

            @Override
            public void queryFailed(BmobException e) {

            }
        });
    }

    private void showRecycleView(boolean login){
        if (!login){
            alphaView.setVisibility(View.VISIBLE);
            txt_login.setVisibility(View.VISIBLE);
            HomePageRecycleViewAdapter adapter = new HomePageRecycleViewAdapter(getActivity(),"s",login);
            mRecyclerView.setAdapter(adapter);
        }else {
            alphaView.setVisibility(View.GONE);
            txt_login.setVisibility(View.GONE);
            HomePageRecycleViewAdapter adapter = new HomePageRecycleViewAdapter(getActivity(),userBean.getRole(),login);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        show = false;
        userBean = null;
        homePageFragment = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_home_page_login_txt:
                LoginActivity.startLoginActivity(getActivity());
                break;
            case R.id.fragment_home_page_announcement_body:
                AnnouncementActivity.go2Activity(getContext(),teacher);
                break;
        }
    }

    public static void syncHomePageFragment(){
        if (!show)
            return;
        userBean = BmobManageUser.getCurrentUser();
        homePageFragment.showTopbar(true);
        homePageFragment.showRecycleView(true);
    }

    public static void syncHomePageFragmentAnno(){
        if (!show)
            return;
        userBean = BmobManageUser.getCurrentUser();
        if (userBean.getRole().equalsIgnoreCase("s")){
            homePageFragment.showAnnouncement();
        }
    }

}
