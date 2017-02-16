package com.wevalue.ui.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import java.util.HashMap;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener, PayInterface {
    private ImageView iv_back;
    private TextView tv_head_title;
    private EditText et_account;
    private EditText et_money;
    private TextView iv_btn_withdraw;
    private String outtype;//提现的类型
    String account;
    String money;
    private EditText et_truename;
    String truename;
    private LinearLayout ll_account;
    String level[] = {"癸", "壬", "辛", "庚", "己", "戊", "丁", "丙", "乙", "甲"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        outtype = getIntent().getStringExtra("outtype");
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("提现");
        et_account = (EditText) findViewById(R.id.et_account);
        et_money = (EditText) findViewById(R.id.et_money);
        iv_btn_withdraw = (TextView) findViewById(R.id.iv_btn_withdraw);
        iv_back.setOnClickListener(this);
        iv_btn_withdraw.setOnClickListener(this);
        et_truename = (EditText) findViewById(R.id.et_truename);
        ll_account = (LinearLayout) findViewById(R.id.ll_account);
        if (outtype.equals(Constants.wxwithdraw)) {
            ll_account.setVisibility(View.GONE);
        }
    }

    private void submit() {
        // validate 微信提现不需要账号信息
        if (outtype.equals(Constants.aliwithdraw)) {
            account = et_account.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                Toast.makeText(this, "请填写支付宝账号", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        money = et_money.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ShowUtil.showToast(this, "请输入金额");
            return;
        }
        int userLevel = SharedPreferencesUtil.getUserUserLevelInt(this);
        double userCanWithDraw = Double.parseDouble(SharedPreferencesUtil.getUserCanWithDraw(this));
        double userMoney = Double.parseDouble(SharedPreferencesUtil.getSuiYinCount(this));
        LogUtils.e("userLevel", userLevel + "");
        LogUtils.e("userLevel", userCanWithDraw + "");
        if (userMoney < Double.parseDouble(money)) {
            ShowUtil.showToast(WithdrawActivity.this, "提现金额不能大于碎银余额哦！");
            return;
        }
        if (userLevel == 4 && (userCanWithDraw < Double.parseDouble(money))) {
            ShowUtil.showToast(this, "留些许碎银吧，用户等级达到丙级可以自由提现，加油！");
            return;
        }
        truename = et_truename.getText().toString().trim();
        if (TextUtils.isEmpty(truename)) {
            Toast.makeText(this, "请输入您的真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        HashMap map = new HashMap();
        map.put("spendtype", Constants.suiyinpay);
        map.put("paytype", Constants.withdraw);
        map.put("money", money);
        if (outtype.equals(Constants.aliwithdraw)) {
            map.put("outaccount", account);
        }
        map.put("outtype", outtype);
        map.put("outtruename", truename);
        PopuUtil.initPayPopu(WithdrawActivity.this, WithdrawActivity.this, map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_btn_withdraw:
                submit();
                break;
        }
    }

    @Override
    public void paySucceed(HashMap map) {
        String paytype = (String) map.get("paytype");
        if (paytype.equals(Constants.withdraw)) {
            ShowUtil.showToast(this, "提现申请操作成功！");
        }
        finish();
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {
        ShowUtil.showToast(this, "提现申请失败！"+failString);
    }
}
