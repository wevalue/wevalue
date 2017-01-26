package com.wevalue.ui.mine.activity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.utils.DataCleanManager;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.UpdateManager;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jupush.JpushTagSet;


/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements OnClickListener, WZHttpListener {

    private ImageView iv_back;
    private TextView tv_head_title, tv_banben, tv_cache_num;
    private TextView tv_back_app;
    private LinearLayout ll_fankui;
    private LinearLayout ll_guanyu;
    private LinearLayout ll_qingchuhuancun;
    private RelativeLayout rl_zhanghu;

    private View prompt_box;
    private PopupWindow promptBoxPopupWindow;
    private TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
    private String localVersion;//本地版本号
    private String newVersion;//服务器版本号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        ll_fankui = (LinearLayout) findViewById(R.id.ll_fankui);
        ll_guanyu = (LinearLayout) findViewById(R.id.ll_guanyu);
        ll_qingchuhuancun = (LinearLayout) findViewById(R.id.ll_qingchuhuancun);
        rl_zhanghu = (RelativeLayout) findViewById(R.id.rl_zhanghu);
        tv_back_app = (TextView) findViewById(R.id.tv_back_app);
        tv_banben = (TextView) findViewById(R.id.tv_banben);
        tv_cache_num = (TextView) findViewById(R.id.tv_cache_num);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("设置");

        ll_fankui.setOnClickListener(this);
        ll_guanyu.setOnClickListener(this);
        tv_back_app.setOnClickListener(this);
        tv_banben.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
            rl_zhanghu.setVisibility(View.GONE);
        } else {
            rl_zhanghu.setVisibility(View.VISIBLE);
            rl_zhanghu.setOnClickListener(this);
        }
        ll_qingchuhuancun.setOnClickListener(this);

        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
            tv_back_app.setVisibility(View.GONE);
        } else {
            tv_back_app.setVisibility(View.VISIBLE);
        }
        try {
            if (DataCleanManager.getFolderSize(this.getCacheDir()) > 1024 ) {
                tv_cache_num.setText(DataCleanManager.getCacheSize(this.getCacheDir()));
            }else {
                tv_cache_num.setText("暂无缓存");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PackageManager manager;
        PackageInfo info = null;
        manager = getPackageManager();
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            localVersion = info.versionName;
            tv_banben.setText("版本信息(V" + localVersion + ")");

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        Intent in;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_qingchuhuancun://清除缓存
                DataCleanManager.cleanInternalCache(this);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Glide.get(SettingActivity.this).clearDiskCache();
                    }
                }.start();
                try {
//				tv_cache_num.setText(DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(this.getExternalCacheDir())));
                    tv_cache_num.setText("暂无缓存");
                    LogUtils.e("---缓存大小-----" + DataCleanManager.getCacheSize(this.getCacheDir()));

//				tv_cache_num.setText("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MobclickAgent.onEvent(this,StatisticsConsts.event_setting,"清除缓存");
                break;
            case R.id.tv_banben://版本更新
                InitAppVersion();
                break;
            case R.id.ll_fankui:
                in = new Intent(this, FeedbackActivity.class);
                startActivity(in);
                break;
            case R.id.ll_guanyu:
                in = new Intent(this, AboutWeValueActivity.class);
                startActivity(in);
                MobclickAgent.onEvent(this,StatisticsConsts.event_setting,"关于微值");
                break;
            case R.id.rl_zhanghu:
                in = new Intent(this, AccountInfoActivity.class);
                startActivity(in);
                MobclickAgent.onEvent(this,StatisticsConsts.event_setting,"账户安全");
                break;
            case R.id.tv_back_app:
                PopuUtil.initpopu(this, "确定退出登录吗？", new PopClickInterface() {
                    @Override
                    public void onClickOk(String content) {
                        SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                        //清空极光推送的别名绑定
                        JpushTagSet tagSet = new JpushTagSet(SettingActivity.this, "null");
                        tagSet.setAlias();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 300);
                    }
                });
                MobclickAgent.onEvent(this,StatisticsConsts.event_setting,"退出登录");
                break;
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
        NetworkRequest.getRequest(url, map, this);
    }


    /**
     * 退出登录popu
     */
    private void initpopu() {
        // 空白区域
        prompt_box = getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);

        prompt_box.setOnClickListener(new OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setTextColor(getResources().getColor(R.color.but_text_color));
        promptBox_tv_submit.setOnClickListener(new OnClickListener() {
            // 确定按钮
            @Override
            public void onClick(View v) {

                SharedPreferencesUtil.clearSharedPreferencesInfo(SettingActivity.this, "UserInfo");
                finish();
                promptBoxPopupWindow.dismiss();
            }

        });
        promptBox_tv_cancel.setTextColor(getResources().getColor(R.color.orange));
        promptBox_tv_cancel.setOnClickListener(new OnClickListener() {
            // 取消按钮
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject object = new JSONObject(content);
            JSONObject data = object.getJSONObject("data");
            newVersion = data.getString("versionnum");
            String appPath = data.getString("apppath");
            LogUtils.e("log", "newVersion = " + newVersion + "---localVersion = " + localVersion + "---apppath =" + appPath);
            SharedPreferencesUtil.setApppath(SettingActivity.this, RequestPath.SERVER_PATH + appPath);
//					AbSharedUtil.putString(SettingActivity.this, "AppPath", appPath);
            if (!newVersion.equals(localVersion)) {
                LogUtils.e("log", "newVersion = " + newVersion + "---localVersion = " + localVersion);
//						tv_version.setText("有新版本 V" + newVersion + ",当前版本 V"
//								+ localVersion);
//						tv_version.setTextColor(Color.RED);
                // 这里来检测版本是否需要更新
                UpdateManager mUpdateManager = new UpdateManager(
                        SettingActivity.this, newVersion);
                mUpdateManager.checkUpdateInfo();
            } else {
//						tv_version.setText("已是最新版本,当前版本 V" + localVersion);
//						tv_version.setTextColor(Color.GRAY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {

    }
}
