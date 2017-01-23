package com.wevalue.net.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.PopupWindow;

import com.alipay.sdk.app.PayTask;
import com.jungly.gridpasswordview.GridPasswordView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 作者：邹永奎
 * 创建时间：2016/11/5
 * 类说明：支付相关的网络请求工具类
 */
public class PaymentBase implements WZHttpListener {
    private Activity activity;
    private PayInterface payInterface;
    private PopupWindow promptBoxPopupWindow;
    private GridPasswordView gpv_normal;
    private HashMap<String, String> map;
    private String money, paytype, userpaypwd, spendtype, userid;// 订单金额 支付类型 支付密码    支付方式  用户id
    private final int SDK_PAY_FLAG = 1;           //支付宝支付成功标志位
    private String alipayOrderNo;

    /*微信支付相关变量*/
    private IWXAPI api;//微信支付的api
    private String wxOrderNo;//微信支付产生的订单号
    private String outtype;//提现到哪里
    private String outaccount;//提现的账户
    private String outtruename;//提现账户的真实姓名
    private ProgressDialog mProgressDialog;

    public PaymentBase(Activity activity, PayInterface payInterface, PopupWindow promptBoxPopupWindow, GridPasswordView gpv_normal, HashMap map) {
        //碎银支付的构造方法
        this.payInterface = payInterface;
        this.promptBoxPopupWindow = promptBoxPopupWindow;
        this.gpv_normal = gpv_normal;
        this.map = map;
        this.activity = activity;
    }

    public PaymentBase(final Activity activity, PayInterface payInterface, HashMap<String, String> map) {
        //第三方支付的构造方法
        this.activity = activity;
        this.payInterface = payInterface;
        this.map = map;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("正在调起微信支付...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && mProgressDialog.isShowing()) {
                    ShowUtil.showToast(activity, "正在获取订单请求，请稍等哒~");
                    return true;
                }
                return false;
            }
        });
    }

    public PaymentBase(Activity activity) {
        this.activity = activity;
    }


    //初始化本地支付订单
    public void initOrderInfo() {
        userid = SharedPreferencesUtil.getUid(activity.getApplicationContext());
        money = map.get("money");
        paytype = map.get("paytype");
        spendtype = map.get("spendtype");
        switch (spendtype) {
            case Constants.alipay:
                //支付宝支付直接获取订单信息
                obtainOrderInfo();
                break;
            case Constants.weixinpay:
                //微信支付直接获取订单信息
                mProgressDialog.show();
                api = WXAPIFactory.createWXAPI(activity, null);
                api.registerApp(Constants.APP_ID);
                obtainOrderInfo();
                initReceiver();//声明微信支付成功的广播接收者
                break;
            case Constants.suiyinpay:
                userpaypwd = map.get("userpaypwd");
                if (paytype.equals(Constants.withdraw)) {
                    //提现操作 校验支付密码
                    outtype = map.get("outtype");
                    outaccount = map.get("outaccount");
                    outtruename = map.get("outtruename");
                    verifyPayPsw();
                } else if (money.equals("0")) {
                    // 0元订单时请求免费条数
                    HashMap map = new HashMap();
                    map.put("code", RequestPath.CODE);
                    map.put("userid", SharedPreferencesUtil.getUid(activity.getApplicationContext()));
                    NetworkRequest.postRequest(RequestPath.POST_USERPOWER, map, this);
                } else {
                    // 碎银支付时需要验证支付密码
                    verifyPayPsw();
                }
                break;
        }
    }

    //校验用户支付密码的网络请求
    private void verifyPayPsw() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", userid);
        map.put("userpaypwd", userpaypwd);
        NetworkRequest.postRequest(RequestPath.POST_VERIFYPAYCODE, map, this);
    }

    //获取服务器端订单信息
    private void obtainOrderInfo() {
        LogUtils.e("wxdebug", "获取支付订单方法");
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", userid);
        map.put("money", money);
        map.put("paytype", paytype);
        map.put("spendtype", spendtype);
        if (paytype.equals(Constants.withdraw)) {
            map.put("outtype", outtype);
            map.put("outaccount", outaccount);
            map.put("outtruename", outtruename);
        }
        NetworkRequest.postRequest(RequestPath.POST_PAYMONEY, map, this);
    }

    //调用支付宝支付方法
    public void payV2(final String orderinfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
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

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            case RequestPath.POST_USERMONRY:
                try {
                    JSONObject jsonObject = new JSONObject(content);
//                    String result = jsonObject.getString("result");
//                    String message = jsonObject.getString("message");
                    String usermoney = jsonObject.getString("usermoney");
                    //保存我的碎银数量
                    if (!TextUtils.isEmpty(usermoney)) {
                        SharedPreferencesUtil.setSuiYinCount(activity, usermoney);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case RequestPath.POST_USERPOWER:
                try {
                    JSONObject powerObject = new JSONObject(content);
                    int noteNum = Integer.parseInt(powerObject.getString("data"));
                    if (noteNum > 0) {
                        obtainOrderInfo();
                    } else {
                        ShowUtil.showToast(activity.getApplicationContext(), "您的免费信息已经不足了...");
                        activity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //验证支付密码
            case RequestPath.POST_VERIFYPAYCODE:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString("result");
                    if (result.equals("1")) {
                        promptBoxPopupWindow.dismiss();
                        obtainOrderInfo();
                    } else {
                        ShowUtil.showToast(activity.getApplicationContext(), "支付密码有误！");
                        gpv_normal.clearPassword();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //获取支付订单
            case RequestPath.POST_PAYMONEY:
                switch (spendtype) {
                    case Constants.suiyinpay:
                        //碎银支付订单的处理
                        try {
                            HashMap ordermap = new HashMap();
                            JSONObject object = new JSONObject(content);
                            if (object.getString("result").equals("1")) {
                                JSONArray datarray = object.getJSONArray("data");
                                object = datarray.getJSONObject(0);
                                ordermap.put("orderno", object.getString("orderno"));
                                object = datarray.getJSONObject(2);
                                ordermap.put("money", object.getString("ordermoney"));
                                ordermap.put("paytype", paytype);
                                ordermap.put("spendtype", Constants.suiyinpay);
                                payCount(money, "suiyin");
                                payInterface.paySucceed(ordermap);
                                //TODO   重新获取碎银的网络请求
                                getsuiyin();

                            } else {
                                JSONObject jsonObject = new JSONObject(content);
                                ShowUtil.showToast(activity.getApplicationContext(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowUtil.showToast(activity.getApplicationContext(), "数据解析异常...");
                        }
                        break;
                    case Constants.alipay:
                        //支付宝支付订单的处理
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            if (jsonObject.getString("result").equals("1")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                JSONArray alipay_data = jsonObject.getJSONArray("alipay_data");
                                jsonObject = alipay_data.getJSONObject(0);
                                String orderInfo = jsonObject.getString("alipay_data");
                                jsonObject = data.getJSONObject(0);
                                alipayOrderNo = jsonObject.getString("orderno");
                                payV2(orderInfo.replaceAll("#", "\""));
                                ShowUtil.showToast(activity.getApplicationContext(), "正在调起支付宝支付...");
                            } else {
                                ShowUtil.showToast(activity.getApplicationContext(), jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowUtil.showToast(activity.getApplicationContext(), "订单数据解析异常,请稍后重试...");
                        }
                        break;
                    case Constants.weixinpay:
                        //微信支付订单的处理
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            String message = jsonObject.getString("message");
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
                                mProgressDialog.dismiss();
                                api.sendReq(request);
                                jsonObject = datarray.getJSONObject(0);
                                wxOrderNo = jsonObject.getString("orderno");
//                                showToast(activity.getApplicationContext(), "正在调起微信支付...");
                            } else {
                                payInterface.payFail(message);
                                ShowUtil.showToast(activity.getApplicationContext(), "后台数据异常");
                            }
                        } catch (JSONException e) {
                            ShowUtil.showToast(activity.getApplicationContext(), "订单数据解析异常,请稍后重试...");
                            e.printStackTrace();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }


    public void getsuiyin() {
        /*重新获取碎银的网络请求*/
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(activity));
        NetworkRequest.postRequest(RequestPath.POST_USERMONRY, map, this);

    }


    //**/  *支付宝支付成功后的回调方法 */
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
                        ShowUtil.showToast(activity.getApplicationContext(), "支付成功！");
                        HashMap ordermap = new HashMap();
                        ordermap.put("orderno", alipayOrderNo);
                        ordermap.put("money", money);
                        ordermap.put("paytype", paytype);
                        ordermap.put("spendtype", Constants.alipay);
                        payCount(money, "zhifubao");
                        payInterface.paySucceed(ordermap);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ShowUtil.showToast(activity.getApplicationContext(), "支付失败！");
                    }
                    break;
                }
            }
        }
    };
    //定义一个过滤器；
    private IntentFilter intentFilter;
    //定义一个广播监听器；
    private PayResultReceiver payResultReceiver;

    private void initReceiver() {
        //实例化过滤器；
        intentFilter = new IntentFilter();
        //添加过滤的Action值；
        intentFilter.addAction("com.wevalue.payresult");
        //实例化广播监听器；
        payResultReceiver = new PayResultReceiver();
        //将广播监听器和过滤器注册在一起；
        activity.registerReceiver(payResultReceiver, intentFilter);
    }

    /*声明微信支付成功的广播接收者*/
    public class PayResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String errCode = intent.getStringExtra("errCode");
            if (errCode.equals("0")) {
                HashMap ordermap = new HashMap();
                ordermap.put("orderno", wxOrderNo);
                ordermap.put("money", money);
                ordermap.put("paytype", paytype);
                ordermap.put("spendtype", Constants.weixinpay);
                ShowUtil.showToast(activity, "支付成功！");
                LogUtils.e("wxdebug", "成功接收广播并发送支付通知");
                payCount(money, "weixin");
                payInterface.paySucceed(ordermap);
                if (payResultReceiver != null) {
                    activity.unregisterReceiver(payResultReceiver);
                }
            }
        }
    }

    //友盟统计支付的金额
    private void payCount(String money, String spendtype) {
        HashMap map = new HashMap();
        map.put("money", money);
        switch (paytype) {
            case Constants.transmit:
                map.put("spendtype", spendtype);
                MobclickAgent.onEvent(activity, StatisticsConsts.event_transmitDone, map);
                break;
            case Constants.release:
                map.put("spendtype", spendtype);
                MobclickAgent.onEvent(activity, StatisticsConsts.event_releaseDone, map);
                break;
            case Constants.charge:
                map.put("spendtype", spendtype);
                MobclickAgent.onEvent(activity, StatisticsConsts.event_charge, map);
                break;
            case Constants.withdraw:
                LogUtils.e("withdraw", "提现成功");
                MobclickAgent.onEvent(activity, StatisticsConsts.event_withdraw, map);
                break;
        }
    }
}
