package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/10
 * 类说明：用户详情页的实体类
 */

public class UserDetailsBean {
    /**
     * result : 1
     * message :
     * data : [[{"userid":"37dd84bde8ba4367ae5eebdaaf615def","usersex":"男","usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","userlevel":"癸","userinfo":"啊教练开发啦解放啦解放啦；就","isfriend":"1","isfocus":"1","isfans":"0","focusnum":"11","fansnum":"4"}]]
     * fabulist : [{"noteid":"151","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-10-09 12:43","content":"啦啦啦","notetype":"1","notevideo":"/upload/social/video/201610091243066448.mp4","notevideopic":"/upload/social/video/201610091243066467.jpg","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"999999","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"150","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-10-08 17:28","content":"111","notetype":"3","notevideo":"","notevideopic":"","clickcount":"21","hotword":"","isself":"0","isfree":"0","paynum":"999999","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[{"url":"/upload/social/img/2016100817284940840.jpg"}]},{"noteid":"145","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-10-08 10:30","content":"111","notetype":"3","notevideo":"","notevideopic":"","clickcount":"6","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[{"url":"/upload/social/img/2016100810302611060.jpg"},{"url":"/upload/social/img/2016100810302611551.jpg"}]},{"noteid":"143","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-29 19:01","content":"红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why红MIG你i哦hi明我定why","notetype":"4","notevideo":"","notevideopic":"","clickcount":"1","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"1","zancount":"1","rewardcount":"0","iszan":"0","list":[]},{"noteid":"141","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 18:23","content":"养生","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"139","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 18:22","content":"464694949","notetype":"4","notevideo":"","notevideopic":"","clickcount":"1","hotword":"","isself":"0","isfree":"0","paynum":"7","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"140","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 18:22","content":"1","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"44","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"137","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 17:28","content":"科技","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"135","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 16:01","content":"震撼测试","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"134","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-28 15:59","content":"1","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"0","isfree":"0","paynum":"1","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]}]
     * zhuanfalist : [{"noteid":"115","repostid":"93","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"测试转发","addtime":"2016-10-09 10:48","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"114","repostid":"89","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"转发一下","addtime":"2016-09-29 16:46","content":"致命","notetype":"4","notevideo":"","notevideopic":"","clickcount":"17","hotword":"","isself":"0","isfree":"0","paynum":"494946","notezone":"0","notemood":"喜爱","isshare":"0","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"127","repostid":"87","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"另一种","addtime":"2016-09-28 17:22","content":"你好","notetype":"4","notevideo":"","notevideopic":"","clickcount":"15","hotword":"","isself":"0","isfree":"0","paynum":"555","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[{"url":"/upload/social/img/2016092718485682000.jpg"},{"url":"/upload/social/img/2016092718485682091.jpg"},{"url":"/upload/social/img/2016092718485682192.jpg"},{"url":"/upload/social/img/2016092718485682193.jpg"}]},{"noteid":"115","repostid":"86","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"mmmmmmmn","addtime":"2016-09-28 17:07","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"115","repostid":"85","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"你好  转发测试","addtime":"2016-09-28 17:06","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"114","repostid":"84","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"6464649","addtime":"2016-09-28 12:33","content":"致命","notetype":"4","notevideo":"","notevideopic":"","clickcount":"17","hotword":"","isself":"0","isfree":"0","paynum":"494946","notezone":"0","notemood":"喜爱","isshare":"0","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"115","repostid":"82","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"111","addtime":"2016-09-28 10:54","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"115","repostid":"83","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"111","addtime":"2016-09-28 10:54","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"115","repostid":"81","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"646466449","addtime":"2016-09-28 10:51","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]},{"noteid":"115","repostid":"79","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"5464949","addtime":"2016-09-27 15:23","content":"骆驼诺克","notetype":"4","notevideo":"","notevideopic":"","clickcount":"55","hotword":"","isself":"0","isfree":"0","paynum":"57578668","notezone":"0","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"1","list":[]}]
     * haoyoulist : [{"noteid":"144","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-30 12:33","content":"你佛","notetype":"4","notevideo":"","notevideopic":"","clickcount":"0","hotword":"","isself":"1","isfree":"1","paynum":"7","notezone":"1","notemood":"喜爱","isshare":"1","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","iszan":"0","list":[]},{"noteid":"92","repostid":"0","notestate":"0","userid":"ae1b20378ca643aa8ab8beb32402d7c4","usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","repostcontent":"","addtime":"2016-09-23 17:52","content":"溜溜溜","notetype":"2","notevideo":"","notevideopic":"","clickcount":"36","hotword":"","isself":"0","isfree":"1","paynum":"57575","notezone":"1","notemood":"喜爱","isshare":"1","shouyi":"44.00","redu":"26.400","repostcount":"0","commcount":"2","moodcount":"1","zancount":"1","rewardcount":"1","iszan":"0","list":[]}]
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
     * isfriend : 1
     * isfocus : 1
     * isfans : 0
     * focusnum : 11
     * fansnum : 4
     */

    public List<DataBean> data;
    /**
     * noteid : 151
     * repostid : 0
     * notestate : 0
     * userid : ae1b20378ca643aa8ab8beb32402d7c4
     * usernickname : 5555
     * userface : /upload/userface/201609231857315403.jpg
     * usersex : 男
     * userlevel : 癸
     * repostcontent :
     * addtime : 2016-10-09 12:43
     * content : 啦啦啦
     * notetype : 1
     * notevideo : /upload/social/video/201610091243066448.mp4
     * notevideopic : /upload/social/video/201610091243066467.jpg
     * clickcount : 0
     * hotword :
     * isself : 0
     * isfree : 0
     * paynum : 999999
     * notezone : 0
     * notemood : 喜爱
     * isshare : 1
     * shouyi : 0.00
     * redu : 0.000
     * repostcount : 0
     * commcount : 0
     * moodcount : 0
     * zancount : 0
     * rewardcount : 0
     * iszan : 0
     * list : []
     */

    private List<FabulistBean> fabulist;
    /**
     * noteid : 115
     * repostid : 93
     * notestate : 0
     * userid : ae1b20378ca643aa8ab8beb32402d7c4
     * usernickname : 5555
     * userface : /upload/userface/201609231857315403.jpg
     * usersex : 男
     * userlevel : 癸
     * repostcontent : 测试转发
     * addtime : 2016-10-09 10:48
     * content : 骆驼诺克
     * notetype : 4
     * notevideo :
     * notevideopic :
     * clickcount : 55
     * hotword :
     * isself : 0
     * isfree : 0
     * paynum : 57578668
     * notezone : 0
     * notemood : 喜爱
     * isshare : 1
     * shouyi : 0.00
     * redu : 0.000
     * repostcount : 0
     * commcount : 0
     * moodcount : 0
     * zancount : 0
     * rewardcount : 0
     * iszan : 1
     * list : []
     */

    private List<ZhuanfalistBean> zhuanfalist;
    /**
     * noteid : 144
     * repostid : 0
     * notestate : 0
     * userid : ae1b20378ca643aa8ab8beb32402d7c4
     * usernickname : 5555
     * userface : /upload/userface/201609231857315403.jpg
     * usersex : 男
     * userlevel : 癸
     * repostcontent :
     * addtime : 2016-09-30 12:33
     * content : 你佛
     * notetype : 4
     * notevideo :
     * notevideopic :
     * clickcount : 0
     * hotword :
     * isself : 1
     * isfree : 1
     * paynum : 7
     * notezone : 1
     * notemood : 喜爱
     * isshare : 1
     * shouyi : 0.00
     * redu : 0.000
     * repostcount : 0
     * commcount : 0
     * moodcount : 0
     * zancount : 0
     * rewardcount : 0
     * iszan : 0
     * list : []
     */

    private List<HaoyoulistBean> haoyoulist;

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

    public List<FabulistBean> getFabulist() {
        return fabulist;
    }

    public void setFabulist(List<FabulistBean> fabulist) {
        this.fabulist = fabulist;
    }

    public List<ZhuanfalistBean> getZhuanfalist() {
        return zhuanfalist;
    }

    public void setZhuanfalist(List<ZhuanfalistBean> zhuanfalist) {
        this.zhuanfalist = zhuanfalist;
    }

    public List<HaoyoulistBean> getHaoyoulist() {
        return haoyoulist;
    }

    public void setHaoyoulist(List<HaoyoulistBean> haoyoulist) {
        this.haoyoulist = haoyoulist;
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
        private String focusnum;
        private String fansnum;

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

        public String getFocusnum() {
            return focusnum;
        }

        public void setFocusnum(String focusnum) {
            this.focusnum = focusnum;
        }

        public String getFansnum() {
            return fansnum;
        }

        public void setFansnum(String fansnum) {
            this.fansnum = fansnum;
        }
    }

    public static class FabulistBean {
        private String noteid;
        private String repostid;
        private String notestate;
        private String userid;
        private String usernickname;
        private String userface;
        private String usersex;
        private String userlevel;
        private String repostcontent;
        private String addtime;
        private String content;
        private String notetype;
        private String notevideo;
        private String notevideopic;
        private String clickcount;
        private String hotword;
        private String isself;
        private String isfree;
        private String paynum;
        private String notezone;
        private String notemood;
        private String isshare;
        private String shouyi;
        private String redu;
        private String repostcount;
        private String commcount;
        private String moodcount;
        private String zancount;
        private String rewardcount;
        private String iszan;
        private List<?> list;

        public String getNoteid() {
            return noteid;
        }

        public void setNoteid(String noteid) {
            this.noteid = noteid;
        }

        public String getRepostid() {
            return repostid;
        }

        public void setRepostid(String repostid) {
            this.repostid = repostid;
        }

        public String getNotestate() {
            return notestate;
        }

        public void setNotestate(String notestate) {
            this.notestate = notestate;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUsersex() {
            return usersex;
        }

        public void setUsersex(String usersex) {
            this.usersex = usersex;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getRepostcontent() {
            return repostcontent;
        }

        public void setRepostcontent(String repostcontent) {
            this.repostcontent = repostcontent;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNotetype() {
            return notetype;
        }

        public void setNotetype(String notetype) {
            this.notetype = notetype;
        }

        public String getNotevideo() {
            return notevideo;
        }

        public void setNotevideo(String notevideo) {
            this.notevideo = notevideo;
        }

        public String getNotevideopic() {
            return notevideopic;
        }

        public void setNotevideopic(String notevideopic) {
            this.notevideopic = notevideopic;
        }

        public String getClickcount() {
            return clickcount;
        }

        public void setClickcount(String clickcount) {
            this.clickcount = clickcount;
        }

        public String getHotword() {
            return hotword;
        }

        public void setHotword(String hotword) {
            this.hotword = hotword;
        }

        public String getIsself() {
            return isself;
        }

        public void setIsself(String isself) {
            this.isself = isself;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getPaynum() {
            return paynum;
        }

        public void setPaynum(String paynum) {
            this.paynum = paynum;
        }

        public String getNotezone() {
            return notezone;
        }

        public void setNotezone(String notezone) {
            this.notezone = notezone;
        }

        public String getNotemood() {
            return notemood;
        }

        public void setNotemood(String notemood) {
            this.notemood = notemood;
        }

        public String getIsshare() {
            return isshare;
        }

        public void setIsshare(String isshare) {
            this.isshare = isshare;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public String getRedu() {
            return redu;
        }

        public void setRedu(String redu) {
            this.redu = redu;
        }

        public String getRepostcount() {
            return repostcount;
        }

        public void setRepostcount(String repostcount) {
            this.repostcount = repostcount;
        }

        public String getCommcount() {
            return commcount;
        }

        public void setCommcount(String commcount) {
            this.commcount = commcount;
        }

        public String getMoodcount() {
            return moodcount;
        }

        public void setMoodcount(String moodcount) {
            this.moodcount = moodcount;
        }

        public String getZancount() {
            return zancount;
        }

        public void setZancount(String zancount) {
            this.zancount = zancount;
        }

        public String getRewardcount() {
            return rewardcount;
        }

        public void setRewardcount(String rewardcount) {
            this.rewardcount = rewardcount;
        }

        public String getIszan() {
            return iszan;
        }

        public void setIszan(String iszan) {
            this.iszan = iszan;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }
    }

    public static class ZhuanfalistBean {
        private String noteid;
        private String repostid;
        private String notestate;
        private String userid;
        private String usernickname;
        private String userface;
        private String usersex;
        private String userlevel;
        private String repostcontent;
        private String addtime;
        private String content;
        private String notetype;
        private String notevideo;
        private String notevideopic;
        private String clickcount;
        private String hotword;
        private String isself;
        private String isfree;
        private String paynum;
        private String notezone;
        private String notemood;
        private String isshare;
        private String shouyi;
        private String redu;
        private String repostcount;
        private String commcount;
        private String moodcount;
        private String zancount;
        private String rewardcount;
        private String iszan;
        private List<?> list;

        public String getNoteid() {
            return noteid;
        }

        public void setNoteid(String noteid) {
            this.noteid = noteid;
        }

        public String getRepostid() {
            return repostid;
        }

        public void setRepostid(String repostid) {
            this.repostid = repostid;
        }

        public String getNotestate() {
            return notestate;
        }

        public void setNotestate(String notestate) {
            this.notestate = notestate;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUsersex() {
            return usersex;
        }

        public void setUsersex(String usersex) {
            this.usersex = usersex;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getRepostcontent() {
            return repostcontent;
        }

        public void setRepostcontent(String repostcontent) {
            this.repostcontent = repostcontent;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNotetype() {
            return notetype;
        }

        public void setNotetype(String notetype) {
            this.notetype = notetype;
        }

        public String getNotevideo() {
            return notevideo;
        }

        public void setNotevideo(String notevideo) {
            this.notevideo = notevideo;
        }

        public String getNotevideopic() {
            return notevideopic;
        }

        public void setNotevideopic(String notevideopic) {
            this.notevideopic = notevideopic;
        }

        public String getClickcount() {
            return clickcount;
        }

        public void setClickcount(String clickcount) {
            this.clickcount = clickcount;
        }

        public String getHotword() {
            return hotword;
        }

        public void setHotword(String hotword) {
            this.hotword = hotword;
        }

        public String getIsself() {
            return isself;
        }

        public void setIsself(String isself) {
            this.isself = isself;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getPaynum() {
            return paynum;
        }

        public void setPaynum(String paynum) {
            this.paynum = paynum;
        }

        public String getNotezone() {
            return notezone;
        }

        public void setNotezone(String notezone) {
            this.notezone = notezone;
        }

        public String getNotemood() {
            return notemood;
        }

        public void setNotemood(String notemood) {
            this.notemood = notemood;
        }

        public String getIsshare() {
            return isshare;
        }

        public void setIsshare(String isshare) {
            this.isshare = isshare;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public String getRedu() {
            return redu;
        }

        public void setRedu(String redu) {
            this.redu = redu;
        }

        public String getRepostcount() {
            return repostcount;
        }

        public void setRepostcount(String repostcount) {
            this.repostcount = repostcount;
        }

        public String getCommcount() {
            return commcount;
        }

        public void setCommcount(String commcount) {
            this.commcount = commcount;
        }

        public String getMoodcount() {
            return moodcount;
        }

        public void setMoodcount(String moodcount) {
            this.moodcount = moodcount;
        }

        public String getZancount() {
            return zancount;
        }

        public void setZancount(String zancount) {
            this.zancount = zancount;
        }

        public String getRewardcount() {
            return rewardcount;
        }

        public void setRewardcount(String rewardcount) {
            this.rewardcount = rewardcount;
        }

        public String getIszan() {
            return iszan;
        }

        public void setIszan(String iszan) {
            this.iszan = iszan;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }
    }

    public static class HaoyoulistBean {
        private String noteid;
        private String repostid;
        private String notestate;
        private String userid;
        private String usernickname;
        private String userface;
        private String usersex;
        private String userlevel;
        private String repostcontent;
        private String addtime;
        private String content;
        private String notetype;
        private String notevideo;
        private String notevideopic;
        private String clickcount;
        private String hotword;
        private String isself;
        private String isfree;
        private String paynum;
        private String notezone;
        private String notemood;
        private String isshare;
        private String shouyi;
        private String redu;
        private String repostcount;
        private String commcount;
        private String moodcount;
        private String zancount;
        private String rewardcount;
        private String iszan;
        private List<?> list;

        public String getNoteid() {
            return noteid;
        }

        public void setNoteid(String noteid) {
            this.noteid = noteid;
        }

        public String getRepostid() {
            return repostid;
        }

        public void setRepostid(String repostid) {
            this.repostid = repostid;
        }

        public String getNotestate() {
            return notestate;
        }

        public void setNotestate(String notestate) {
            this.notestate = notestate;
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

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUsersex() {
            return usersex;
        }

        public void setUsersex(String usersex) {
            this.usersex = usersex;
        }

        public String getUserlevel() {
            return userlevel;
        }

        public void setUserlevel(String userlevel) {
            this.userlevel = userlevel;
        }

        public String getRepostcontent() {
            return repostcontent;
        }

        public void setRepostcontent(String repostcontent) {
            this.repostcontent = repostcontent;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNotetype() {
            return notetype;
        }

        public void setNotetype(String notetype) {
            this.notetype = notetype;
        }

        public String getNotevideo() {
            return notevideo;
        }

        public void setNotevideo(String notevideo) {
            this.notevideo = notevideo;
        }

        public String getNotevideopic() {
            return notevideopic;
        }

        public void setNotevideopic(String notevideopic) {
            this.notevideopic = notevideopic;
        }

        public String getClickcount() {
            return clickcount;
        }

        public void setClickcount(String clickcount) {
            this.clickcount = clickcount;
        }

        public String getHotword() {
            return hotword;
        }

        public void setHotword(String hotword) {
            this.hotword = hotword;
        }

        public String getIsself() {
            return isself;
        }

        public void setIsself(String isself) {
            this.isself = isself;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getPaynum() {
            return paynum;
        }

        public void setPaynum(String paynum) {
            this.paynum = paynum;
        }

        public String getNotezone() {
            return notezone;
        }

        public void setNotezone(String notezone) {
            this.notezone = notezone;
        }

        public String getNotemood() {
            return notemood;
        }

        public void setNotemood(String notemood) {
            this.notemood = notemood;
        }

        public String getIsshare() {
            return isshare;
        }

        public void setIsshare(String isshare) {
            this.isshare = isshare;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public String getRedu() {
            return redu;
        }

        public void setRedu(String redu) {
            this.redu = redu;
        }

        public String getRepostcount() {
            return repostcount;
        }

        public void setRepostcount(String repostcount) {
            this.repostcount = repostcount;
        }

        public String getCommcount() {
            return commcount;
        }

        public void setCommcount(String commcount) {
            this.commcount = commcount;
        }

        public String getMoodcount() {
            return moodcount;
        }

        public void setMoodcount(String moodcount) {
            this.moodcount = moodcount;
        }

        public String getZancount() {
            return zancount;
        }

        public void setZancount(String zancount) {
            this.zancount = zancount;
        }

        public String getRewardcount() {
            return rewardcount;
        }

        public void setRewardcount(String rewardcount) {
            this.rewardcount = rewardcount;
        }

        public String getIszan() {
            return iszan;
        }

        public void setIszan(String iszan) {
            this.iszan = iszan;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }
    }
}
