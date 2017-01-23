package com.wevalue.ui.we.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.we.activity.MeassageActivity;
import com.wevalue.ui.we.adapter.MessageAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.youmeng.StatisticsConsts;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/9/29
 * 类说明：朋友们“消息”的显示界面
 */

public class We_MessageFragment extends Fragment implements WZHttpListener {
    View view;
    ListView messageList;
    ProgressBar pgb;
    MessageAdapter messageAdapter;
    SiteMessageModel messageModel;
    List<SiteMessageModel.DataBean> dataBeanList;
    WeFragment weFragment ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friendsmessage, null);
        initView(view);
        weFragment = (WeFragment) getActivity().getSupportFragmentManager().getFragments().get(2);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        weFragment.initWeInfo();
    }
    private void initView(View view) {
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        messageList = (ListView) view.findViewById(R.id.ll_messagelist);
        initMessageData();
    }
    @Override
    public void onSuccess(String content, String isUrl) {
        LogUtils.e("isUrl",content);
        pgb.setVisibility(View.GONE);
        Gson gson = new Gson();
        messageModel = gson.fromJson(content, SiteMessageModel.class);
        if (messageModel.getResult() == 1) {
            dataBeanList = messageModel.getSitemesslist();
            messageAdapter = new MessageAdapter(getActivity(), dataBeanList);
            messageList.setAdapter(messageAdapter);
            messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv_red_cycle = (TextView) view.findViewById(R.id.tv_red_cycle);
                    tv_red_cycle.setVisibility(View.GONE);
                    weFragment.initWeInfo();
                    Intent intent = new Intent(getActivity(), MeassageActivity.class);
                    intent.putExtra("messageType", dataBeanList.get(position).getMesstype() + "");
                    startActivity(intent);
                    switch (dataBeanList.get(position).getMesstype()) {
                        case 1:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的好友");
                            break;
                        case 2:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的粉丝");
                            break;
                        case 3:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的打赏");
                            break;
                        case 4:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的转发");
                            break;
                        case 5:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的点赞");
                            break;
                        case 6:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开新的评论");
                            break;
                        case 7:
                            MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开系统消息");
                            break;
                    }
                }
            });
        }
    }
    @Override
    public void onFailure(String content) {

    }
    public void initMessageData(){
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(WeValueApplication.applicationContext));
        map.put("wepagedata", "1");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }
}
