package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.ui.login.ModifyPayPswActivity;
import com.wevalue.ui.login.ModifyPswActivity;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;


public class AccountInfoActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;
    private TextView tv_head_title;


    private LinearLayout ll_modify_login_psw;
    private LinearLayout ll_pay_psw;
    private LinearLayout ll_wangji_pay_psw;


    private String userTel;//获取记录在本地的用户手机号
    private String userEmail;//获取记录在本地的用户邮箱

    private String payStr;//是否设置过支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        userTel = SharedPreferencesUtil.getMobile(this);
        userEmail = SharedPreferencesUtil.getEmail(this);
        payStr = SharedPreferencesUtil.getPayPswStatus(this);
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);

        tv_head_title.setText("账户与安全");

        ll_modify_login_psw = (LinearLayout) findViewById(R.id.ll_modify_login_psw);
        ll_pay_psw = (LinearLayout) findViewById(R.id.ll_pay_psw);
        ll_wangji_pay_psw = (LinearLayout) findViewById(R.id.ll_wangji_pay_psw);


        iv_back.setOnClickListener(this);

        ll_modify_login_psw.setOnClickListener(this);
        ll_pay_psw.setOnClickListener(this);
        ll_wangji_pay_psw.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_modify_login_psw://修改登录密码

                if (!TextUtils.isEmpty(userTel) || !TextUtils.isEmpty(userEmail)) {
                    intent = new Intent(this, ModifyPswActivity.class);
                    intent.putExtra("who", "2");
                    startActivity(intent);
                } else {
                    ShowUtil.showToast(this, "您还没有绑定手机或者邮箱");
                }

                break;

            case R.id.ll_pay_psw://支付密码

                switch (payStr) {
                    case "1"://修改支付密码
                        intent = new Intent(this, ModifyPayPswActivity.class);
                        intent.putExtra("who", "3");
                        startActivity(intent);
                        break;
                    case "0"://设置支付密码
                        intent = new Intent(this, SetPayPswActivity.class);
                        intent.putExtra("isSet", "set");
                        startActivity(intent);
                        break;
                }


                break;
            case R.id.ll_wangji_pay_psw://忘记支付密码

                switch (payStr) {
                    case "1"://设置过支付密码 ,进入到忘记支付密码界面
                        intent = new Intent(this, SetPayPswActivity.class);
                        intent.putExtra("isSet", "modify");
                        startActivity(intent);
                        break;
                    case "0"://没有设置过支付密码进入到 设置支付密码界面
                        intent = new Intent(this, SetPayPswActivity.class);
                        intent.putExtra("isSet", "set");
                        startActivity(intent);
                        break;
                }


                break;

        }


    }


}
