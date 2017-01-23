package com.wevalue.ui.mine.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 绑定或修改手机/邮箱
 */
public class BindingTelEmailActivity extends BaseActivity implements OnClickListener, WZHttpListener {

    private EditText et_tel, et_code;
    private EditText et_old_tel;
    private ImageView iv_back;
    private TextView tv_head_title, iv_queding, tv_isTel_email;
    private Button but_getcode;
    private String tel;
    private String old_tel;
    private String isTelAndEmail;

    private String type = "1";
    private String isParam;
    private String who;//1 = 手机号;  2=邮箱
    private String acthCode;

    private int coun;
    private Handler mSendCoedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (coun <= 0) {
                        but_getcode.setText("重新获取");
                        but_getcode.setClickable(true);
                        removeMessages(1);
                        break;
                    }
                    coun--;
                    but_getcode.setText(coun + "s");
                    sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    coun = 60;
                    sendEmptyMessage(1);
                    break;
                case 3:
                    sendEmptyMessage(1);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_tel);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        but_getcode = (Button) findViewById(R.id.but_getcode);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        iv_queding = (TextView) findViewById(R.id.iv_queding);
        iv_queding.setVisibility(View.VISIBLE);
        iv_queding.setOnClickListener(this);

        tv_isTel_email = (TextView) findViewById(R.id.tv_isTel_email);

        et_tel = (EditText) findViewById(R.id.et_tel);
        et_code = (EditText) findViewById(R.id.et_code);
        et_old_tel = (EditText) findViewById(R.id.et_old_tel);

        iv_back.setOnClickListener(this);
        but_getcode.setOnClickListener(this);


        who = getIntent().getStringExtra("who");
        if (who.equals("tel")) {
            isTelAndEmail = "userphone";
            if (TextUtils.isEmpty(SharedPreferencesUtil.getMobile(this))) {
                tv_head_title.setText("绑定手机号");
            } else {
                tv_head_title.setText("修改手机号码");
                StringBuffer buffer = new StringBuffer(SharedPreferencesUtil.getMobile(this));
                buffer.replace(3, 7, "****");
                et_old_tel.setText(buffer.toString());
                et_old_tel.setTextColor(Color.BLACK);
                et_old_tel.setFocusableInTouchMode(false);
                et_old_tel.setFocusable(false);
            }

        } else if (who.equals("email")) {
            if (TextUtils.isEmpty(SharedPreferencesUtil.getMobile(this))) {
                tv_head_title.setText("绑定邮箱");
            } else {
                tv_head_title.setText("修改邮箱");
            }
            et_tel.setHint(R.string.hint_email);
            tv_isTel_email.setText("邮箱:");
            isTelAndEmail = "useremail";
        }
    }


    /**
     * 获取验证码
     */
    private void sendCode() {
        tel = et_tel.getText().toString().trim();
        if (!TextUtils.isEmpty(tel)) {
            switch (who) {
                case "tel":
                    if (RegexUtils.etPhoneRegex(tel)/*判断手机号正则*/) {
                        type = "1";
                        isParam = "phone";
                        LogUtils.e("log", "手机号正确");
                    } else {
                        ShowUtil.showToast(this, "请输入正确的手机号!");
                        LogUtils.e("log", "手机号错误-------");
                        return;
                    }
                    break;
                case "email":
                    if (RegexUtils.etEmail(tel)/*判断邮箱正则*/) {
                        isParam = "email";
                        type = "2";
                    } else {
                        ShowUtil.showToast(this, "请输入正确的邮箱!");
                        return;
                    }
                    break;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("code", RequestPath.CODE);
            map.put("type", type);
            map.put(isParam, tel);
            NetworkRequest.postRequest(RequestPath.POST_GETCODE, map, this);
        } else {
            ShowUtil.showToast(this, "请输入手机号!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_getcode://获取验证码
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    if (TextUtils.isEmpty(et_tel.getText().toString())) {
                        ShowUtil.showToast(this, "请输入新手机号码!");
                        return;
                    }
                    if (et_tel.getText().toString().trim().equals(et_old_tel.getText().toString().trim())) {
                        ShowUtil.showToast(this, "新旧手机号码不能一样!");
                        return;
                    }
                    if (!RegexUtils.etPhoneRegex(et_tel.getText().toString())) {
                        ShowUtil.showToast(this, "请输入正确的新手机号码!");
                        return;
                    }

                    but_getcode.setClickable(false);
                    mSendCoedHandler.sendEmptyMessage(2);
                    sendCode();
                }
                break;
            case R.id.iv_queding://确定
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    LogUtils.e("log", "  if----");
                    return;
                } else {
                    old_tel = et_old_tel.getText().toString().trim();
                    if (TextUtils.isEmpty(old_tel)) {
                        ShowUtil.showToast(this, "请输入旧手机号码!");
                        return;
                    }
//                    if (!SharedPreferencesUtil.getMobile(this).equals(old_tel)) {
//                        ShowUtil.showToast(this, "您输入的号码不匹配!");
//                        return;
//                    }
                    if (TextUtils.isEmpty(et_tel.getText().toString())) {
                        ShowUtil.showToast(this, "请输入新手机号码!");
                        return;
                    }
                    if (et_tel.getText().toString().equals(old_tel)) {
                        ShowUtil.showToast(this, "新旧手机号码不能一样!");
                        return;
                    }
                    if (!RegexUtils.etPhoneRegex(et_tel.getText().toString())) {
                        ShowUtil.showToast(this, "请输入正确的新手机号码!");
                        return;
                    }
                    if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                        ShowUtil.showToast(this, "请输入验证码!");
                        return;
                    }
                    if (!et_code.getText().toString().trim().equals(acthCode)) {
                        ShowUtil.showToast(this, "输入验证码不正确！");
                        return;
                    }
                    postTelAndEmail();
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }

    }

    /**
     * 绑定手机或邮箱
     */
    private void postTelAndEmail() {

        String url = RequestPath.POST_UPDATEUSERINFO;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put(isTelAndEmail, tel);

        NetworkRequest.postRequest(url, map, this);

    }

    @Override
    public void onSuccess(String content, String isUrl) {
        LogUtils.e("--onSuccess----content = " + content);
        switch (isUrl) {
            case RequestPath.POST_GETCODE://获取验证码
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        acthCode = obj.getString("data");
//                        et_code.setText(obj.getString("data"));
                    } else {
                        ShowUtil.showToast(this, obj.getString("message"));
                        coun = 0;
                        mSendCoedHandler.sendEmptyMessage(3);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case RequestPath.POST_UPDATEUSERINFO://修改和绑定手机或邮箱

                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(BindingTelEmailActivity.this, obj.getString("message"));
                        LogUtils.e("who=" + who);
                        switch (who) {
                            case "tel":
                                SharedPreferencesUtil.setMobile(getApplicationContext(), tel);
                                LogUtils.e("手机 = " + SharedPreferencesUtil.getMobile(getApplicationContext()));
                                break;
                            case "email":
                                SharedPreferencesUtil.setEmail(getApplicationContext(), tel);
                                LogUtils.e("邮箱 = " + SharedPreferencesUtil.getEmail(getApplicationContext()));
                                break;
                        }
                        finish();
                    } else {
                        ShowUtil.showToast(BindingTelEmailActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("--onFailure---content = " + content);
        ShowUtil.showToast(BindingTelEmailActivity.this, content);
    }
}
