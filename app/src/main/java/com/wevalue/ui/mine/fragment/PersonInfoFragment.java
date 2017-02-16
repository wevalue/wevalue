package com.wevalue.ui.mine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseFragment;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.mine.activity.MyInfoActivity;
import com.wevalue.ui.mine.activity.MyNoteActivity;
import com.wevalue.ui.mine.activity.RankActivity;
import com.wevalue.ui.mine.activity.WalletActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jupush.JpushTagSet;

/**
 * Created by Administrator on 2016-06-27.
 */
public class PersonInfoFragment extends BaseFragment implements View.OnClickListener, WZHttpListener {

    private View view;
    private Context mContext;
    private TextView tv_denglu;
    private TextView tv_wevalue_number;
    private ImageView iv_arrow_right, iv_user_photo;
    private TextView tv_user_info, tv_nickname;
    private TextView tv_dengji, tv_sex;
    private LinearLayout ll_denglu_status;//登录状态
    private LinearLayout ll_qianbao;//钱包
    private LinearLayout ll_my_send_msg;//我的发布
    private LinearLayout ll_my_forward_msg;//我的转发
    private LinearLayout ll_my_reward;//我的打赏
    private LinearLayout ll_paihangbang;
    private boolean isFirstLoad = true;
    private boolean isVisibleToUser;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (isFirstLoad) {
                isFirstLoad = false;
            }
            //相当于Fragment的onResume
            LogUtils.e("我的------- isVisibleToUser = " + isVisibleToUser);
            if (view != null) {
                setIsLoginStatus();
            }
            MobclickAgent.onPageStart("home_me");
            LogUtils.e("lifeStyle", "我進入頁面統計");
        } else {
            if (!isFirstLoad) {
                MobclickAgent.onPageEnd("home_me");
                LogUtils.e("lifeStyle", "我退出頁面統計");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_personinfo, null);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageStart("home_me");
            LogUtils.e("lifeStyle", "我進入頁面統計");
        }
        setIsLoginStatus();
    }

    @Override
    public void onPause() {
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageEnd("home_me");
            LogUtils.e("lifeStyle", "我退出頁面統計");
        }
        super.onPause();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);

        tv_user_info = (TextView) view.findViewById(R.id.tv_user_info);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        tv_dengji = (TextView) view.findViewById(R.id.tv_dengji);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_wevalue_number = (TextView) view.findViewById(R.id.tv_wevalue_number);

        ll_denglu_status = (LinearLayout) view.findViewById(R.id.ll_denglu_status);
        ll_qianbao = (LinearLayout) view.findViewById(R.id.ll_qianbao);
        ll_my_send_msg = (LinearLayout) view.findViewById(R.id.ll_my_send_msg);
        ll_my_forward_msg = (LinearLayout) view.findViewById(R.id.ll_my_forward_msg);
        ll_my_reward = (LinearLayout) view.findViewById(R.id.ll_my_reward);


        tv_denglu = (TextView) view.findViewById(R.id.tv_denglu);
        iv_arrow_right = (ImageView) view.findViewById(R.id.iv_arrow_right);
        ll_paihangbang = (LinearLayout) view.findViewById(R.id.ll_paihangbang);

        ll_paihangbang.setOnClickListener(this);

        ll_denglu_status.setOnClickListener(this);
        tv_denglu.setOnClickListener(this);
        iv_user_photo.setOnClickListener(this);
        ll_qianbao.setOnClickListener(this);
        ll_my_send_msg.setOnClickListener(this);
        ll_my_forward_msg.setOnClickListener(this);
        ll_my_reward.setOnClickListener(this);
    }

    /**
     * 获取用户信息
     */
    private void getUserInfoData() {
        String uid = SharedPreferencesUtil.getUid(getActivity());
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", uid);
        NetworkRequest.postRequest(RequestPath.POST_USERINFO, map, this);
    }

    /**
     * 是否登录
     */
    public void setIsLoginStatus() {
        if (getActivity() == null) {
            return;
        }
        String userId = SharedPreferencesUtil.getUid(getActivity());
        initView();
        if (!TextUtils.isEmpty(userId)) {
            ll_denglu_status.setVisibility(View.VISIBLE);
            iv_arrow_right.setVisibility(View.VISIBLE);
            tv_denglu.setVisibility(View.GONE);
            getUserInfoData();
            // 图片的下载
            iv_user_photo.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(getActivity()))
                    .placeholder(R.mipmap.default_head)
                    .dontAnimate()
                    .into(iv_user_photo);
            tv_nickname.setText(SharedPreferencesUtil.getNickname(getActivity()));
            tv_user_info.setText(SharedPreferencesUtil.getUserInfo(getActivity()));
            tv_dengji.setText(SharedPreferencesUtil.getUserleve(getActivity()));
            tv_sex.setText(SharedPreferencesUtil.getSex(getActivity()));
            tv_wevalue_number.setText("微值号：" + SharedPreferencesUtil.getUsernumber(getActivity()));
        } else {
            iv_user_photo.setVisibility(View.VISIBLE);
            tv_denglu.setVisibility(View.VISIBLE);
            ll_denglu_status.setVisibility(View.GONE);
            iv_arrow_right.setVisibility(View.GONE);
            iv_user_photo.setImageResource(R.mipmap.default_head);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(mContext))) {
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        switch (v.getId()) {
            case R.id.ll_paihangbang:
                startActivity(new Intent(getActivity(), RankActivity.class));
                finish();
                break;
            case R.id.ll_denglu_status:
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
                break;
            case R.id.iv_user_photo://头像
                if (!TextUtils.isEmpty(SharedPreferencesUtil.getUid(getActivity()))) {
                    startActivity(new Intent(getActivity(), MyInfoActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.tv_denglu://登录
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_qianbao://钱包
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            case R.id.ll_my_send_msg://我的发布
                intent = new Intent(getActivity(), MyNoteActivity.class);
                intent.putExtra("status", "1");
                startActivity(intent);
                break;
            case R.id.ll_my_forward_msg://我的转发
                intent = new Intent(getActivity(), MyNoteActivity.class);
                intent.putExtra("status", "2");
                startActivity(intent);
                break;
            case R.id.ll_my_reward://我的打赏
                intent = new Intent(getActivity(), MyNoteActivity.class);
                intent.putExtra("status", "3");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.getString("result").equals("1")) {
                JSONObject data = obj.getJSONObject("data");
                SharedPreferencesUtil.setZuZzhiname(mContext, data.getString("orgname"));
                SharedPreferencesUtil.setUid(mContext, data.getString("userid"));
                SharedPreferencesUtil.setMobile(mContext, data.getString("userphone"));
                SharedPreferencesUtil.setEmail(mContext, data.getString("useremail"));
                SharedPreferencesUtil.setAvatar(mContext, data.getString("userface"));
                SharedPreferencesUtil.setNickname(mContext, data.getString("usernickname"));
                SharedPreferencesUtil.setSex(mContext, data.getString("usersex"));//用户性别
                SharedPreferencesUtil.setScore(mContext, data.getString("userscore"));
                SharedPreferencesUtil.setUserleve(mContext, data.getString("userlevel"));//用户等级
                try {
                    SharedPreferencesUtil.setUserLevelInt(mContext, Integer.parseInt(data.getString("userscore")));//用户等级的int型
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SharedPreferencesUtil.setUsertype(mContext, data.getString("usertype"));//用户类型
                SharedPreferencesUtil.setLoginPswStatus(mContext, data.getString("havepwd"));
                SharedPreferencesUtil.setPayPswStatus(mContext, data.getString("havepaypwd"));
                SharedPreferencesUtil.setCityName(mContext, data.getString("usercity"));
                LogUtils.e("cityname", data.getString("usercity"));
                SharedPreferencesUtil.setProvinceName(mContext, data.getString("userprovince"));
                SharedPreferencesUtil.setCounty(mContext, data.getString("userdistrict"));
                SharedPreferencesUtil.setQR_code(mContext, data.getString("usercode"));//用户二维码
                SharedPreferencesUtil.setLatitude(mContext, data.getString("userlat"));
                SharedPreferencesUtil.setLongitude(mContext, data.getString("userlon"));
                SharedPreferencesUtil.setUserInfo(mContext, data.getString("userinfo"));//用户简介
                SharedPreferencesUtil.setPayPswWenTi_1(mContext, data.getString("payquestion1"));//密保问题1
                SharedPreferencesUtil.setPayPswWenTi_2(mContext, data.getString("payquestion2"));//密保问题2
                SharedPreferencesUtil.setUsernumber(mContext, data.getString("usernumber"));//微值号
                SharedPreferencesUtil.setSuiYinCount(mContext, data.getString("usermoney"));//保存用户碎银数量
                SharedPreferencesUtil.setUerAuthentic(mContext, data.getString("istrue"));
            } else if (obj.getString("result").equals("3")) {
                ShowUtil.showToast(mContext, "抱歉，该用户已经禁用。");
                SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                setIsLoginStatus();
            } else if (obj.getString("result").equals("4")) {
                ShowUtil.showToast(mContext, "抱歉，该用户已删除。");
                SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                setIsLoginStatus();
            } else if (obj.getString("result").equals("5")){
                ShowUtil.showToast(mContext, obj.getString("message"));
                SharedPreferencesUtil.clearSharedPreferencesInfo(WeValueApplication.applicationContext, "UserInfo");
                setIsLoginStatus();
            }else {
                ShowUtil.showToast(mContext, obj.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*设置极光推送的别名 进行个人推送*/
    private void setJupushAlisa(String usernumber) {
        JpushTagSet tagSet = new JpushTagSet(getActivity(), usernumber);
        tagSet.setAlias();
        LogUtils.e("sss", "jgtrue");
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(mContext, "网络开小差了，请检查网络稍后再试...");
    }
}
