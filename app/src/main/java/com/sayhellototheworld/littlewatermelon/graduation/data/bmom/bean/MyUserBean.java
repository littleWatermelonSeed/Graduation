package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 123 on 2017/9/5.
 */

public class MyUserBean extends BmobUser {

    private String sex;
    private String myEmail;
    private String nickName;
    private String birthday;
    private String schoolName;
    private String location;
    private String introduction;
    private BmobFile skin;
    private BmobFile headPortrait;
    private String loginDeviceId;

    public String getLoginDeviceId() {
        return loginDeviceId;
    }

    public void setLoginDeviceId(String installation) {
        loginDeviceId = installation;
    }

    public BmobFile getSkin() {
        return skin;
    }

    public void setSkin(BmobFile skin) {
        this.skin = skin;
    }

    public BmobFile getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loaction) {
        this.location = loaction;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

}
