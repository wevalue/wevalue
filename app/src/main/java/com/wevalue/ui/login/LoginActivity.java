package com.wevalue.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.DialogUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.NetStatusUtil;
import com.wevalue.utils.ShareLoginHelper;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
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
    private LinearLayout ll_weixin, ll_qq;
    private String type;//第三方登录的方式
    ShareLoginHelper shareLoginHelper;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Platform mPlatform = (Platform) msg.obj;
            switch (msg.what) {
                case 1:
                    otherLogin(mPlatform);
                    break;
                case 2:
                    if (mProgressDialog.isShowing())  mProgressDialog.dismiss();
                    ShowUtil.showToast(context,"登录出错");
                    break;
                case 3:
                    if (mProgressDialog.isShowing())  mProgressDialog.dismiss();
                    ShowUtil.showToast(context,"取消登录");
                    break;

            }
        }
    };

    private Dialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        shareLoginHelper = new ShareLoginHelper(this, handler);
        initView();
        mProgressDialog = DialogUtil.createLoadingDialog(this);
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
    }

    private void initView() {
        tv_register_but = (TextView) findViewById(R.id.tv_register_but);
        tv_login_but = (TextView) findViewById(R.id.tv_login_but);
        tv_back_psw = (TextView) findViewById(R.id.tv_back_psw);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_psw = (EditText) findViewById(R.id.et_psw);
        ll_qq = (LinearLayout) findViewById(R.id.ll_qq);
        ll_weixin = (LinearLayout) findViewById(R.id.ll_weixin);

        et_tel.setText(SharedPreferencesUtil.getZhangHao(this));
        tv_register_but.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_login_but.setOnClickListener(this);
        tv_back_psw.setOnClickListener(this);

        ll_qq.setOnClickListener(this);
        ll_weixin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent in;
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
                if (!NetStatusUtil.getStatus(this)) {
                    ShowUtil.showToast(this, "无网络连接,请检查您的网络!");
                    return;
                }
                mProgressDialog.show();
                type = "qq";
                shareLoginHelper.loginQQ();
                break;
            case R.id.ll_weixin:
                if (!NetStatusUtil.getStatus(this)) {
                    ShowUtil.showToast(this, "无网络连接,请检查您的网络!");
                    return;
                }
                mProgressDialog.show();
                type = "wx";
                shareLoginHelper.loginWx();
                break;
        }
    }

    /**
     * 第三方登录
     */
    private void otherLogin(Platform platform) {
        PlatformDb platFormDB = platform.getDb();
        String userId = platFormDB.getUserId();
        String nickname = platFormDB.getUserName();
        String otherface = platFormDB.getUserIcon();
        String sex = platFormDB.getUserGender();
        if (sex.equals("f")) {
            sex = "女";
        } else if (sex.equals("m")) {
            sex = "男";
        } else {
            sex = "";
        }
        Map map = new HashMap();
        map.put("type", type);
        map.put("code", RequestPath.CODE);
        map.put("nickname", nickname);
        map.put("usersex", sex);
        map.put("othercode", userId);
        map.put("otherface", otherface);
        otherLogin(map);
    }

    private void otherLogin(Map map) {
        NetworkRequest.postRequest(RequestPath.POST_OTHER_LOGIN, map, this);
    }

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
            /** 所有登录返回同样的数据 **/
            JSONObject obj = new JSONObject(content);
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
                SharedPreferencesUtil.setUserv(getApplicationContext(), data.getString("userv")); //是否是大V
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
        if (mProgressDialog.isShowing())  mProgressDialog.dismiss();
    }
}
