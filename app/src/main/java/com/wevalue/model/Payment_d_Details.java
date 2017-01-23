package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/9
 * 类说明：收支明细的详情页
 */

public class Payment_d_Details {


    /**
     * result : 1
     * message :
     * data : [{"noteid":"1","repostid":"134","notestate":"0","userid":"ba39301d25d34b4fa20fac7cca3cc523","usernickname":"天空之城","userface":"/upload/userface/201608102248257712.jpg","olduserid":"ba39301d25d34b4fa20fac7cca3cc523","oldusernickname":"天空之城","olduserface":"/upload/userface/201608102248257712.jpg","usersex":"男","userlevel":"庚","repostcontent":"这是转发评论评论！ // 天空之城:转发信息","addtime":"2016-11-04 10:11","content":"测试图片文字测试图片文字测试图片文字测试图片文字测试图片文字","notetype":"3","notevideo":"","notevideopic":"","clickcount":"0","hotword":"测试","isself":"0","isfree":"0","paynum":"10.00","notezone":"0","notemood":"开心","isshare":"0","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"0","zancount":"0","rewardcount":"0","rewardgetsum":"0.00","rankrepost":"转发0次","rankreward":"被打赏总金额：0.00元","iszan":"1","list":[{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"}],"list_1":[]}]
     */

    private int result;
    private String message;
    /**
     * noteid : 1
     * repostid : 134
     * notestate : 0
     * userid : ba39301d25d34b4fa20fac7cca3cc523
     * usernickname : 天空之城
     * userface : /upload/userface/201608102248257712.jpg
     * olduserid : ba39301d25d34b4fa20fac7cca3cc523
     * oldusernickname : 天空之城
     * olduserface : /upload/userface/201608102248257712.jpg
     * usersex : 男
     * userlevel : 庚
     * repostcontent : 这是转发评论评论！ // 天空之城:转发信息
     * addtime : 2016-11-04 10:11
     * content : 测试图片文字测试图片文字测试图片文字测试图片文字测试图片文字
     * notetype : 3
     * notevideo :
     * notevideopic :
     * clickcount : 0
     * hotword : 测试
     * isself : 0
     * isfree : 0
     * paynum : 10.00
     * notezone : 0
     * notemood : 开心
     * isshare : 0
     * shouyi : 0.00
     * redu : 0.000
     * repostcount : 0
     * commcount : 0
     * moodcount : 0
     * zancount : 0
     * rewardcount : 0
     * rewardgetsum : 0.00
     * rankrepost : 转发0次
     * rankreward : 被打赏总金额：0.00元
     * iszan : 1
     * list : [{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"},{"url":"/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"}]
     * list_1 : []
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
        private String noteid;
        private String repostid;
        private String notestate;
        private String userid;
        private String usernickname;
        private String userface;
        private String olduserid;
        private String oldusernickname;
        private String olduserface;
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
        private String rewardgetsum;
        private String rankrepost;
        private String rankreward;
        private String iszan;
        /**
         * url : /upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg
         */

        private List<ListBean> list;
        private List<?> list_1;

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

        public String getOlduserid() {
            return olduserid;
        }

        public void setOlduserid(String olduserid) {
            this.olduserid = olduserid;
        }

        public String getOldusernickname() {
            return oldusernickname;
        }

        public void setOldusernickname(String oldusernickname) {
            this.oldusernickname = oldusernickname;
        }

        public String getOlduserface() {
            return olduserface;
        }

        public void setOlduserface(String olduserface) {
            this.olduserface = olduserface;
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

        public String getRewardgetsum() {
            return rewardgetsum;
        }

        public void setRewardgetsum(String rewardgetsum) {
            this.rewardgetsum = rewardgetsum;
        }

        public String getRankrepost() {
            return rankrepost;
        }

        public void setRankrepost(String rankrepost) {
            this.rankrepost = rankrepost;
        }

        public String getRankreward() {
            return rankreward;
        }

        public void setRankreward(String rankreward) {
            this.rankreward = rankreward;
        }

        public String getIszan() {
            return iszan;
        }

        public void setIszan(String iszan) {
            this.iszan = iszan;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<?> getList_1() {
            return list_1;
        }

        public void setList_1(List<?> list_1) {
            this.list_1 = list_1;
        }

        public static class ListBean {
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
