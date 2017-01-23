package com.wevalue.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.NetStatusUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jupush.JpushTagSet;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener, WZHttpListener {
    private TextView tv_register_but, tv_back_psw;
    private TextView tv_login_but;
    private EditText et_tel, et_psw;
    private ImageView iv_back;
    private String tel;
    //	private Platform mPlatform;
    private LinearLayout ll_weixin, ll_qq, ll_weibo;
    private String type;//第三方登录的方式


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//				otherLogin(mPlatform);
                    break;

            }
        }
    };

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//		ShareSDK.initSDK(this);//初始化share
        initView();
    }

    private void initView() {
        tv_register_but = (TextView) findViewById(R.id.tv_register_but);
        tv_login_but = (TextView) findViewById(R.id.tv_login_but);
        tv_back_psw = (TextView) findViewById(R.id.tv_back_psw);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_psw = (EditText) findViewById(R.id.et_psw);
        ll_weibo = (LinearLayout) findViewById(R.id.ll_weibo);
        ll_qq = (LinearLayout) findViewById(R.id.ll_qq);
        ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);

        et_tel.setText(SharedPreferencesUtil.getZhangHao(this));
        tv_register_but.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_login_but.setOnClickListener(this);
        tv_back_psw.setOnClickListener(this);

        ll_qq.setOnClickListener(this);
        ll_weixin.setOnClickListener(this);
        ll_weibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent in;
//		Platform qqPlatform =null;
        switch (v.getId()) {
            case R.id.tv_register_but:
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    in = new Intent(this, UserRegisterActivity.class);
                    startActivity(in);
                }

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login_but:
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    login();
                }
                break;
            case R.id.tv_back_psw:
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    in = new Intent(this, ForgetPasswordActivity.class);
                    startActivity(in);
                }

                break;

            case R.id.ll_qq:
//			qqPlatform = ShareSDK.getPlatform(this, QQ.NAME);
//			qqPlatform.setPlatformActionListener(news MyPlatformActionListener());
//			qqPlatform.authorize();
                break;
            case R.id.ll_weixin:
//			IWXAPI wxApi = WXAPIFactory.createWXAPI(LoginActivity.this, "", true);
//			if (!wxApi.isWXAppInstalled()) {
//				ShowUtil.showToast(LoginActivity.this,"没有安装微信");
//				return;
//			}
//			if (!wxApi.isWXAppSupportAPI()) {
//				ShowUtil.showToast(LoginActivity.this,"当前版本不支持登录功能");
//				return;
//			}
//			qqPlatform = ShareSDK.getPlatform(this, Wechat.NAME);
//			qqPlatform.setPlatformActionListener(news MyPlatformActionListener());
//			qqPlatform.authorize();
                break;
            case R.id.ll_weibo:
//			qqPlatform = ShareSDK.getPlatform(this, SinaWeibo.NAME);
//			qqPlatform.setPlatformActionListener(news MyPlatformActionListener());
//			qqPlatform.authorize();
                break;


        }
    }


    /**
     *
     * Title: MyPlatformActionListener<br>
     * Description: 平台操作监听<br>
     * Depend : TODO
     * @since JDK 1.7
     */
//	class MyPlatformActionListener implements PlatformActionListener {
//
//
//
//		/** 取消授权 */
//		@Override
//		public void onCancel(Platform platform, int action) {
//			LogUtils.e("log", "onCancel   action:" + action);
////			ShowProgressDialog.ShowProgressOff();
//		}
//
//		/** 完成授权 */
//		@Override
//		public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
//			mPlatform =platform;
//			handler.sendEmptyMessage(1);
//			// 检测账号是否绑定
//
//		}
//
//
//		/** 错误 */
//		@Override
//		public void onError(Platform platform, int action, Throwable t) {
//			LogUtils.e("log", "error---");
//			LogUtils.e("log", "error-----action-"+action);
//			LogUtils.e("log", "error-----platform-"+platform.getDb().getPlatformNname());
//			LogUtils.e("log", "error----t---"+t.getMessage().toString());
////			ShowProgressDialog.ShowProgressOff();
//		}
//
//	}
//	/**第三方登录*/
//	private void otherLogin(Platform platform) {
//		LogUtils.e("log", "platform=" + platform.toString());
//		LogUtils.e("log", "platform.getDb()=" + platform.getDb().toString());
//		final PlatformDb platFormDB = platform.getDb();
//		String userId = platFormDB.getUserId();
//		String nickname = platFormDB.getUserName();
//		String otherface = platFormDB.getUserIcon();
//		String sex = platFormDB.getUserGender();
//		LogUtils.e("log", "sex=" + sex);
//		LogUtils.e("log", "otherface=" + otherface);
//		LogUtils.e("log", "nickname=" + nickname);
//		LogUtils.e("log", "userId=" + userId);
//		LogUtils.e("log", "getPlatformNname=" + platFormDB.getPlatformNname());
//		LogUtils.e("log", "getPlatformVersion=" + platFormDB.getPlatformVersion());
//		LogUtils.e("log", "getPlatformVersion=" + platFormDB.getTokenSecret());
//		LogUtils.e("log", "getToken=" + platFormDB.getToken());
//		if (sex.equals("f")) {
//			sex = "女";
//		} else if (sex.equals("m")) {
//			sex = "男";
//		} else {
//			sex = "";
//		}
//	}


    /**
     * 登录
     */
    private void login() {

        if (!NetStatusUtil.getStatus(this)) {
            ShowUtil.showToast(this, "无网络连接,请检查您的网络!");
            return;
        }

        tel = et_tel.getText().toString().trim();
        String password = et_psw.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            ShowUtil.showToast(this, "请输入手机号或微值号!");
            return;
        }
        if (tel.length() < 6) {
            ShowUtil.showToast(LoginActivity.this, "该用户不存在，请您检查账户！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ShowUtil.showToast(this, "请输入密码!");
            return;
        }
        if (password.length() < 6) {
            ShowUtil.showToast(LoginActivity.this, "用户名和密码不匹配，请检查后重新输入！");
            return;
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在登录...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        //登陆过程中禁止用户取消登陆的行为
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && mProgressDialog.isShowing()) {
                    ShowUtil.showToast(LoginActivity.this, "正在登录，请您稍等哒~");
                    return true;
                }
                return false;
            }
        });

        mProgressDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("phone", tel);
        map.put("password", password);
        NetworkRequest.postRequest(RequestPath.POST_LOGIN, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        String s = content;
        mProgressDialog.dismiss();
        try {
            JSONObject obj = new JSONObject(content);
//			Set<String> set = getAllKeys(obj.toString());
//			LogUtils.e("set = "+set.toString());
            if (obj.getString("result").equals("1")) {
                ShowUtil.showToast(LoginActivity.this, obj.getString("message"));
                JSONObject data = obj.getJSONObject("data");
                SharedPreferencesUtil.setUserToken(getApplicationContext(), data.getString("logintoken"));
                SharedPreferencesUtil.setUid(getApplicationContext(), data.getString("userid"));
                SharedPreferencesUtil.setMobile(getApplicationContext(), data.getString("userphone"));
                SharedPreferencesUtil.setEmail(getApplicationContext(), data.getString("useremail"));
                SharedPreferencesUtil.setLatitude(getApplicationContext(), data.getString("userlat"));
                SharedPreferencesUtil.setLongitude(getApplicationContext(), data.getString("userlon"));
                SharedPreferencesUtil.setNickname(getApplicationContext(), data.getString("usernickname"));
                SharedPreferencesUtil.setCityName(getApplicationContext(), data.getString("usercity"));
                SharedPreferencesUtil.setProvinceName(getApplicationContext(), data.getString("userprovince"));
                SharedPreferencesUtil.setCounty(getApplicationContext(), data.getString("userdistrict"));
                SharedPreferencesUtil.setZuZzhiname(getApplicationContext(), data.getString("orgname"));
                SharedPreferencesUtil.setSex(getApplicationContext(), data.getString("usersex"));
                SharedPreferencesUtil.setQR_code(getApplicationContext(), data.getString("usercode"));
                SharedPreferencesUtil.setUserInfo(getApplicationContext(), data.getString("userinfo"));
                SharedPreferencesUtil.setUserleve(getApplicationContext(), data.getString("userlevel"));
                SharedPreferencesUtil.setUserLevelInt(getApplicationContext(), Integer.parseInt(data.getString("userscore")));
                SharedPreferencesUtil.setUsertype(getApplicationContext(), data.getString("usertype"));
                SharedPreferencesUtil.setAvatar(getApplicationContext(), data.getString("userface"));
                SharedPreferencesUtil.setLoginPswStatus(getApplicationContext(), data.getString("havepwd"));
                SharedPreferencesUtil.setPayPswStatus(getApplicationContext(), data.getString("havepaypwd"));
                SharedPreferencesUtil.setPayPswWenTi_1(getApplicationContext(), data.getString("payquestion1"));//密保问题1
                SharedPreferencesUtil.setPayPswWenTi_2(getApplicationContext(), data.getString("payquestion2"));//密保问题2
                SharedPreferencesUtil.setUsernumber(getApplicationContext(), data.getString("usernumber"));//微值号
                SharedPreferencesUtil.setSuiYinCount(getApplicationContext(), data.getString("usermoney"));//保存用户碎银数量
                SharedPreferencesUtil.setUerAuthentic(getApplicationContext(), data.getString("istrue"));
                setJupushAlisa(data.getString("userid"));
                SharedPreferencesUtil.setZhangHao(getApplicationContext(), tel);
                finish();
            } else {
                ShowUtil.showToast(LoginActivity.this, obj.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowUtil.showToast(LoginActivity.this, "服务数据异常，请稍后重试！");
        }
    }

    /*设置极光推送的别名 进行个人推送*/
    private void setJupushAlisa(String usernumber) {
        JpushTagSet tagSet = new JpushTagSet(this, usernumber);
        tagSet.setAlias();
    }

    @Override
    public void onFailure(String content) {
        mProgressDialog.dismiss();
    }

    /**
     * 从JSON字符串里得到所有key
     *
     * @param jsonString json字符串
     * @return Set
     */
    public static Set<String> getAllKeys(String jsonString) {
        Set<String> set = new HashSet<>();
        //按照","将json字符串分割成String数组
        String[] keyValue = jsonString.split(",");
        for (int i = 0; i < keyValue.length; i++) {
            String s = keyValue[i];
            //找到":"所在的位置，然后截取
            int index = s.indexOf(":");
            //第一个字符串因带有json的"{"，需要特殊处理
            if (i == 0) {
                set.add(s.substring(2, index - 1));
            } else {
                set.add(s.substring(1, index - 1));
            }
        }
        return set;
    }
}
