package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/19
 * 类说明：搜索好友的bean类
 */

public class SearchFriendBean {

    /**
     * result : 1
     * message :
     * dtfriend : [{"userid":"005086ecd2154b9da3a75c4a1699bba7","usersex":"男","usernickname":"1","userface":"/upload/userface/201609241818299436.jpg","userlevel":"癸","userinfo":"了","isfriend":"1","isfocus":"0","isfans":"0"}]
     * dtfocus : []
     * dtfans : []
     * data : []
     */
    private int result;
    private String message;
    /**
     * userid : 005086ecd2154b9da3a75c4a1699bba7
     * usersex : 男
     * usernickname : 1
     * userface : /upload/userface/201609241818299436.jpg
     * userlevel : 癸
     * userinfo : 了
     * isfriend : 1
     * isfocus : 0
     * isfans : 0
     */
    private List<DtfriendBean> dtfriend;
    private List<DtfriendBean> dtfocus;
    private List<DtfriendBean> dtusers;
    private List<DtfriendBean> dtnotes;
    private List<DtfriendBean> dtfans;
    private List<DtfriendBean> data;
    private List<DtfriendBean> welist;
    private List<DtfriendBean> friendlist;

    public List<DtfriendBean> getDtusers() {
        if(dtusers!=null&&dtusers.size()>0){
            dtusers.get(0).setUserType("陌生人");
        }
        return dtusers;
    }

    public void setDtusers(List<DtfriendBean> dtusers) {
        this.dtusers = dtusers;
    }

    public List<DtfriendBean> getDtnotes(String userType) {
        if (null != dtnotes && dtnotes.size() != 0) {
            try {
                dtnotes.get(0).setUserType(userType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dtnotes;
    }

    public List<DtfriendBean> getDtnotes() {

        return dtnotes;
    }

    public void setDtnotes(List<DtfriendBean> dtnotes) {
        this.dtnotes = dtnotes;
    }

    public List<DtfriendBean> getWelist(String userType) {
        if (welist.size() != 0) {
            welist.get(0).setUserType(userType);
        }
        return welist;
    }

    public void setWelist(List<DtfriendBean> welist) {
        this.welist = welist;
    }

    public List<DtfriendBean> getFriendlist(String userType) {
        if (friendlist.size() != 0) {
            {
                friendlist.get(0).setUserType(userType);
            }
        }
        return friendlist;
    }

    public void setFriendlist(List<DtfriendBean> friendlist) {
        this.friendlist = friendlist;
    }

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

    public List<DtfriendBean> getDtfriend(String userType) {
        try {
            dtfriend.get(0).setUserType(userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtfriend;
    }

    public void setDtfriend(List<DtfriendBean> dtfriend) {
        this.dtfriend = dtfriend;
    }

    public List<DtfriendBean> getDtfocus(String userType) {
        try {
            dtfocus.get(0).setUserType(userType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dtfocus;
    }

    public void setDtfocus(List<DtfriendBean> dtfocus) {
        this.dtfocus = dtfocus;
    }

    public List<DtfriendBean> getDtfans(String userType) {
        try {
            dtfans.get(0).setUserType(userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtfans;
    }

    public void setDtfans(List<DtfriendBean> dtfans) {
        this.dtfans = dtfans;
    }

    public List<DtfriendBean> getData(String userType) {
        try {
            data.get(0).setUserType(userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void setData(List<DtfriendBean> data) {
        this.data = data;
    }
//    public static void setUserType(String userType, List<DtfriendBean> list) {
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).setUserType(userType);
//        }
//    }

    public static class DtfriendBean {
        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
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
        private String title;
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


        private String userinfo;
        private String usernumber;
        private String isfriend;
        private String isfocus;
        private String isfans;
        private List<NoteBean.ImgUrl> list;
        private List<NoteBean.ImgUrl> list_1;
        private String userType;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUsernumber() {
            return usernumber;
        }

        public void setUsernumber(String usernumber) {
            this.usernumber = usernumber;
        }

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

        public List<NoteBean.ImgUrl> getList() {
            return list;
        }

        public void setList(List<NoteBean.ImgUrl> list) {
            this.list = list;
        }

        public List<NoteBean.ImgUrl> getList_1() {
            return list_1;
        }

        public void setList_1(List<NoteBean.ImgUrl> list_1) {
            this.list_1 = list_1;
        }
    }


    /**
     * dtnotes : {"noteid":"524","repostid":"0","notestate":"0","userid":"60f2da9d6da743acbc297d6c4f27c180","usernickname":"孟庆航","userface":"/upload/userface/20161118202634206827.jpg","olduserid":"60f2da9d6da743acbc297d6c4f27c180","oldusernickname":"孟庆航","olduserface":"/upload/userface/20161118202634206827.jpg","usersex":"男","userlevel":"辛","repostcontent":"","addtime":"2016-11-23 15:51","content":"今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行##换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行##换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。","notetype":"4","notevideo":"","notevideopic":"","clickcount":"35","hotword":"","isself":"1","isfree":"0","paynum":"100.00","notezone":"0","notemood":"","isshare":"0","shouyi":"0.00","redu":"0.000","repostcount":"0","commcount":"0","moodcount":"1","zancount":"1","rewardcount":"0","rewardgetsum":"0.00","rankrepost":"转发0次","rankreward":"被打赏总金额：0.00元","iszan":"0","list":[],"list_1":[]}
     */


    public class DtnotesBean {
        /**
         * noteid : 524
         * repostid : 0
         * notestate : 0
         * userid : 60f2da9d6da743acbc297d6c4f27c180
         * usernickname : 孟庆航
         * userface : /upload/userface/20161118202634206827.jpg
         * olduserid : 60f2da9d6da743acbc297d6c4f27c180
         * oldusernickname : 孟庆航
         * olduserface : /upload/userface/20161118202634206827.jpg
         * usersex : 男
         * userlevel : 辛
         * repostcontent :
         * addtime : 2016-11-23 15:51
         * content : 今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行##换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。#换行##换行#今年的10月10日晚上，黑龙江七台河市公安局戍企分局接到报警，说在电厂小区11号楼下，躺着一名浑身是血的男子，民警立即赶往现场。男子头部被砍伤，因为失血过多，在送往医院的途中不幸身亡。
         * notetype : 4
         * notevideo :
         * notevideopic :
         * clickcount : 35
         * hotword :
         * isself : 1
         * isfree : 0
         * paynum : 100.00
         * notezone : 0
         * notemood :
         * isshare : 0
         * shouyi : 0.00
         * redu : 0.000
         * repostcount : 0
         * commcount : 0
         * moodcount : 1
         * zancount : 1
         * rewardcount : 0
         * rewardgetsum : 0.00
         * rankrepost : 转发0次
         * rankreward : 被打赏总金额：0.00元
         * iszan : 0
         * list : []
         * list_1 : []
         */
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
        private List<NoteBean.ImgUrl> list;
        private List<NoteBean.ImgUrl> list_1;
        private String userType;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

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

        public List<NoteBean.ImgUrl> getList() {
            return list;
        }

        public void setList(List<NoteBean.ImgUrl> list) {
            this.list = list;
        }

        public List<NoteBean.ImgUrl> getList_1() {
            return list_1;
        }

        public void setList_1(List<NoteBean.ImgUrl> list_1) {
            this.list_1 = list_1;
        }
    }

//    public static class ImgUrl {
//        public String url;//": "/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }
}
