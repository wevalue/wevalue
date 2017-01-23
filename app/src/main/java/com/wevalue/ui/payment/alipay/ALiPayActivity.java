package com.wevalue.ui.payment.alipay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayResult;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
* 支付宝支付的界面
* */
public class ALiPayActivity extends BaseActivity implements WZHttpListener {
    private final int SDK_PAY_FLAG = 1;
    private String money = "";
    private String paytype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_pay);
        money = getIntent().getStringExtra("money");
        paytype = getIntent().getStringExtra("paytype");
        obtainOrderInfo();
    }

    //获取订单信息
    private void obtainOrderInfo() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getApplicationContext()));
        map.put("money", money);
        map.put("paytype", paytype);
        map.put("spendtype", Constants.alipay);
        NetworkRequest.postRequest(RequestPath.POST_PAYMONEY, map, ALiPayActivity.this);
    }

    //调用支付宝支付方法
    public void payV2(final String orderinfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ALiPayActivity.this);
                Map<String, String> result = alipay.payV2(orderinfo, true);
                LogUtils.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ShowUtil.showToast(getApplicationContext(), "支付成功！");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ShowUtil.showToast(getApplicationContext(), "支付失败！");
                    }
                    finish();
                    break;
                }
            }
        }
    };

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            //保存订单编号
//            SharedPreferencesUtil.setOrderNo(this, jsonObject.getString("orderno"));
//            //保存支付方式
//            SharedPreferencesUtil.setSpendtype(this, PaymentConstants.alipay);

            JSONArray alipay_data = jsonObject.getJSONArray("alipay_data");
            jsonObject = alipay_data.getJSONObject(0);
            String orderInfo = jsonObject.getString("alipay_data");
            payV2(orderInfo.replaceAll("#", "\""));
        } catch (JSONException e) {
            e.printStackTrace();
            ShowUtil.showToast(getApplicationContext(), "数据解析异常,请稍后重试...");
            finish();
        }
    }

    @Override
    public void onFailure(String content) {

    }
}
