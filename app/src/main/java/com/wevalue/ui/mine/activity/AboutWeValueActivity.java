package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;

import static com.wevalue.R.id.tv_banben;

/**
 * Created by Administrator on 2016-11-21.
 * 关于微值界面
 */

public class AboutWeValueActivity extends BaseActivity implements View.OnClickListener{


    private ImageView iv_back;
    private TextView tv_head_title, tv_banbenhao;
    private RelativeLayout rl_bangzhu;
    private RelativeLayout rl_xieyi;
    private RelativeLayout rl_cost_statement;
    private String localVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_wevalue);


        initView();
        getAppVersion();
    }

    //获取版本信息
    private void getAppVersion() {
        PackageManager manager;
        PackageInfo info = null;
        manager = getPackageManager();
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            localVersion = info.versionName;
            tv_banbenhao.setText("微值 v" + localVersion );

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //初始化控件
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_banbenhao = (TextView) findViewById(R.id.tv_banbenhao);

        tv_head_title.setText("关于微值");

        rl_bangzhu = (RelativeLayout) findViewById(R.id.rl_bangzhu);
        rl_xieyi = (RelativeLayout) findViewById(R.id.rl_xieyi);
        rl_cost_statement = (RelativeLayout) findViewById(R.id.rl_cost_statement);


        iv_back.setOnClickListener(this);
        rl_xieyi.setOnClickListener(this);
        rl_cost_statement.setOnClickListener(this);
        rl_bangzhu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent in;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
            break;
            case R.id.rl_bangzhu:
                in = new Intent(this, WebActivity.class);
                in.putExtra("url", RequestPath.POST_ABOUTUS);
                in.putExtra("isWho", 3);
                startActivity(in);
            break;
            case R.id.rl_xieyi:

                in = new Intent(this, WebActivity.class);
                in.putExtra("url", RequestPath.GET_GETAGREEMENT);
                in.putExtra("isWho", 5);
                startActivity(in);
            break;
            case R.id.rl_cost_statement:
                in = new Intent(this, WebActivity.class);
                in.putExtra("url", RequestPath.GET_COST_STATEMENT);
                in.putExtra("isWho", 6);
                startActivity(in);
                break;

        }
    }
}
