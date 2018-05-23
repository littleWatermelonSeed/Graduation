package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/18.
 */

public class RequestRepairBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean repairman;
    private String schoolKey;
    private String place;
    private String userName;
    private String contact;
    private String describe;
    private Integer statue;
    private String appointmentTime;
    private Boolean uRead;
    private Boolean rRead;

    public Boolean getuRead() {
        return uRead;
    }

    public void setuRead(Boolean uRead) {
        this.uRead = uRead;
    }

    public Boolean getrRead() {
        return rRead;
    }

    public void setrRead(Boolean rRead) {
        this.rRead = rRead;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

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
