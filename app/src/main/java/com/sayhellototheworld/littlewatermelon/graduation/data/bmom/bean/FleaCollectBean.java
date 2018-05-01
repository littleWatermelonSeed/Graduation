package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/1.
 */

public class FleaCollectBean extends BmobObject{

    private MyUserBean user;
    private FleaMarketBean fleaMarket;
    private BmobDate collectTime;

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public FleaMarketBean getFleaMarket() {
        return fleaMarket;
    }

    public void setFleaMarket(FleaMarketBean fleaMarket) {
        this.fleaMarket = fleaMarket;
    }

    public BmobDate getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(BmobDate collectTime) {
        this.collectTime = collectTime;
    }
}
