package com.wevalue.base;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.wevalue.utils.ActivityManagers;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;

public class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity.log";
    private ActivityManagers activityManager = ActivityManagers.getActivityManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**禁止横屏的代码*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onCreate() invoked!******");
        /**Android 沉浸式*/
//		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// 透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			SystemBarTintManager tintManager = news SystemBarTintManager(this);
//			tintManager.setStatusBarTintEnabled(false);
//			tintManager.setStatusBarTintResource(R.color.background_theme);// 通知栏所需颜色
//		}

        super.onCreate(savedInstanceState);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//		}
        activityManager.pushActivity(this);
//		overridePendingTransition(R.anim.activity_open,R.anim.retain);  //启动动画

    }

    private void setTranslucentStatus(boolean b) throws JSONException {
        // TODO Auto-generated method stub
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onStart() invoked!******");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onRestart() invoked!******");
        super.onRestart();

    }

    @Override
    protected void onResume() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onResume() invoked!******");
        super.onResume();
        if (!getClass().toString().equals("class com.wevalue.MainActivity")) {
            MobclickAgent.onPageStart(getClass().toString());
            LogUtils.e("lifeStyle", getClass().toString() + "進入頁面統計");
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onPause() invoked!******");
        super.onPause();
        if (!getClass().toString().equals("class com.wevalue.MainActivity")) {
            MobclickAgent.onPageEnd(getClass().toString());
            LogUtils.e("lifeStyle", getClass().toString() + "退出頁面統計");
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onStop() invoked!******");
        super.onStop();
//        ShowUtil.cancelLastToast();
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "******" + this.getClass().getSimpleName() + "onDestroy() invoked!******");
        super.onDestroy();
        activityManager.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        finish();
//		overridePendingTransition(R.anim.retain,R.anim.activity_close );
        super.onBackPressed();
    }
}
