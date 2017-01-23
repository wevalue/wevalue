package com.wevalue.ui.add.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NearbyEntity;
import com.wevalue.model.NearbyEntity.NearbyUser;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.add.adapter.NearbyAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016-06-08.
 */
public class AddFromNearbyActivity extends BaseActivity implements WZHttpListener, View.OnClickListener, FriendManagerInterface {
    private ListView lv_nearby;
    private TextView tv_head_title;
    private ImageView iv_back;
    private NearbyAdapter mAdapter;
    private List<NearbyUser> nearbyUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_nearby = (ListView) findViewById(R.id.lv_nearby);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("附近的人");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        getNearbyData();
    }

    /**
     * 获取附近的人
     */
    private void getNearbyData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getApplicationContext()));
        map.put("userlon", SharedPreferencesUtil.getLongitude(this));
        map.put("userlat", SharedPreferencesUtil.getLatitude(this));
        NetworkRequest.getRequest(RequestPath.GET_NEARBYUSER, map, AddFromNearbyActivity.this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            case RequestPath.GET_NEARBYUSER:
                Gson gson = new Gson();
                NearbyEntity nearbyEntity = gson.fromJson(content, NearbyEntity.class);
                if (nearbyEntity.getResult().equals("1")) {
                    nearbyUserList = nearbyEntity.data;
                    if (null != nearbyUserList && nearbyUserList.size() > 0) {
                        setAdapter();
                    } else {
                        Toast.makeText(this, "抱歉，附近没有其他好友...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
                break;
            //默认是添加关注和添加好友的请求
            default:
                JSONObject object = null;
                String message = null;
                String result = "";
                getNearbyData();
                try {
                    object = new JSONObject(content);
                    message = object.getString("message");
                    result = object.getString("result");
                    getNearbyData();
                    LogUtils.e("getuser", "true");
                    ShowUtil.showToast(this, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }

    private void setAdapter() {

        if (mAdapter == null) {
            mAdapter = new NearbyAdapter(nearbyUserList, this, this);
            lv_nearby.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(nearbyUserList);
            mAdapter.notifyDataSetChanged();
            LogUtils.e("getuser", "notifyDataSetChanged");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
        }
    }

    @Override
    public void manageFriend(String content) {

    }

    @Override
    public void manageFriend(String content, String userid) {
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(this, userid, this);
                break;
            case "解除好友":
                FriendManageBase.DelFriend(this, userid, this);
                break;
            case "加关注":
                FriendManageBase.AddFocus(this, userid, this);
                break;
            case "取消关注":
                FriendManageBase.DelFocus(this, userid, this);
                break;
        }
    }
}
