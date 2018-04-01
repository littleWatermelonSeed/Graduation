package com.sayhellototheworld.littlewatermelon.graduation.my_interface.userManage_interface;


import com.sayhellototheworld.littlewatermelon.graduation.data.bmom.bean.MyUserBean;

/**
 * Created by 123 on 2017/9/6.
 */

public interface ViRegisterUserCoDo {

    void getKeyCode(String phoneNum);

    void registerSubmit(final MyUserBean userBean, final String keyCode, String password);

}
