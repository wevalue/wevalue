package com.wevalue.ui.login;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.Area;
import com.wevalue.model.City;
import com.wevalue.model.Province;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.RegexUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements OnClickListener, WZHttpListener {
    String checkresult = "";
    private TextView tv_register_but;
    private TextView tv_xieyi;
    private TextView tv_set_sex;
    private TextView tv_choice_city;
    private TextView tv_choice_attr;
    private EditText et_user_name, et_jianjie, et_psw, et_psw_2;
    private ImageView iv_back;
    private TextView tv_head_right;
    private TextView tv_head_title;
    private String tel;
    private String code;
    private String sex = null;

    private DbUtils dbUtils;
    private ArrayList<Province> provinceList;
    private List<City> cityList;
    private ArrayList<ArrayList<String>> cityItems = new ArrayList<>();
    private List<Area> areaList;


    private OptionsPickerView pvOptions;
    private int isSexAndAttr = -1;

    private View prompt_box;
    private PopupWindow promptBoxPopupWindow;
    private TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
    private String isUsertype = null;
    private String agreement;
    private String zuzhiName;
    private String trueName;
    private String IDnumber;
    private String telNumber;
    private String mailbox;
    private String usertype = "1";
    private boolean isAuthenticType = false;//判断用户是否为待认证类型
    private String istrueinfo = "0";//判断用户是否已经提交认证信息
    public boolean hasFocus = false;//
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbUtils = DbUtils.create(this, "WeValue.db");
//        getAgreement();
        initView();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在注册...");
        mProgressDialog.setCanceledOnTouchOutside(false);
//        initAddressPicker();
    }

    /**
     * 初始化 车轮控件
     */
    private void initAddressPicker() {
        try {
            cityList = new ArrayList<>();
            cityList = dbUtils.findAll(Selector.from(City.class));
            areaList = new ArrayList<>();
            areaList = dbUtils.findAll(Selector.from(Area.class));
            provinceList = new ArrayList<Province>();
            provinceList = (ArrayList) dbUtils.findAll(Selector.from(Province.class));
            if (provinceList.size() > 0) {
                for (int i = 0; i < provinceList.size(); i++) {
                    // 设置市级
                    ArrayList<String> city_2 = new ArrayList<String>();
                    String id = provinceList.get(i).getProvinceid();
                    String name = provinceList.get(i).getProvincename();
                    for (int j = 0; j < cityList.size(); j++) {
                        String pid = cityList.get(j).getpId();
                        if (id.equals(pid)) {
//							if(name.equals(cityList.get(j).getCityname())){
//								for (int k = 0; k < areaList.size(); k++) {
//									if(id.equals(areaList.get(k).getpId())){
//										city_2.add(areaList.get(k).getDistrictname());
//									}
//								}
//
//							}else {
                            city_2.add(cityList.get(j).getCityname());
//							}
                        }
                    }
                    cityItems.add(city_2);
                }
            }


        } catch (DbException e) {
            e.printStackTrace();
        }

        //选项选择器
        pvOptions = new OptionsPickerView(this);

        //二级联动效果
        pvOptions.setPicker(provinceList, cityItems, true);
        //设置选择的二级单位
        pvOptions.setLabels("", "");
//		pvOptions.setTitle("选择地址");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的二级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                //返回的分别是二个级别的选中位置
                String provinceName = provinceList.get(options1).getProvincename();
                String city;
                city = cityItems.get(options1).get(options2);
                if (provinceName.equals(city)) {
                    tv_choice_city.setText(provinceName);
                } else {
                    tv_choice_city.setText(provinceName + " " + city);
                }

            }
        });


    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_register_but = (TextView) findViewById(R.id.tv_register_but);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        tv_xieyi = (TextView) findViewById(R.id.tv_xieyi);
        tv_set_sex = (TextView) findViewById(R.id.tv_set_sex);
//        tv_choice_city = (TextView) findViewById(R.id.tv_choice_city);
        tv_choice_attr = (TextView) findViewById(R.id.tv_choice_attr);
        tv_head_title.setText("我的信息");

        et_user_name = (EditText) findViewById(R.id.et_user_name);
        //限制用户输入非法昵称
        et_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = et_user_name.getText().toString();
                if (s.contains("\'")) {
                    et_user_name.setText(s.replace("\'", ""));
                }
                if (s.contains("\"")) {
                    et_user_name.setText(s.replace("\"", ""));
                }
                if (s.contains("\\")) {
                    et_user_name.setText(s.replace("\\", ""));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_psw_2 = (EditText) findViewById(R.id.et_psw_2);
//        et_jianjie = (EditText) findViewById(R.id.et_jianjie);

        tv_head_right.setVisibility(View.VISIBLE);
        tv_head_right.setText("下一步");
//        tv_register_but.setOnClickListener(this);
        iv_back.setOnClickListener(this);
//        tv_choice_city.setOnClickListener(this);
        tv_choice_attr.setOnClickListener(this);
        tv_set_sex.setOnClickListener(this);
        tv_xieyi.setOnClickListener(this);
        tv_head_right.setOnClickListener(this);
        tv_xieyi.setVisibility(View.GONE);

        tel = getIntent().getStringExtra("tel");
        code = getIntent().getStringExtra("code");

        et_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    ShowUtil.showToast(RegisterActivity.this, "十字以内！");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //光标获取焦点的时候  用户 昵称大于2的时候   判断用户昵称
                if (hasFocus && s.length() > 2) {
                    checkNickName();
                }
            }
        });
        /*光标游离监听事件*/
        et_user_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                RegisterActivity.this.hasFocus = hasFocus;
            }
        });


        //为文本框设置多种颜色
        SpannableStringBuilder style = new SpannableStringBuilder("注册代表你同意微值协议");
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_text_blue)), 7, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_xieyi.setText(style);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right://注册
//			testNickname();//验证昵称
                if (!ButtontimeUtil.isFastDoubleClick()) {
                    register();
                }
                break;
            case R.id.iv_back://返回
                RenZhengActivity.mImgBase64List.clear();

                finish();
                break;
//            case R.id.tv_choice_city://城市选择
//                pvOptions.show();
//                break;
            case R.id.tv_set_sex://选择性别
                isSexAndAttr = 1;
                choiceSex("男", "女");
                break;
            case R.id.tv_choice_attr://选择属性
                isSexAndAttr = 2;
                choiceSex("个人", "组织");
                break;
            case R.id.tv_xieyi://微值协议
                isXieyi = true;
                initpopu(R.layout.wz_popupwindow_prompt_box_2);
//			promptBox_tv_content.setText(agreement);
                promptBox_tv_content_1.loadDataWithBaseURL(null, agreement, "text/html", "UTF-8", null);
                promptBoxPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
        }

    }

    /**
     * 检测用户名是否重复(接口已经失效)
     */
    private void testNickname() {
        String nickname = et_user_name.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("usernickname", nickname);
        map.put("usertype", usertype);
        NetworkRequest.postRequest(RequestPath.POST_REGUSERCHECK, map, this);
    }

    /**
     * 检测用户名是否重复
     */
    private void checkNickName() {
        String nickname = et_user_name.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("usernickname", nickname);
        NetworkRequest.postRequest(RequestPath.POST_REGCHECKNICK, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        LogUtils.e("--onSuccess----content = " + content);
        switch (isUrl) {
            case RequestPath.POST_REGCHECKNICK://验证nickname是否重复
                String message = "";
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        checkresult = obj.getString("checkresult");
                        message = obj.getString("message");
                        if (checkresult.equals("0")) {
                            return;
                        } else if (checkresult.equals("1")) {
                            ShowUtil.showToast(RegisterActivity.this, "该昵称已经存在，请您修改昵称！");
                            return;
                        } else if (checkresult.equals("2")) {
                            ShowUtil.showToast(RegisterActivity.this, obj.getString("message"));
//                            isXieyi = false;
//                            initpopu(R.layout.wz_popupwindow_prompt_box);
//                            promptBox_tv_content.setText("尊，您所注册的昵称用词为受保护的词语，若继续使用该昵称请您提供相关资料，小微将进行认真确认后，才可通过注册");
//                            promptBox_tv_cancel.setText("修改昵称");
//                            promptBox_tv_submit.setText("去认证");
//                            promptBoxPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
//            case RequestPath.POST_REGUSERCHECK://验证nickname是否重复
//                try {
//                    JSONObject obj = news JSONObject(content);
//                    if (obj.getString("result").equals("1")) {
//                        isUsertype = "1";
//                        tv_choice_attr.setText("个人");
//                    } else {
////						ShowUtil.showToast(RegisterActivity.this, obj.getString("message"));
//                        isXieyi = false;
//                        initpopu(R.layout.wz_popupwindow_prompt_box);
//                        promptBox_tv_content.setText("尊，您所注册的昵称用词为受保护的词语，若继续使用该昵称请您提供相关资料，小微将进行认真确认后，才可通过注册");
//                        promptBox_tv_cancel.setText("修改昵称");
//                        promptBox_tv_submit.setText("去认证");
//                        promptBoxPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
            case RequestPath.POST_REGUSER://注册
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
//						ShowUtil.showToast(RegisterActivity.this, obj.getString("message"));
                        SharedPreferencesUtil.setUsertype(RegisterActivity.this, usertype);
                        if (!TextUtils.isEmpty(et_user_name.getText().toString())) {
                            SharedPreferencesUtil.setZuZzhiname(this, et_user_name.getText().toString());
                        }
                        if (isAuthenticType) {
                            SharedPreferencesUtil.setUerAuthentic(RegisterActivity.this, "2");
                        } else {
                            SharedPreferencesUtil.setUerAuthentic(RegisterActivity.this, "0");
                        }
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                        startActivity(intent);

                        RenZhengActivity.mImgBase64List.clear();
                        finish();
                    } else {
                        ShowUtil.showToast(RegisterActivity.this, obj.getString("message"));
                        mProgressDialog.dismiss();

                        RenZhengActivity.mImgBase64List.clear();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }
                break;
        }

    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("--onFailure---content = " + content);
        ShowUtil.showToast(RegisterActivity.this, "服务器异常，请稍后再试...");
        mProgressDialog.dismiss();
    }

    /**
     * 点击下一步验证信息是否全部填写
     */
    private void register() {
//        String addr = tv_choice_city.getText().toString().trim();
//        String jianjie = et_jianjie.getText().toString().trim();
        String nickname = et_user_name.getText().toString().trim();
        String password = et_psw.getText().toString().trim();
        String password_2 = et_psw_2.getText().toString().trim();
        String attr = tv_choice_attr.getText().toString().trim();
//        if (TextUtils.isEmpty(nickname)) {
//            ShowUtil.showToast(this, "请输入昵称!");
//            return;
//        }
        if (nickname.length() < 2) {
            ShowUtil.showToast(this, "昵称最少为2个字哦！");
            return;
        }
        if ("1".equals(checkresult)) {
            ShowUtil.showToast(this, "该昵称已经被注册，请您修改昵称后重试！");
            return;
        }
        if (TextUtils.isEmpty(sex)) {
            ShowUtil.showToast(this, "请选择性别!");
            return;
        }

        if (TextUtils.isEmpty(attr)) {
            ShowUtil.showToast(this, "请选择属性!");

            return;
        }
        if ("2".equals(checkresult) && "0".equals(istrueinfo)) {
            isXieyi = false;
            initpopu(R.layout.wz_popupwindow_prompt_box);
            promptBox_tv_content.setText("尊，您所注册的昵称用词为受保护的词语，若继续使用该昵称请您提供相关资料，小微将进行认真确认后，才可通过注册");
            promptBox_tv_cancel.setText("修改昵称");
            promptBox_tv_submit.setText("去认证");
            promptBoxPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            return;
        }

//        if (TextUtils.isEmpty(addr)) {
//            ShowUtil.showToast(this, "请选择地区!");
//            return;
//        }

//        if (TextUtils.isEmpty(jianjie)) {
//            ShowUtil.showToast(this, "请输入个人简介!");
//            return;
//        }

//        if (TextUtils.isEmpty(tel) || !RegexUtils.etPhoneRegex(tel)) {
//            ShowUtil.showToast(this, "请输入手机号或邮箱!");
//            return;
//        }
        if (TextUtils.isEmpty(password)) {
            ShowUtil.showToast(this, "请设置密码!");
            return;
        }
        if (password.length() < 6) {
            ShowUtil.showToast(this, "密码最少需要6位!");
            return;
        }
        if (!RegexUtils.isLetterDigitAndChinese(password)) {
            ShowUtil.showToast(this, "密码需要同时包含字母和数字!");
            return;
        }
        if (TextUtils.isEmpty(password_2)) {
            ShowUtil.showToast(this, "请再次输入密码!");
            return;
        }
        if (!password.equals(password_2)) {
            ShowUtil.showToast(this, "两次密码输入不一样!");
            return;
        }
//        String[] addrArr = addr.split(" ");
        HashMap map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("usernickname", nickname);
        map.put("usersex", sex);
        map.put("type", "1");
        map.put("phone", tel);
        map.put("password", password);
        if ("2".equals(isUsertype)) {
            map.put("orgname", zuzhiName);
        }
        map.put("usertype", usertype);
        map.put("username", trueName);
        map.put("useridcard", IDnumber);
        map.put("truephone", telNumber);
        map.put("trueemail", mailbox);
        mProgressDialog.show();

        if (RenZhengActivity.mImgBase64List != null && RenZhengActivity.mImgBase64List.size() > 0) {
            for (int i = 0; i < RenZhengActivity.mImgBase64List.size(); i++) {
                map.put("usertrueimg" + (i + 1), RenZhengActivity.mImgBase64List.get(i));
                LogUtils.e("usertrueimg" + (i + 1), String.valueOf(map.get("usertrueimg" + (i + 1))));
            }
        }
        map.put("istrueinfo", istrueinfo);
        NetworkRequest.postRequest(RequestPath.POST_REGUSER, map, this);

    }

    private void getAgreement() {
        String url = RequestPath.GET_GETAGREEMENT;
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        NetworkRequest.getRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        agreement = obj.getString("data");
                    } else {
                        LogUtils.e("微值协议 message=" + obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(RegisterActivity.this, content);
                mProgressDialog.dismiss();
            }
        });

    }

//	String zuzhiName = et_zuzhi_name.getText().toString().trim();
//	String trueName = et_true_name.getText().toString().trim();
//	String IDnumber = et_ID_number.getText().toString().trim();
//	String telNumber = et_tel_number.getText().toString().trim();
//	String mailbox = et_mailbox.getText().toString().trim();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1002) {
                istrueinfo = "1";
                isAuthenticType = true;
                tv_choice_attr.setText("组织");
                tv_choice_attr.setClickable(false);
                et_user_name.setFocusable(false);
                et_user_name.setFocusableInTouchMode(false);
                isUsertype = "2";
                zuzhiName = data.getStringExtra("zuzhiName");
                trueName = data.getStringExtra("trueName");
                IDnumber = data.getStringExtra("IDnumber");
                telNumber = data.getStringExtra("telNumber");
                mailbox = data.getStringExtra("mailbox");
            } else if (requestCode == 1001) {
                istrueinfo = "1";
                isAuthenticType = true;
                tv_choice_attr.setText("个人");
                tv_choice_attr.setClickable(false);
                isUsertype = "1";
                trueName = data.getStringExtra("trueName");
                IDnumber = data.getStringExtra("IDnumber");
                telNumber = data.getStringExtra("telNumber");
                mailbox = data.getStringExtra("mailbox");
            }
        }
    }

    /**
     * isSexAndAttr =1 是选择性别, isSexAndAttr= 2 是选择属性
     */
    private void choiceSex(final String item_1, final String item_2) {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(item_1, ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (isSexAndAttr == 1) {
                                    sex = item_1;
                                    tv_set_sex.setText(sex);
                                } else if (isSexAndAttr == 2) {
                                    usertype = "1";
                                    tv_choice_attr.setText(item_1);
                                    LogUtils.e("ggg", item_1);
//                                    testNickname();//验证昵称
//                                    checkNickName();
                                }

                            }
                        }).addSheetItem(item_2, ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                if (isSexAndAttr == 1) {
                    sex = item_2;
                    tv_set_sex.setText(sex);
                } else if (isSexAndAttr == 2) {
                    usertype = "2";
                    tv_choice_attr.setText(item_2);
                    LogUtils.e("ggg", item_2);
//                    Intent intent = news Intent(RegisterActivity.this, RenZhengActivity.class);
//                    intent.putExtra("isWho", "2");
//                    startActivityForResult(intent, 1002);
                }
            }
        }).show();
    }

    WebView promptBox_tv_content_1;
    boolean isXieyi = false;


    /***/
    private void initpopu(int ID) {
        // 空白区域
        prompt_box = getLayoutInflater().inflate(ID, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);
        if (isXieyi) {
            promptBox_tv_content_1 = (WebView) prompt_box.findViewById(R.id.promptBox_tv_content_1);
        }
        prompt_box.setOnClickListener(new OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setTextColor(getResources().getColor(R.color.but_text_color));
        promptBox_tv_submit.setOnClickListener(new OnClickListener() {
            // 去认证
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, RenZhengActivity.class);
                intent.putExtra("isWho", usertype);
                switch (usertype) {
                    case "1":
                        startActivityForResult(intent, 1001);
                        break;
                    case "2":
                        intent.putExtra("zuzhiName", et_user_name.getText().toString());
                        startActivityForResult(intent, 1002);
                        break;
                }
                promptBoxPopupWindow.dismiss();
            }

        });
        promptBox_tv_cancel.setTextColor(getResources().getColor(R.color.orange));
        promptBox_tv_cancel.setOnClickListener(new OnClickListener() {
            // 修改昵称
            @Override
            public void onClick(View v) {
                et_user_name.setText("");
                isUsertype = null;
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
