package com.wevalue.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

/**
 * 注册成功界面
 */
public class RegisterSuccessActivity extends BaseActivity {


    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_Determine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);


        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_Determine = (TextView) findViewById(R.id.tv_Determine);
        tv_head_title.setText("注册成功");
        if ("2".equals(SharedPreferencesUtil.getUerAuthentic(this))) {
            ShowUtil.showToast(this, "尊，您的资料我们已经收到，我们会妥善保存您的资料" +
                    "，小微将在两个工作日内确认是否合适，我们将通过电话或邮箱联系您，" +
                    "以确认注册通过还是需要提供进一步的资料，谢谢您！");
        }
        iv_back.setVisibility(View.GONE);
        tv_Determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSuccessActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
