package com.wevalue.ui.we.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.wevalue.utils.PopuUtil;
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
    Context mContext;
    View convertView;
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
        mContext = getContext();
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        messageList = (ListView) view.findViewById(R.id.ll_messagelist);
        initMessageData();
        initHeadView();
    }
    private void initHeadView() {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.itemmessage, null);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView tv_tittle = (TextView) convertView.findViewById(R.id.tv_tittle);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        tv_time.setVisibility(View.GONE);
        TextView tv_red_cycle = (TextView) convertView.findViewById(R.id.tv_red_cycle);
        tv_red_cycle.setVisibility(View.GONE);
        iv_icon.setImageResource(R.mipmap.we_message_invite_friends);
        tv_tittle.setText("邀请好友");
        tv_content.setText("让美好阅读的初心接力下去");
        messageList.addHeaderView(convertView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initFriends();
            }
        });
    }
   private void  initFriends(){
       String url = RequestPath.SERVER_PATH+"/web/invite.html";
       String content = "好朋友就会玩一样的爱屁屁|#|我发现了个新世界，邀请你一起来看！";

       SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Shared.sx",Context.MODE_PRIVATE);
       boolean ishared = sharedPreferences.getBoolean("iShared",false);
       HashMap<String,String> map = new HashMap<String, String>();
       map.put("url",url);
       map.put("message",content);

       if (ishared){
           PopuUtil.initShareInvitePopup(getActivity(),new Handler(),map);
       }else {
           SharedPreferences.Editor edit = sharedPreferences.edit();
           edit.putBoolean("iShared",true);
           edit.commit();
           PopuUtil.inviteFriends(getActivity(),new Handler(),map);
       }
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
                    SiteMessageModel.DataBean  data  =  dataBeanList.get((int) parent.getAdapter().getItemId(position));
                    TextView tv_red_cycle = (TextView) view.findViewById(R.id.tv_red_cycle);
                    tv_red_cycle.setVisibility(View.GONE);
                    weFragment.initWeInfo();
                    Intent intent = new Intent(getActivity(), MeassageActivity.class);
                    intent.putExtra("messageType", data.getMesstype() + "");
                    startActivity(intent);
                    MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_newMessageOpen, "打开"+data.getMesstitle());
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
