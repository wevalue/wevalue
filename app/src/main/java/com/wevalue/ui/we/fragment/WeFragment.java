package com.wevalue.ui.we.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.UsInfo;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;

/** 我们
 * Created by Administrator on 2016-06-27.
 */
public class WeFragment extends BaseFragment implements View.OnClickListener, WZHttpListener {
    private View view;
    private Context mContext;
    private We_AttentionFragment attentionFragment;
    private We_FansFragment fansFragment;
    private We_FriendsFragment friendsFragment;
    private We_MessageFragment messageFragment;
    View fragment;
    //新消息数量
    private TextView tv_message, tv_messagew;
    private LinearLayout ll_message;
    //朋友数量
    private TextView tv_friends, tv_friendsw;

    private LinearLayout ll_friends;
    //关注数量
    private TextView tv_attention, tv_attentionw;
    private LinearLayout ll_attention;
    //粉丝数量
    private TextView tv_fans, tv_fansw;
    private LinearLayout ll_fans;
    UsInfo usInfo;

    MainActivity mainActivity;
    private boolean isFirstLoad = true;
    private boolean isVisibleToUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_friends, null);
        initView(view);
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && tv_message != null && getActivity() != null) {
            if (isFirstLoad) {
                isFirstLoad = false;
            }
            initWeInfo();
            if (messageFragment != null) {
                messageFragment.initMessageData();
            }
            MobclickAgent.onPageStart("home_we");
            LogUtils.e("lifeStyle", "我們進入頁面統計");
        } else {
            if (!isFirstLoad) {
                MobclickAgent.onPageEnd("home_we");
                LogUtils.e("lifeStyle", "我們退出頁面統計");
            }
        }
    }

    @Override
    public void onResume() {
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageStart("home_we");
            LogUtils.e("lifeStyle", "我們進入頁面統計");
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageEnd("home_we");
            LogUtils.e("lifeStyle", "我們退出頁面統計");
        }
        super.onPause();
    }

    //初始化布局控件
    private void initView(View view) {
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_messagew = (TextView) view.findViewById(R.id.tv_messagew);
        ll_message = (LinearLayout) view.findViewById(R.id.ll_message);
        tv_friends = (TextView) view.findViewById(R.id.tv_friends);
        ll_friends = (LinearLayout) view.findViewById(R.id.ll_friends);
        tv_friendsw = (TextView) view.findViewById(R.id.tv_friendsw);
        tv_attention = (TextView) view.findViewById(R.id.tv_attention);
        ll_attention = (LinearLayout) view.findViewById(R.id.ll_attention);
        tv_attentionw = (TextView) view.findViewById(R.id.tv_attentionw);
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);
        ll_fans = (LinearLayout) view.findViewById(R.id.ll_fans);
        tv_fansw = (TextView) view.findViewById(R.id.tv_fansw);
        fragment = view.findViewById(R.id.fl_layout);
        //设置监听事件
        ll_message.setOnClickListener(this);
        ll_attention.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        ll_friends.setOnClickListener(this);

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        messageFragment = new We_MessageFragment();
        transaction.replace(R.id.fl_layout, messageFragment);
        tv_message.setTextColor(getResources().getColor(R.color.blue));
        tv_messagew.setTextColor(getResources().getColor(R.color.blue));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.ll_message:
                if (messageFragment == null) {
                    messageFragment = new We_MessageFragment();
                }
                tv_message.setTextColor(getResources().getColor(R.color.blue));
                tv_friends.setTextColor(Color.BLACK);
                tv_attention.setTextColor(Color.BLACK);
                tv_fans.setTextColor(Color.BLACK);

                tv_messagew.setTextColor(getResources().getColor(R.color.blue));
                tv_friendsw.setTextColor(Color.BLACK);
                tv_attentionw.setTextColor(Color.BLACK);
                tv_fansw.setTextColor(Color.BLACK);
                // 使用当前Fragment的布局替代fl_layout的控件
                transaction.replace(R.id.fl_layout, messageFragment);
                break;
            case R.id.ll_attention:
                if (attentionFragment == null) {
                    attentionFragment = new We_AttentionFragment();
                }
                tv_message.setTextColor(Color.BLACK);
                tv_friends.setTextColor(Color.BLACK);
                tv_attention.setTextColor(getResources().getColor(R.color.blue));
                tv_fans.setTextColor(Color.BLACK);

                tv_messagew.setTextColor(Color.BLACK);
                tv_friendsw.setTextColor(Color.BLACK);
                tv_attentionw.setTextColor(getResources().getColor(R.color.blue));
                tv_fansw.setTextColor(Color.BLACK);
                // 使用当前Fragment的布局替代fl_layout的控件
                transaction.replace(R.id.fl_layout, attentionFragment);
                break;
            case R.id.ll_fans:
                if (fansFragment == null) {
                    fansFragment = new We_FansFragment();
                }
                tv_message.setTextColor(Color.BLACK);
                tv_friends.setTextColor(Color.BLACK);
                tv_attention.setTextColor(Color.BLACK);
                tv_fans.setTextColor(getResources().getColor(R.color.blue));

                tv_messagew.setTextColor(Color.BLACK);
                tv_friendsw.setTextColor(Color.BLACK);
                tv_attentionw.setTextColor(Color.BLACK);
                tv_fansw.setTextColor(getResources().getColor(R.color.blue));
                // 使用当前Fragment的布局替代fl_layout的控件
                transaction.replace(R.id.fl_layout, fansFragment);
                break;
            case R.id.ll_friends:
                if (friendsFragment == null) {
                    friendsFragment = new We_FriendsFragment();
                }
                tv_message.setTextColor(Color.BLACK);
                tv_friends.setTextColor(getResources().getColor(R.color.blue));
                tv_attention.setTextColor(Color.BLACK);
                tv_fans.setTextColor(Color.BLACK);

                tv_messagew.setTextColor(Color.BLACK);
                tv_friendsw.setTextColor(getResources().getColor(R.color.blue));
                tv_attentionw.setTextColor(Color.BLACK);
                tv_fansw.setTextColor(Color.BLACK);
                // 使用当前Fragment的布局替代fl_layout的控件
                transaction.replace(R.id.fl_layout, friendsFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        Gson gson = new Gson();
        System.out.print("sss" + content);
        usInfo = gson.fromJson(content, UsInfo.class);
        if (usInfo.getResult() == 1) {
            fillData();
        } else {
        }
    }

    @Override
    public void onFailure(String content) {
    }

    private void fillData() {
        tv_message.setText(usInfo.getSitemessnum());
        tv_friends.setText(usInfo.getFridendsnum());
        tv_attention.setText(usInfo.getFocusnum());
        tv_fans.setText(usInfo.getFansnum());
        LogUtils.e("USINFO", usInfo.getSitemessnum());
        if (usInfo.getSitemessnum().equals("0")) {
            mainActivity.setTv_Cycle_Visible(false);
        } else {
            mainActivity.setTv_Cycle_Visible(true);
        }
    }

    /*更新头部数据*/
    public void initWeInfo() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
        map.put("wepagedata", "0");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }
}