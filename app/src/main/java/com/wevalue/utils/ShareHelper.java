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

    public void initShare(String sharePlatform, String url, String message) {
        String title = message;
        String content = message;
        if (message.contains("|#|")) {
            int cutting  = message.lastIndexOf("|#|");
            title = message.substring(0,cutting);
            content = message.substring(cutting+3,message.length());
        }

        switch (sharePlatform) {
            case Constants.shareSina: //分享到微博
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText(title);
                sp.setTitle(content);
                sp.setUrl(url);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.authorize();
                weibo.setPlatformActionListener(new MyActionListener()); // 设置分享事件回调
                weibo.share(sp);
                break;
            case Constants.shareWeixinMoment: //分享到微信朋友圈
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform wx = ShareSDK.getPlatform(context.getApplicationContext(), WechatMoments.NAME);
//                wx.authorize();
                wx.setPlatformActionListener(new MyActionListener());
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
                qzone.setPlatformActionListener(new MyActionListener()); // 设置分享事件回调
                qzone.share(qzonesp);
                break;
            case Constants.shareWeixinFriend://分享到微信朋友
                if (intentFilter == null) {
                    initReceiver();
                }
                Platform weixin = ShareSDK.getPlatform(context.getApplicationContext(), Wechat.NAME);
                weixin.setPlatformActionListener(new MyActionListener());
                Wechat.ShareParams sp_wx_friend = new Wechat.ShareParams();
                sp_wx_friend.setShareType(Platform.SHARE_WEBPAGE);
                sp_wx_friend.setUrl(url);
                sp_wx_friend.setText(title);
                sp_wx_friend.setTitle(content);
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
