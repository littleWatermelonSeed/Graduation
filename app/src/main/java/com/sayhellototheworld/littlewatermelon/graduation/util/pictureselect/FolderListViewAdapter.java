package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;

import java.util.List;

/**
 * Created by 123 on 2017/7/12.
 */

public class FolderListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Folder> folderList;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    private PictureDataManager manager;

    public FolderListViewAdapter(Context context) {
        manager = PictureDataManager.getManagerInstance();
        this.context = context;
        folderList = manager.getFolderList();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_folder_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.adapter_folder_list_item_leftImage);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.adapter_folder_list_item_textView);
            viewHolder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.adapter_folder_list_item_layout);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        String folderName = folderList.get(position).getName();
        int imageNum = folderList.get(position).getImages().size();
        viewHolder.textView.setText(folderName + "(" + imageNum + ")");
        Glide.with(context)
                .load(folderList.get(position).getImages().get(0).getPath())
                .into(viewHolder.imageView);
        listener = new ItemClickListener(position);
        viewHolder.relativeLayout.setOnClickListener(listener);
        return convertView;
    }

    class ItemClickListener implements View.OnClickListener{


        private int position;

        public ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ShowPictureActivity.startShowPictureActivityFromFolderList(context,position);
            ((Activity)context).finish();
        }
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;
    }

}
