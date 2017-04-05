package com.wevalue.ui.mine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.Interfacerequest.UserEditRequest;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.ModifyPayPswActivity;
import com.wevalue.ui.login.ModifyPswActivity;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class AccountInfoActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_back;

    private TextView tv_head_title;


    private LinearLayout ll_modify_login_psw;
    private LinearLayout ll_pay_psw;
    private LinearLayout ll_wangji_pay_psw;
    private LinearLayout ll_no_pwd;//免密支付开关
    private ImageView iv_no_pwd;//免密支付开关

    private String userTel;//获取记录在本地的用户手机号
    private String userEmail;//获取记录在本地的用户邮箱

    private String payStr;//是否设置过支付密码
    boolean isOpen = false; //当前免密支付状态
    private boolean isHavePaypwd = false; //用户有没有设置支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        userTel = SharedPreferencesUtil.getMobile(this);
        userEmail = SharedPreferencesUtil.getEmail(this);
        payStr = SharedPreferencesUtil.getPayPswStatus(this);
        if ("1".equals(payStr)) {
            isHavePaypwd = true;
            checkedMianMi();
        }
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
        ll_no_pwd = (LinearLayout) findViewById(R.id.ll_no_pwd);
        ll_no_pwd.setOnClickListener(this);
        iv_no_pwd = (ImageView) findViewById(R.id.iv_no_pwd);

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
                    ShowUtil.showToast(this, "请先绑定手机号");
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
            case R.id.ll_no_pwd://开启或关闭免密支付

                //如果短时间内连点 则 不执行点击方法
                if (!ButtontimeUtil.isFastDoubleClick()) {
                    if (TextUtils.isEmpty(SharedPreferencesUtil.getMobile(this))) {
                        Intent it = new Intent(this, BindingTelEmailActivity.class);
                        it.putExtra("who", "tel");
                        this.startActivity(it);
                        ShowUtil.showToast(this, "请先绑定手机号");
                    } else if (!"1".equals(SharedPreferencesUtil.getPayPswStatus(this))){
                        //设置支付密码
                        Intent set = new Intent();
                        set.setClass(this, SetPayPswActivity.class);
                        set.putExtra("isSet", "set");
                        this.startActivity(set);
                        ShowUtil.showToast(this, "请设置支付密码");
                    }else{
                        openMianMi(!isOpen);
                    }

                }
                break;
        }
    }

    /**
     * 开启或关闭免密支付
     *
     * @param
     */
    private void openMianMi(final boolean b) {
        if (!isHavePaypwd) {
            Intent intent = new Intent(this, SetPayPswActivity.class);
            intent.putExtra("isSet", "set");
            startActivity(intent);
            ShowUtil.showToast(this, "请先去设置支付密码");
            return;
        }
        loadingDialog.show();
        UserEditRequest request = UserEditRequest.initUserEditRequest(this);
        request.openOnepay("5", b, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                if (loadingDialog.isShowing()) loadingDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(content);
                    String result = object.getString("result");
                    if ("1".equals(result) && !isOpen) {
                        ShowUtil.showToast(context, "开启免密支付");
                        iv_no_pwd.setImageResource(R.mipmap.ic_open);
                        isOpen = true;
                    } else if ("1".equals(result) && isOpen) {
                        ShowUtil.showToast(context, "关闭免密支付");
                        iv_no_pwd.setImageResource(R.mipmap.ic_closs);
                        isOpen = false;
                    } else {
                        ShowUtil.showToast(context, "网络繁忙，请稍后再试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowUtil.showToast(context, "网络繁忙，请稍后再试");
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(context, "网络繁忙，请稍后再试");
                if (loadingDialog.isShowing()) loadingDialog.dismiss();
            }
        });
    }


    //是否开启了免密支付
    private void checkedMianMi() {
        String userid = SharedPreferencesUtil.getUid(this);
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", userid);
        //每个链接都会自动加 logintoken 所以在此不用再加了
        // map.put("logintoken", logintoken);
        NetworkRequest.postRequest(RequestPath.POST_CHECKONEPAY, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                Log.e("checkedMianMi", "content = " + content);
                try {
                    JSONObject object = new JSONObject(content);
                    String result = object.getString("result");
                    //判断数据是否成功返回 如果没有开启免密支付 则返回 0
                    if ("1".equals(result)) {
                        iv_no_pwd.setImageResource(R.mipmap.ic_open);
                        isOpen = true;
                    } else {
                        iv_no_pwd.setImageResource(R.mipmap.ic_closs);
                        isOpen = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowUtil.showToast(context, "网络繁忙，请稍后再试");
                }
            }

            @Override
            public void onFailure(String content) {
                Log.e("checkedMianMi", "content = " + content);
                ShowUtil.showToast(context, "content");
            }
        });
    }
}
