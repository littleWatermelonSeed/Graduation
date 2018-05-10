package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/10.
 */

public class TeacherBean extends BmobObject {

    private MyUserBean student;
    private MyUserBean teacher;
    private Boolean agreen;

    public Boolean getAgreen() {
        return agreen;
    }

    public void setAgreen(Boolean agreen) {
        this.agreen = agreen;
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
