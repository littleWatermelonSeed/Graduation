package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/30.
 */

public class FriendRequestBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean friend;
    private Integer statue;
    private Boolean userRead;
    private Boolean friendRead;
    private String remark;

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public MyUserBean getFriend() {
        return friend;
    }

    public void setFriend(MyUserBean friend) {
        this.friend = friend;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Boolean getUserRead() {
        return userRead;
    }

    public void setUserRead(Boolean userRead) {
        this.userRead = userRead;
    }

    public Boolean getFriendRead() {
        return friendRead;
    }

    public void setFriendRead(Boolean friendRead) {
        this.friendRead = friendRead;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
