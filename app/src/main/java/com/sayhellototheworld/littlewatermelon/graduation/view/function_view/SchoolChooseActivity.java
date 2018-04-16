package com.sayhellototheworld.littlewatermelon.graduation.view.function_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.adapter.SchoolChooseAdapter;
import com.sayhellototheworld.littlewatermelon.graduation.data.json.College;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.function_interface.SchoolSelected;
import com.sayhellototheworld.littlewatermelon.graduation.util.GsonHelper;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;

public class SchoolChooseActivity extends BaseStatusActivity implements View.OnClickListener {

    public static final int SCHOOL_CHOOSE_REQUST_CODE = 0;

    private TextView txt_back;
    private TextView txt_ensure;
    private TextView txt_province;
    private TextView txt_city;
    private TextView txt_school;
    private View vProvince;
    private View vCity;
    private View vSchool;
    private LinearLayout llProvince;
    private LinearLayout llCity;
    private LinearLayout llSchool;

    private View dialog_view;
    private ListView dialog_listView;
    private TextView dialog_title;

    private College mCollege;

    private String shcoolName = "";
    private String schoolKey = "";

    private boolean chooseProvince = false;
    private boolean chooseCity = false;
    private boolean chooseCollege = false;
    private int indexProvince = -1;
    private int indexCity = -1;
    private int indexCollege = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                chooseProvince = true;
                vProvince.setAlpha(0f);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_school_choose);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        txt_back = (TextView) findViewById(R.id.activity_school_choose_back);
        txt_back.setOnClickListener(this);
        txt_ensure = (TextView) findViewById(R.id.activity_school_choose_ensure);
        txt_ensure.setOnClickListener(this);
        txt_province = (TextView) findViewById(R.id.activity_school_choose_txt_province);
        txt_city = (TextView) findViewById(R.id.activity_school_choose_txt_city);
        txt_school = (TextView) findViewById(R.id.activity_school_choose_txt_college);
        vProvince = findViewById(R.id.activity_school_choose_view_province);
        vProvince.setAlpha(0.4f);
        vCity = findViewById(R.id.activity_school_choose_view_city);
        vCity.setAlpha(0.4f);
        vSchool = findViewById(R.id.activity_school_choose_view_college);
        vSchool.setAlpha(0.4f);
        llProvince = (LinearLayout) findViewById(R.id.activity_school_choose_linear_province);
        llProvince.setOnClickListener(this);
        llCity = (LinearLayout) findViewById(R.id.activity_school_choose_linear_city);
        llCity.setOnClickListener(this);
        llSchool = (LinearLayout) findViewById(R.id.activity_school_choose_linear_college);
        llSchool.setOnClickListener(this);

        dialog_view = LayoutInflater.from(this).inflate(R.layout.school_choose_dialog, null);
        dialog_listView = (ListView) dialog_view.findViewById(R.id.school_choose_dialog_listView);
        dialog_title = (TextView) dialog_view.findViewById(R.id.school_choose_dialog_title);
    }

    @Override
    protected void initParam() {
        parseCollegeJson();
    }

    @Override
    protected void initShow() {
        tintManager.setStatusBarTintResource(R.color.white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_school_choose_linear_province:
                if (!chooseProvince) {
                    MyToastUtil.showToast("正在解析数据....");
                    return;
                }
                chooseProvince();
                break;
            case R.id.activity_school_choose_linear_city:
                if (!chooseCity) {
                    MyToastUtil.showToast("请先选择省份");
                    return;
                }
                chooseCity();
                break;
            case R.id.activity_school_choose_linear_college:
                if (!chooseCollege) {
                    MyToastUtil.showToast("请先选择城市");
                    return;
                }
                chooseCollege();
                break;
            case R.id.activity_school_choose_back:
                finish();
                break;
            case R.id.activity_school_choose_ensure:
                returnSchoolMessage();
                break;
        }
    }

    private void chooseProvince() {
        final AlertDialog dialog = getDialog();
        dialog_title.setText("选择省份");
        SchoolChooseAdapter adapter = new SchoolChooseAdapter(this, SchoolChooseAdapter.DATA_TYPE_COLLEGE, mCollege, new SchoolSelected() {
            @Override
            public void selected(String id, String name) {
                indexProvince = Integer.parseInt(id);
                txt_province.setText(name);
                chooseCity = true;
                chooseCollege = false;
                txt_city.setText("");
                txt_school.setText("");
                shcoolName = "";
                vCity.setAlpha(0f);
                vSchool.setAlpha(0.4f);
                dialog.dismiss();
            }
        });
        dialog_listView.setAdapter(adapter);
    }

    private void chooseCity() {
        final AlertDialog dialog = getDialog();
        dialog_title.setText("选择城市");
        SchoolChooseAdapter adapter = new SchoolChooseAdapter(this, SchoolChooseAdapter.DATA_TYPE_DATABEAN, mCollege.getData().get(indexProvince - 1), new SchoolSelected() {
            @Override
            public void selected(String id, String name) {
                indexCity = Integer.parseInt(id);
                txt_city.setText(name);
                chooseCollege = true;
                txt_school.setText("");
                shcoolName = "";
                vSchool.setAlpha(0f);
                dialog.dismiss();
            }
        });
        dialog_listView.setAdapter(adapter);
    }

    private void chooseCollege() {
        final AlertDialog dialog = getDialog();
        dialog_title.setText("选择大学");
        SchoolChooseAdapter adapter = new SchoolChooseAdapter(this,
                SchoolChooseAdapter.DATA_TYPE_COLLEGELOCATIONBEAN, mCollege.getData().get(indexProvince - 1).getCollegeLocations().get(indexCity - 1),
                new SchoolSelected() {
                    @Override
                    public void selected(String id, String name) {
                        indexCollege = Integer.parseInt(id);
                        txt_school.setText(name);
                        shcoolName = name;
                        dialog.dismiss();
                    }
                });
        dialog_listView.setAdapter(adapter);
    }

    private AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.show();

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效
        if (dialog_view.getParent() != null) {
            ((ViewGroup) dialog_view.getParent()).removeAllViews();
        }
        dialog.getWindow().setContentView(dialog_view);
        return dialog;
    }

    private void parseCollegeJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCollege = GsonHelper.getCollege();
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    public static void startSchoolChooseActivityForResult(Context context) {
        ((Activity) context).startActivityForResult(new Intent(context, SchoolChooseActivity.class), SCHOOL_CHOOSE_REQUST_CODE);
    }

    private void returnSchoolMessage() {
        if (shcoolName.equals("")) {
            MyToastUtil.showToast("请选择你的大学");
            return;
        }
        schoolKey = indexProvince + "-" + indexCity + "-" + indexCollege;
        Intent intent = new Intent();
        intent.putExtra("schoolName", shcoolName);
        intent.putExtra("schoolKey", schoolKey);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GsonHelper.closeCollege();
    }

}
