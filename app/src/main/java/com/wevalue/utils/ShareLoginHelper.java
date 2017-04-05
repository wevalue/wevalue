package com.wevalue.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * 作者：邹永奎
 * 创建时间：2016/11/11
 * 类说明：分享的助手类
 */

public class ShareLoginHelper {
    Context context;
    private Handler mHan;

    public ShareLoginHelper(Context context, Handler mHan) {
        this.context = context;
        this.mHan = mHan;
        ShareSDK.initSDK(context);
    }

    public void loginQQ() {
        Platform qqPlatform = ShareSDK.getPlatform(context, QQ.NAME);
        if (qqPlatform.isAuthValid())qqPlatform.removeAccount(true);
        qqPlatform.setPlatformActionListener(platformActionListener);//
        qqPlatform.SSOSetting(false);//此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
        qqPlatform.showUser(null);//获得用户数据
        //qqPlatform.authorize();
    }

    public void loginWx() {
        Platform qqPlatform = ShareSDK.getPlatform(context, Wechat.NAME);
        if (qqPlatform.isAuthValid())qqPlatform.removeAccount(true);
        qqPlatform.setPlatformActionListener(platformActionListener);
        qqPlatform.showUser(null);
        //qqPlatform.authorize();
    }
    /**
     * 回调事件
     **/
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            arg2.printStackTrace();
            LogUtils.e("log", "---onError---Throwable--" + arg2.getMessage().toString());
            Message message = new Message();
            message.what = 2;
            message.obj = arg0;
            mHan.sendMessage(message);
            //ShowUtil.showToast(context, "失败");
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            LogUtils.e("log", "---onComplete-----");
            Message message = new Message();
            message.what = 1;
            message.obj = arg0;
            mHan.sendMessage(message);
            //ShowUtil.showToast(context, "成功");
        }

        @Override
        public void onCancel(Platform arg0, int arg1) {
            LogUtils.e("log", "---onCancel-----");
            Message message = new Message();
            message.what = 3;
            message.obj = arg0;
            mHan.sendMessage(message);
            // ShowUtil.showToast(context, "取消");
        }
    };



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
