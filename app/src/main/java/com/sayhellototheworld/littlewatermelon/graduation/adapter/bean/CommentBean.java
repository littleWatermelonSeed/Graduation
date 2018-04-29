package com.sayhellototheworld.littlewatermelon.graduation.adapter.bean;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

/**
 * Created by 123 on 2018/4/29.
 */

public class CommentBean {

    private MyUserBean user;
    private String content;
    private String createDate;

    public CommentBean(){

    }

    public CommentBean(MyUserBean user,String content,String createDate){
        this.user = user;
        this.content = content;
        this.createDate = createDate;
    }

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
