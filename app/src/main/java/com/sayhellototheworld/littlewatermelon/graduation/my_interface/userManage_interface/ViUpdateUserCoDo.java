package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;


import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 123 on 2017/9/7.
 */

public interface ViUpdateUserCoDo {

    void updateUser(MyUserBean userBean, BmobFile headPic);

}
