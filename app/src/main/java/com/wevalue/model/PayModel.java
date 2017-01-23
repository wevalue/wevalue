package com.wevalue.model;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/29
 * 类说明：
 */

public class PayModel {


    /**
     * result : 1
     * message : 生成支付订单成功！
     * datat : [{"orderno":"1201610292103112525"},{"ordername":"进行转发信息操作的订单"},{"ordermoney":"5"},{"orderdes":"这是进行转发信息操作的订单的描述！"},{"ordertime":"2016-10-29 21:03:11"},{"WX_partnerId":"1403282702"},{"WX_prepayId":"wx20161029210312780743577c0580836394"},{"WX_nonceStr":"13fe9d84310e77f13a6d184dbf1232f3"},{"WX_timeStamp":"1477746191"},{"WX_package":"Sign=WXPay"},{"WX_sign":"A4E97ACB22EBA9B6412426953004581E"}]
     */

    private int result;
    private String message;
    /**
     * orderno : 1201610292103112525
     */

    private List<DatatBean> datat;

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

    public List<DatatBean> getDatat() {
        return datat;
    }

    public void setDatat(List<DatatBean> datat) {
        this.datat = datat;
    }

    /**
     * result : 1
     * message : 生成支付订单成功！
{"wx_package":"Sign=WXPay"},{"wx_sign":"9321DE214528FF8B3AB36664EDA2EF9E"}]
     */
    public static class DatatBean {
        private String orderno;
        private String ordername;
        private String ordermoney;
        private String orderdes;
        private String ordertime;
        private String wx_partnerId;
        private String wx_prepayId;
        private String wx_nonceStr;
        private String wx_timeStamp;
        private String wx_package;
        private String wx_sign;

        public String getOrdername() {
            return ordername;
        }

        public void setOrdername(String ordername) {
            this.ordername = ordername;
        }

        public String getOrdermoney() {
            return ordermoney;
        }

        public void setOrdermoney(String ordermoney) {
            this.ordermoney = ordermoney;
        }

        public String getOrderdes() {
            return orderdes;
        }

        public void setOrderdes(String orderdes) {
            this.orderdes = orderdes;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }

        public String getWX_partnerId() {
            return wx_partnerId;
        }

        public void setWX_partnerId(String WX_partnerId) {
            this.wx_partnerId = WX_partnerId;
        }

        public String getWX_prepayId() {
            return wx_prepayId;
        }

        public void setWX_prepayId(String WX_prepayId) {
            this.wx_prepayId = WX_prepayId;
        }

        public String getWX_nonceStr() {
            return wx_nonceStr;
        }

        public void setWX_nonceStr(String WX_nonceStr) {
            this.wx_nonceStr = WX_nonceStr;
        }
        public String getWX_timeStamp() {
            return wx_timeStamp;
        }
        public void setWX_timeStamp(String WX_timeStamp) {
            this.wx_timeStamp = WX_timeStamp;
        }
        public String getWX_package() {
            return wx_package;
        }
        public void setWX_package(String WX_package) {
            this.wx_package = WX_package;
        }
        public String getWX_sign() {
            return wx_sign;
        }
        public void setWX_sign(String WX_sign) {
            this.wx_sign = WX_sign;
        }
        public String getOrderno() {
            return orderno;
        }
        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }
    }
}
