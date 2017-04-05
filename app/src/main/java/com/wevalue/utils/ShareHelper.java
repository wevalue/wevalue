package com.wevalue.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
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
    MyActionListener myActionListener ;
    public ShareHelper(Context context, Handler mHan) {
        this.context = context;
        this.mHan = mHan;
        myActionListener = new MyActionListener();
    }

    public void initShare(String sharePlatform, String url, String message) {
        String title = message;
        String content = message;
        if (message.contains("|#|")) {
            int cutting = message.lastIndexOf("|#|");
            content = message.substring(0, cutting);
            title = message.substring(cutting + 3, message.length());
        }

        switch (sharePlatform) {
            case Constants.shareSina: //分享到微博
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText(content + "\r\n\t"
                        + title + "\n\t" + url);
                sp.setUrl(url);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.authorize();
                weibo.setPlatformActionListener(myActionListener); // 设置分享事件回调
                weibo.share(sp);
                break;
            case Constants.shareWeixinMoment: //分享到微信朋友圈
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform wx = ShareSDK.getPlatform(context.getApplicationContext(), WechatMoments.NAME);
//                wx.authorize();
                wx.setPlatformActionListener(myActionListener);
                WechatMoments.ShareParams sp_weixin = new WechatMoments.ShareParams();
                sp_weixin.setShareType(Platform.SHARE_WEBPAGE);
                sp_weixin.setUrl(url);
                sp_weixin.setText(title);
                sp_weixin.setTitle(content);
                wx.share(sp_weixin);
                break;
            case Constants.shareQzone://分享qq空间
                QZone.ShareParams qzonesp = new QZone.ShareParams();
                qzonesp.setText(title);
                qzonesp.setTitle(content);
                qzonesp.setTitleUrl(url); // 标题的超链接
                qzonesp.setSite("陕西微值辩证科技有限公司");
                qzonesp.setSiteUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(myActionListener); // 设置分享事件回调
                qzone.share(qzonesp);
                break;
            case Constants.shareWeixinFriend://分享到微信朋友
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform weixin = ShareSDK.getPlatform(context.getApplicationContext(), Wechat.NAME);
                weixin.setPlatformActionListener(myActionListener);
                Wechat.ShareParams sp_wx_friend = new Wechat.ShareParams();
                sp_wx_friend.setShareType(Platform.SHARE_WEBPAGE);
                sp_wx_friend.setUrl(url);
                sp_wx_friend.setText(title);
                sp_wx_friend.setTitle(content);
                weixin.share(sp_wx_friend);
                break;
        }
    }

    public void initShare(String sharePlatform, HashMap<String, String> map) {
        String url = map.get("url");
        String title = map.get("title");
        String content = map.get("content");
        String imgUrl = map.get("imgUrl");
        switch (sharePlatform) {
            case Constants.shareSina: //分享到微博
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText(content + "\r\n\t"
                        + title + "\n\t" + url);
                //sp.setUrl(url);
                //sp.setImageUrl(imgUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.authorize();
                weibo.setPlatformActionListener(myActionListener); // 设置分享事件回调
                weibo.share(sp);
                break;
            case Constants.shareWeixinMoment: //分享到微信朋友圈
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform wx = ShareSDK.getPlatform(context.getApplicationContext(), WechatMoments.NAME);
                wx.setPlatformActionListener(myActionListener);
                WechatMoments.ShareParams sp_weixin = new WechatMoments.ShareParams();
                sp_weixin.setShareType(Platform.SHARE_WEBPAGE);
                sp_weixin.setUrl(url);
                sp_weixin.setText(content);
                sp_weixin.setTitle(title);
                sp_weixin.setImageUrl(imgUrl);
                wx.share(sp_weixin);
                break;
            case Constants.shareQzone://分享qq空间
                QZone.ShareParams qzonesp = new QZone.ShareParams();
                qzonesp.setText(content);
                qzonesp.setTitle(title);
                qzonesp.setTitleUrl(url); // 标题的超链接
                qzonesp.setSite("陕西微值辩证科技有限公司");
                qzonesp.setSiteUrl(url);
                qzonesp.setImageUrl(imgUrl);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(myActionListener); // 设置分享事件回调
                qzone.share(qzonesp);
                break;
            case Constants.shareWeixinFriend://分享到微信朋友
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform weixin = ShareSDK.getPlatform(context.getApplicationContext(), Wechat.NAME);
                weixin.setPlatformActionListener(myActionListener);
                Wechat.ShareParams sp_wx_friend = new Wechat.ShareParams();
                sp_wx_friend.setShareType(Platform.SHARE_WEBPAGE);
                sp_wx_friend.setUrl(url);
                sp_wx_friend.setText(content);
                sp_wx_friend.setTitle(title);
                 sp_wx_friend.setImageUrl(imgUrl);
                weixin.share(sp_wx_friend);
                break;
        }
    }

    public static int contentLength = 23;
    public static int titleLength = 23; //默认为99 则不限制标题

    public static String getContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return "免费看收费的内容";
        } else {
            if (content.length() > contentLength)
                content = content.substring(0, contentLength) + "...";
            return content;
        }
    }

    public static String getTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return "微值—价值分享";
        } else {
            if (title.length() > titleLength)
                title = title.substring(0, titleLength) + "...";
            return title;
        }
    }

    /**
     * 分享回调事件
     **/
    public class MyActionListener implements PlatformActionListener {
        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            arg2.printStackTrace();
            LogUtils.e("log", "---onError---Throwable--" + arg2.getMessage().toString());
            mHan.sendEmptyMessage(2);
            //ShowUtil.showToast(context, "分享失败");
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            LogUtils.e("log", "---onComplete-----");
            mHan.sendEmptyMessage(1);
            //ShowUtil.showToast(context, "分享成功");
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            LogUtils.e("log", "---onCancel-----");
            mHan.sendEmptyMessage(3);
            // ShowUtil.showToast(context, "取消分享");
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
