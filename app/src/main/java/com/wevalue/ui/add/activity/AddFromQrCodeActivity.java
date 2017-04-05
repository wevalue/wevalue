package com.wevalue.ui.add.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.QrAddFriendBean;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddFromQrCodeActivity extends BaseActivity implements WZHttpListener, FriendManagerInterface, View.OnClickListener {

    private ImageView iv_icon;
    private ImageView iv_back;
    private TextView tv_name;
    private TextView tv_jianjie;
    private TextView tv_cancel_attention;
    private TextView tv_add_friend;
    private TextView tv_head_title;
    private LinearLayout ll_content_ui;


    String codeuerid;
    String isFriend;
    String isFocus;
    QrAddFriendBean friendBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_qr_code);
        codeuerid = getIntent().getStringExtra("codeuserid");
        initView();
    }

    private void initView() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_cancel_attention = (TextView) findViewById(R.id.tv_cancel_attention);
        tv_add_friend = (TextView) findViewById(R.id.tv_add_friend);
        ll_content_ui = (LinearLayout) findViewById(R.id.ll_content_ui);
        tv_add_friend.setOnClickListener(this);
        tv_cancel_attention.setOnClickListener(this);
        tv_head_title.setText("");
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("codeuserid", codeuerid);
        NetworkRequest.getRequest(RequestPath.GET_ADDCODEUSER, map, this);

        ll_content_ui.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            case RequestPath.GET_ADDCODEUSER:
                Gson gson = new Gson();
                friendBean = gson.fromJson(content, QrAddFriendBean.class);
                if (friendBean.getResult() == 1) {
                    Glide.with(this)
                            .load(RequestPath.SERVER_PATH + friendBean.getData().get(0).getUserface())
                            .placeholder(R.mipmap.default_head)
                            .into(iv_icon);
                    isFriend = friendBean.getData().get(0).getIsfriend();
                    isFocus = friendBean.getData().get(0).getIsfocus();
                    tv_name.setText(friendBean.getData().get(0).getUsernickname() + "       " + friendBean.getData().get(0).getUserlevel());
                    tv_jianjie.setText(friendBean.getData().get(0).getUserinfo());

                    if(!codeuerid.equals(SharedPreferencesUtil.getUid(AddFromQrCodeActivity.this))){
                        if (isFriend.equals("1")) {
                            //该用户是自己的好友
                            tv_cancel_attention.setText("解除好友");
                            tv_add_friend.setVisibility(View.GONE);
                            tv_cancel_attention.setVisibility(View.VISIBLE);
                        } else {
                            if (isFocus.equals("1")) {//该用户是自己关注的人
                                if (isFriend.equals("2") || isFriend.equals("3")) {
                                    tv_add_friend.setText("申请中");
                                } else {
                                    tv_add_friend.setText("加好友");
                                }
                                tv_cancel_attention.setText("取消关注");
                                tv_cancel_attention.setVisibility(View.VISIBLE);
                                tv_add_friend.setVisibility(View.VISIBLE);
                            } else {//该用户是自己的粉丝或者陌生人
                                if (isFriend.equals("2") || isFriend.equals("3")) {
                                    tv_cancel_attention.setText("申请中");
                                } else {
                                    tv_cancel_attention.setText("加好友");
                                }
                                tv_add_friend.setText("加关注");
                                tv_add_friend.setVisibility(View.VISIBLE);
                                tv_cancel_attention.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
                break;
            default:
                JSONObject object = null;
                String message = null;
                String result = "";
                try {
                    object = new JSONObject(content);
                    message = object.getString("message");
                    result = object.getString("result");
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

    @Override
    public void manageFriend(String content) {
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(getApplicationContext(), friendBean.getData().get(0).getUserid(), this);
                break;
            case "解除好友":
                FriendManageBase.DelFriend(getApplicationContext(), friendBean.getData().get(0).getUserid(), this);
                break;
            case "加关注":
                FriendManageBase.AddFocus(getApplicationContext(), friendBean.getData().get(0).getUserid(), this);
                break;
            case "取消关注":
                FriendManageBase.DelFocus(getApplicationContext(), friendBean.getData().get(0).getUserid(), this);
                break;
        }
    }

    @Override
    public void manageFriend(String content, String userid) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_attention:
                if (tv_cancel_attention.getText().equals("申请中")) {
                    return;
                }
                PopuUtil.initManageFriend(this, tv_cancel_attention.getText().toString(), this);
                break;
            case R.id.tv_add_friend:
                if (tv_add_friend.getText().equals("申请中")) {
                    return;
                }
                PopuUtil.initManageFriend(this, tv_add_friend.getText().toString(), this);
                break;
            case R.id.ll_content_ui:
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra("detailuserid",codeuerid);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
