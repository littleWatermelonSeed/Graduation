package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/10.
 */

public class TeacherBean extends BmobObject {

    private MyUserBean student;
    private MyUserBean teacher;
    private Integer statue;
    private Boolean tRead;
    private Boolean sRead;
    private String remark;
    private BmobDate createTime;

    public BmobDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(BmobDate createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean gettRead() {
        return tRead;
    }

    public void settRead(Boolean tRead) {
        this.tRead = tRead;
    }

    public Boolean getsRead() {
        return sRead;
    }

    public void setsRead(Boolean sRead) {
        this.sRead = sRead;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public MyUserBean getStudent() {
        return student;
    }

    public void setStudent(MyUserBean student) {
        this.student = student;
    }

    public MyUserBean getTeacher() {
        return teacher;
    }

    public void setTeacher(MyUserBean teacher) {
        this.teacher = teacher;
    }
}
