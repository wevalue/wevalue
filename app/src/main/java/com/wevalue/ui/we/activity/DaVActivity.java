package com.wevalue.ui.we.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.we.adapter.DaVAdapter;
import com.wevalue.ui.we.adapter.MessageDetailsAdapter;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 感兴趣的人
 * <p>
 * Created by xox on 2017/3/25.
 */

public class DaVActivity extends BaseActivity implements View.OnClickListener,WZHttpListener{

    private PullToRefreshListView pullLayout;
    DaVAdapter  adapter;
    private TextView title;
    private int pageindex = 1;//当前页数
    SiteMessageModel messageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_we_da_v);
        findViewById(R.id.iv_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_head_title);
        title.setText("感兴趣的人");

        initPullToRefreshScrollView();
        queryData();
    }

    private void initPullToRefreshScrollView() {

        pullLayout = (PullToRefreshListView) findViewById(R.id.pullLayout);
        pullLayout.setFocusable(false);
        pullLayout.setMode(PullToRefreshBase.Mode.BOTH);
        pullLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindex = 1;
                queryData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindex++;
                queryData();
            }
        });

        adapter = new DaVAdapter(this);
        ListView listView = pullLayout.getRefreshableView();
        listView.setAdapter(adapter);
        listView.setSelector(R.color.gray_e5);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SiteMessageModel.DataBean  dataBean = (SiteMessageModel.DataBean) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(DaVActivity.this, UserDetailsActivity.class);
                intent.putExtra("detailuserid",dataBean.getUserid());
                startActivity(intent);
            }
        });
    }
    protected void queryData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("wepagedata", "1");
        map.put("messtype", "8");
        map.put("pagenum", "20");
        map.put("pageindex", pageindex + "");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }
    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        Log.d("DaVActivity","content = "+content);
        Gson gson = new Gson();
        messageModel = gson.fromJson(content, SiteMessageModel.class);
        if (messageModel.getResult() == 1) {
            List<SiteMessageModel.DataBean> dataBeanList = messageModel.getSitemesslist();
            if (dataBeanList!=null&&!dataBeanList.isEmpty()){
                if (pageindex==1){
                    adapter.clear();
                }
                adapter.addAll(dataBeanList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFailure(String content) {

    }
}
