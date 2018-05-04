package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 123 on 2018/5/4.
 */

public class ResourceShareBean extends BmobObject {

    private MyUserBean user;
    private BmobDate releaseTime;
    private String content;
    private String link;
    private String linkType;
    private String linkPassword;
    private List<String> imageUrls;
    private String schoolKey;

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

    public BmobDate getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(BmobDate releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkPassword() {
        return linkPassword;
    }

    public void setLinkPassword(String linkPassword) {
        this.linkPassword = linkPassword;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSchoolKey() {
        return schoolKey;
    }

    public void setSchoolKey(String schoolKey) {
        this.schoolKey = schoolKey;
    }
}
