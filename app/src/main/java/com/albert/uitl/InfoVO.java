package com.albert.uitl;

import android.graphics.Bitmap;

/**
 * Created by Albert on 2017/9/21 0021.
 */

public class InfoVO {
//    /**
//     * 样式的声明
//     */
//    public static final int SECOND_TYPE = 0;
//    public static final int THIRD_TYPE = 1;

    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    public static final int TYPE_FOR = 4;

    /**
     * 区别布局类型
     */
    public int type;
    public Bitmap userPic;
    public String userName;
    public int userIfo1;
    public int userIfo2;
    public String msg;

    public String dunshou;


    public InfoVO() {

    }

    public InfoVO(Bitmap userPic, String userName, int userIfo1, int userIfo2, String msg) {
        this.userPic = userPic;
        this.userName = userName;
        this.userIfo1 = userIfo1;
        this.userIfo2 = userIfo2;
        this.msg = msg;


    }


    //    public Bitmap getUserPic() {
//        return userPic;
//    }
//
//    public void setUserPic(Bitmap userPic) {
//        this.userPic = userPic;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public int getUserIfo1() {
//        return userIfo1;
//    }
//
//    public void setUserIfo1(int userIfo1) {
//        this.userIfo1 = userIfo1;
//    }
//
//    public int getUserIfo2() {
//        return userIfo2;
//    }
//
//    public void setUserIfo2(int userIfo2) {
//        this.userIfo2 = userIfo2;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
    public int getType() {
        return type;
    }
}
