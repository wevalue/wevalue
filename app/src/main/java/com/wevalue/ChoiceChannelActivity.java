package com.wevalue;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wevalue.adapter.ChannelGridViewAdapter;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.ChannelBean;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollGridView;

import java.util.List;

public class ChoiceChannelActivity extends BaseActivity implements View.OnClickListener {


    private NoScrollGridView gridView;
    private TextView tv_goto;
    private ChannelBean channelBean;
    private ChannelGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_channel);
        initView();
        getChannel();
    }

    private void initView() {
        tv_goto = (TextView) findViewById(R.id.tv_goto);
        tv_goto.setOnClickListener(this);
        gridView = (NoScrollGridView) findViewById(R.id.userGridView);
        adapter = new ChannelGridViewAdapter(this);
        gridView.setAdapter(adapter);
        //监听选择的频道要大于3个
        adapter.setNotificationBoolean(new ChannelGridViewAdapter.NotificationBoolean() {
            @Override
            public void notification(boolean bol) {
                if (bol) {
                    tv_goto.setBackgroundResource(R.mipmap.iv_go_world_p);
                    tv_goto.setEnabled(true);
                } else {
                    tv_goto.setBackgroundResource(R.mipmap.iv_go_world_n);
                    tv_goto.setEnabled(false);
                }
            }
        });
    }

    //获取保存在本地的频道 在启动页已经获取服务器的所有频道了
    public void getChannel() {
        Gson gson = new Gson();
        channelBean = gson.fromJson(SharedPreferencesUtil.getAllChannel(this), ChannelBean.class);
       if (channelBean==null){
           ShowUtil.showToast(this,"连接不到网络，请退出重试.");
           return;
       }
        List<ChannelBean.Channel> channels = channelBean.getData();
        if (channelBean.getData().size() > 3) {
            channels.remove(channels.get(0));
            channels.remove(channels.get(0));
            channels.remove(channels.get(0));
        }
        adapter.addAll(channelBean.getData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        //点击保存获取的频道名称
        if (view == tv_goto) {
            List<String> channels = adapter.getChannels();
            String str = "";
            for (int i = 0; i < channels.size(); i++) {
                if (i == channels.size() - 1) {
                    str += channels.get(i);
                } else {
                    str += channels.get(i) + ",";
                }
            }
            LogUtils.e("str==" + str);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            SharedPreferencesUtil.setUserlike(this, str);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
