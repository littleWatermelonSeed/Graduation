package com.sayhellototheworld.littlewatermelon.graduation.adapter.bean;

import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/15.
 */

public class MessageDetailsBean {

    private MyUserBean commentUser;
    private String comment;
    private String firstImgUrl;
    private String releaseUserName;
    private String content;
    private BmobObject bmobObject;
    private String createTime;
    private boolean read;

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BmobObject getBmobObject() {
        return bmobObject;
    }

    public void setBmobObject(BmobObject bmobObject) {
        this.bmobObject = bmobObject;
    }

    public MyUserBean getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(MyUserBean commentUser) {
        this.commentUser = commentUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFirstImgUrl() {
        return firstImgUrl;
    }

    public void setFirstImgUrl(String firstImgUrl) {
        this.firstImgUrl = firstImgUrl;
    }

    public String getReleaseUserName() {
        return releaseUserName;
    }

    public void setReleaseUserName(String releaseUserName) {
        this.releaseUserName = releaseUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
