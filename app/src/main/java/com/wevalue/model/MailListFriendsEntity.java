package com.wevalue.model;

import java.util.List;

/**
 * 手机通讯录好友实体类----主类
 */
public class MailListFriendsEntity {

    public String result;//": 1,
    public String message;//": "",
    public List<PhoneFriend> data;//": [{

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

    public List<PhoneFriend> getData() {
        return data;
    }

    public void setData(List<PhoneFriend> data) {
        this.data = data;
    }

    /**
     * 手机通讯录好友实体类
     */
    public static class PhoneFriend{

//        06-13 11:28:02.254 6048-6048/com.dbcooper E/log: responseInfo-----{
// "result":1,"message":"","data":
// [{"userid":"733d3db1924848788c1028386ab522d9",
// "phone":"13617258650",
// "phonename":"胥",
// "usernickname":"气象局",
// "userface":"",
// "isfriend":"1"}]}

        public String isfriend;// = 1;//0 非dbcooper用户 -1未添加好友 1已添加好友
        public String phone;// = 13700000001;//通讯录手机号码
        public String phonename;// = DanielHiggins;//通讯录姓名
        public String userface;// = "/upload/dbface/201606081021453879.jpg";//通讯录用户对应头像
        public String userid ;//= 2b02b33f8e5c427bb1647efe4d4f67fd;//通讯录用户对应id
        public String usernickname;// = "\U7f57\U66fc\U8482\U514b";//通讯录用户对应昵称
        public String sortLetters;  //显示数据拼音的首字母

        public String getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(String isfriend) {
            this.isfriend = isfriend;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsernickname() {
            return usernickname;
        }

        public void setUsernickname(String usernickname) {
            this.usernickname = usernickname;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        @Override
        public String toString() {
            return "PhoneFriend{" +
                    "isfriend='" + isfriend + '\'' +
                    ", phone='" + phone + '\'' +
                    ", phonename='" + phonename + '\'' +
                    ", userface='" + userface + '\'' +
                    ", userid='" + userid + '\'' +
                    ", usernickname='" + usernickname + '\'' +
                    '}';
        }
    }

}
