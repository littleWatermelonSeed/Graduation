package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/25.
 */

/**
 * type:
 * 0:回复帖子
 * 1：回复评论
 */
public class ForumCommentBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean otherUser;
    private MyUserBean publishUser;
    private ForumBean forum;
    private String content;
    private Boolean read;
    private BmobDate releaseTime;
    private Integer type;

    public ForumBean getForum() {
        return forum;
    }

    public void setForum(ForumBean forum) {
        this.forum = forum;
    }

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public MyUserBean getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(MyUserBean otherUser) {
        this.otherUser = otherUser;
    }

    public MyUserBean getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(MyUserBean publishUser) {
        this.publishUser = publishUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public BmobDate getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(BmobDate releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
