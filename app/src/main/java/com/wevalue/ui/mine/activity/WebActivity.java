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
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-08-01.
 */
public class WebActivity extends BaseActivity{

    private ImageView iv_back;
    private TextView tv_head_title;
    private WebView web_mingxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        web_mingxi = (WebView) findViewById(R.id.web_mingxi);
        web_mingxi.getSettings().setBlockNetworkImage(false);
        web_mingxi.getSettings().setLoadsImagesAutomatically(true);
        //支持js
        web_mingxi.getSettings().setJavaScriptEnabled(true);
        tv_head_title.setText("钱包");
        String url = getIntent().getStringExtra("url");
        int isWho = getIntent().getIntExtra("isWho",-1);
        if(isWho==1){
            tv_head_title.setText("碎银说明");
        }else if(isWho==2){
            tv_head_title.setText("权限说明");
        }else if(isWho==3){
            tv_head_title.setText("说明与帮助");
        }else if(isWho==4){
            tv_head_title.setText("获奖用户名单");
        }else if(isWho==5){
            tv_head_title.setText("用户协议");
        }else if(isWho==6){
            tv_head_title.setText("费用说明");
        }else {
            tv_head_title.setText("");
            web_mingxi.loadUrl(RequestPath.SERVER_WEB_PATH+url);
            return;
        }
        getGradeInfo(url);
    }


    /**获取权限说明或 碎银说明  或关于微值**/
    private void getGradeInfo(String url) {

        Map<String ,String > map = new HashMap<>();
        map.put("code", RequestPath.CODE);

        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if(obj.getString("result").equals("1")){
                        String agreement = obj.getString("data");
                        web_mingxi.loadDataWithBaseURL(RequestPath.SERVER_WEB_PATH,agreement, "text/html", "UTF-8",null);

                    }else {

                        LogUtils.e("微值协议 message="+obj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(WebActivity.this, content);
            }
        });

    }
}
