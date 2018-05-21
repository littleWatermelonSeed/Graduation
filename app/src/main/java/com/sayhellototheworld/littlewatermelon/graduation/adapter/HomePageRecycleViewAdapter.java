package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.AnnouncementActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.lost_and_find.LostAndFindActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.request_leave.RequestLeaveActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.RequestRepairsActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.resource_share.ResourceSharingActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.respond_leave.RespondLeaveActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.RespondRepairActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 123 on 2018/4/15.
 */

public class HomePageRecycleViewAdapter extends RecyclerView.Adapter<HomePageRecycleViewAdapter.MyViewHolder>{

    private int[] icons_student = {R.drawable.lost_and_find,R.drawable.flea_market,R.drawable.resource_sharing,
            R.drawable.repairs,R.drawable.ask_for_leave};
    private int[] icons_repairman = {R.drawable.lost_and_find,R.drawable.flea_market,R.drawable.resource_sharing,
            R.drawable.repairs};
    private int[] icons_teacher = {R.drawable.lost_and_find,R.drawable.flea_market,R.drawable.resource_sharing,
            R.drawable.repairs,R.drawable.ask_for_leave,R.drawable.announcement_icon};
    private String[] itemName_student = {"失物招领","跳蚤市场","资源共享","我要报修","我要请假"};
    private String[] itemName_repairman = {"失物招领","跳蚤市场","资源共享","维修申请"};
    private String[] itemName_teacher = {"失物招领","跳蚤市场","资源共享","我要报修","学生请假","发布公告"};

    private int[] icons;
    private String[] itemName;
    private ItemClickListener listener;

    private String role;
    private Context context;
    private boolean login;

    public HomePageRecycleViewAdapter(Context context,String role,boolean login){
        this.context = context;
        this.role = role;
        this.login = login;
        if (role.equalsIgnoreCase("s")){
            icons = icons_student;
            itemName = itemName_student;
        }
        if (role.equalsIgnoreCase("r")){
            icons = icons_repairman;
            itemName = itemName_repairman;
        }
        if (role.equalsIgnoreCase("t")){
            icons = icons_teacher;
            itemName = itemName_teacher;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_page_recycle_view,parent,false);
        AutoUtils.autoSize(view);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txt.setText(itemName[position]);
        holder.imageView.setImageResource(icons[position]);
        if (!login)
            return;
        holder.imageView.setOnClickListener(new ItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView imageView;  //这两个控件为RecyclerView中每个item中的控件
        public MyViewHolder(View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.item_home_page_recycle_view_txt);  //获取控件
            imageView = (ImageView)view.findViewById(R.id.item_home_page_recycle_view_image);
        }
    }

    class ItemClickListener implements View.OnClickListener{

        private int position;
        public ItemClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (role.equalsIgnoreCase("s")){
                studentCallBack(position);
            }
            if (role.equalsIgnoreCase("r")){
                repairmanCallBack(position);
            }
            if (role.equalsIgnoreCase("t")){
                teacherCallBack(position);
            }
        }
    }

    private void studentCallBack(int position){
        switch (position){
            case 0:
                startLostAndFind();
                break;
            case 1:
                startFleaMarket();
                break;
            case 2:
                startResourceSharing();
                break;
//            case 3:
//                startClassSchedule();
//                break;
            case 3:
                RequestRepairsActivity.go2Activity(context);
                break;
            case 4:
                RequestLeaveActivity.go2Activity(context);
                break;
        }
    }

    private void repairmanCallBack(int position){
        switch (position){
            case 0:
                startLostAndFind();
                break;
            case 1:
                startFleaMarket();
                break;
            case 2:
                startResourceSharing();
                break;
            case 3:
                RespondRepairActivity.go2Activity(context);
                break;
        }
    }

    private void teacherCallBack(int position){
        switch (position){
            case 0:
                startLostAndFind();
                break;
            case 1:
                startFleaMarket();
                break;
            case 2:
                startResourceSharing();
                break;
            case 3:
                RequestRepairsActivity.go2Activity(context);
                break;
            case 4:
                RespondLeaveActivity.go2Activity(context);
                break;
            case 5:
                AnnouncementActivity.go2Activity(context);
                break;
        }
    }

    private void startLostAndFind(){
        LostAndFindActivity.go2Activity(context,false);
    }

    private void startFleaMarket(){
        FleaMarketActivity.go2Activity(context,FleaMarketActivity.TYPE_FLEA_MARK_HOME);
    }

    private void startResourceSharing(){
        ResourceSharingActivity.go2Activity(context,ResourceSharingActivity.TYPE_RESOURCE_SHARE_HOME);
    }

    private void startClassSchedule(){
        AnnouncementActivity.go2Activity(context);
    }

}
