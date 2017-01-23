package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/24
 * 类说明：
 */

public class PaymentDetailModel {

    /**
     * result : 1
     * message :
     * data : [{"id":8,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"购买权限","money":5,"addtime":"2016-10-24T12:43:23.887","type":0},{"id":7,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"充值收入","money":5,"addtime":"2016-10-21T15:48:28.72","type":1},{"id":6,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"提现支出","money":5,"addtime":"2016-10-21T15:48:26.033","type":0},{"id":5,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"提现支出","money":5,"addtime":"2016-10-21T15:48:23.027","type":0},{"id":4,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"充值收入","money":5,"addtime":"2016-10-21T15:48:20.32","type":1},{"id":3,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"提现支出","money":5,"addtime":"2016-10-21T15:48:15.85","type":0},{"id":2,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"充值收入","money":5,"addtime":"2016-10-21T15:48:08.22","type":1},{"id":1,"userid":"ae1b20378ca643aa8ab8beb32402d7c4","title":"充值收入","money":5,"addtime":"2016-10-21T15:48:05.83","type":1}]
     */

    private int result;
    private String message;
    /**
     * id : 8
     * userid : ae1b20378ca643aa8ab8beb32402d7c4
     * title : 购买权限
     * money : 5.0
     * addtime : 2016-10-24T12:43:23.887
     * type : 0
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
        private int id;
        private String userid;
        private String title;
        private String money;
        private String addtime;
        private int type;
        private String orderno;
        private String ordertype;

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getOrdertype() {
            return ordertype;
        }

        public void setOrdertype(String ordertype) {
            this.ordertype = ordertype;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
