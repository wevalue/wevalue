package com.wevalue.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.UserRenZhengModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.adapter.RenZhengZhongAdapter;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*用户认证中查看信息的activity*/
public class RenZhengZhongActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_is_zuzhi;
    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_head_right;
    private TextView tv_prompt_content;
    private TextView tv_lianxiren;
    private EditText et_zuzhi_name;//组织名
    private EditText et_true_name;//联系人名
    private EditText et_ID_number;//身份证号
    private EditText et_tel_number;//手机号
    private EditText et_mailbox;//邮箱
    private String userType;
    private String istrue;//用户认证的状态

    private GridView gv_gridView;//
    private RenZhengZhongAdapter mImgAdapter;
    UserRenZhengModel userRenZhengModel;
    private TextView degree1;
    private TextView degree2;
    private TextView degree3;
    private TextView tv_shngqing;
    private TextView tv_tixianzhong;
    private TextView tv_withdraw_succ;
    private LinearLayout ll_biaoji;
    private LinearLayout ll_withdraw;
    List<String> picList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_zheng_zhong);
        picList = new ArrayList<>();
        userType = getIntent().getStringExtra("isWho");
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_is_zuzhi = (LinearLayout) findViewById(R.id.ll_is_zuzhi);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_lianxiren = (TextView) findViewById(R.id.tv_lianxiren);
        tv_prompt_content = (TextView) findViewById(R.id.tv_prompt_content);
        et_zuzhi_name = (EditText) findViewById(R.id.et_zuzhi_name);
        et_true_name = (EditText) findViewById(R.id.et_true_name);
        et_ID_number = (EditText) findViewById(R.id.et_ID_number);
        et_tel_number = (EditText) findViewById(R.id.et_tel_number);
        et_mailbox = (EditText) findViewById(R.id.et_mailbox);
        gv_gridView = (GridView) findViewById(R.id.gv_gridView);
        iv_back.setOnClickListener(this);
        tv_head_right.setText(R.string.Determine);
        tv_head_right.setVisibility(View.VISIBLE);

        getUserData();
        degree1 = (TextView) findViewById(R.id.degree1);
        degree1.setOnClickListener(this);
        degree2 = (TextView) findViewById(R.id.degree2);
        degree2.setOnClickListener(this);
        degree3 = (TextView) findViewById(R.id.degree3);
        degree3.setOnClickListener(this);
        tv_shngqing = (TextView) findViewById(R.id.tv_shngqing);
        tv_shngqing.setOnClickListener(this);
        tv_tixianzhong = (TextView) findViewById(R.id.tv_tixianzhong);
        tv_tixianzhong.setOnClickListener(this);
        tv_withdraw_succ = (TextView) findViewById(R.id.tv_withdraw_succ);
        tv_withdraw_succ.setOnClickListener(this);
        ll_biaoji = (LinearLayout) findViewById(R.id.ll_biaoji);
        ll_biaoji.setOnClickListener(this);
        ll_withdraw = (LinearLayout) findViewById(R.id.ll_withdraw);
        ll_withdraw.setOnClickListener(this);
        if (!TextUtils.isEmpty(userType)) {
            switch (userType) {
                case "1":
                    tv_head_title.setText("用户认证");
                    tv_prompt_content.setHint("(身份证正面照片、反面照片、本人手持身份证正面照片，并确保本人头像及身份证信息清晰完整。)");
                    break;
                case "2":
                    tv_prompt_content.setHint("(营业执照、其他平台账号证明资料、联系人身份证正面照片、反面照片、本人手持身份证正面照片，并确保本人头像及身份证信息清晰完整。)");
                    tv_head_title.setText("组织认证");
                    break;
            }
        }
        tv_head_right.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_head_right:

                break;
            case R.id.tv_qurenzheng:
                Intent intent = new Intent(RenZhengZhongActivity.this, RenZhengActivity.class);
                intent.putExtra("houxurenzheng", "1");
                intent.putExtra("isWho", SharedPreferencesUtil.getUsertype(RenZhengZhongActivity.this));
                startActivity(intent);
                finish();
                break;
        }
    }


    private void getUserData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_USERTRUEINFO, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                Gson gson = new Gson();
                userRenZhengModel = gson.fromJson(content, UserRenZhengModel.class);
                if (1 == userRenZhengModel.getResult()) {
                    istrue = userRenZhengModel.getData().getIstrue();
                    if (TextUtils.isEmpty(istrue)) {
                        return;
                    }
                    setUiData();
                }
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    private void setUiData() {
        Drawable drawable = getResources().getDrawable(R.mipmap.me_withdrawdegree);
        Drawable drawable_2 = getResources().getDrawable(R.mipmap.icon_tixian_shibai);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        drawable_2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界

        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg1())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg1());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg2())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg2());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg3())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg3());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg4())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg4());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg5())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg5());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg6())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg6());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg7())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg7());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg8())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg8());
        }
        if (!TextUtils.isEmpty(userRenZhengModel.getData().getUsertrueimg9())) {
            picList.add(userRenZhengModel.getData().getUsertrueimg9());
        }
        mImgAdapter = new RenZhengZhongAdapter(this, picList);
        gv_gridView.setAdapter(mImgAdapter);
        //0 未认证   1已经认证   2认证中   3认证失败
        SharedPreferencesUtil.setUerAuthentic(this, istrue);
        switch (istrue) {
            case "1":
                degree2.setBackgroundResource(R.color.blue);
                degree3.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_withdraw_succ.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
                tv_withdraw_succ.setCompoundDrawables(null, drawable, null, null);
                break;
            case "2":
                degree2.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
                break;
            case "3":
                degree2.setBackgroundResource(R.color.blue);
                degree3.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_withdraw_succ.setTextColor(getResources().getColor(R.color.orange));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
                tv_withdraw_succ.setCompoundDrawables(null, drawable_2, null, null);
                tv_withdraw_succ.setText("认证失败");
                findViewById(R.id.tv_fail).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_qurenzheng).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_qurenzheng).setOnClickListener(this);
                break;
        }
        et_ID_number.setText(userRenZhengModel.getData().getUseridcard());
        et_tel_number.setText(userRenZhengModel.getData().getTruephone());
        et_mailbox.setText(userRenZhengModel.getData().getTrueemail());
        switch (userType) {
            //1.个人 2.组织
            case "1":
                ll_is_zuzhi.setVisibility(View.GONE);
                et_true_name.setText(userRenZhengModel.getData().getUsername());

                break;
            case "2":
                ll_is_zuzhi.setVisibility(View.VISIBLE);
                tv_lianxiren.setText("联系人 :");
                et_zuzhi_name.setText(userRenZhengModel.getData().getOrgname());
                et_true_name.setText(userRenZhengModel.getData().getUsername());
                break;
        }
    }
}
