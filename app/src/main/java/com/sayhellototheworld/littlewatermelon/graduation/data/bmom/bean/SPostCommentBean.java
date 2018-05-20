package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/18.
 */

public class SPostCommentBean extends BmobObject{

    private MyUserBean user;
    private MyUserBean publishUser;
    private SchoolPostBean SPost;
    private String content;
    private Boolean read;
    private BmobDate releaseTime;

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public MyUserBean getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(MyUserBean publishUser) {
        this.publishUser = publishUser;
    }

    public SchoolPostBean getSPost() {
        return SPost;
    }

    public void setSPost(SchoolPostBean SPost) {
        this.SPost = SPost;
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
}
