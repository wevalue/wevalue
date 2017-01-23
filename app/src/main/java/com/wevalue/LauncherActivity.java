package com.wevalue;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

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
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;

public class LauncherActivity extends BaseActivity implements WZHttpListener {

    private DbUtils dbUtils;
    private Context mContext;
    private String content;
    private Long shangyichi;
    private Long dierchi;
    ImageView imageView;
    long currentSeconnd;
    private Realm realm;
    private static final int LUNCHTIMER = 897;
    private static final int LUNCHACTIVITY = 569;
    private boolean isAllCityOk = false, isHotCityOk = false, isGetTypeOk = false, isGetNoteOk = true;
    private String jpush;  //极光推送标签

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LUNCHTIMER:
                    launchTimerTask();
                    break;
                case LUNCHACTIVITY:
                    if (TextUtils.isEmpty(SharedPreferencesUtil.getIsFristStart(LauncherActivity.this))) {
                        Intent intent = new Intent(LauncherActivity.this, GuideActivity.class);
                        startActivity(intent);
                        finish();
                        SharedPreferencesUtil.setIsFristStart(LauncherActivity.this, "1");
                    } else {
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        intent.putExtra("jpush", jpush);
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
        realm = Realm.getDefaultInstance();
        currentSeconnd = System.currentTimeMillis();
        imageView = (ImageView) findViewById(R.id.iv_img);
        getAllChanel();
        mContext = getApplicationContext();
        WeValueApplication.phoneName = android.os.Build.BRAND;
        LogUtils.e("手机品牌--=" + WeValueApplication.phoneName);
        shangyichi = SharedPreferencesUtil.getGetAddrTime(this);
        if (shangyichi == -1) {
            shangyichi = System.currentTimeMillis();
            getHotCity();
            getcityAllData();
        } else {
            dierchi = System.currentTimeMillis();
            if (dierchi - shangyichi > 86400 * 1000) {
                SharedPreferencesUtil.setGetAddrTime(this, dierchi);
                getcityAllData();
                getHotCity();
            } else {
                isHotCityOk = true;
                isAllCityOk = true;
            }
        }

        handler.sendEmptyMessage(LUNCHTIMER);
        getUserInfoData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
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
        dbUtils = DbUtils.create(mContext, "WeValue.db");
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

            //启动计时任务
            isAllCityOk = true;
//            if (isAllCityOk && isGetNoteOk && isGetTypeOk && isHotCityOk) {

//            }
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

        switch (isUrl) {
            case RequestPath.GET_GETALLCITY:
//                SharedPreferencesUtil.setAllCity(this, content);
                this.content = content;
                initCityData();
                break;
            case RequestPath.GET_GETHOTCITY:
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        SharedPreferencesUtil.setHotCity(LauncherActivity.this, content);
                        isHotCityOk = true;
//                        if (isAllCityOk && isGetNoteOk && isGetTypeOk && isHotCityOk) {
//                            handler.sendEmptyMessage(LUNCHTIMER);
//                        }
                        LogUtils.e("content = " + SharedPreferencesUtil.getHotCity(LauncherActivity.this));
                    } else {
                        ShowUtil.showToast(LauncherActivity.this, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //把所有的频道信息保存到本地
            case RequestPath.GET_GETNOTETYPE:
                LogUtils.e("alluserlike", content);
                String userLikeCity = SharedPreferencesUtil.getUserLikeCity(LauncherActivity.this);
                if (!TextUtils.isEmpty(userLikeCity)) {
                    //如果用户选择了喜好的城市 则把频道信息中的城市名字替换为喜好的城市名
                    content = content.replaceAll("地区", userLikeCity);
                }
                SharedPreferencesUtil.setAllChannel(this, content);
//                Gson gson = news Gson();
//                ChannelBean channelBean = gson.fromJson(content, ChannelBean.class);
//                LogUtils.e("realm", channelBean.getData().get(1).getId());
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("result").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        saveTypeToRealm(jsonArray);
                        isGetTypeOk = true;
//                        if (isAllCityOk && isGetNoteOk && isGetTypeOk && isHotCityOk) {
//                            handler.sendEmptyMessage(LUNCHTIMER);
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            case RequestPath.GET_GETNOTE:
//                SharedPreferencesUtil.setContent(this, "tuijian", content);
//                isGetNoteOk = true;
//                if (isAllCityOk && isGetNoteOk && isGetTypeOk && isHotCityOk) {
//                    handler.sendEmptyMessage(LUNCHTIMER);
//                }
//                break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("alluserlikeerror" + content);
    }

    private void getAllChanel() {
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(RequestPath.GET_GETNOTETYPE, map, this);
    }

    /**
     * Title: launchTimerTask<br>
     * Description: 启动页定时器<br>
     * Depend : TODO <br>
     *
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

    public void launchTimerTask() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(LUNCHACTIVITY);
            }
        };
        if (System.currentTimeMillis() - currentSeconnd < 2450) {
            timer.schedule(timerTask, 2500 - (System.currentTimeMillis() - currentSeconnd));
        } else {
            handler.sendEmptyMessage(LUNCHACTIVITY);
        }
    }

    /*获取最新token*/
    private void getUserInfoData() {
        String uid = SharedPreferencesUtil.getUid(null);
        if (TextUtils.isEmpty(uid)) {
            return;
        }
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
            }

            @Override
            public void onFailure(String content) {
                getUserInfoData();
//                ShowUtil.showToast(LauncherActivity.this, "网络开小差了，请检查网络稍后再试...");
            }
        });
    }

}
