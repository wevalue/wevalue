package com.wevalue.ui.details.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

//用户举报的界面
public class UserReportActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_head_title;

    private RadioButton rb_other;
    private EditText et_content;
    private TextView tv_submit;
    private RadioGroup rg_report;
    private String content;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);
        initView();
    }


    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("举报");

        rb_other = (RadioButton) findViewById(R.id.rb_other);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setOnClickListener(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        rg_report = (RadioGroup) findViewById(R.id.rg_report);
        rg_report.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isChecked = true;
                tv_submit.setBackgroundResource(R.mipmap.me_login_btn);
                switch (checkedId) {
                    case R.id.rb_sq:
                        content = "色情";
                        break;
                    case R.id.rb_qq:
                        content = "侵权";
                        break;
                    case R.id.rb_bl:
                        content = "暴力";
                        break;
                    case R.id.rb_wf:
                        content = "违法";
                        break;
                    case R.id.rb_fb:
                        content = "诽谤";
                        break;
                    case R.id.rb_yy:
                        content = "谣言";
                        break;
                    case R.id.rb_gg:
                        content = "广告";
                        break;
                    case R.id.rb_other:
                        content = "其他";

                        break;
                }
            }
        });
    }

    private void submit() {
        // validate
        if (!isChecked) {
            Toast.makeText(this, "请详细说明问题，我们会尽快回复", Toast.LENGTH_SHORT).show();
            return;
        }
        String etString = et_content.getText().toString().trim();
        if (content.equals("其他")) {
            if (TextUtils.isEmpty(etString)) {
                Toast.makeText(this, "请详细说明问题，我们会尽快回复", Toast.LENGTH_SHORT).show();
                return;
            }
            content = etString;
        }

        // TODO validate success, do something
        postJubao(getIntent().getStringExtra("noteId"), getIntent().getStringExtra("repostid"), new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                Toast.makeText(UserReportActivity.this, "您的问题我们已经收到，将在审核问题后很快给您回复。", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String content) {
                Toast.makeText(UserReportActivity.this, "网络不好，请稍后重试。", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    /**
     * 帖子举报接口
     */
    public void postJubao(String noteid, String repostid, WZHttpListener wzHttpListener) {
        String url = RequestPath.POST_ADDADVICE;
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteid", noteid);
        map.put("repostid", repostid);
        map.put("content", content);
        NetworkRequest.postRequest(url, map, wzHttpListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                submit();
                break;
            case R.id.et_content:
                rb_other.setChecked(true);
                break;
        }
    }
}
