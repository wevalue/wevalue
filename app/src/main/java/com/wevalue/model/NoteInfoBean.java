package com.wevalue.model;

import java.util.List;

/**
 * 帖子详情 实体类
 */
public class NoteInfoBean {

    public String result;//": 1,
    public String message;//": "",
    public List<NoteInfoEntity> data;//": [{
    public String comment_Total;//": "5",  评论总数
    public String repost_Total;//": "6",  转发总数
    public String mood_Total;//": "4",    情绪总数
    public String zan_Total;//": "3",       点赞总数
    public String reward_Total;//": "7"    打赏总数

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

    public List<NoteInfoEntity> getData() {
        return data;
    }

    public void setData(List<NoteInfoEntity> data) {
        this.data = data;
    }

    public String getComment_Total() {
        return comment_Total;
    }

    public void setComment_Total(String comment_Total) {
        this.comment_Total = comment_Total;
    }

    public String getRepost_Total() {
        return repost_Total;
    }

    public void setRepost_Total(String repost_Total) {
        this.repost_Total = repost_Total;
    }

    public String getMood_Total() {
        return mood_Total;
    }

    public void setMood_Total(String mood_Total) {
        this.mood_Total = mood_Total;
    }

    public String getZan_Total() {
        return zan_Total;
    }

    public void setZan_Total(String zan_Total) {
        this.zan_Total = zan_Total;
    }

    public String getReward_Total() {
        return reward_Total;
    }

    public void setReward_Total(String reward_Total) {
        this.reward_Total = reward_Total;
    }

    public static class NoteInfoEntity {

        //*****转发列表  数据结构*****
        public String noteid;//": 1,//信息id
        public String repostid;//": 1,//转发id
        public String repostcontent;//": "我就要转发",//转发内容
        public String reposttime;//": "2016-08-03 17:34"//转发时间

        public String userid;//": "6bf8beb898f5476392b9f547cb019f10",//用户id
        public String usernickname;//;": "WeValue ",
        public String userface;//;": "/upload/userface/201608221353300848.jpg",
        public String userlevel;//": "癸"

        //******信息评论列表 数据结构*********
        public String commid;//": "24",//评论id
        public String commentid;//2832,  //这两个id 都是一样的 为什么？ 我也不知道  可能是因为ios的名字冲突了 所以又新加了一个名字

        public String commcontent;//": "我回复直接评论1,这是回复内容",//评论内容
        public String commuserid;//": "6bf8beb898f5476392b9f547cb019f10",//评论人id
        public String commuser;//": "DB",//评论人昵称
        public String commface;//": "/upload/userface/201608031630530466.jpg",//评论人头像
        public String commtime;//": "2016-08-03 17:44",//添加时间
        public String replyuserid;//": "6bf8beb898f5476392b9f547cb019f10",//被评论用户id
        public String replyuser;//": "DB",//被评论用户昵称
        public String replyface;//": "/upload/userface/201608031630530466.jpg",//被评论用户头像
        public String replycommid;//": "19"//被评论的 评论id
        public String commimg;//": 评论的图片
        public List<NoteInfoBean.NoteInfoEntity> replycomm; //回复集合
        public String zannums;//": 赞的数量

        //******信息情绪列表 数据结构*********
        public String moodid;//": 1,//情绪id
        public String moodcontent;//": "开心",//情绪内容
        public String moodtime;//": "2016-08-03 17:37"//发表时间

        //******信息点赞列表 数据结构*********
        public String zanid;//": 1,
        public String zantime;//": "2016-08-03 17:35"


        //******信息打赏列表 数据结构*********
        public String rewardid;//": 1,
        public String rewardmoney;//": 5,
        public String rewardtime;//": "2016-08-03 17:36"

        public List<NoteInfoEntity> getReplycomm() {
            return replycomm;
        }

        public void setReplycomm(List<NoteInfoEntity> replycomm) {
            this.replycomm = replycomm;
        }

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public void setZannums(String zannums) {
            this.zannums = zannums;
        }

        public String getZannums() {
            return zannums;
        }

        public String getCommimg() {
            return commimg;
        }

        public void setCommimg(String commimg) {
            this.commimg = commimg;
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

        public String getRepostid() {
            return repostid;
        }

        public void setRepostid(String repostid) {
            this.repostid = repostid;
        }

        public String getNoteid() {
            return noteid;
        }

        public void setNoteid(String noteid) {
            this.noteid = noteid;
        }

        public String getRepostcontent() {
            return repostcontent;
        }

        public void setRepostcontent(String repostcontent) {
            this.repostcontent = repostcontent;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getReposttime() {
            return reposttime;
        }

        public void setReposttime(String reposttime) {
            this.reposttime = reposttime;
        }

        public String getCommid() {
            return commid;
        }

        public void setCommid(String commid) {
            this.commid = commid;
        }

        public String getCommcontent() {
            return commcontent;
        }

        public void setCommcontent(String commcontent) {
            this.commcontent = commcontent;
        }

        public String getCommuserid() {
            return commuserid;
        }

        public void setCommuserid(String commuserid) {
            this.commuserid = commuserid;
        }

        public String getCommuser() {
            return commuser;
        }

        public void setCommuser(String commuser) {
            this.commuser = commuser;
        }

        public String getCommface() {
            return commface;
        }

        public void setCommface(String commface) {
            this.commface = commface;
        }

        public String getCommtime() {
            return commtime;
        }

        public void setCommtime(String commtime) {
            this.commtime = commtime;
        }

        public String getReplyuserid() {
            return replyuserid;
        }

        public void setReplyuserid(String replyuserid) {
            this.replyuserid = replyuserid;
        }

        public String getReplyuser() {
            String s=replyuser;
            return replyuser;
        }

        public void setReplyuser(String replyuser) {
            this.replyuser = replyuser;
        }

        public String getReplyface() {
            return replyface;
        }

        public void setReplyface(String replyface) {
            this.replyface = replyface;
        }

        public String getReplycommid() {
            return replycommid;
        }

        public void setReplycommid(String replycommid) {
            this.replycommid = replycommid;
        }

        public String getMoodid() {
            return moodid;
        }

        public void setMoodid(String moodid) {
            this.moodid = moodid;
        }

        public String getMoodcontent() {
            return moodcontent;
        }

        public void setMoodcontent(String moodcontent) {
            this.moodcontent = moodcontent;
        }

        public String getMoodtime() {
            return moodtime;
        }

        public void setMoodtime(String moodtime) {
            this.moodtime = moodtime;
        }

        public String getZanid() {
            return zanid;
        }

        public void setZanid(String zanid) {
            this.zanid = zanid;
        }

        public String getZantime() {
            return zantime;
        }

        public void setZantime(String zantime) {
            this.zantime = zantime;
        }

        public String getRewardid() {
            return rewardid;
        }

        public void setRewardid(String rewardid) {
            this.rewardid = rewardid;
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
    }
}

