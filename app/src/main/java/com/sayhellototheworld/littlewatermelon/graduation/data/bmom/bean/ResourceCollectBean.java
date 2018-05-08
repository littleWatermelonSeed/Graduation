package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/6.
 */

public class ResourceCollectBean extends BmobObject{

    private MyUserBean user;
    private ResourceShareBean resourceShareBean;
    private BmobDate collectTime;

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public ResourceShareBean getResourceShareBean() {
        return resourceShareBean;
    }

    public void setResourceShareBean(ResourceShareBean resourceShareBean) {
        this.resourceShareBean = resourceShareBean;
    }

    public BmobDate getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(BmobDate collectTime) {
        this.collectTime = collectTime;
    }
}
