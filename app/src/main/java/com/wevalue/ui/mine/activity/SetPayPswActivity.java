package com.wevalue.ui.mine.activity;

import android.graphics.Color;
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
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置支付密码界面
 */
public class SetPayPswActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {


    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_head_right;
    private TextView tv_tel;
    //    private TextView tv_queding_but;
    private TextView tv_wenti_1;
    private TextView tv_wenti_2;
    private EditText et_code;
    private EditText et_wenti_daan_1;
    private EditText et_wenti_daan_2;
    private List<String> wenti_1_List;
    private List<String> wenti_2_List;
    private Button but_getcode;
    private String tel;
    private String psw;
    private String smsCode;

    private EditText et_psw, et_psw_2;

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
    private String uid;
    private String wenti_1;
    private String wenti_2;
    private String isSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_psw);

        tel = SharedPreferencesUtil.getMobile(this);
        uid = SharedPreferencesUtil.getUid(this);

        initView();
        initData();
    }

    /**
     * 初始化问题数据
     */
    private void initData() {
        wenti_1_List = new ArrayList<>();
        wenti_1_List.add("我的父亲的生日：");
        wenti_1_List.add("我的母亲的生日：");
        wenti_1_List.add("我的配偶的生日：");
        wenti_1_List.add("我童年时代的绰号：");

        wenti_2_List = new ArrayList<>();
        wenti_2_List.add("我的小学就读于哪一所学校：");
        wenti_2_List.add("我最喜欢吃的是什么：");
        wenti_2_List.add("我最喜欢的歌曲：");
        wenti_2_List.add("我的特长是：");

    }

    /**
     * 初始化控件
     */
    private void initView() {

        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("确定");

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);

        isSet = getIntent().getStringExtra("isSet");
        if (!isSet.equals("set")) {
            tv_head_title.setText("忘记支付密码");
        } else {
            tv_head_title.setText("设置支付密码");
        }
        but_getcode = (Button) findViewById(R.id.but_getcode);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
//        tv_queding_but = (TextView) findViewById(R.id.tv_queding_but);
        tv_wenti_1 = (TextView) findViewById(R.id.tv_wenti_1);
        tv_wenti_2 = (TextView) findViewById(R.id.tv_wenti_2);
        et_code = (EditText) findViewById(R.id.et_code);
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_psw_2 = (EditText) findViewById(R.id.et_psw_2);
        et_wenti_daan_1 = (EditText) findViewById(R.id.et_wenti_daan_1);
        et_wenti_daan_2 = (EditText) findViewById(R.id.et_wenti_daan_2);
        StringBuffer buffer = new StringBuffer(tel);
        buffer.replace(3, 7, "****");
        tv_tel.setText(buffer.toString());
        tv_tel.setTextColor(Color.BLACK);
        tv_tel.setFocusable(false);
        tv_tel.setFocusableInTouchMode(false);

        but_getcode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);
        tv_wenti_1.setOnClickListener(this);
        tv_wenti_2.setOnClickListener(this);
    }


    /**
     * 获取验证码
     */
    private void sendCode() {

        but_getcode.setClickable(false);
        mSendCoedHandler.sendEmptyMessage(2);

        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("type", "1");
        map.put("phone", tel);
        map.put("resetpwd", "1");

        NetworkRequest.postRequest(RequestPath.POST_GETCODE, map, this);
    }

    private void jiluMibao() {

        String code = et_code.getText().toString().trim();
        String psw_1 = et_psw.getText().toString().trim();
        String psw_2 = et_psw_2.getText().toString().trim();
//        wenti_1 = tv_wenti_1.getText().toString().trim();
//        wenti_2 = tv_wenti_2.getText().toString().trim();
//        String daan_1 = et_wenti_daan_1.getText().toString().trim();
//        String daan_2 = et_wenti_daan_2.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            ShowUtil.showToast(this, "请输入验证码!");
            return;
        }
        if (!code.equals(smsCode)) {
            ShowUtil.showToast(this, "验证码有误！");
        }

        if (TextUtils.isEmpty(psw_1)) {
            ShowUtil.showToast(this, "请设置支付密码!");
            return;
        }
        if (TextUtils.isEmpty(psw_2)) {
            ShowUtil.showToast(this, "请再输入一次支付密码!");
            return;
        }
        if (!psw_1.equals(psw_2)) {
            ShowUtil.showToast(this, "两次密码不一致!");
            return;
        } else {
            psw = psw_1;
        }
        if (psw.length() < 6) {
            ShowUtil.showToast(this, "请设置六位的支付密码");
            return;
        }


//        if(TextUtils.isEmpty(wenti_1)&& wenti_1.equals("请设置密保问题一：(四选一)")){
//            ShowUtil.showToast(this,"请设置密保问题!");
//            return;
//        }
//        if(TextUtils.isEmpty(wenti_2)&&wenti_1.equals("请设置密保问题一：(四选一)")){
//            ShowUtil.showToast(this,"请设置密保问题!");
//            return;
//        }
//
//        if (TextUtils.isEmpty(daan_1)) {
//            ShowUtil.showToast(this,"请设置密保答案!");
//            return;
//        }
//        if (TextUtils.isEmpty(daan_2)) {
//            ShowUtil.showToast(this,"请设置密保答案!");
//            return;
//        }


        setPaywsd();
//        Map<String ,Object> map = news HashMap<>();
//        map.put("code", RequestPath.CODE);
//        map.put("userid",uid);
//        map.put("payquestion1",wenti_1);
//        map.put("payquestion2",wenti_2);
//        map.put("payanswer1",daan_1);
//        map.put("payanswer2",daan_2);
//
//        NetworkRequest.postRequest(RequestPath.POST_SETPAYQUESTION, map,this);
    }

    /**
     * 设置支付密码接口请求
     */
    private void setPaywsd() {
        String url = RequestPath.POST_UPDATEUSERPWD;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", uid);
        map.put("type", "2");
        map.put("newpwd", psw);

        NetworkRequest.postRequest(url, map, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_wenti_1://问题1
                PopuUtil.initpopu(this, tv_wenti_1, wenti_1_List, et_wenti_daan_1);
                break;
            case R.id.tv_wenti_2://问题2
                PopuUtil.initpopu(this, tv_wenti_2, wenti_2_List, et_wenti_daan_2);
                break;
            case R.id.tv_head_right://确定按钮
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    jiluMibao();
                }
                break;
            case R.id.but_getcode://获取验证码
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    sendCode();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {

        switch (isUrl) {
            case RequestPath.POST_GETCODE://获取验证码接口
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        smsCode = obj.getString("data");
//                        et_code.setText(obj.getString("data"));
                    } else {
                        ShowUtil.showToast(SetPayPswActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_SETPAYQUESTION://设置密保问题及答案接口
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        setPaywsd();
                    } else {
                        ShowUtil.showToast(SetPayPswActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_UPDATEUSERPWD://设置支付密码接口
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
//                        SharedPreferencesUtil.setPayPswWenTi_1(this,wenti_1);
//                        SharedPreferencesUtil.setPayPswWenTi_2(this,wenti_2);
                        SharedPreferencesUtil.setPayPswStatus(this, "1");
                        finish();
                        ShowUtil.showToast(SetPayPswActivity.this, obj.getString("message"));
                    } else {
                        ShowUtil.showToast(SetPayPswActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onFailure(String content) {

        LogUtils.e("设置支付密码界面请求接口  错误=" + content);
    }


}
