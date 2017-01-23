package com.wevalue.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 微值协议界面
 */
public class WeValueAgreementActivity extends BaseActivity implements View.OnClickListener ,WZHttpListener {


    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_Determine;
    private TextView tv_no;
    private TextView tv_yes;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wevalue_agreement);

        initView();
        getAgreement();
    }

    private void getAgreement() {

        String url = RequestPath.GET_GETAGREEMENT;
        Map<String ,String > map = new HashMap<>();
        map.put("code", RequestPath.CODE);

        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("result").equals("1")){
                        tv_content.setText(obj.getString("data"));
                    }else {
                        ShowUtil.showToast(WeValueAgreementActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(WeValueAgreementActivity.this, content);
            }
        });

    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_Determine = (TextView) findViewById(R.id.tv_Determine);
        tv_no = (TextView) findViewById(R.id.tv_no);
        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_head_title.setText("微值协议");


        iv_back.setOnClickListener(this);
        tv_Determine.setOnClickListener(this);
        tv_no.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_Determine:

                intent = new Intent(WeValueAgreementActivity.this,RegisterSuccessActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_no:

                intent = new Intent(WeValueAgreementActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_yes:


                register();
                break;
        }
    }





    /**注册*/
    private void register() {

        String nickname = getIntent().getStringExtra("nickname");
        String sex = getIntent().getStringExtra("sex");
        String addr = getIntent().getStringExtra("addr");
        String jianjie = getIntent().getStringExtra("jianjie");
        String tel = getIntent().getStringExtra("tel");
        String password = getIntent().getStringExtra("password");
        String userlike = getIntent().getStringExtra("userlike");

        String[] addrArr = addr.split(" ");

        Map<String,Object> map = new HashMap<>();

        map.put("code", RequestPath.CODE);
        map.put("usernickname",nickname);
        map.put("usersex",sex);
        map.put("type","1");
        map.put("phone",tel);
        map.put("password",password);
        map.put("userprovince",addrArr[0]);
        map.put("usercity",addrArr[1]);
        map.put("userinfo",jianjie);
        map.put("userlike",userlike);


        NetworkRequest.postRequest(RequestPath.POST_REGUSER, map, this);

    }

    @Override
    public void onSuccess(String content, String isUrl) {

        switch (isUrl){
            case RequestPath.POST_REGUSER:


                try {
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("result").equals("1")){
                        ShowUtil.showToast(WeValueAgreementActivity.this, obj.getString("message"));
                        Intent intent = new Intent(WeValueAgreementActivity.this,RegisterSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        ShowUtil.showToast(WeValueAgreementActivity.this, obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            break;
        }

    }

    @Override
    public void onFailure(String content) {

    }
}
