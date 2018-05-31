package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/18.
 */

public class FriendBean extends BmobObject {

    private MyUserBean user;
    private MyUserBean friend;
    private String remarkName;

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

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
}
