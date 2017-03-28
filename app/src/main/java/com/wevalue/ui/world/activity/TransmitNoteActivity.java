package com.wevalue.ui.world.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.payment.PaymentBase;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuhua on 2016/8/15.
 * 转发帖子的活动界面
 */
public class TransmitNoteActivity extends BaseActivity implements View.OnClickListener, WZHttpListener, PayInterface {
    private TextView tv_nicheng, tv_shijie;
    private ImageView img_herd;
    private EditText ed_zhuanfa_content;
    private TextView tv_user_nickname;
    private TextView tv_back;
    private TextView tv_nickname;
    private TextView tv_pinglun_content;
    private TextView tv_send_note;
    private TextView tv_head_title;
    //帖子ID
    private String noteId;
    private String isfree = "0";
    private String noteFee = "";
    private String notecontent;
    private String imgurl;
    private String nickname;
    //被转发者的ID
    private String repostid;
    //被转发的信息是否为原创内容
    private String isself;
    private ArrayList<String> World;//世界
    private String paymoney;//支付金额;
    private String spendType;//支付方式
    private String orderNo;//支付订单
    private String notezone = "2";//转发范围
    private String paynum;//发布视频时支付的价格
    private ProgressDialog mProgressDialog;
    private String repostfrom = "3";
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuanfa_info);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在转发信息...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        uid = SharedPreferencesUtil.getUid(this);
        initView();
        initHualunData();
        if (getIntent() != null) {
            noteId = getIntent().getStringExtra("noteId");
            notecontent = getIntent().getStringExtra("notecontent");
            nickname = getIntent().getStringExtra("nickname");
            repostid = getIntent().getStringExtra("repostid");
            isself = getIntent().getStringExtra("isself");
            imgurl = getIntent().getStringExtra("imgurl");
            noteFee = getIntent().getStringExtra("noteFee");
            paynum = getIntent().getStringExtra("paynum");
            if (!TextUtils.isEmpty(getIntent().getStringExtra("repostfrom"))) {
                repostfrom = getIntent().getStringExtra("repostfrom");
            }
            LogUtils.e("repostfrom", getIntent().getStringExtra("repostfrom"));
            LogUtils.e("repostfromstring", getIntent().getStringExtra("repostfrom"));
            tv_user_nickname.setText(nickname);
            tv_pinglun_content.setText(notecontent);
            imgViewSetData(imgurl, img_herd);
        } else {
            noteId = "1";
        }
        if (TextUtils.isEmpty(uid)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void imgViewSetData(String url, ImageView iv) {
        Glide.with(this)
                .load(RequestPath.SERVER_PATH + url)
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shijie:
                if ("0".equals(noteFee)) {
                    //收费帖子
                    if (tv_shijie.getText().toString().equals("圈子")) {
                        tv_shijie.setText("关注");
                        isfree = "0";
                        notezone = "2";
                    } else {
                        tv_shijie.setText("圈子");
                        isfree = "1";
                        notezone = "1";
                    }

                } else if (noteFee.equals("1")) {
                    //免费帖子
//                    tv_shijie.setText("朋友们");
//                    notezone = "1";
                }
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_send_note:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                if (noteFee.equals("0")) {
                    //信息为收费信息  则执行支付流程
//                    HashMap map = new HashMap();
//                    map.put("money", paynum);
//                    map.put("paytype", Constants.transmit);
//                    PopuUtil.initPayPopu(this, this, map);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("paytype", Constants.transmit);
                    hashMap.put("spendtype", Constants.suiyinpay);
                    hashMap.put("money", paynum);//
                    PopuUtil.initPayfom(this, hashMap, this);
                } else if (noteFee.equals("1")) {
                    //转发免费帖子   获取0元的预订单
                    HashMap map = new HashMap();
                    map.put("money", "0");
                    map.put("paytype", Constants.transmit);
                    map.put("spendtype", Constants.suiyinpay);
                    PaymentBase paymentBase = new PaymentBase(this, this, map);
                    paymentBase.initOrderInfo();
                }
                break;
        }
    }

    private void initView() {
        ed_zhuanfa_content = (EditText) findViewById(R.id.ed_zhuanfa_content);
        ed_zhuanfa_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 100) {
                    ShowUtil.showToast(TransmitNoteActivity.this, "100字以内");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tv_shijie = (TextView) findViewById(R.id.tv_shijie);
        tv_user_nickname = (TextView) findViewById(R.id.tv_user_nickname);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_pinglun_content = (TextView) findViewById(R.id.tv_pinglun_content);
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
         /*标题控件*/
        tv_head_title.setText("转发信息");
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_back.setText("取消");
        tv_send_note.setText("发布");
        img_herd = (ImageView) findViewById(R.id.img_head);
        tv_send_note.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_shijie.setOnClickListener(this);
    }

    private OptionsPickerView pvOptions;

    /**
     * 初始化 车轮控件
     */
    private void initHuaLunPicker(ArrayList<String> list) {
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        pvOptions.setPicker(list);
        pvOptions.setLabels("", "");
        pvOptions.setCyclic(false, false, false);
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                tv_shijie.setText(World.get(options1));
                if (options1 == 0) {
                    isfree = "0";
                    notezone = "2";
                } else if (options1 == 1) {
                    isfree = "1";
                    notezone = "1";
                }
            }
        });
    }

    /**
     * 初始化数据
     **/
    public void initHualunData() {
        World = new ArrayList<>();
        World.add("影响力");
        World.add("朋友们");
    }

    //转发帖子的方法
    private void transmitNote() {
        String repostcontent = ed_zhuanfa_content.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteId);
        map.put("repostcontent", repostcontent);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("repostid", repostid);
        map.put("isfree", isfree);
        map.put("isself", isself);
        map.put("paymoney", paymoney);
        map.put("isshare", "0");
        map.put("notezone", notezone);
        map.put("orderno", orderNo);
        map.put("spendtype", spendType);
        map.put("repostfrom", repostfrom);
        LogUtils.e("repostfromnet",repostfrom);
        mProgressDialog.show();

        NetworkRequest.postRequest(RequestPath.POST_ZHUANFA, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject json = new JSONObject(content);
            if (json.getString("result").equals("1")) {
                ShowUtil.showToast(this, json.getString("message"));
                setResult(RESULT_OK);
                mProgressDialog.dismiss();
                finish();
            } else {
                ShowUtil.showToast(this, content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(this, content);
    }

    @Override
    public void paySucceed(HashMap map) {
        paymoney = (String) map.get("money");
        spendType = (String) map.get("spendtype");
        orderNo = (String) map.get("orderno");
        transmitNote();
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {

    }
}