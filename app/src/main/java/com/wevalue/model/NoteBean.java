package com.wevalue.model;

import java.util.List;

/**
 * 世界帖子列表实体类
 */
public class NoteBean {
    public String result;//": 1,
    public String message;//": "",
    public List<NoteEntity> data;//": [{
    public List<NoteEntity> data_lunbo;//": [{
    public List<NoteEntity> data_jiage;//": [{
    private List<UserBean> user;
    private List<UserBean> userinfo;

    public String allcount;//":"41",//全部
    public String wordcount;//":"41",//世界
    public String friendcount;//":"0"//朋友们

    public String getSharefree() {
        return sharefree;
    }

    public void setSharefree(String sharefree) {
        this.sharefree = sharefree;
    }

    private String sharefree;//是否免费分享

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

    public void setUserinfo(List<UserBean> userinfo) {
        this.userinfo = userinfo;
    }

    public List<UserBean> getUserinfo() {
        return userinfo;
    }

    public List<UserBean> getUser() {
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

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

    public List<NoteEntity> getData() {
        return data;
    }

    public void setData(List<NoteEntity> data) {
        this.data = data;
    }

    public List<NoteEntity> getData_lunbo() {
        return data_lunbo;
    }

    public void setData_lunbo(List<NoteEntity> data_lunbo) {
        this.data_lunbo = data_lunbo;
    }

    public List<NoteEntity> getData_jiage() {
        return data_jiage;
    }

    public void setData_jiage(List<NoteEntity> data_jiage) {
        this.data_jiage = data_jiage;
    }

    public static class NoteEntity {
        private String outstate;//提现的状态
        private String olduserid;//原帖人id
        private String oldusernickname;//原帖人姓名
        private String olduserface;//原帖人头像
        public String noteid;//": "2",//信息id
        public String repostid;//转发帖子id
        public String userid;//": "ba39301d25d34b4fa20fac7cca3cc523",//发帖人id
        public String notetype;//": "3",//信息类型 1.视频文字2.音乐文字3.图片文字4.纯文字
        public String content;//": "测试图片文字测试图片文字测试图片文字测试图片文字测试图片文字",//内容
        public String notevideo;//": "",//视频链接
        public String notevideopic;//": ""//视频截图
        public String clickcount;//": "123",//点击量
        public String hotword;//": "测试",//关键字
        public String isself;//": "0",//0原创 1非原创
        public String isfree;//": "0",//0付费 1免费
        public String paynum;//": "10",//付费金额
        public String notezone;//": "0",//发布范围 0世界 1朋友
        public String notemood;//": "开心",//情绪
        public String isshare;//": "0",//分享至其他 0允许 1不允许
        public String firstnoteid;//": "",//原信息id
        public String firstuserid;//": "",//原发信息人id
        public String repostcount;//": "0",//转发量
        public String zancount;//": "3",//点赞量
        public String rewardcount;//": "26",//打赏总金额
        public String iszan;//": "0",//是否点赞  0未点赞 1已点赞
        public String addtime;//": "2016-08-03 17:27",//添加时间
        public String notestate;//": "0",//信息状态 0正常 1禁用
        public String usernickname;//": "天空之城",//发信息人昵称
        public String userface;//": "/upload/userface/201607261537328552.jpg",//发信息人头像
        public String usersex;//": "男 ",//发信息人昵称
        public String userlevel;//": "癸",//发信息人等级
        public String repostcontent;//"://转发评论信息
        public List<ImgUrl> list;//": [{//图片文件
        public List<ImgUrl> list_1;//图片缩略图
        private String rankrepost;//转发次数
        private String rankreward;//被打赏金额
        private String shouyi;//用户的收益
        private String sharefree;//用户分享是否超过72小时
        private String commcount;//": "0",//发帖/转发信息评论量
        private String moodcount;//": "0",//发帖/转发信息情绪量

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

        public String getSharefree() {
            return sharefree;
        }

        public void setSharefree(String sharefree) {
            this.sharefree = sharefree;
        }

        public String getShouyi() {
            return shouyi;
        }

        public void setShouyi(String shouyi) {
            this.shouyi = shouyi;
        }

        public List<ImgUrl> getList_1() {
            return list_1;
        }

        public void setList_1(List<ImgUrl> list_1) {
            this.list_1 = list_1;
        }

        public String getOutstate() {
            return outstate;
        }

        public void setOutstate(String outstate) {
            this.outstate = outstate;
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

        public String getRepostid() {
            return repostid;
        }

        public void setRepostid(String repostid) {
            this.repostid = repostid;
        }

        public String getRepostcontent() {
            return repostcontent;
        }

        public void setRepostcontent(String repostcontent) {
            this.repostcontent = repostcontent;
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

        public String getNotetype() {
            return notetype;
        }

        public void setNotetype(String notetype) {
            this.notetype = notetype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getFirstnoteid() {
            return firstnoteid;
        }

        public void setFirstnoteid(String firstnoteid) {
            this.firstnoteid = firstnoteid;
        }

        public String getFirstuserid() {
            return firstuserid;
        }

        public void setFirstuserid(String firstuserid) {
            this.firstuserid = firstuserid;
        }

        public String getRepostcount() {
            return repostcount;
        }

        public void setRepostcount(String repostcount) {
            this.repostcount = repostcount;
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

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getNotestate() {
            return notestate;
        }

        public void setNotestate(String notestate) {
            this.notestate = notestate;
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

        public List<ImgUrl> getList() {
            return list;
        }

        public void setList(List<ImgUrl> list) {
            this.list = list;
        }
    }

    public static class UserBean {
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
        private String usernumber;
        private String userremark;
        private String isblack;

        public String getIsblack() {
            return isblack;
        }

        public void setIsblack(String isblack) {
            this.isblack = isblack;
        }

        public String getUserremark() {
            return userremark;
        }

        public void setUserremark(String userremark) {
            this.userremark = userremark;
        }

        public String getUsernumber() {
            return usernumber;
        }

        public void setUsernumber(String usernumber) {
            this.usernumber = usernumber;
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
//            usernickname
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

    public static class ImgUrl {
        public String url;//": "/upload/userusertrueimg/ba39301d25d34b4fa20fac7cca3cc5231.jpg"

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}

