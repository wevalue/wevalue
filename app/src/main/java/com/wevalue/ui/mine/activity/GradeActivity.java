package com.wevalue.ui.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 等级详情界面
 */
public class GradeActivity extends BaseActivity {

    private ImageView iv_back;
    private TextView tv_head_title;
    //    private TextView tv_dengji;
    private WebView web_dengji_info;
    private ImageView iv_level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        initView();
        getGradeInfo();
    }


    /**
     * 初始化控件
     **/
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
//        tv_dengji = (TextView) findViewById(R.id.tv_dengji);
        tv_head_title.setText("我的等级");
        String level = SharedPreferencesUtil.getUserleve(this);
//        tv_dengji.setText(level);
        web_dengji_info = (WebView) findViewById(R.id.web_dengji_info);
        iv_level = (ImageView) findViewById(R.id.iv_level);
        switch (level) {
            case "甲":
                iv_level.setImageResource(R.mipmap.level_9);
                break;
            case "乙":
                iv_level.setImageResource(R.mipmap.level_8);
                break;
            case "丙":
                iv_level.setImageResource(R.mipmap.level_7);
                break;
            case "丁":
                iv_level.setImageResource(R.mipmap.level_6);
                break;
            case "戊":
                iv_level.setImageResource(R.mipmap.level_5);
                break;
            case "己":
                iv_level.setImageResource(R.mipmap.level_4);
                break;
            case "庚":
                iv_level.setImageResource(R.mipmap.level_3);
                break;
            case "辛":
                iv_level.setImageResource(R.mipmap.level_2);
                break;
            case "壬":
                iv_level.setImageResource(R.mipmap.level_1);
                break;
            case "癸":
                iv_level.setImageResource(R.mipmap.level_0);
                break;
        }
    }

    /**
     * 获取等级规则
     **/
    private void getGradeInfo() {

        String url = RequestPath.GET_WEIZHILEVEL;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);

        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        String agreement = obj.getString("data");
                        web_dengji_info.loadDataWithBaseURL(null, agreement, "text/html", "UTF-8", null);
                    } else {
                        LogUtils.e("微值协议 message=" + obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(GradeActivity.this, content);
            }
        });
    }
}
