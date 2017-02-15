package com.wevalue;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.influence.InfluenceFragment;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.mine.activity.SettingActivity;
import com.wevalue.ui.mine.fragment.PersonInfoFragment;
import com.wevalue.ui.release.ReleaseNoteActivity;
import com.wevalue.ui.we.fragment.WeFragment;
import com.wevalue.ui.world.activity.AudioActivity;
import com.wevalue.ui.world.activity.GetvAudioAndVideoActivity;
import com.wevalue.ui.world.activity.NewRecordVideoActivity;
import com.wevalue.ui.world.activity.PicChoiceActivity;
import com.wevalue.ui.world.activity.SearchActivity;
import com.wevalue.ui.world.fragment.WorldFragment;
import com.wevalue.utils.ActivityManagers;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PermissionsChecker;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.UpdateManager;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.LazyViewPager;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import jupush.ExampleUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private String pushTag;            //极光推送的标志
    private ActivityManagers activityManager = ActivityManagers.getActivityManager();

    private RadioButton main_home;
    private RadioButton main_pengyouquan;
    private RadioButton main_jiaoyou;
    private RadioButton main_wode;
    private RadioButton ll_add_sendnote;
    private ArrayList<Fragment> fragmentList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LazyViewPager lvp_main;
    private long mExitTime = 0;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private int isClickType = 0; //1 = 图片 , 2 = 音频; 3 = 视频;
    private static final String PHOTO_FILE_NAME = "wevalue_img.jpg";
    private Long suijishu;
    private RelativeLayout re_head_title;
    public ImageView iv_head_search, iv_head_add;
    public TextView tv_head_right_text;
    public TextView tv_head_title;
    PersonInfoFragment personInfoFragment;
    WorldFragment worldFragment;
    public static boolean isEditChannel = false;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<>();
    private int width;
    private int height;
    private String localVersion;
    private String newVersion;
    private TextView tv_red_cycle;
    private InfluenceFragment influenceFragment;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    private MessageReceiver mMessageReceiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getUserInfoData();//获取用户状态
        /**获取频幕宽高*/
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        SharedPreferencesUtil.setPictureProportionHeight(this, height + "");
        initView();
        mPermissionsChecker = new PermissionsChecker(this);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();


//        pushTag = getIntent().getStringExtra("jpush");
//        if ("jpush".equals(pushTag)) {
//            setTv_Cycle_Visible(true);
//            main_jiaoyou.performClick();
//        }
        handleJpushMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        int index = lvp_main.getCurrentItem();
        LogUtils.e("main onResume  index=" + index);
        switch (index) {
            case 0:
                setViewIsShow(true, index);
                break;
            case 1:
                setViewIsShow(true, index);
                break;
            case 2:
                setViewIsShow(true, index);
                break;
            case 3:
                setViewIsShow(false, index);
                break;
        }
    }

    /**
     * Title:  initView<br>
     * Description: 初始化控件  Action<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private void initView() {
        tv_red_cycle = (TextView) findViewById(R.id.tv_red_cycle);
        main_home = (RadioButton) findViewById(R.id.main_home);
        main_pengyouquan = (RadioButton) findViewById(R.id.main_pengyouquan);
        main_jiaoyou = (RadioButton) findViewById(R.id.main_jiaoyou);
        main_wode = (RadioButton) findViewById(R.id.main_wode);
        ll_add_sendnote = (RadioButton) findViewById(R.id.ll_add_sendnote);
        main_home.setOnClickListener(this);
        main_pengyouquan.setOnClickListener(this);
        main_jiaoyou.setOnClickListener(this);
        main_wode.setOnClickListener(this);
        lvp_main = (LazyViewPager) findViewById(R.id.lvp_main);
        fragmentList = new ArrayList<Fragment>();
        influenceFragment = new InfluenceFragment();
        WeFragment friendsFragment = new WeFragment();
        worldFragment = new WorldFragment();
        personInfoFragment = new PersonInfoFragment();

        fragmentList.add(worldFragment);
        fragmentList.add(influenceFragment);
        fragmentList.add(friendsFragment);
        fragmentList.add(personInfoFragment);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        lvp_main.setAdapter(myViewPagerAdapter);
        lvp_main.setScrollble(false);
        lvp_main.setCurrentItem(0);

        re_head_title = (RelativeLayout) findViewById(R.id.re_head_title);
        iv_head_search = (ImageView) findViewById(R.id.iv_head_search);
        iv_head_add = (ImageView) findViewById(R.id.iv_head_add);
        tv_head_right_text = (TextView) findViewById(R.id.tv_head_right_text);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);

        ll_add_sendnote.setOnClickListener(this);
        iv_head_search.setOnClickListener(this);
        iv_head_add.setOnClickListener(this);
        tv_head_right_text.setOnClickListener(this);
        lvp_main.setOffscreenPageLimit(4);
        registerMessageReceiver();
        if (!ExampleUtil.isConnected(this)) {
            ShowUtil.showToast(this, "当前设备无网络，请您检查网络设置。");
        }
        jianCeBanBen();
        InitAppVersion();
    }

    public void setViewIsShow(boolean isShow, int index) {
        if (isShow) {
            iv_head_search.setVisibility(View.VISIBLE);
            iv_head_add.setVisibility(View.VISIBLE);
            tv_head_right_text.setVisibility(View.GONE);
        } else {
            iv_head_search.setVisibility(View.GONE);
            iv_head_add.setVisibility(View.GONE);
            tv_head_right_text.setVisibility(View.VISIBLE);
        }
        re_head_title.setVisibility(View.VISIBLE);
        LogUtils.e("main 的 index = " + index);
        switch (index) {
            case 0:
                tv_head_title.setText("世界");
                worldFragment.resetUI();
//                worldFragment.reSetTitles();
                break;
            case 1:
                re_head_title.setVisibility(View.GONE);
                tv_head_title.setText("影响力");
                break;
            case 2:
                tv_head_title.setText("我们");
                break;
            case 3:
                tv_head_title.setText("我");
//                personInfoFragment.setIsLoginStatus();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        re_head_title.setVisibility(View.VISIBLE);
        String uid = SharedPreferencesUtil.getUid(this);
        switch (v.getId()) {
            case R.id.main_home:
                if (worldFragment == null) {
                    return;
                }

                int i = lvp_main.getCurrentItem();
                if (i == 0) {
                    worldFragment.refreshData();
                }

                lvp_main.setCurrentItem(0);
                tv_head_title.setText("世界");
                setViewIsShow(true, 0);
                break;
            case R.id.main_pengyouquan:
                if (TextUtils.isEmpty(uid)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    setCheckedStatus(main_pengyouquan);
                } else {
//                Intent intent=getPackageManager().getLaunchIntentForPackage(getPackageName());
//                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                    int ii = lvp_main.getCurrentItem();
                    if (ii == 1) {
                        influenceFragment.refreshData();
                    }
                    re_head_title.setVisibility(View.GONE);
                    tv_head_title.setText("影响力");
                    lvp_main.setCurrentItem(1);
                    setViewIsShow(true, 1);
                }
                break;
            case R.id.main_jiaoyou:
                if (TextUtils.isEmpty(uid)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    setCheckedStatus(main_jiaoyou);
                } else {
                    tv_head_title.setText("我们");
                    lvp_main.setCurrentItem(2);
                    setViewIsShow(true, 2);
                }
                break;
            case R.id.main_wode:
                tv_head_title.setText("我");
                lvp_main.setCurrentItem(3);
                setViewIsShow(false, 3);

                break;
            case R.id.iv_head_add://add
                PopuUtil.initpopu(this, iv_head_add);
                break;
            case R.id.iv_head_search://搜索
                MobclickAgent.onEvent(MainActivity.this, StatisticsConsts.event_search);
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.tv_head_right_text://设置
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.ll_add_sendnote:
                if (!ExampleUtil.isConnected(this)) {
                    ShowUtil.showToast(this, "当前设备无网络，无法发布帖子。");
                    return;
                }
                if (TextUtils.isEmpty(uid)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    PopuUtil.initpopu(this);
                }
                setCheckedStatus(ll_add_sendnote);
                break;
        }
    }

    //
    private void setCheckedStatus(RadioButton rb) {
        int i = lvp_main.getCurrentItem();
        lvp_main.setCurrentItem(i);
        rb.setChecked(false);
        switch (i) {
            case 0:
                main_home.setChecked(true);
                break;
            case 1:
                main_pengyouquan.setChecked(true);
                break;
            case 2:
                main_jiaoyou.setChecked(true);
                break;
            case 3:
                main_wode.setChecked(true);
                break;
        }
    }

    /**
     * 选择拍照或相册
     */
    public void addImg(String str, String str2, int isType) {
        this.isClickType = isType;
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(str, ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent;
                                int code = ContextCompat.checkSelfPermission(MainActivity.this,
                                        Manifest.permission.CAMERA);
                                switch (isClickType) {
                                    case 1://拍照

                                        if (code != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                    new String[]{Manifest.permission.CAMERA},
                                                    100);
                                        } else {
                                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            suijishu = System.currentTimeMillis();

                                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",
                                                            suijishu + PHOTO_FILE_NAME)));
                                            startActivityForResult(intent, 1);
                                        }

                                        break;
                                    case 2://录音ll_add_sendnote
                                        //音频录制
//                                        intent = news Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                        intent = new Intent(MainActivity.this, AudioActivity.class);
                                        startActivityForResult(intent, 3);
                                        break;
                                    case 3://录制视频 5
                                        if (code != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                                                    100);
                                        } else {
                                            intent = new Intent(MainActivity.this, NewRecordVideoActivity.class);
                                            startActivityForResult(intent, 5);
                                        }

                                        break;
                                }
                            }
                        }).addSheetItem(str2, ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent;
                switch (isClickType) {
                    case 1://本地选择照片
                        if (mSelectedImage == null) {
                            mSelectedImage = new LinkedList<>();
                        }
                        intent = new Intent(MainActivity.this, PicChoiceActivity.class);
                        intent.putExtra("sendAct", 1);
                        intent.putExtra("choiceLimit", 9);

                        startActivity(intent);
                        break;
                    case 2://本地选择音频 4
                        intent = new Intent(MainActivity.this, GetvAudioAndVideoActivity.class);
                        intent.putExtra("type", "audio");
                        startActivity(intent);
                        break;
                    case 3://本地选择 视频 6
                        intent = new Intent(MainActivity.this, GetvAudioAndVideoActivity.class);
                        intent.putExtra("type", "video");
                        startActivity(intent);
                        break;
                }
            }
        }).show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意
                    LogUtils.e("-----onRequestPermissionsResult  // 同意-----  ");
                    Intent intent = null;
                    switch (isClickType) {
                        case 1://拍照
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            suijishu = System.currentTimeMillis();

                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera",
                                            suijishu + PHOTO_FILE_NAME)));
                            startActivityForResult(intent, 1);

                            break;
                        case 3://录制视频 5
                            intent = new Intent(MainActivity.this, NewRecordVideoActivity.class);
                            startActivityForResult(intent, 5);
                            break;
                    }
                } else {
                    //拒绝
                    LogUtils.e(" ----onRequestPermissionsResult // //拒绝-----  ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。" +
                            "请点击“设置”-“权限”-打开所需权限。" +
                            "最后点击两次后退按钮，即可返回。");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//							setResult(10102);
//							finish();
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    });

                    builder.show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    // 启动本应用的权限设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
//            finish();
            ShowUtil.showToast(this, "您拒绝了该权限,部分功能可能无法使用!");
        }
        if (resultCode == RESULT_OK) {
            LogUtils.e("---------------" + requestCode);
            Intent intent;
            switch (requestCode) {
                case 1:
                    File tempFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", suijishu + PHOTO_FILE_NAME);
                    Bitmap b;
                    if (mSelectedImage == null) {
                        mSelectedImage = new LinkedList<>();
                    }
                    if (WeValueApplication.phoneName.equals("samsung")) {
                        b = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
                        b = ZipBitmapUtil.reduce(b, width, height, true);
                        b = ZipBitmapUtil.rotate(b, 90);
                        File file = saveMyBitmap(Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + suijishu + "__1" + PHOTO_FILE_NAME, b);
                        mSelectedImage.add(file.getAbsolutePath());
                    } else {
                        mSelectedImage.add(tempFile.getAbsolutePath());
                    }
                    LogUtils.e("123321", tempFile.getAbsolutePath());
                    intent = new Intent(MainActivity.this, ReleaseNoteActivity.class);
                    intent.putExtra("isSendType", 3);
                    startActivity(intent);
                    break;
                case 3:
                    Uri uri = data.getData();
                    LogUtils.e("uri = " + uri.getEncodedPath());
                    break;
                case 5:
                    String videoUrl = data.getStringExtra("videoUrl");
                    intent = new Intent(MainActivity.this, ReleaseNoteActivity.class);
                    intent.putExtra("isSendType", 1);
                    intent.putExtra("fileUrl", videoUrl);
                    startActivity(intent);
                    break;
            }
        }
    }


    /****
     * 触摸事件
     *****/
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        boolean onTouch(MotionEvent ev);
    }
    /*****触摸事件*****/
    /**
     * Title: MyViewPagerAdapter<br>
     * Description: TODO Viewpager适配器<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
        //去除页面切换时的滑动翻页效果

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ShowUtil.showToast(this, "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=5*60000;
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        LogUtils.e("---dingwei-----");
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
//            StringBuffer sb = news StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLocationDescribe());
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//                Toast.makeText(MainActivity.this, "gps定位成功", Toast.LENGTH_SHORT).show();
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                Toast.makeText(MainActivity.this, "无法获取有效定位依据导致定位失败，一般是由于手机的原因", Toast.LENGTH_SHORT).show();
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            SharedPreferencesUtil.setLatitude(getApplicationContext(), lat);
            SharedPreferencesUtil.setLongitude(getApplicationContext(), lon);
            SharedPreferencesUtil.setLocationCity(getApplicationContext(), location.getProvince());
            LogUtils.e("msg", location.getCity());
            mLocationClient.stop();
            HashMap map = new HashMap();
            map.put("code", RequestPath.CODE);
            map.put("userid", SharedPreferencesUtil.getUid(getApplicationContext()));
            map.put("userlon", lon);
            map.put("userlat", lat);
            NetworkRequest.postRequest(RequestPath.POST_UPDATEADDR, map, new WZHttpListener() {
                @Override
                public void onSuccess(String content, String isUrl) {
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String message = jsonObject.getString("message");
                        LogUtils.e("msg", "地理位置更新" + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String content) {

                }
            });
        }
    }

    /**
     * Title:  saveMyBitmap<br>
     * Description: TODO  保存图片<br>
     *
     * @param bitName
     * @param mBitmap
     * @since JDK 1.7
     */
    public File saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File(bitName);
        File path = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private void jianCeBanBen() {
        PackageManager manager;
        PackageInfo info = null;
        manager = getPackageManager();
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            localVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载检测App版本号
     */
    private void InitAppVersion() {
        String url = RequestPath.GET_GETVERSION;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("appcode", RequestPath.CODE);

        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject object = new JSONObject(content);
                    JSONObject data = object.getJSONObject("data");
                    newVersion = data.getString("versionnum");
                    String appPath = data.getString("apppath");
                    String isfoucs = data.getString("isfoucs");//是否轻质更新
                    LogUtils.e("log", "newVersion = " + newVersion + "---localVersion = " + localVersion + "---apppath =" + appPath);
                    SharedPreferencesUtil.setApppath(MainActivity.this, RequestPath.SERVER_PATH + appPath);
                    if (!newVersion.equals(localVersion)) {
                        LogUtils.e("log", "newVersion = " + newVersion + "---localVersion = " + localVersion);
                        // 这里来检测版本是否需要更新
                        UpdateManager mUpdateManager = new UpdateManager(MainActivity.this, newVersion,isfoucs);
                        mUpdateManager.checkUpdateInfo();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /*我们小圆点的展示和隐藏方法*/
    public void setTv_Cycle_Visible(Boolean b) {
        if (b) {
            tv_red_cycle.setVisibility(View.VISIBLE);
        } else {
            tv_red_cycle.setVisibility(View.GONE);
        }
    }

    /*注册接收广播*/
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    /*极光推送接收到广播后的相应处理*/
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                //      极光推送修改消息类型
                //      1-好友申请消息 2-被关注消息 3-打赏消息 4-转发消息
                //      5-点赞消息 6-评论消息 7-系统推送 8-升级消息 0-其他消息
                String userstate = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.e("userstate  =" + userstate);
                String isClickInform = bundle.getString("isClickInform");
                if (!TextUtils.isEmpty(userstate)) {
                    try {
                        JSONObject jsonObject = new JSONObject(userstate);
                        userstate = jsonObject.getString("userstate");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setTv_Cycle_Visible(true);
                if ("ok".equals(isClickInform)) {//用户点击了通知栏消息
                    activityManager.setCurrentAct("com.wevalue.MainActivity");
                    activityManager.popOtherActivities();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            main_jiaoyou.performClick();
                        }
                    }, 500);
                } else {
                    if (!TextUtils.isEmpty(userstate)) {
                        //接收到用户状态改变的信息
                        if (userstate.equals("3")) {
                            ShowUtil.showToast(MainActivity.this, "该用户已经禁用");
                            SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                        }
                        if (userstate.equals("4")) {
                            ShowUtil.showToast(MainActivity.this, "该用户已经删除");
                            SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                        }
                    }
                }
            }
        }
    }

    /*处理极光消息*/
    private void handleJpushMessage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pushTag = getIntent().getStringExtra("jpush");
                if ("jpush".equals(pushTag)) {
                    setTv_Cycle_Visible(true);
                    main_jiaoyou.performClick();
                }
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMessageReceiver != null) {
            unregisterReceiver(mMessageReceiver);
        }
    }

    private void getUserInfoData() {
        String uid = SharedPreferencesUtil.getUid(this);
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", uid);
        map.put("logintoken", SharedPreferencesUtil.getUserToken(this));
        LogUtils.e("token!!!", SharedPreferencesUtil.getUserToken(this));
        NetworkRequest.postRequest(RequestPath.POST_SETTOKENLONGTIME, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                //1.用户状态正常 3.用户禁用 4.用户删除 5 登录过期
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("3") || obj.getString("result").equals("4")) {
                        SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                    } else if (obj.getString("result").equals("5")) {
                        ShowUtil.showToast(MainActivity.this, "登录已经过期，请重新登录！");
                        SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                    } else if (obj.getString("result").equals("1")) {
                        SharedPreferencesUtil.setUserToken(MainActivity.this, obj.optString("data"));
                        LogUtils.e("token!!!", obj.optString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(MainActivity.this, "网络开小差了，请检查网络稍后再试...");
            }
        });
    }
}
