package com.wevalue.ui.login;


import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户注册,手机验证界面
 */
public class UserRegisterActivity extends BaseActivity implements OnClickListener, WZHttpListener {

    private EditText et_tel, et_code,et_recommend;
    private ImageView iv_back;
    private ImageView iv_isclick_ur;
    private TextView tv_head_title;
    private TextView tv_register;
    private Button but_getcode;
    private String tel;//注册手机号
    private String rtel;//推荐人手机号
    private String requestCode ;//验证码
    private int is_click =0;//用户是否同意了用户协议


    private View prompt_box;
    private PopupWindow promptBoxPopupWindow;
    private WebView promptBox_tv_content_1;


    private int coun;
    private Handler mSendCoedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (coun <= 0) {
                        but_getcode.setText("重新获取");
                        but_getcode.setClickable(true);
                        removeMessages(1);
                        break;
                    }
                    coun--;
                    but_getcode.setText(coun + "s");
                    sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    coun = 60;
                    sendEmptyMessage(1);
                    break;
            }

        }
    };
    private TextView tv_xieyi;
    private String agreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initView();
        getAgreement();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_isclick_ur = (ImageView) findViewById(R.id.iv_isclick_ur);
        but_getcode = (Button) findViewById(R.id.but_getcode);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);

        et_tel = (EditText) findViewById(R.id.et_tel);
        et_code = (EditText) findViewById(R.id.et_code);
        et_recommend = (EditText) findViewById(R.id.et_recommend);

        tv_register.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_xieyi.setOnClickListener(this);
        but_getcode.setOnClickListener(this);
        iv_isclick_ur.setOnClickListener(this);
        tv_head_title.setText("注册用户");
    }

    /**
     * 获取验证码
     */
    private void sendCode() {
        tel = et_tel.getText().toString().trim();
        if (TextUtils.isEmpty(tel) || !RegexUtils.etPhoneRegex(tel)) {
            ShowUtil.showToast(this, "请输入正确的手机号!");
            return;
        }
        but_getcode.setClickable(false);
        mSendCoedHandler.sendEmptyMessage(2);

        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("type", "1");
        map.put("phone", tel);
        NetworkRequest.postRequest(RequestPath.POST_GETCODE, map, this);
    }

    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.but_getcode://获取验证码
                if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
                    return;
                } else {
                    sendCode();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_register:
                if(is_click==1){
                    registerClick();
                }else {
                    ShowUtil.showToast(this,"请同意微值协议");
                }
                break;
            case R.id.iv_isclick_ur:
                if(is_click==1){
                    is_click = 0;
                    iv_isclick_ur.setImageResource(R.mipmap.icon_fabu_weigouxuan);
                }else {
                    is_click = 1;
                    iv_isclick_ur.setImageResource(R.mipmap.iconfont_duihao);
                }
                break;
            case R.id.tv_xieyi:
                initpopu(R.layout.wz_popupwindow_prompt_box_2);
                promptBox_tv_content_1.loadDataWithBaseURL(null, agreement, "text/html", "UTF-8", null);
                promptBoxPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;

        }
    }

    /***/
    private void initpopu(int ID) {
        // 空白区域
        prompt_box = getLayoutInflater().inflate(ID, null);

        promptBox_tv_content_1 = (WebView) prompt_box.findViewById(R.id.promptBox_tv_content_1);

        prompt_box.setOnClickListener(new OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });


        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
    }

    private void getAgreement() {
        String url = RequestPath.GET_GETAGREEMENT;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        agreement = obj.getString("data");
                    } else {
                        LogUtils.e("微值协议 message=" + obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(UserRegisterActivity.this, content);
            }
        });

    }

    /**
     * 点击注册逻辑处理
     */
    private void registerClick() {
        requestCode = "666666";
        rtel = et_recommend.getText().toString().trim();
        if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
            LogUtils.e("log", "  if----");
            return;
        } else {
            if (TextUtils.isEmpty(et_tel.getText().toString().trim()) || !RegexUtils.etPhoneRegex(et_tel.getText().toString().trim())) {
                ShowUtil.showToast(this, "请输入正确的手机号!");
                return;
            } else {
                if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                    ShowUtil.showToast(this, "请输入验证码!");
                    return;
                }
                if (!et_code.getText().toString().trim().equals(requestCode)) {
                    ShowUtil.showToast(this, "验证码不正确！");
                    return;
                }

                HashMap map = new HashMap<>();
                map.put("phone", tel);
                map.put("rphone", rtel);
                map.put("code", "weizhi");
                NetworkRequest.postRequest(RequestPath.POST_QUICKREG_REGUSER, map, this);
            }
        }
    }

    /**
     * 点击下一步逻辑处理
     */
    private void quedingClick() {
        if (ButtontimeUtil.isFastDoubleClick()) {  //两秒之内不能重复点击
            LogUtils.e("log", "  if----");
            return;
        } else {
            if (TextUtils.isEmpty(et_tel.getText().toString().trim()) || !RegexUtils.etPhoneRegex(et_tel.getText().toString().trim())) {
                ShowUtil.showToast(this, "请输入正确的手机号!");
                return;
            } else {
                if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                    ShowUtil.showToast(this, "请输入验证码!");
                    return;
                }
                if (!et_code.getText().toString().trim().equals(requestCode)) {
                    ShowUtil.showToast(this, "验证码不正确！");
                    return;
                }
                //注册
                Intent in = new Intent(this, RegisterActivity.class);
                in.putExtra("code", et_code.getText().toString().trim());
                in.putExtra("tel", tel);
                startActivity(in);
                finish();
            }
        }
    }

    /**
     * 验证码获取结果
     */
    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject obj = new JSONObject(content);
            if (isUrl.contains(RequestPath.POST_QUICKREG_REGUSER)){
               ShowUtil.showToast(UserRegisterActivity.this,obj.getString("message"));
                Intent in = new Intent(this, RegisterSuccessActivity.class);
                startActivity(in);
            }else if (isUrl.contains(RequestPath.POST_GETCODE)){
                if (obj.getString("result").equals("1")) {
                    requestCode = obj.getString("data");
                } else {
                    but_getcode.setClickable(true);
                    but_getcode.setText("获取验证码");
                    mSendCoedHandler.removeMessages(1);
                    ShowUtil.showToast(UserRegisterActivity.this, obj.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(UserRegisterActivity.this, content);
    }
}
