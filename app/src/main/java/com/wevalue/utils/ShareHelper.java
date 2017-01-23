package com.wevalue.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 作者：邹永奎
 * 创建时间：2016/11/11
 * 类说明：分享的助手类
 */

public class ShareHelper {
    Context context;
    private Handler mHan;

    public ShareHelper(Context context, Handler mHan) {
        this.context = context;
        this.mHan = mHan;
    }

    public void initShare(String sharePlatform, String url) {
        switch (sharePlatform) {
            case Constants.shareSina:
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText("微值价值分享");
//                sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
//                sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
//                sp.setTitleUrl(url); // 标题的超链接
                sp.setUrl(url);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.authorize();
                weibo.setPlatformActionListener(new MyActionListener()); // 设置分享事件回调
                weibo.share(sp);
//                        Platform.ShareParams sp_sina;
//                        sp_sina = news Platform.ShareParams();
//                        sp_sina.setTitle("微值");
//                        sp_sina.setText("微值" + "\"" + RequestPath.SERVER_PATH + "/web/myfly.aspx?flyjson=\"\""/*+setFlyInfo()+"\""*/);
//                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                        weibo.setPlatformActionListener(news MyActionListener()); // 设置分享事件回调
//                        // 执行图文分享
//                        weibo.share(sp_sina);

                break;
            case Constants.shareWeixinMoment:
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform wx = ShareSDK.getPlatform(context.getApplicationContext(), WechatMoments.NAME);
//                wx.authorize();
                wx.setPlatformActionListener(new MyActionListener());
                WechatMoments.ShareParams sp_weixin = new WechatMoments.ShareParams();
                sp_weixin.setShareType(Platform.SHARE_WEBPAGE);
                sp_weixin.setUrl(url);
//                sp_weixin.setText("微值价值分享");
                sp_weixin.setTitle("微值价值分享");
                wx.share(sp_weixin);
                break;
            case Constants.shareQzone:
                QZone.ShareParams qzonesp = new QZone.ShareParams();
                qzonesp.setTitle("微值价值分享");
                qzonesp.setTitleUrl(url); // 标题的超链接
//                qzonesp.setText("微值价值分享");
//                qzonesp.setImageUrl("http://www.someserver.com/测试图片网络地址.jpg");
                qzonesp.setSite("陕西微值辩证科技有限公司");
//                qzonesp.setSiteUrl("发布分享网站的地址");
                qzonesp.setSiteUrl(url);

                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//                qzone.setPlatformActionListener(news MyActionListener());
//                qzone.authorize();
                qzone.setPlatformActionListener(new MyActionListener()); // 设置分享事件回调
                qzone.share(qzonesp);
                break;
            case Constants.shareWeixinFriend:
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform weixin = ShareSDK.getPlatform(context.getApplicationContext(), Wechat.NAME);
                weixin.setPlatformActionListener(new MyActionListener());
//                weixin.authorize();
                Wechat.ShareParams sp_wx_friend = new Wechat.ShareParams();
                sp_wx_friend.setShareType(Platform.SHARE_WEBPAGE);
                sp_wx_friend.setUrl(url);
//                sp_wx_friend.setText("微值价值分享");
                sp_wx_friend.setTitle("微值价值分享");
//                sp_1.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
                weixin.share(sp_wx_friend);
                break;
        }
    }

    /**
     * 分享回调事件
     **/
    public class MyActionListener implements PlatformActionListener {
        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            LogUtils.e("log", "---onError---Throwable--" + arg2.getMessage().toString());
            mHan.sendEmptyMessage(2);
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            LogUtils.e("log", "---onComplete-----");
            mHan.sendEmptyMessage(1);
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            LogUtils.e("log", "---onCancel-----");
            mHan.sendEmptyMessage(3);
        }
    }

    //定义一个过滤器；
    private IntentFilter intentFilter;
    //定义一个广播监听器；
    private ShareResultReceiver shareResultReceiver;

    private void initReceiver() {
        //实例化过滤器；
        intentFilter = new IntentFilter();
        //添加过滤的Action值；
        intentFilter.addAction("com.wevalue.wxshare");
        //实例化广播监听器；
        shareResultReceiver = new ShareResultReceiver();
        //将广播监听器和过滤器注册在一起；
        context.registerReceiver(shareResultReceiver, intentFilter);
    }

    /*声明微信分享成功的广播界接收者*/
    public class ShareResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String extInfo = intent.getStringExtra("extInfo");
            if (!TextUtils.isEmpty(extInfo)) {
                switch (extInfo) {
                    case "分享成功！":
                        mHan.sendEmptyMessage(1);
                        break;
                    case "取消分享!":
                        mHan.sendEmptyMessage(3);
                        break;
                    default:
                        mHan.sendEmptyMessage(2);
                        break;
                }
            }
        }
    }
}
