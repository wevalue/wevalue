package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/17
 * 类说明：用户站内消息的模板
 */

public class SiteMessageModel {
    /**
     * result : 1
     * message :
     * data : [{"messid":11,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"好友申请消息","noteid":null,"repostid":null,"messreplyid":null,"messtype":1,"addtime":"2016-10-14 17:31","messstate":0,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":""},{"messid":12,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"被关注消息","noteid":null,"repostid":null,"messreplyid":null,"messtype":2,"addtime":"2016-10-14 17:31","messstate":1,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":""},{"messid":13,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"打赏消息","noteid":"5","repostid":"0","messreplyid":null,"messtype":3,"addtime":"2016-10-14 17:35","messstate":0,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":"打赏：¥1.00"},{"messid":14,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"转发消息","noteid":"5","repostid":"1","messreplyid":null,"messtype":4,"addtime":"2016-10-14 17:35","messstate":0,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":"转发信息"},{"messid":15,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"赞了你","noteid":"5","repostid":"2","messreplyid":null,"messtype":5,"addtime":"2016-10-14 17:35","messstate":0,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":"转发信息"},{"messid":16,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":"37dd84bde8ba4367ae5eebdaaf615def","messtitle":"评论消息","noteid":"5","repostid":"3","messreplyid":"2","messtype":6,"addtime":"2016-10-14 17:51","messstate":0,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","messcontent":"回复 我:开始啦"},{"messid":17,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","fromuserid":null,"messtitle":"系统推送","noteid":null,"repostid":null,"messreplyid":null,"messtype":7,"addtime":"2016-10-14 17:54","messstate":0,"usernickname":null,"userface":null,"usersex":null,"messcontent":"测试推送1测试推送1测试推送1测试推送1..."}]
     */
    private int result;
    private String message;

    private String uplevel;//": "",
    private String sitemessnum;//": "1",
    private String fridendsnum;//": "0",
    private String focusnum;//": "1",
    private String fansnum;//": "0",

    private List<DataBean> data;
    private List<DataBean> friendlist;
    private List<DataBean> sitemesslist;
    private List<DataBean> focuslist;
    private List<DataBean> fanslist;

    public String getUplevel() {
        return uplevel;
    }

    public void setUplevel(String uplevel) {
        this.uplevel = uplevel;
    }

    public String getSitemessnum() {
        return sitemessnum;
    }

    public void setSitemessnum(String sitemessnum) {
        this.sitemessnum = sitemessnum;
    }

    public String getFridendsnum() {
        return fridendsnum;
    }

    public void setFridendsnum(String fridendsnum) {
        this.fridendsnum = fridendsnum;
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

    public List<DataBean> getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(List<DataBean> friendlist) {
        this.friendlist = friendlist;
    }

    public List<DataBean> getFocuslist() {
        return focuslist;
    }

    public void setFocuslist(List<DataBean> focuslist) {
        this.focuslist = focuslist;
    }

    public List<DataBean> getFanslist() {
        return fanslist;
    }

    public void setFanslist(List<DataBean> fanslist) {
        this.fanslist = fanslist;
    }

    public List<DataBean> getSitemesslist() {
        return sitemesslist;
    }


    public void setSitemesslist(List<DataBean> sitemesslist) {
        this.sitemesslist = sitemesslist;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    /**
     * "messid": 3,
     * "userid": "ae1b20378ca643aa8ab8beb32402d7c4",//用户ID
     * "fromuserid": "cdac0293295746d585a51eefa58d9a09",//来自用户id
     * "messtitle": "好友申请消息",//标题
     * "noteid": null,//信息id
     * "repostid": null,//转发id
     * "messreplyid": null,//回复评论id 6评论消息使用
     * "messtype": 2,//1好友申请消息2被关注消息3打赏消息4转发消息5点赞消息6评论消息7系统推送
     * "addtime": "2016-10-14 17:31",//添加时间
     * "messstate": 1,//已读未读 0未读 1已读
     * "messnotecontent": "", //信息内容 3打赏消息4转发消息5点赞消息6评论消息使用
     * "messcontent": "",//消息内容
     * "usernickname": "杨一郎",//来自用户
     * "userface": "",//来自用户头像
     * "usersex": "女",//来自用户性别
     * "userlevel":"癸","
     * userinfo":"啊教练开发啦解放啦解放啦；就",
     * "isfriend": "0",//是否好友  1好友申请消息使用
     * "isfocus": "1",//是否已关注 2被关注消息使用
     * "webcontent": "" //html格式内容 7系统推送使用
     */

    public static class DataBean {
        private int messid;
        private String userid;
        private String fromuserid;
        private String messtitle;
        private String noteid;
        private String repostid;
        private String messreplyid;
        private int messtype;
        private int messstate;
        private String messnotecontent;
        private String messcontent;
        private String addtime;
        private String usernickname;
        private String userface;
        private String usersex;
        private String userlevel;//": "癸",
        private String userinfo;//": "你好好欧米茄买手机了买手机了精疲力尽几级了几级了你理解几级了",
        private String isfriend;//": "0",
        private String isfocus;//": "0",
        private String isfree;//": "0 = 付费, 1 = 免费,
        private String webcontent;//":  //html格式内容 7系统推送使用
        private String thistypenum;//未读消息的数量
        private String userv;//是否大V
        private String usernumber;//是否大V

        public String getUsernumber() {
            return usernumber;
        }

        public void setUsernumber(String usernumber) {
            this.usernumber = usernumber;
        }

        public String getUserv() {
            return userv;
        }

        public void setUserv(String userv) {
            this.userv = userv;
        }

        public String getIsfree() {
            return isfree;
        }

        public void setIsfree(String isfree) {
            this.isfree = isfree;
        }

        public String getThistypenum() {
            return thistypenum;
        }

        public void setThistypenum(String thistypenum) {
            this.thistypenum = thistypenum;
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

        public String getWebcontent() {
            return webcontent;
        }

        public void setWebcontent(String webcontent) {
            this.webcontent = webcontent;
        }

        public String getMessnotecontent() {
            return messnotecontent;
        }

        public void setMessnotecontent(String messnotecontent) {
            this.messnotecontent = messnotecontent;
        }

        public int getMessid() {
            return messid;
        }

        public void setMessid(int messid) {
            this.messid = messid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFromuserid() {
            return fromuserid;
        }

        public void setFromuserid(String fromuserid) {
            this.fromuserid = fromuserid;
        }

        public String getMesstitle() {
            return messtitle;
        }

        public void setMesstitle(String messtitle) {
            this.messtitle = messtitle;
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

        public String getMessreplyid() {
            return messreplyid;
        }

        public void setMessreplyid(String messreplyid) {
            this.messreplyid = messreplyid;
        }

        public int getMesstype() {
            return messtype;
        }

        public void setMesstype(int messtype) {
            this.messtype = messtype;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getMessstate() {
            return messstate;
        }

        public void setMessstate(int messstate) {
            this.messstate = messstate;
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

        public String getMesscontent() {
            return messcontent;
        }

        public void setMesscontent(String messcontent) {
            this.messcontent = messcontent;
        }
    }
}
