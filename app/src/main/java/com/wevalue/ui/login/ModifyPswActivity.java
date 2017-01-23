package com.wevalue.ui.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 设置新密码界面
 */
public class ModifyPswActivity extends BaseActivity implements OnClickListener, WZHttpListener {

    private EditText et_psw, et_psw_2;
    private ImageView iv_back;
    private TextView tv_head_title, tv_hint;
    private TextView tv_head_right;
    private String psw;

    private String tel;
    private String who;
    private EditText et_old_psw;
    private LinearLayout ll_old;
    private String type;//1= 登录密码修改 ; 2=支付密码修改
    private String payStr;
    private String loginStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_psw);
        payStr = SharedPreferencesUtil.getPayPswStatus(this);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        et_old_psw = (EditText) findViewById(R.id.et_old_psw);
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_psw_2 = (EditText) findViewById(R.id.et_psw_2);
        ll_old = (LinearLayout) findViewById(R.id.ll_old);
        iv_back.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);
        tv_head_right.setText("确定");
        tv_head_right.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        who = getIntent().getStringExtra("who");
        if (who.equals("1")) {//重置密码
            tel = getIntent().getStringExtra("tel");
            tv_head_title.setText("重置登录密码");
        } else if (who.equals("2")) {//修改登录密码
            loginStr = SharedPreferencesUtil.getLoginPswStatus(this);
            switch (loginStr) {
                case "0":
                    tv_head_title.setText("设置登录密码");
                    type = "1";
                    break;
                case "1":
                    ll_old.setVisibility(View.VISIBLE);
                    tv_head_title.setText("修改登录密码");
                    type = "1";
                    break;
            }
        } else if (who.equals("3")) {
            //修改支付密码
            ll_old.setVisibility(View.VISIBLE);
            tv_hint.setText("支付密码");
            tv_head_title.setText("修改支付密码");
            type = "2";
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_head_right:
                modifyPsw();
                break;

        }
    }

    /**
     * 重置密码  --修改密码
     */
    private void modifyPsw() {
        String old_psw = et_old_psw.getText().toString().trim();
        psw = et_psw.getText().toString().trim();
        if (!et_psw_2.getText().toString().trim().equals(psw)) {
            ShowUtil.showToast(ModifyPswActivity.this, "您两次输入的密码不一样!");
            return;
        }
        if(!RegexUtils.isLetterDigitAndChinese(psw)){

            ShowUtil.showToast(ModifyPswActivity.this, "请输入6-16位字母加数字的密码!");
            return;
        }
//		AbRequestParams params = news AbRequestParams();
        String url = "";
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("newpwd", psw);
        switch (who) {
            case "1"://重置登录密码
                map.put("phone", tel);
                url = RequestPath.POST_RESETUSERPWD;
                break;
            case "2"://修改登录密码
                switch (loginStr) {
                    case "0"://设置登录密码
                        map.put("userid", SharedPreferencesUtil.getUid(this));
//				params.put("oldpwd", old_psw);
                        map.put("type", type);
                        url = RequestPath.POST_UPDATEUSERPWD;
                        break;
                    case "1"://修改登录密码
                        map.put("userid", SharedPreferencesUtil.getUid(this));
                        map.put("oldpwd", old_psw);
                        map.put("type", type);
                        url = RequestPath.POST_UPDATEUSERPWD;
                        break;
                }
                break;
            case "3"://修改支付密码
                map.put("userid", SharedPreferencesUtil.getUid(this));
                map.put("oldpwd", old_psw);
                map.put("type", type);
                url = RequestPath.POST_UPDATEUSERPWD;
                break;
        }
        NetworkRequest.postRequest(url, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.getString("result").equals("1")) {
                ShowUtil.showToast(ModifyPswActivity.this, obj.getString("message"));
                finish();
            } else {
                ShowUtil.showToast(ModifyPswActivity.this, obj.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(ModifyPswActivity.this, content);
    }
}
