package com.wevalue;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.InstallIdUtil;
import com.wevalue.utils.SharedPreferencesUtil;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Administrator on 2016-07-28.
 */
public class WeValueApplication extends Application implements WZHttpListener {
    public static Context applicationContext;
    public static String phoneName;
    private static WeValueApplication instance;
    private long shangyichi;
    private long dierchi;
    private DbUtils dbUtils;
    private String content;
    NetworkRequest okhttpRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        okhttpRequest = new NetworkRequest();
        //设置友盟统计场景
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        //getDeviceInfo(this);
        //捕获异常 用友盟发送
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        crashHandler.sendCrashReportsToServer(this);

        /*初始化relam数据库*/
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        /*初始化sharesdk*/
        ShareSDK.initSDK(this);

        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

        instance = this;
        phoneName = android.os.Build.MODEL;

        if (TextUtils.isEmpty(SharedPreferencesUtil.getIsFristStart(this))) {
            SharedPreferencesUtil.setUserlike(this, "推荐,视频,地区");
            SharedPreferencesUtil.setLastCity(this, "地区");
        }
        shangyichi = SharedPreferencesUtil.getGetAddrTime(this);
        if (shangyichi == -1) {
            shangyichi = System.currentTimeMillis();
        } else {
            dierchi = System.currentTimeMillis();
            if (dierchi - shangyichi > 86400 * 1000) {
                SharedPreferencesUtil.setGetAddrTime(this, dierchi);
            }
        }
        //存储唯一安装标识码
        InstallIdUtil idUtil = new InstallIdUtil(getApplicationContext());
        SharedPreferencesUtil.setDeviceid(getApplicationContext(), idUtil.getsID());
    }

    @Override
    public void onSuccess(String content, String isUrl) {
    }
    @Override
    public void onFailure(String content) {
    }
}
