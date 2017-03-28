package jupush;

import android.app.Activity;
import android.app.Notification;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.wevalue.R;
import com.wevalue.utils.LogUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/12
 * 类说明：
 */

public class JpushTagSet {
    Activity activity;
    String userid;
    private static final String TAG = "JPush";

    public JpushTagSet(FragmentActivity activity, String usernumber) {
        this.activity = activity;
        this.userid = usernumber;
    }

    public void setTag() {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(null)) {
//            Toast.makeText(PushSetActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }
//        setStyleBasic();
        // ","隔开的多个 转换成 Set
        String[] sArray = userid.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
//                Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }
        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }

    public void setAlias() {
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, userid));
    }


    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(activity);
        builder.statusBarDrawable = R.mipmap.ic_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
        Toast.makeText(activity.getApplicationContext(), "Basic Builder - 1", Toast.LENGTH_SHORT).show();
    }


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    public void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(activity.getApplicationContext(), R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.mipmap.ic_logo;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
//        Toast.makeText(activity.getApplicationContext(), "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "用户已经关联激光推送";
//                    SharedPreferencesUtil.setJupushTagSet(activity, true);
                    LogUtils.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.i(TAG, logs);
                    if (ExampleUtil.isConnected(activity)) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        LogUtils.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(TAG, logs);
            }
//            ExampleUtil.showToast(logs, activity.getApplicationContext());
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
//                    SharedPreferencesUtil.setJupushTagSet(activity, true);
                    LogUtils.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.i(TAG, logs);
                    if (ExampleUtil.isConnected(activity.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, activity.getApplicationContext());
        }

    };

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(activity.getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    LogUtils.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(activity.getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    LogUtils.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

}
