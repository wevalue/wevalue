package com.wevalue.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.QuanXianEntity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.mine.adapter.QuanXianAdapter;
import com.wevalue.utils.Constants;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class BuyPermissionActivity extends BaseActivity implements View.OnClickListener, PayInterface, WZHttpListener {


    private TextView tv_buy_now;
    private ImageView iv_back;
    private TextView tv_head_title;
    private String money;//购买帖子花费的金钱
    private String number;//购买帖子的条数
    private String spendtype;//订单支付方式
    private String orderno;//订单编号

    private NoScrollListview lv_quaxian;
    private List<QuanXianEntity.DataBean> mDatas;
    private QuanXianAdapter mAdapter;
    private int mIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_permission);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, StatisticsConsts.event_buyLimits);

    }

    private void initView() {
        tv_buy_now = (TextView) findViewById(R.id.tv_buy_now);
        lv_quaxian = (NoScrollListview) findViewById(R.id.lv_quaxian);

        tv_buy_now.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("提升权限");
        getQuanXianData();
        lv_quaxian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mIndex = position;
                parent.getChildAt(position).setBackgroundResource(R.mipmap.me_login_btn);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (position != i) {
                        parent.getChildAt(i).setBackgroundResource(R.mipmap.me_rect);
                    }

                }

            }
        });
    }

    private void getQuanXianData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(RequestPath.GET_GETPOWERLIST, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                QuanXianEntity entity = new Gson().fromJson(content, QuanXianEntity.class);
                if (entity.getResult() == 1) {
                    if (entity.getData() != null && entity.getData().size() > 0) {
                        mDatas = entity.getData();
                        mAdapter = new QuanXianAdapter(mDatas, BuyPermissionActivity.this);
                        lv_quaxian.setAdapter(mAdapter);
                    }
                }

            }

            @Override
            public void onFailure(String content) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_buy_now:
                if (mIndex == -1) {
                    ShowUtil.showToast(this, "请先选择套餐类型！");
                } else {
                    number = mDatas.get(mIndex).getNumber();
                    HashMap map = new HashMap();
                    map.put("paytype", Constants.buypermission);
                    map.put("taocan", "购买权限");
                    map.put("money", mDatas.get(mIndex).getMoney());
                    PopuUtil.initPayPopu(this, this, map);
                }
                break;
        }
    }

    //校验支付成功的方法
    private void verifyPayState() {
        HashMap map = new HashMap();
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("code", RequestPath.CODE);
        map.put("money", money);
        map.put("number", number);
        map.put("spendtype", spendtype);
        map.put("orderno", orderno);
        NetworkRequest.postRequest(RequestPath.POST_BUYPOWERSUCC, map, BuyPermissionActivity.this);
    }

    @Override
    public void paySucceed(HashMap map) {
        money = (String) map.get("money");
        spendtype = (String) map.get("spendtype");
        orderno = (String) map.get("orderno");
        verifyPayState();
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {

    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            case RequestPath.POST_USERMONRY:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String usermoney = jsonObject.getString("usermoney");
                    //保存我的碎银数量
                    if (!TextUtils.isEmpty(usermoney)) {
                        SharedPreferencesUtil.setSuiYinCount(this, usermoney);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case RequestPath.POST_BUYPOWERSUCC:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    ShowUtil.showToast(this, jsonObject.getString("message"));
                    HashMap map = new HashMap();
                    map.put("money", money+"元");
                    MobclickAgent.onEvent(BuyPermissionActivity.this, StatisticsConsts.event_buyLimitsDone, map);
                    getsuiyin();
                    getUserPower();
                } catch (JSONException e) {
                    e.printStackTrace();
                    ShowUtil.showToast(this, "后台数据异常...");
                }
                break;
            case RequestPath.POST_USERPOWER:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String userpower = jsonObject.getString("data");
                    if (jsonObject.getString("result").equals("1") && !TextUtils.isEmpty(userpower)) {
                        SharedPreferencesUtil.setUserPower(this, userpower);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ShowUtil.showToast(this, "后台数据异常...");
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
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERMONRY, map, this);
    }

    public void getUserPower() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERPOWER, map, this);
    }
}
