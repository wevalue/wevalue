package com.wevalue.ui.mine.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016-08-01.
 */
public class MySuiYinActivity extends BaseActivity implements View.OnClickListener, PayInterface, WZHttpListener {

    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_suiyin_guice;
    private TextView tv_charge;
    private TextView tv_withdraw;
    private TextView tv_suiyin_num;
    private String money;//订单金额
    private String orderno = "";//订单号
    private String spendtype;// 支付方式
    private IWXAPI api;//微信支付的api


    String nickname;
    String openid;
    String headimgurl;
    String level[] = {"癸", "壬", "辛", "庚", "己", "戊", "丁", "丙", "乙", "甲"};
//    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_my_suiyin);
        initView();

        getsuiyin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_suiyin_num.setText("¥ " + SharedPreferencesUtil.getSuiYinCount(WeValueApplication.applicationContext));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_suiyin_guice = (TextView) findViewById(R.id.tv_suiyin_guice);
        tv_head_title.setText("我的碎银");
        iv_back.setOnClickListener(this);
        tv_suiyin_guice.setText(Html.fromHtml("<u>" + "碎银说明" + "</u>"));
        tv_suiyin_guice.setOnClickListener(this);
        tv_charge = (TextView) findViewById(R.id.tv_charge);
        tv_charge.setOnClickListener(this);
        tv_withdraw = (TextView) findViewById(R.id.tv_withdraw);
        tv_suiyin_num = (TextView) findViewById(R.id.tv_suiyin_num);
        tv_withdraw.setOnClickListener(this);
    }

    private void initWxLogin() {
        //api注册
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信息
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_demo_test";
        //向微信发送请求
        api.sendReq(req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_suiyin_guice:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", RequestPath.GET_WEIZHIMONEY);
                intent.putExtra("isWho", 1);
                startActivity(intent);
                break;
            case R.id.tv_charge:
                new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("支付宝", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                HashMap map = new HashMap();
                                map.put("paytype", Constants.charge);
                                map.put("spendtype", Constants.alipay);
                                PopuUtil.initPayPopu(MySuiYinActivity.this, MySuiYinActivity.this, map);
                            }
                        })
                        .addSheetItem("微信", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                HashMap map = new HashMap();
                                map.put("paytype", Constants.charge);
                                map.put("spendtype", Constants.weixinpay);
                                PopuUtil.initPayPopu(MySuiYinActivity.this, MySuiYinActivity.this, map);
                            }
                        }).show();
                break;
            case R.id.tv_withdraw:
                int userLevel = SharedPreferencesUtil.getUserUserLevelInt(this);
                LogUtils.e("userLevel", userLevel + "");
                if (userLevel < 4) {
                    ShowUtil.showToast(this, "尊，用户等级达到己级才可以提现，邀请朋友们一起来传播价值吧。");
                    return;
                }
                if (!SharedPreferencesUtil.getUerAuthentic(this).equals("1")) {
                    ShowUtil.showToast(MySuiYinActivity.this, "您还未通过认证，请您认证后再执行提现操作哦！");
                    return;
                }
                new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("支付宝", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //选择了提现到支付宝
                                Intent intent = new Intent(MySuiYinActivity.this, WithdrawActivity.class);
                                intent.putExtra("outtype", Constants.aliwithdraw);
                                startActivity(intent);
                            }
                        })
                        .addSheetItem("微信", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
//                                //选择提现到微信
                                initWxLogin();
                                initReceiver();
                            }
                        }).show();
                break;
        }
    }

    //请求后台信息判断是否充值成功
    private void checkPayState(String orderno) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("money", money);
        map.put("orderno", orderno);
        map.put("spendtype", spendtype);
        NetworkRequest.postRequest(RequestPath.POST_RECHARGE_SUCC, map, this);
    }

    /*绑定微信支付账号*/
    private void bindWxWithdraw() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("userwxname", nickname);
//        map.put("userwxface", headimgurl);
        map.put("userwxcode", openid);
        NetworkRequest.postRequest(RequestPath.POST_BINDWXCODE, map, this);
    }

    @Override
    public void paySucceed(HashMap map) {
        money = (String) map.get("money");
        spendtype = (String) map.get("spendtype");
        if (orderno.equals(map.get("orderno"))) {
            // TODO: 2016/12/19 保证同一个订单号只能请求一次验证支付是否成功的接口
            return;
        }
        orderno = (String) map.get("orderno");
        LogUtils.e("wxdebug", "paySucceed" + orderno);
        checkPayState((String) map.get("orderno"));
//        mProgressDialog.dismiss();
    }

    @Override
    public void payStart(String startType) {
//        if (startType.equals(Constants.weixinpay)) {
//            mProgressDialog.show();
//        }
    }

    @Override
    public void payFail(String failString) {
//        mProgressDialog.dismiss();
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        JSONObject jsonObject = null;
        switch (isUrl) {
            case RequestPath.POST_RECHARGE_SUCC:
                try {
                    jsonObject = new JSONObject(content);
                    ShowUtil.showToast(this, jsonObject.getString("message"));
                    String usermoney = jsonObject.getString("usermoney");
                    MobclickAgent.onEvent(MySuiYinActivity.this, "event_charge", new HashMap<String, String>().put("money", money));
                    //保存我的碎银数量
                    LogUtils.e("usermoney", usermoney);
                    if (!TextUtils.isEmpty(usermoney)) {
                        tv_suiyin_num.setText("¥ " + usermoney);
                        SharedPreferencesUtil.setSuiYinCount(this, usermoney);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_BINDWXCODE:
                String result = "";
                try {
                    jsonObject = new JSONObject(content);
                    String message = jsonObject.getString("message");
                    ShowUtil.showToast(this, message);
                    result = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("1")) {
                    Intent intent = new Intent(MySuiYinActivity.this, WithdrawActivity.class);
                    intent.putExtra("outtype", Constants.wxwithdraw);
                    startActivity(intent);
                }
                break;
            case RequestPath.POST_USERMONRY:
                try {
                    jsonObject = new JSONObject(content);
                    String usermoney = jsonObject.getString("usermoney");
                    String usemoney = jsonObject.getString("usemoney");
                    //保存我的碎银数量
                    if (!TextUtils.isEmpty(usermoney)) {
                        SharedPreferencesUtil.setSuiYinCount(this, usermoney);
                        tv_suiyin_num.setText("¥ " + usermoney);
                    }
                    //保存可提现碎银的数量
                    if (!TextUtils.isEmpty(usemoney)) {
                        SharedPreferencesUtil.setUserCanWithDraw(this, usemoney);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
    }

    //定义一个过滤器；
    private IntentFilter intentFilter;
    //定义一个广播监听器；
    private WxLoginReceiver wxLoginReceiver;

    private void initReceiver() {
        //实例化过滤器；
        intentFilter = new IntentFilter();
        //添加过滤的Action值；
        intentFilter.addAction("com.wevalue.wxlogin");
        //实例化广播监听器；
        wxLoginReceiver = new WxLoginReceiver();
        //将广播监听器和过滤器注册在一起；
        registerReceiver(wxLoginReceiver, intentFilter);
    }


    /*声明微信登录成功的广播界接收者*/
    public class WxLoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("request").equals("1")) {
                nickname = intent.getStringExtra("nickname");
                openid = intent.getStringExtra("openid");
                headimgurl = intent.getStringExtra("headimgurl");
                bindWxWithdraw();
            } else {
                ShowUtil.showToast(MySuiYinActivity.this, "服务器异常，请稍后重试...");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxLoginReceiver != null) {
            unregisterReceiver(wxLoginReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void getsuiyin() {
        /*重新获取碎银的网络请求*/
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERMONRY, map, this);
    }
}
