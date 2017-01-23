package com.wevalue.model;

import java.util.List;

/**
 * 系统消息列表 实体类
 */
public class SystemMessageBean {

   public String result;//": 1,
    public String message;//": "",
    public List<SystemMsgEntity> data;//": [{

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

    public List<SystemMsgEntity> getData() {
        return data;
    }

    public void setData(List<SystemMsgEntity> data) {
        this.data = data;
    }

    public static class SystemMsgEntity{
        public String messid;//": 16,
        public String userid;//": "c330268e03784a038c2fc0f5447125d2", //用户ID
        public String fromuserid;//": "2b02b33f8e5c427bb1647efe4d4f67fd", //发送者id
        public String fromuser;//": "微信号", //发送者昵称
        public String fromuserface;//": "/upload/dbface/201606160807227294.jpg", //发送者头像
        public String messpic;//": "/upload/social/img/2016062113480313460.jpg", //相关图片
        public String messpictype;//":1,//图片类型 1.图文 2.视频截图
        public String messtitle;//": "好友", //标题
        public String messurl;//": "www.baidu", //链接
        public String messtype;//": 1, //类型 1.交友消息 2.朋友圈消息 3.站内通知 4.订单信息 5.广告
        public String addtime;//": "2016-01-01 00:00", //添加时间
        public String messstate;//": 0, //已读未读 0未读 1已读
        public String messcontent;//": "楼梯" //内容


        public String getMessid() {
            return messid;
        }

        public void setMessid(String messid) {
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

        public String getFromuser() {
            return fromuser;
        }

        public void setFromuser(String fromuser) {
            this.fromuser = fromuser;
        }

        public String getFromuserface() {
            return fromuserface;
        }

        public void setFromuserface(String fromuserface) {
            this.fromuserface = fromuserface;
        }

        public String getMesspic() {
            return messpic;
        }

        public void setMesspic(String messpic) {
            this.messpic = messpic;
        }

        public String getMesspictype() {
            return messpictype;
        }

        public void setMesspictype(String messpictype) {
            this.messpictype = messpictype;
        }

        public String getMesstitle() {
            return messtitle;
        }

        public void setMesstitle(String messtitle) {
            this.messtitle = messtitle;
        }

        public String getMessurl() {
            return messurl;
        }

        public void setMessurl(String messurl) {
            this.messurl = messurl;
        }

        public String getMesstype() {
            return messtype;
        }

        public void setMesstype(String messtype) {
            this.messtype = messtype;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getMessstate() {
            return messstate;
        }

        public void setMessstate(String messstate) {
            this.messstate = messstate;
        }

        public String getMesscontent() {
            return messcontent;
        }

        public void setMesscontent(String messcontent) {
            this.messcontent = messcontent;
        }
    }




}
