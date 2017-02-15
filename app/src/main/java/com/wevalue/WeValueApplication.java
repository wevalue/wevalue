package com.wevalue;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.model.Area;
import com.wevalue.model.City;
import com.wevalue.model.Province;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.InstallIdUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getDeviceInfo(this);
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
//            getHotCity();
//            getcityAllData();
        } else {
            dierchi = System.currentTimeMillis();
            if (dierchi - shangyichi > 86400 * 1000) {
                SharedPreferencesUtil.setGetAddrTime(this, dierchi);
//                getcityAllData();
//                getHotCity();
//                initData();
            }
        }
        //存储唯一安装标识码
        InstallIdUtil idUtil = new InstallIdUtil(getApplicationContext());
        SharedPreferencesUtil.setDeviceid(getApplicationContext(), idUtil.getsID());
    }

    public static WeValueApplication getInstance() {
        return instance;
    }

    /**
     * 获取热门城市
     */
    private void getHotCity() {
        String url = RequestPath.GET_GETHOTCITY;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(url, map, this);
    }

    private void initCityData() {
        dbUtils = DbUtils.create(applicationContext, "WeValue.db");
        dierchi = System.currentTimeMillis();
        SharedPreferencesUtil.setGetAddrTime(this, dierchi);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(10);
                    dbUtils.createTableIfNotExist(Province.class);
                    Thread.sleep(10);
                    dbUtils.createTableIfNotExist(City.class);
                    Thread.sleep(10);
                    dbUtils.createTableIfNotExist(Area.class);
                    Thread.sleep(10);
                    InitDataBaseTask initDataBaseTask = new InitDataBaseTask();
                    initDataBaseTask.execute();
                } catch (DbException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Title: InitDataBaseTask<br>
     * Description: TODO 异步加载数据库<br>
     * Depend : TODO
     *
     * @since JDK 1.7
     */
    class InitDataBaseTask extends AsyncTask<Void, Integer, String> {

        private ArrayList<Province> provinceList;

        @Override
        protected void onPreExecute() {

            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            // TODO Auto-generated method stub
            provinceList = new ArrayList<Province>();
            try {
                provinceList = (ArrayList) dbUtils.findAll(Selector.from(Province.class));
                if (!(provinceList.size() > 0)) {
                    initDataBase();
                    publishProgress();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            return "执行完毕";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                LogUtils.e("LauncherActivity.log", "数据库---" + result);

            }
        }

    }

    /**
     * Title: initDataBase<br>
     * Description: TODO 初始化关二级联动的数据库<br>
     * Depend : TODO <br>
     *
     * @since JDK 1.7
     */
    private void initDataBase() {

        // TODO Auto-generated method stub

        // 省
        try {
            if (dbUtils.tableIsExist(Province.class)) {
                LogUtils.i("tag", dbUtils.tableIsExist(Province.class));
                dbUtils.createTableIfNotExist(Province.class);
                dbUtils.deleteAll(Province.class);
                LogUtils.e("tag", "省 创建成功！");
                List<Province> provinceList = dbUtils.findAll(Province.class);
                LogUtils.e("tag", "provinceList长度" + provinceList.size());
                if (provinceList.size() == 0) {
                    getProvince(1);
                }
                LogUtils.e("tag", "provinceList长度" + provinceList.size());
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 市
        try {
            if (dbUtils.tableIsExist(City.class)) {
                LogUtils.i("tag", dbUtils.tableIsExist(City.class));
                dbUtils.createTableIfNotExist(City.class);
                LogUtils.e("tag", "市  创建成功！");
                dbUtils.deleteAll(City.class);
                List<City> cityList = dbUtils.findAll(City.class);
                LogUtils.e("tag", "City长度" + cityList.size());
                if (cityList.size() == 0) {
                    getProvince(2);
                }
                LogUtils.e("tag", "City长度" + cityList.size());
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 县区
        try {
            if (dbUtils.tableIsExist(Area.class)) {
                LogUtils.i("tag", dbUtils.tableIsExist(Area.class));
                dbUtils.createTableIfNotExist(Area.class);
                LogUtils.e("tag", "区  创建成功！");
                dbUtils.deleteAll(Area.class);
                List<Area> AreaList = dbUtils.findAll(Area.class);
                LogUtils.e("tag", "Area长度" + AreaList.size());
                if (AreaList.size() == 0) {
                    getProvince(3);
                }
                LogUtils.e("tag", "Area长度" + AreaList.size());
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void getProvince(int who) {
        LogUtils.e("-------who = " + who);
        try {
            JSONObject obj = new JSONObject(content);
            String result = obj.getString("result");
            if (result.equals("1")) {
                JSONArray data = obj.getJSONArray("data");
                Province province;
                City city;
                Area area;
                List<City> cityList;
                List<Area> areaList;
                if (data != null && data.length() > 0) {//获取省信息
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject pObj = data.getJSONObject(i);
                        String pId = pObj.getString("provinceid");
                        String pName = pObj.getString("provincename");
                        JSONArray cityArray = pObj.getJSONArray("list");
                        cityList = new ArrayList<>();
                        if (cityArray != null && cityArray.length() > 0) {//获取省中的市
                            for (int c = 0; c < cityArray.length(); c++) {
                                JSONObject cObj = cityArray.getJSONObject(c);
                                String cId = cObj.getString("cityid");
                                String cName = cObj.getString("cityname");
                                JSONArray areaArray = cObj.getJSONArray("list");
                                areaList = new ArrayList<>();
                                if (areaArray != null && areaArray.length() > 0) {//获取市中的区.县信息
                                    for (int a = 0; a < areaArray.length(); a++) {
                                        JSONObject aObj = areaArray.getJSONObject(a);
                                        String aId = aObj.getString("districtid");
                                        String aName = aObj.getString("districtname");
                                        area = new Area(aId, aName, pId, cId);
                                        if (who == 3) {
//											LogUtils.e("if(who==3) "+who);
                                            dbUtils.save(area);
                                        }
                                        areaList.add(area);
                                    }
                                }
                                city = new City(cId, cName, areaList, pId);
                                if (who == 2) {
//									LogUtils.e("if(who==2) "+who);
                                    dbUtils.save(city);
                                }
                                cityList.add(city);
                            }
                        }
                        province = new Province(pId, pName, cityList);
                        if (who == 1) {
//							LogUtils.e("if(who==1) "+who);
                            dbUtils.save(province);
                        }
                    }
                }
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void getcityAllData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(RequestPath.GET_GETALLCITY, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        LogUtils.e("获取全部省市content = " + content);
        switch (isUrl) {
            case RequestPath.GET_GETALLCITY:
                this.content = content;
                SharedPreferencesUtil.setAllCity(applicationContext, content);
                initCityData();
                break;
            case RequestPath.GET_GETNOTETYPE:
//                this.content =content;
//                initCityData();
//                SharedPreferencesUtil.setAllChannel(applicationContext,content);
                break;
            case RequestPath.GET_GETHOTCITY:
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        SharedPreferencesUtil.setHotCity(applicationContext, content);
                        LogUtils.e("content = " + SharedPreferencesUtil.getHotCity(applicationContext));
                    } else {
                        ShowUtil.showToast(applicationContext, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("获取---app-- 错误--" + content);
    }

    /**
     * 初始化数据
     */
//    private void initData() {
//        Map<String, String> map = news HashMap<>();
//        map.put("code", RequestPath.CODE);
//        NetworkRequest.getRequest(RequestPath.GET_GETNOTETYPE, map, this);
//    }
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
