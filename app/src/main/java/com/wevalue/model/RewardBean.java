package com.wevalue.model;
import java.util.List;

/**
 * Created by xuhua on 2016/8/24.
 */
public class RewardBean {

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<RewardEntity> getData() {
        return data;
    }

    public void setData(List<RewardEntity> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String result;//": 1,
    private String message;//": "",
    private List<RewardEntity> data;//": [{

    public String allcount;//":"41",//全部
    public String wordcount;//":"41",//世界
    public String friendcount;//":"0"//朋友们

    public String getAllcount() {
        return allcount;
    }

    public void setAllcount(String allcount) {
        this.allcount = allcount;
    }

    public String getWordcount() {
        return wordcount;
    }

    public void setWordcount(String wordcount) {
        this.wordcount = wordcount;
    }

    public String getFriendcount() {
        return friendcount;
    }

    public void setFriendcount(String friendcount) {
        this.friendcount = friendcount;
    }

    public static class RewardEntity {


        public String getRewardid() {
            return rewardid;
        }

        public void setRewardid(String rewardid) {
            this.rewardid = rewardid;
        }

        public String getNoteid() {
            return noteid;
        }

        public void setNoteid(String noteid) {
            this.noteid = noteid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getRewardmoney() {
            return rewardmoney;
        }

        public void setRewardmoney(String rewardmoney) {
            this.rewardmoney = rewardmoney;
        }

        public String getRewardtime() {
            return rewardtime;
        }

        public void setRewardtime(String rewardtime) {
            this.rewardtime = rewardtime;
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

        public String getInitial_usernickname() {
            return initial_usernickname;
        }

        public void setInitial_usernickname(String initial_usernickname) {
            this.initial_usernickname = initial_usernickname;
        }

        public String getInitial_userface() {
            return initial_userface;
        }

        public void setInitial_userface(String initial_userface) {
            this.initial_userface = initial_userface;
        }

        public String getInitial_userlevel() {
            return initial_userlevel;
        }

        public void setInitial_userlevel(String initial_userlevel) {
            this.initial_userlevel = initial_userlevel;
        }

        public String getInitial_content() {
            return initial_content;
        }

        public void setInitial_content(String initial_content) {
            this.initial_content = initial_content;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        private String rewardid;//": 7,//打赏ID
        private String noteid;//": 1,//文章id
        private String userid;//": "ba39301d25d34b4fa20fac7cca3cc523",//打赏人ID
        private String rewardmoney;//": 6,//打赏金额
        private String rewardtime;//": "2016-08-03 17:36",//打赏时间
        private String usernickname;//": "天空之城",//打赏人昵称
        private String userface;//": "/upload/userface/201608102248257712.jpg",//打赏人头像
        private String userlevel;//": "癸",//打赏人等级
        private String initial_usernickname;//": "天空之城",//被打赏人昵称
        private String initial_userface;//": "/upload/userface/201608102248257712.jpg",//被打赏人头像
        private String initial_userlevel;//": "癸",//被打赏人等级
        private String initial_content;//": "测试图片文字测试图片文字测试图片文字测试图片文字测试图片文字"//被打赏
        private String isfree;//": 是否免费, 1=免费, 0 = 付费;

    }
}