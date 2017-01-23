package com.wevalue.model;

import java.util.List;

/**
 * Created by Administrator on 2016-06-12.
 * 附近实体
 */
public class NearbyEntity {

    public String result;//": 1,
    public String message;//": "",
    public List<NearbyUser> data;//": [{

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NearbyUser> getData() {
        return data;
    }

    public void setData(List<NearbyUser> data) {
        this.data = data;
    }

    /**
     * 附近的人实体类
     */
    public static class NearbyUser {
        public String userid;//": "b5cdf7da50584d8e8727bad49dd276d6",//用户id
        public String usersex;//": "女 ",//用户性别
        public String usernickname;//": "罗曼蒂克1",//用户昵称
        public String userface;//": "/upload/dbface/201606071750493140.jpg",//用户头像
        public String distance;//": "0.0445"//两用户之间距离 单位：km,
        public String isfriend;//": "0"//是否已经是好友 0不是 1是
        public String isfocus;//是否为已经关注的人
        public String userlevel;//用户的等级
        public String isfans;//用户是否为本人粉丝
        public String userinfo;//用户个人简介信息

        public String getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(String userinfo) {
            this.userinfo = userinfo;
        }

        public String getIsfans() {
            return isfans;
        }

        public void setIsfans(String isfans) {
            this.isfans = isfans;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getIsfocus() {
            return isfocus;
        }

        public void setIsfocus(String isfocus) {
            this.isfocus = isfocus;
        }

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
    }
}