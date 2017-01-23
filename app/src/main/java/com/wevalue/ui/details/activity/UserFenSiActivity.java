package com.wevalue.ui.details.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.SearchFriendBean;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.adapter.SousuoAdapter;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.wevalue.net.RequestPath.GET_USERFRIEND;

/**
 * Created by xuhua on 2016/8/18.
 */
public class UserFenSiActivity extends BaseActivity implements View.OnClickListener, FriendManagerInterface, WZHttpListener {
    private ImageView iv_back;
    private TextView tv_head_title;
    private SousuoAdapter sosuoadapter;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private List<SearchFriendBean.DtfriendBean> mList;
    private SearchFriendBean mSearchFriendBean;
    private final String FENSI = "2";
    private String detailuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanzhu);
        detailuserid = getIntent().getStringExtra("detailuserid");
        initView();
    }

    private void initView() {
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        iv_back.setOnClickListener(this);
        tv_head_title.setOnClickListener(this);
        tv_head_title.setText("他的粉丝");
        getUserFans();
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                prsv_ScrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                prsv_ScrollView.onRefreshComplete();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
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

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            case GET_USERFRIEND:
                Gson gson = new Gson();
                mSearchFriendBean = gson.fromJson(content, SearchFriendBean.class);
                if (mSearchFriendBean != null && mSearchFriendBean.getResult() == 1) {
                    mList = new ArrayList<>();
//                    mList.addAll(mSearchFriendBean.getWelist());
                    mList.addAll(mSearchFriendBean.getFriendlist(""));
                    if (mList != null && mList.size() > 0) {
                        sosuoadapter = new SousuoAdapter(this, mList, this);
                        mNoScrollListview.setAdapter(sosuoadapter);
                    }
                } else {
                    ShowUtil.showToast(this, content);
                }
                break;
            default:
                JSONObject objectAddFriend = null;
                String messageAddFriend = "";
                try {
                    objectAddFriend = new JSONObject(content);
                    messageAddFriend = objectAddFriend.getString("message");
//                    添加好友操作执行成功后   重新获取粉丝列表
                    getUserFans();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, messageAddFriend, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }

    private void getUserFans() {
        /*获取用户粉丝列表*/
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("detailuserid", detailuserid);
        map.put("datatype", FENSI);
        NetworkRequest.getRequest(RequestPath.GET_USERFRIEND, map, this);
    }
}
