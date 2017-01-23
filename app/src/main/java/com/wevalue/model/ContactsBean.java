package com.wevalue.model;

import java.util.List;

/**
 * Created by zyK on 2016/9/23.
 * 通讯录实体类
 */
public class ContactsBean {
    /**
     * result : 1
     * message :
     * data : [{"userid":"9bfadc8640174d8b8bcbc084f21d643f","usersex":"男","phone":"13700000001","phonename":"DanielHiggins","usernickname":"看来几乎肯定是卡上","userface":"/upload/userface/201609232310176028.jpg","userlevel":"癸","userinfo":"阿斯顿发啊手机客户端","distance":"5.7305","isfriend":"1","isfocus":"1","isfans":"1"}]
     */

    private int result;
    private String message;
    /**
     * userid : 9bfadc8640174d8b8bcbc084f21d643f
     * usersex : 男
     * phone : 13700000001
     * phonename : DanielHiggins
     * usernickname : 看来几乎肯定是卡上
     * userface : /upload/userface/201609232310176028.jpg
     * userlevel : 癸
     * userinfo : 阿斯顿发啊手机客户端
     * distance : 5.7305
     * isfriend : 1
     * isfocus : 1
     * isfans : 1
     *
     */

    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class Contacts {
        private String userPhone;
        private String userName;
        public String getUserPhone() {
            return userPhone;
        }
        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
    }


    public static class DataBean {
        private String userid;
        private String usersex;
        private String phone;
        private String phonename;
        private String usernickname;
        private String userface;
        private String userlevel;
        private String userinfo;
        private String distance;
        private String isfriend;
        private String isfocus;
        private String isfans;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsersex() {
            return usersex;
        }

        public void setUsersex(String usersex) {
            this.usersex = usersex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhonename() {
            return phonename;
        }

        public void setPhonename(String phonename) {
            this.phonename = phonename;
        }

        public String getUsernickname() {
            return usernickname;
        }

        public void setUsernickname(String usernickname) {
            this.usernickname = usernickname;
        }

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(String userinfo) {
            this.userinfo = userinfo;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(String isfriend) {
            this.isfriend = isfriend;
        }

        public String getIsfocus() {
            return isfocus;
        }

        public void setIsfocus(String isfocus) {
            this.isfocus = isfocus;
        }

        public String getIsfans() {
            return isfans;
        }

        public void setIsfans(String isfans) {
            this.isfans = isfans;
        }
    }

}
