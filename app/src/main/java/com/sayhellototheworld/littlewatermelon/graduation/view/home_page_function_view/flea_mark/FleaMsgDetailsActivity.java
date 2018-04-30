package com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseSlideBcakStatusActivity;

import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_HOME;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OTHER;
import static com.sayhellototheworld.littlewatermelon.graduation.view.home_page_function_view.flea_mark.FleaMarketActivity.TYPE_FLEA_MARK_OWN;

public class FleaMsgDetailsActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private TextView txt_back;
    private ImageView img_more;

    private int fleaMarkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_flea_msg_details);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_flea_msg_details_back);
        txt_back.setOnClickListener(this);
        img_more = (ImageView) findViewById(R.id.activity_flea_msg_details_more);
        img_more.setOnClickListener(this);
    }

    @Override
    protected void initParam() {
        fleaMarkType = getIntent().getIntExtra("fleaMarkType",-1);
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    protected void initShow() {
        showMoreIcon();
    }

    private void showMoreIcon(){
        switch (fleaMarkType){
            case TYPE_FLEA_MARK_HOME:
            case TYPE_FLEA_MARK_OTHER:
                img_more.setVisibility(View.GONE);
                break;
            case TYPE_FLEA_MARK_OWN:
                break;
        }
    }

    public static void go2Activity(Context context, int fleaMarkType){
        Intent intent = new Intent(context,FleaMsgDetailsActivity.class);
        intent.putExtra("fleaMarkType",fleaMarkType);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
