package com.wevalue;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.Area;
import com.wevalue.model.Channels;
import com.wevalue.model.City;
import com.wevalue.model.Province;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.GuideActivity;
import com.wevalue.utils.ImageUitls;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

public class LauncherActivity extends BaseActivity implements WZHttpListener {

    private DbUtils dbUtils;
    private Context mContext;
    private String content;
    ImageView imageView;
    private Realm realm;
    private static final int LUNCHACTIVITY = 569;
    private boolean isAllCityOk = false, isHotCityOk = false, isGetTypeOk = false, isGetNoteOk = true;
    private String jpush;  //极光推送标签

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LUNCHACTIVITY:
                    //如果isFristStart = “1”  则不是第一次
                    String isFristStart = SharedPreferencesUtil.getIsFristStart(LauncherActivity.this);
                    if (!TextUtils.isEmpty(isFristStart)&&isFristStart.equals("2")) {
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        intent.putExtra("jpush", jpush);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LauncherActivity.this, GuideActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jpush = getIntent().getStringExtra("jpush");
        //不显示程序的标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //不显示系统的标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        imageView = (ImageView) findViewById(R.id.iv_img);

        realm = Realm.getDefaultInstance();
        mContext = getApplicationContext();
        WeValueApplication.phoneName = android.os.Build.BRAND;
        LogUtils.e("手机品牌--=" + WeValueApplication.phoneName);
        lanucherImg();
        getAllChanel();
        getUserInfoData();
        handler.sendEmptyMessageAtTime(LUNCHACTIVITY, SystemClock.uptimeMillis()+4000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    /* 获取启动页图片*/
    private  void lanucherImg(){
        HashMap map = new HashMap();
        map.put("code","weizhi");
        NetworkRequest.postRequest(RequestPath.POST_LANYCHER_IMAGE, map, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    @Override
    public void onSuccess(String content, String isUrl) {

        switch (isUrl) {
            //把所有的频道信息保存到本地
            case RequestPath.GET_GETNOTETYPE:
                try {
                    SharedPreferencesUtil.setAllChannel(this, content);
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("result").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        saveTypeToRealm(jsonArray);
                        isGetTypeOk = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_LANYCHER_IMAGE: //获取启动页图片
                showImg(content);
               break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("alluserlikeerror" + content);
    }

    /**
     * 显示启动页图片
     * @param content
     */
    private void showImg(String content){
        try{
            LogUtils.e("IMAGE_URL",content);
            JSONObject jsonObject = new JSONObject(content);
            String result = jsonObject.getString("result");
            if ("1".equals(result)){
                JSONObject data = jsonObject.getJSONObject("data");
                String url = data.getString("uipath");
                ImageUitls.setImg(url,imageView,R.mipmap.iv_launcher);
            }else {
                ShowUtil.showToast(this,jsonObject.getString("message"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取频道
     */
    private void getAllChanel() {
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(RequestPath.GET_GETNOTETYPE, map, this);
    }

    /**
     * 保存频道数据
     * @Modified by
     * @Version
     * @since JDK 1.7
     */
    private void saveTypeToRealm(final JSONArray jsonarray) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Channels.class, jsonarray);
            }
        });
    }

    /*获取最新token*/
    private void getUserInfoData() {
        String uid = SharedPreferencesUtil.getUid(null);
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        handler.removeCallbacksAndMessages(null);// 移除上一个handler消息
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", uid);
        map.put("logintoken", SharedPreferencesUtil.getUserToken(null));
        LogUtils.e("token!!!", SharedPreferencesUtil.getUserToken(null));
        NetworkRequest.postRequest(RequestPath.POST_SETTOKENLONGTIME, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                //1.用户状态正常 3.用户禁用 4.用户删除 5 登录过期
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("3") || obj.getString("result").equals("4")) {
                        SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                    } else if (obj.getString("result").equals("5")) {
                        SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                    } else if (obj.getString("result").equals("1")) {
                        SharedPreferencesUtil.setUserToken(LauncherActivity.this, obj.optString("data"));
                        LogUtils.e("token!!!", obj.optString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //获取用户信息成功后再去 跳转
                handler.sendEmptyMessageAtTime(LUNCHACTIVITY, SystemClock.uptimeMillis()+3500);
            }

            @Override
            public void onFailure(String content) {
                //获取用户信息成功后再去 跳转
                handler.sendEmptyMessageAtTime(LUNCHACTIVITY, SystemClock.uptimeMillis()+3500);
                ShowUtil.showToast(LauncherActivity.this, "登录已过期");
            }
        });
    }

}
