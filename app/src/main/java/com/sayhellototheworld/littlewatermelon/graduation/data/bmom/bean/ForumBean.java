package com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2018/5/24.
 */

public class ForumBean extends BmobObject {

    private MyUserBean user;
    private String content;
    private List<String> imageUrls;
    private String schoolKey;
    private Integer likeNum;
    private Integer commentNum;
    private String likeUserObjID;

    public String getLikeUserObjID() {
        return likeUserObjID;
    }

    public void setLikeUserObjID(String likeUserObjID) {
        this.likeUserObjID = likeUserObjID;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
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
