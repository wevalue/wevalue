package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_head_title;
    private RelativeLayout rl_shouzhimingxi;
    private RelativeLayout rl_suiyin;
    private RelativeLayout rl_quanxian;
    private TextView tv_suiyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        getsuiyin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_suiyin.setText("¥ " + SharedPreferencesUtil.getSuiYinCount(WeValueApplication.applicationContext));
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("银库");
        rl_suiyin = (RelativeLayout) findViewById(R.id.rl_suiyin);
        rl_shouzhimingxi = (RelativeLayout) findViewById(R.id.rl_shouzhimingxi);
        rl_quanxian = (RelativeLayout) findViewById(R.id.rl_quanxian);
        iv_back.setOnClickListener(this);
        rl_suiyin.setOnClickListener(this);
        rl_shouzhimingxi.setOnClickListener(this);
        rl_quanxian.setOnClickListener(this);
        tv_suiyin = (TextView) findViewById(R.id.tv_suiyin);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_suiyin:
                intent = new Intent(this, MySuiYinActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_shouzhimingxi:
                intent = new Intent(this, PaymentDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_quanxian:
                intent = new Intent(this, MyQuanxianActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void getsuiyin() {
        /*重新获取碎银的网络请求*/
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERMONRY, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String usermoney = jsonObject.getString("usermoney");
                    String usemoney = jsonObject.getString("usemoney");
                    //保存我的碎银数量
                    if (!TextUtils.isEmpty(usermoney)) {
                        SharedPreferencesUtil.setSuiYinCount(WalletActivity.this, usermoney);
                        tv_suiyin.setText("¥ " + usermoney);
                    }
                    //保存可提现碎银的数量
                    if (!TextUtils.isEmpty(usemoney)) {
                        SharedPreferencesUtil.setUserCanWithDraw(WalletActivity.this, usemoney);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

}
