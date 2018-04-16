package com.sayhellototheworld.littlewatermelon.graduation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.data.json.College;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.function_interface.SchoolSelected;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 123 on 2018/4/14.
 */

public class SchoolChooseAdapter extends BaseAdapter{

    private int dataType;
    private Context mContext;
    private LayoutInflater inflater;
    private SchoolSelected schoolSelected;

    private College mCollege;
    private College.DataBean mDataBean;
    private College.DataBean.CollegeLocationsBean mCollegeLocationsBean;

    public static final int DATA_TYPE_COLLEGE = 0;
    public static final int DATA_TYPE_DATABEAN = 1;
    public static final int DATA_TYPE_COLLEGELOCATIONBEAN = 2;

    public SchoolChooseAdapter(Context context, int dataType, College college,SchoolSelected schoolSelected){
        this.dataType = dataType;
        mContext = context;
        mCollege = college;
        this.schoolSelected = schoolSelected;
        inflater = LayoutInflater.from(mContext);
    }

    public SchoolChooseAdapter(Context context,int dataType, College.DataBean dataBean,SchoolSelected schoolSelected){
        this.dataType = dataType;
        mContext = context;
        mDataBean = dataBean;
        this.schoolSelected = schoolSelected;
        inflater = LayoutInflater.from(mContext);
    }

    public SchoolChooseAdapter(Context context,int dataType,College.DataBean.CollegeLocationsBean collegeLocationsBean,SchoolSelected schoolSelected){
        this.dataType = dataType;
        mContext = context;
        mCollegeLocationsBean = collegeLocationsBean;
        this.schoolSelected = schoolSelected;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (dataType == DATA_TYPE_COLLEGE){
            return mCollege.getData().size();
        }else if (dataType == DATA_TYPE_DATABEAN){
            return mDataBean.getCollegeLocations().size();
        }else if (dataType == DATA_TYPE_COLLEGELOCATIONBEAN){
            return mCollegeLocationsBean.getCollegeNames().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (dataType == DATA_TYPE_COLLEGE){
            return mCollege.getData().get(position);
        }else if (dataType == DATA_TYPE_DATABEAN){
            return mDataBean.getCollegeLocations().get(position);
        }else if (dataType == DATA_TYPE_COLLEGELOCATIONBEAN){
            return mCollegeLocationsBean.getCollegeNames().get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_listview_school_choose,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.adapter_listview_school_choose_txt);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (dataType){
            case DATA_TYPE_COLLEGE:
                viewHolder.textView.setText(mCollege.getData().get(position).getDepartName());
                viewHolder.textView.setOnClickListener(new TxtClickListener(mCollege.getData().get(position).getDepartId(),
                        mCollege.getData().get(position).getDepartName()));
                break;
            case DATA_TYPE_DATABEAN:
                viewHolder.textView.setText(mDataBean.getCollegeLocations().get(position).getLocationName());
                viewHolder.textView.setOnClickListener(new TxtClickListener(mDataBean.getCollegeLocations().get(position).getLocationId(),
                        mDataBean.getCollegeLocations().get(position).getLocationName()));
                break;
            case DATA_TYPE_COLLEGELOCATIONBEAN:
                viewHolder.textView.setText(mCollegeLocationsBean.getCollegeNames().get(position).getName());
                viewHolder.textView.setOnClickListener(new TxtClickListener(mCollegeLocationsBean.getCollegeNames().get(position).getLocationId(),
                        mCollegeLocationsBean.getCollegeNames().get(position).getName()));
                break;
        }
        return convertView;
    }

    class TxtClickListener implements View.OnClickListener{

        private String id;
        private String name;

        public TxtClickListener(String id,String name){
            this.id = id;
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            schoolSelected.selected(id,name);
        }
    }

    class ViewHolder {
        TextView textView;
    }

}
