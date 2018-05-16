package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2017/10/2.
 */

public class LostCommentBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean publishUser;
    private LostAndFindBean lost;
    private String content;
    private Boolean read;
    private BmobDate releaseTime;

    public MyUserBean getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(MyUserBean publishUser) {
        this.publishUser = publishUser;
    }

    public LostAndFindBean getLost() {
        return lost;
    }

    public void setLost(LostAndFindBean lost) {
        this.lost = lost;
    }

    public BmobDate getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(BmobDate releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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
}
