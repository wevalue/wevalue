package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016-08-01.
 */
public class MyQuanxianActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {

    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_quanxian_info;
    private TextView tv_prompt_quanxian;
    private TextView tv_quanxian_num;
    private String noteNum; //  可以免费发布的帖子数目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quanxian);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_quanxian_num.setText(SharedPreferencesUtil.getUserPower(this) + "\r\n" + "条");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_quanxian_info = (TextView) findViewById(R.id.tv_quanxian_info);
        tv_quanxian_num = (TextView) findViewById(R.id.tv_quanxian_num);
        tv_head_title.setText("我的权限");

        iv_back.setOnClickListener(this);
        tv_quanxian_info.setOnClickListener(this);
        tv_quanxian_info.setText(Html.fromHtml("<u>" + "权限说明" + "</u>"));
        tv_prompt_quanxian = (TextView) findViewById(R.id.tv_prompt_quanxian);
        tv_prompt_quanxian.setOnClickListener(this);

        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERPOWER, map, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_quanxian_info:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url", RequestPath.GET_WEIZHIPOWER);
                intent.putExtra("isWho", 2);
                startActivity(intent);
                break;
            case R.id.tv_prompt_quanxian:
                Intent intent1 = new Intent(MyQuanxianActivity.this, BuyPermissionActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.getString("result").equals("1")) {
                noteNum = jsonObject.getString("data");
                tv_quanxian_num.setText(noteNum + "\r\n" + "条");
                if (!TextUtils.isEmpty(noteNum)) {
                    SharedPreferencesUtil.setUserPower(this, noteNum);
                }
            } else {
                ShowUtil.showToast(this, jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ShowUtil.showToast(this, "服务器数据解析异常，暂时无法获取免费发布的信息条数...");
        }
    }

    @Override
    public void onFailure(String content) {

    }
}
