package com.wevalue.ui.we.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.LogUtils;

public class PushContentActivity extends BaseActivity {


    private ImageView iv_back;
    private TextView tv_head_title;
    private WebView web_mingxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_content);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        web_mingxi = (WebView) findViewById(R.id.web_mingxi);
        tv_head_title.setText("消息详情");
        String content = getIntent().getStringExtra("messcontent");
        LogUtils.e("con", content);
        getPushContent(content);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取推送内容
     **/
    private void getPushContent(String content) {
        web_mingxi.loadDataWithBaseURL(RequestPath.SERVER_PATH, content, "text/html", "UTF-8", null);
    }
}
