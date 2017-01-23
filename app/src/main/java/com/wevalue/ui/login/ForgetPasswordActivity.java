package com.wevalue.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-05.
 */

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {


    private EditText et_tel, et_code;
    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_head_right;
    private Button but_getcode;
    private String tel;
    private String authCode;


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
            }

        }
    };
    private EditText et_old_psw;
    private EditText et_new_psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        but_getcode = (Button) findViewById(R.id.but_getcode);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        et_old_psw = (EditText) findViewById(R.id.et_old_psw);
        et_new_psw = (EditText) findViewById(R.id.et_new_psw);


        et_tel = (EditText) findViewById(R.id.et_tel);
        et_code = (EditText) findViewById(R.id.et_code);


        iv_back.setOnClickListener(this);
        but_getcode.setOnClickListener(this);


        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setOnClickListener(this);
        tv_head_right.setText("确定");
        tv_head_title.setText("找回密码");
    }

    /**
     * 获取验证码
     */
    private void sendCode() {
        tel = et_tel.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            ShowUtil.showToast(this, "请输入手机号!");
            return;
        }
        but_getcode.setClickable(false);
        mSendCoedHandler.sendEmptyMessage(2);

        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("type", "1");
        map.put("phone", tel);
        map.put("resetpwd", "1");

        NetworkRequest.postRequest(RequestPath.POST_GETCODE, map, this);
    }

    /**
     * 重置密码
     */
    private void modifyPsw() {
        String old_psw = et_old_psw.getText().toString().trim();
        String psw = et_new_psw.getText().toString().trim();
        if (!old_psw.equals(psw)) {
            ShowUtil.showToast(ForgetPasswordActivity.this, "您两次输入的密码不一样!");
            return;
        }
        String url = "";
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("newpwd", psw);
        map.put("phone", tel);
        url = RequestPath.POST_RESETUSERPWD;
        NetworkRequest.postRequest(url, map, this);
    }

    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.but_getcode://获取验证码
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    sendCode();
                }
                break;
            case R.id.tv_head_right://确定
                quedingClick();

                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 点击下一步逻辑处理
     */
    private void quedingClick() {
        if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
            LogUtils.e("log", "  if----");
            return;
        } else {
            tel = et_tel.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ShowUtil.showToast(this, "请输入手机号!");
                return;
            }
            String code = et_code.getText().toString();
            if (TextUtils.isEmpty(code)) {
                ShowUtil.showToast(this, "请输入验证码！");
                return;
            }
            if (!code.equals(authCode)) {
                ShowUtil.showToast(this, "验证码不正确！");
                return;
            }
            modifyPsw();
        }
    }


    /**
     * 验证码获取结果
     */
    @Override
    public void onSuccess(String content, String isUrl) {

        switch (isUrl) {
            case RequestPath.POST_GETCODE:
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        authCode = obj.getString("data");
                    } else {
                        ShowUtil.showToast(ForgetPasswordActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_RESETUSERPWD:
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(ForgetPasswordActivity.this, obj.getString("message"));
                        finish();
                    } else {
                        ShowUtil.showToast(ForgetPasswordActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(ForgetPasswordActivity.this, content);
    }

}
