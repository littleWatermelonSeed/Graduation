package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/18.
 */

public class RequestRepairBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean repairman;
    private String schoolKey;
    private String schoolName;
    private String location;
    private String describe;
    private Integer statue;

    public MyUserBean getRepairman() {
        return repairman;
    }

    public void setRepairman(MyUserBean repairman) {
        this.repairman = repairman;
    }

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public String getSchoolKey() {
        return schoolKey;
    }

    public void setSchoolKey(String schoolKey) {
        this.schoolKey = schoolKey;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }
}
