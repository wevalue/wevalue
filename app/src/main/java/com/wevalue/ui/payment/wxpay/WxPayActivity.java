package com.wevalue.ui.payment.wxpay;


import android.os.Bundle;
import android.view.View;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
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


//微信支付的活动界面
public class WxPayActivity extends BaseActivity implements WZHttpListener {
    private IWXAPI api;
    private String money = "";
    private String paytype = "";
//    private static final String charge = "1", withdraw = "2", transmit = "3", dasahng = "4", buypermission = "5", release = "6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        money = getIntent().getStringExtra("money");
        paytype = getIntent().getStringExtra("paytype");
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
        pay();
    }

    private void pay() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getApplicationContext()));
        map.put("money", money);
        map.put("paytype", paytype);
        map.put("spendtype", Constants.weixinpay);
        NetworkRequest.postRequest(RequestPath.POST_PAYMONEY, map, WxPayActivity.this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            LogUtils.e("msg", jsonObject.toString());
            if (jsonObject.getString("result").equals("1")) {
                PayReq request = new PayReq();
                request.appId = Constants.APP_ID;
                JSONArray datarray = jsonObject.getJSONArray("data");
                JSONArray jsarray = jsonObject.getJSONArray("wxpay_data");
                jsonObject = jsarray.getJSONObject(0);
                request.partnerId = jsonObject.getString("wx_partnerId");
                jsonObject = jsarray.getJSONObject(1);
                request.prepayId = jsonObject.getString("wx_prepayId");
                jsonObject = jsarray.getJSONObject(2);
                request.nonceStr = jsonObject.getString("wx_nonceStr");
                jsonObject = jsarray.getJSONObject(3);
                request.timeStamp = jsonObject.getString("wx_timeStamp");
                jsonObject = jsarray.getJSONObject(4);
                request.packageValue = jsonObject.getString("wx_package");
                jsonObject = jsarray.getJSONObject(5);
                request.sign = jsonObject.getString("wx_sign");
                api.sendReq(request);
                jsonObject = datarray.getJSONObject(0);
                //保存订单编号
//                SharedPreferencesUtil.setOrderNo(this, jsonObject.getString("orderno"));
//                //保存支付方式
//                SharedPreferencesUtil.setSpendtype(this,PaymentConstants. weixinpay);
//                //隐藏进度条
                findViewById(R.id.ll_pgb).setVisibility(View.GONE);
                ShowUtil.showToast(this, "微信支付正常调起...");
            } else {
                ShowUtil.showToast(this, "后台数据异常");
            }
        } catch (JSONException e) {
            ShowUtil.showToast(this, "订单信息解析异常");
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
