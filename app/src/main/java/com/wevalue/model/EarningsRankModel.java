package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/21
 * 类说明：
 */

public class EarningsRankModel {

    /**
     * result : 1
     * message :
     * data : [{"userid":"ae1b20378ca643aa8ab8beb32402d7c4","shouyi":1.1246146E7,"usernickname":"5555","userface":"/upload/userface/201609231857315403.jpg","usersex":"男","userlevel":"癸","userinfo":"了了了具体","rankcontent":"总收益：11246146.00"},{"userid":"2c617ecf1b984331985a65b3d3cba3e1","shouyi":5242.4,"usernickname":"??","userface":"/upload/userface/201610081419185168.jpg","usersex":"男","userlevel":"癸","userinfo":"愤怒??","rankcontent":"总收益：5242.40"},{"userid":"37dd84bde8ba4367ae5eebdaaf615def","shouyi":562,"usernickname":"啦啦a","userface":"/upload/userface/201609221648478798.jpg","usersex":"男","userlevel":"癸","userinfo":"啊教练开发啦解放啦解放啦；就","rankcontent":"总收益：562.00"},{"userid":"ba39301d25d34b4fa20fac7cca3cc523","shouyi":57,"usernickname":"天空之城","userface":"/upload/userface/201608102248257712.jpg","usersex":"男","userlevel":"癸","userinfo":"你木就马上就突然就USA胡突然就台式机突然理解提亏","rankcontent":"总收益：57.00"},{"userid":"0835bdbb93954f57b492cf751c26b0f5","shouyi":11,"usernickname":"Romantic_1","userface":"/upload/userface/201608101534450506.jpg","usersex":"男","userlevel":"癸","userinfo":"Romantic test demo!","rankcontent":"总收益：11.00"},{"userid":"6bf8beb898f5476392b9f547cb019f10","shouyi":8,"usernickname":"WeValue ","userface":"/upload/userface/201609181531172430.jpg","usersex":"男","userlevel":"癸","userinfo":"这是一个神奇的平台，你可以发布文字、发布图片、发布音乐、发布视频、发布一切你认为有意义的东西，我们鼓励你的创新！即将健康快乐近几年来","rankcontent":"总收益：8.00"},{"userid":"c3ed12ff1fde4ddd9c64de8b5c29ba3d","shouyi":6,"usernickname":"BYK","userface":"","usersex":"男","userlevel":"癸","userinfo":"Gkwgergerg","rankcontent":"总收益：6.00"},{"userid":"f6513b7c45db4aacba9b454598c6cc80","shouyi":0,"usernickname":"x","userface":"","usersex":"女","userlevel":"癸","userinfo":"nu","rankcontent":"总收益：0.00"},{"userid":"cdac0293295746d585a51eefa58d9a09","shouyi":0,"usernickname":"杨一郎","userface":"/upload/userface/201610201400372674.jpg","usersex":"女","userlevel":"癸","userinfo":"这是杨一郎的简介部分","rankcontent":"总收益：0.00"},{"userid":"aa992f59fe6c44f99e4d5f94a90dbe3b","shouyi":0,"usernickname":"Afag","userface":"","usersex":"男","userlevel":"癸","userinfo":"Dasfaaga","rankcontent":"总收益：0.00"},{"userid":"9bfadc8640174d8b8bcbc084f21d643f","shouyi":0,"usernickname":"看来几乎肯定是卡上","userface":"/upload/userface/201609232310176028.jpg","usersex":"男","userlevel":"癸","userinfo":"阿斯顿发啊手机客户端","rankcontent":"总收益：0.00"},{"userid":"64b8db01d6234274a5eceac7874cec52","shouyi":0,"usernickname":"你好","userface":"","usersex":"男","userlevel":"癸","userinfo":"您","rankcontent":"总收益：0.00"}]
     */

    private int result;
    private String message;
    /**
     * userid : ae1b20378ca643aa8ab8beb32402d7c4
     * shouyi : 1.1246146E7
     * usernickname : 5555
     * userface : /upload/userface/201609231857315403.jpg
     * usersex : 男
     * userlevel : 癸
     * userinfo : 了了了具体
     * rankcontent : 总收益：11246146.00
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
        private String userid;
        private double shouyi;
        private String usernickname;
        private String userface;
        private String usersex;
        private String userlevel;
        private String userinfo;
        private String rankcontent;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public double getShouyi() {
            return shouyi;
        }

        public void setShouyi(double shouyi) {
            this.shouyi = shouyi;
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

        public String getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(String userinfo) {
            this.userinfo = userinfo;
        }

        public String getRankcontent() {
            return rankcontent;
        }

        public void setRankcontent(String rankcontent) {
            this.rankcontent = rankcontent;
        }
    }
}
