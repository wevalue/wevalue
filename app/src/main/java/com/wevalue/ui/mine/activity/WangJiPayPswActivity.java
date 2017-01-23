package com.wevalue.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码界面
 */
public class WangJiPayPswActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {

    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_head_right;

    private TextView tv_wenti_1;
    private TextView tv_wenti_2;
    private EditText et_wenti_daan_1;
    private EditText et_wenti_daan_2;
    private EditText et_pay_wsd_2;
    private EditText et_pay_wsd_1;
    private String uid;
    private String paywsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangji_pay_psw);
        uid = SharedPreferencesUtil.getUid(this);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("确定");
        tv_head_right.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("忘记支付密码");

        tv_wenti_1 = (TextView) findViewById(R.id.tv_wenti_1);
        tv_wenti_2 = (TextView) findViewById(R.id.tv_wenti_2);
        et_wenti_daan_1 = (EditText) findViewById(R.id.et_wenti_daan_1);
        et_wenti_daan_2 = (EditText) findViewById(R.id.et_wenti_daan_2);
        et_pay_wsd_1 = (EditText) findViewById(R.id.et_pay_wsd_1);
        et_pay_wsd_2 = (EditText) findViewById(R.id.et_pay_wsd_2);

        iv_back.setOnClickListener(this);

        tv_wenti_1.setText(SharedPreferencesUtil.getPayPswWenTi_1(this));
        tv_wenti_2.setText(SharedPreferencesUtil.getPayPswWenTi_2(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_head_right://确定按钮
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    yanzhengmibao();
                }
                break;
        }
    }


    /**
     * 验证密保问题接口
     */
    private void yanzhengmibao() {

        String payanswer1 = et_wenti_daan_1.getText().toString().trim();
        String payanswer2 = et_wenti_daan_2.getText().toString().trim();
        String paywsd_1 = et_pay_wsd_1.getText().toString().trim();
        String paywsd_2 = et_pay_wsd_2.getText().toString().trim();

        if (TextUtils.isEmpty(payanswer1)) {
            ShowUtil.showToast(this, "请输入密保答案!");
            return;
        }
        if (TextUtils.isEmpty(payanswer2)) {
            ShowUtil.showToast(this, "请输入密保答案!");
            return;
        }
        if (TextUtils.isEmpty(paywsd_1)) {
            ShowUtil.showToast(this, "请设置支付密码!");
            return;
        }
        if (TextUtils.isEmpty(paywsd_2)) {
            ShowUtil.showToast(this, "请再次输入支付密码!");
            return;
        }
        if (!paywsd_2.equals(paywsd_1)) {
            ShowUtil.showToast(this, "两次输入的密码不一致!");
            return;
        } else {
            paywsd = paywsd_2;
        }
        if(paywsd.length()<5){
            ShowUtil.showToast(this, "请设置六位的支付密码!");
            return;
        }
        String url = RequestPath.POST_GETPAYRESULT;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", uid);
        map.put("payanswer1", payanswer1);
        map.put("payanswer2", payanswer2);
        NetworkRequest.postRequest(url, map, this);
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
        map.put("newpwd", paywsd);

        NetworkRequest.postRequest(url, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        LogUtils.e("responseInfo-----" + content + "-------" + isUrl);
        switch (isUrl) {
            case RequestPath.POST_GETPAYRESULT://验证密保答案
                try {
                    JSONObject json = new JSONObject(content);
                    String code = json.getString("result");
                    if (code.equals("1")) {
                        setPaywsd();
                    } else {
                        ShowUtil.showToast(this, json.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_UPDATEUSERPWD://修改密码
                try {
                    JSONObject json = new JSONObject(content);
                    String code = json.getString("result");
                    if (code.equals("1")) {
                        ShowUtil.showToast(this, json.getString("message"));
                        finish();
                    } else {
                        LogUtils.e(content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("忘记密码界面 接口请求错误 =" + content);
    }
}
