package com.wevalue.utils;

public class Constants {
    // 微信的appid
    public static final String APP_ID = "wxeb128aba017ef3b9";
    public static final String APP_SECRET = "f0909bea96024116bff551643ce54c0e";

    /**
     * 支付行为 打赏 转发 充值 提现.....
     **/
    public static final String charge = "1",//充值
            withdraw = "2",//提现
            transmit = "3",//转发
            dasahng = "4",//打赏
            buypermission = "5",//提升权限
            release = "6", //发布
            share = "7",//分享
            zan = "8";//点赞

    /**
     * 支付方式 碎银 支付宝 微信
     **/
    public static final String suiyinpay = "1", alipay = "2", weixinpay = "3";

    /**
     * 分享
     **/
    public static final String shareWeixinFriend = "1", shareWeixinMoment = "2", shareQzone = "3", shareSina = "4";

    /**
     * 提现方式 支付宝 微信
     **/
    public static final String wxwithdraw = "2", aliwithdraw = "1";


}
