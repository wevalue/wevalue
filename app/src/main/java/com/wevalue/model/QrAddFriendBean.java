package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/19
 * 类说明：
 */

public class QrAddFriendBean  {

    /**
     * result : 1
     * message :
     * data : [{"userid":"37dd84bde8ba4367ae5eebdaaf615def","usersex":"男","usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","userlevel":"癸","userinfo":"啊教练开发啦解放啦解放啦；就","isfriend":"0","isfocus":"1","isfans":"1"}]
     */

    private int result;
    private String message;
    /**
     * userid : 37dd84bde8ba4367ae5eebdaaf615def
     * usersex : 男
     * usernickname : 啦啦a
     * userface : /upload/userface/201609221648478798.jpg
     * userlevel : 癸
     * userinfo : 啊教练开发啦解放啦解放啦；就
     * isfriend : 0
     * isfocus : 1
     * isfans : 1
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

    public static class DataBean {
        private String userid;
        private String usersex;
        private String usernickname;
        private String userface;
        private String userlevel;
        private String userinfo;
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
