package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/16.
 */

public class StudentBean extends BmobObject{

    private MyUserBean student;
    private MyUserBean teacher;
    private Integer statue;

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

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }
}
