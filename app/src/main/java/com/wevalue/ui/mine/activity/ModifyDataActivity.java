package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import java.util.Map;


/**
 * 修改资料
 */
public class ModifyDataActivity extends BaseActivity implements OnClickListener {

    private TextView tv_head_title, tv_head_right, tv_xiugai_tishi;
    private EditText et_set_nickname;
    private ImageView iv_back;
    private ImageView iv_delete;
    private String nickname;//修改前的
    private String newNickname;
    private String isWho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {


        nickname = getIntent().getStringExtra("nickname");
        isWho = getIntent().getStringExtra("isWho");
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_xiugai_tishi = (TextView) findViewById(R.id.tv_xiugai_tishi);


        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("确定");
        et_set_nickname = (EditText) findViewById(R.id.et_set_nickname);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        if (!TextUtils.isEmpty(nickname)) {
            et_set_nickname.setText(nickname);
        }

        if (isWho.equals("1")) {
            tv_head_title.setText("修改昵称");
            et_set_nickname.setHint("请设置昵称");
            et_set_nickname.setSingleLine();
            tv_xiugai_tishi.setText("10字以内，可由中英文、数字、“_”、 “-”组成");
            et_set_nickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); //最大输入长度
            et_set_nickname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 10) {
                        ShowUtil.showToast(ModifyDataActivity.this, "十字以内");
                    }
                    //限制用户昵称出现特殊字符
                    String s = et_set_nickname.getText().toString();
                    if (s.contains("\'")) {
                        et_set_nickname.setText(s.replace("\'", ""));
                    }
                    if (s.contains("\"")) {
                        et_set_nickname.setText(s.replace("\"", ""));
                    }
                    if (s.contains("\\")) {
                        et_set_nickname.setText(s.replace("\\", ""));
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else if (isWho.equals("2")) {
            tv_head_title.setText("修改简介");
//			tv_xiugai_tishi.setVisibility(View.GONE);
            tv_xiugai_tishi.setText("20字以内，可由中英文、数字、“_”、 “-”组成");
            et_set_nickname.setHint("请输入您的简介");
            et_set_nickname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
            et_set_nickname.setLines(2);
            et_set_nickname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 20) {
                        ShowUtil.showToast(ModifyDataActivity.this, "二十字以内");

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_head_right:
                newNickname = et_set_nickname.getText().toString();
                returnData();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_delete:
                et_set_nickname.setText("");
                break;
        }

    }

    /**
     * 修改昵称
     */
    private void returnData() {

        if (TextUtils.isEmpty(newNickname)) {

            if (isWho.equals("1")) {
                ShowUtil.showToast(this, "请设置昵称!");
            } else if (isWho.equals("2")) {
                ShowUtil.showToast(this, "请输入简介!");
            }
            return;
        }
        if (newNickname.equals(nickname)) {
            finish();
        } else {

            String url = RequestPath.POST_UPDATEUSERINFO;
            Map<String, Object> map = new HashMap<>();
            map.put("code", RequestPath.CODE);
            map.put("userid", SharedPreferencesUtil.getUid(this));
            if (isWho.equals("1")) {
                map.put("usernickname", newNickname);
            } else if (isWho.equals("2")) {
                map.put("userinfo", newNickname);
            }


            NetworkRequest.postRequest(url, map, new WZHttpListener() {
                @Override
                public void onSuccess(String content, String isUrl) {
                    try {
                        JSONObject obj = new JSONObject(content);

                        if (obj.getString("result").equals("1")) {
                            ShowUtil.showToast(ModifyDataActivity.this, obj.getString("message"));
                            Intent it = new Intent();
                            it.putExtra("nickname", newNickname);
                            setResult(RESULT_OK, it);
                            finish();
                        } else {
                            ShowUtil.showToast(ModifyDataActivity.this, obj.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(String content) {
                    ShowUtil.showToast(ModifyDataActivity.this, content);
                }
            });


        }

    }


}
