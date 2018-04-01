package com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.FolderListViewAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseNoStatusActivity;


public class FolderListviewActivity extends BaseNoStatusActivity implements View.OnClickListener{

    private TextView textView_cancle;
    private ListView listView;
    private static boolean isCreate = false;
    private FolderListViewAdapter adapter;

    public final static int FOLDER_REQUESTCODE = 0;
    public final static int FOLDER_RESULTCODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_listview);
        isCreate = true;
        init();
    }

    private void init(){
        textView_cancle = (TextView)findViewById(R.id.activity_folder_listview_cancle);
        listView = (ListView)findViewById(R.id.activity_folder_listview_listView);

        textView_cancle.setOnClickListener(this);
        adapter = new FolderListViewAdapter(this);
        listView.setAdapter(adapter);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    public static void startFolderListviewActivityForResult(Context context){
        Intent intent = new Intent(context,FolderListviewActivity.class);
        ((FragmentActivity) context).startActivityForResult(intent,FOLDER_REQUESTCODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShowPictureActivity.startShowPictureActivityFromFolderList(this,-1);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        ShowPictureActivity.startShowPictureActivityFromFolderList(this,-2);
    }

}
