package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/18.
 */

public class RequestLeaveBean extends BmobObject {

    private MyUserBean student;
    private MyUserBean teahcer;
    private String reason;
    private String studenName;
    private String classNum;
    private String beginTime;
    private String endTime;
    private BmobDate releaseTime;
    private Integer statue;
    private BmobDate backTime;
    private Boolean sRead;
    private Boolean tRead;

    public Boolean getsRead() {
        return sRead;
    }

    public void setsRead(Boolean sRead) {
        this.sRead = sRead;
    }

    public Boolean gettRead() {
        return tRead;
    }

    public void settRead(Boolean tRead) {
        this.tRead = tRead;
    }

    public BmobDate getBackTime() {
        return backTime;
    }

    public void setBackTime(BmobDate backTime) {
        this.backTime = backTime;
    }

    public BmobDate getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(BmobDate releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getStudenName() {
        return studenName;
    }

    public void setStudenName(String studenName) {
        this.studenName = studenName;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public MyUserBean getStudent() {
        return student;
    }

    public void setStudent(MyUserBean student) {
        this.student = student;
    }

    public MyUserBean getTeahcer() {
        return teahcer;
    }

    public void setTeahcer(MyUserBean teahcer) {
        this.teahcer = teahcer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

}
