package com.sayhellototheworld.littlewatermelon.graduation.view.user_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.graduation.R;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.graduation.customwidget.LiTopBar;
import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.graduation.data.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.base_interface.ShowCurUserInfo;
import com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface.ViUpdateUserCoDo;
import com.sayhellototheworld.littlewatermelon.graduation.presenter.user_manage.ControlUpdateUser;
import com.sayhellototheworld.littlewatermelon.graduation.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.graduation.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.graduation.view.base_activity.BaseStatusActivity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInformationActivity extends BaseStatusActivity implements BaseActivityDo,
        View.OnClickListener, ShowCurUserInfo {

    private LiTopBar mLiTopBar;
    private CircleImageView mCircleImageView;
    private EditText editText_nickName;
    private EditText editText_school;
    private EditText editText_email;
    private EditText editText_introduction;
    private EditText editText_location;
    private TextView textView_birthday;
    private RadioGroup mRadioGroup;

    private File headPortraitPath;
    private String nickName;
    private String sex;
    private String birthday;
    private String loaction;
    private String school;
    private String email;
    private String introduction;

    private ViUpdateUserCoDo vud;
    private MyUserBean mUserBean;

    private BmobFile headPic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        init();
    }

    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        mCircleImageView = (CircleImageView) findViewById(R.id.activity_personal_information_headPortrait);
        mCircleImageView.setOnClickListener(this);
        editText_nickName = (EditText) findViewById(R.id.activity_personal_information_editTextNickName);
        editText_school = (EditText) findViewById(R.id.activity_personal_information_editTextSchool);
        editText_email = (EditText) findViewById(R.id.activity_personal_information_editTextEmail);
        editText_location = (EditText) findViewById(R.id.activity_personal_information_editTextLocation);
        editText_introduction = (EditText) findViewById(R.id.activity_personal_information_editTextIntroduction);
        textView_birthday = (TextView) findViewById(R.id.activity_personal_information_textViewBirthday);
        textView_birthday.setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.activity_personal_information_radioGroupSex);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.activity_personal_information_radioMan) {
                    sex = "男";
                } else {
                    sex = "女";
                }
            }
        });
        mLiTopBar = (LiTopBar) findViewById(R.id.activity_personal_information_topbar);
        mLiTopBar.setLeftTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancal();
            }
        });
        mLiTopBar.setRightTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getUserbean()) {
                    return;
                }
                vud.updateUser(mUserBean, headPic);
            }
        });
    }

    @Override
    public void initParam() {
        sex = "男";
        mUserBean = new MyUserBean();
        baseActivityManager.addActivityToList(this);
        vud = new ControlUpdateUser(this, this);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    @Override
    public void initShow() {

    }

    @Override
    public void showUserInformation(MyUserBean userBean) {
        if (userBean == null)
            return;

        editText_nickName.setText(userBean.getNickName());
        textView_birthday.setText(userBean.getBirthday());
        editText_location.setText(userBean.getLocation());
        editText_school.setText(userBean.getSchoolName());
        editText_email.setText(userBean.getMyEmail());
        editText_introduction.setText(userBean.getIntroduction());
        if (userBean.getSex().equals("男")) {
            ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(true);
            ((RadioButton) (mRadioGroup.getChildAt(1))).setChecked(false);
        } else {
            ((RadioButton) (mRadioGroup.getChildAt(0))).setChecked(false);
            ((RadioButton) (mRadioGroup.getChildAt(1))).setChecked(true);
        }

        if (userBean.getHeadPortrait() == null || userBean.getHeadPortrait().getUrl() == null) {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(mCircleImageView);
        } else {
            Glide.with(this)
                    .load(ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(userBean.getHeadPortrait().getUrl())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.head_log1)
                    .dontAnimate()
                    .into(mCircleImageView);
        }

    }

    private boolean getUserbean() {
        nickName = editText_nickName.getText().toString();
        loaction = editText_location.getText().toString();
        school = editText_school.getText().toString();
        email = editText_email.getText().toString();
        introduction = editText_introduction.getText().toString();

        if (nickName.equals("")) {
            MyToastUtil.showToast("昵称不能为空");
            return false;
        }

        if (sex != null && !sex.equals("")) {
            mUserBean.setSex(sex);
        }
        if (nickName != null && !nickName.equals("")) {
            mUserBean.setNickName(nickName);
        }
        if (birthday != null && !birthday.equals("")) {
            mUserBean.setBirthday(birthday);
        }
        if (loaction != null && !loaction.equals("")) {
            mUserBean.setLocation(loaction);
        }
        if (school != null && !school.equals("")) {
            mUserBean.setSchoolName(school);
        }
        if (email != null && !email.equals("")) {
            mUserBean.setMyEmail(email);
        }
        if (introduction != null && !introduction.equals("")) {
            mUserBean.setIntroduction(introduction);
        }

        if (headPortraitPath != null) {
            headPic = new BmobFile(headPortraitPath);
        }

        return true;
    }

    private void cancal() {
        DialogConfirm.newInstance("提示", "确定取消修改/完善个人资料?", new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {

            }

            @Override
            public void ok() {
                baseActivityManager.destroyedListActivity();
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public static void startPersonalInformationActivity(final Context context) {
        ((Activity) context).startActivity(new Intent(context, PersonalInformationActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_personal_information_textViewBirthday:
                getBirthday();
                break;
            case R.id.activity_personal_information_headPortrait:
                getHeadPortrait();
                break;
        }
    }

    private void getBirthday() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                birthday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                textView_birthday.setText(birthday);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void getHeadPortrait() {
        NiceDialog.init()
                .setLayoutId(R.layout.nicedialog_head_portrait)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final TextView editText1 = holder.getView(R.id.nicedialog_head_portrait_takePicture);
                        final TextView editText2 = holder.getView(R.id.nicedialog_head_portrait_chooseDFromAlbum);
                        final TextView editText3 = holder.getView(R.id.nicedialog_head_portrait_choosedFromSystem);
                        View view = holder.getView(R.id.nicedialog_head_portrait_line);
                        view.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                takePicture();
                                dialog.dismiss();
                            }
                        });
                        editText2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                choosePicture();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setMargin(10)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }


    private void takePicture() {

    }

    private void choosePicture() {
        ShowPictureActivity.startShowPictureActivityForResult(this, ShowPictureActivity.TARGET_HEAD);
    }

    private void refreshHeadPortrait() {
        headPortraitPath = new File(GetFile.getExternalTemporaryImageFile(), "temporary_head.png");
        if (headPortraitPath.exists()) {
            Glide.with(this)
                    .load(headPortraitPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(mCircleImageView);
        }
    }

    @Override
    protected void onRestart() {
        if (isActive) {
            refreshHeadPortrait();
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        headPortraitPath = new File(GetFile.getExternalTemporaryImageFile(), "temporary_head.png");
        if (headPortraitPath.exists()) {
            headPortraitPath.delete();
        }
    }

}
