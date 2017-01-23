package com.wevalue.ui.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.model.NoteBean.NoteEntity;
import com.wevalue.model.SearchFriendBean;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.world.adapter.SousuoAdapter;
import com.wevalue.ui.world.adapter.Sousuo_zongheAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuhua on 2016/8/17.
 * 按照内容搜索用户或者站内信息
 */
public class SearchJieguoActivity extends BaseActivity implements View.OnClickListener, FriendManagerInterface, WZHttpListener {
    private ImageView iv_back;
    private TextView tv_head_title;
    private NoScrollListview list_sosuo;
    private SouSuoNoteAdapter adapter;
    private SousuoAdapter sousuoAdapter;
    private List<NoteEntity> noteEntityList;
    private NoteBean noteBeen;
    private List<SearchFriendBean.DtfriendBean> dtfriendBeanList;
    SearchFriendBean searchFriendBean;
    String searchContent;

    private PullToRefreshScrollView prsv_ScrollView;
    private int pageindex = 1;
    private int isWho;
    private Sousuo_zongheAdapter zongheAdapter;
    private int size;//综合搜索结果用户的数量;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_jieguo);
        searchContent = getIntent().getStringExtra("content");
        LogUtils.e("searchContent = =" + getIntent().getStringExtra("content"));
        infoView();
    }

    private void infoView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        list_sosuo = (NoScrollListview) findViewById(R.id.list_sousuo);
        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
        tv_head_title.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        list_sosuo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtils.e("ssss", i + "");
                if (isWho == 1) {
                    Intent intent = new Intent(SearchJieguoActivity.this, UserDetailsActivity.class);
                    intent.putExtra("detailuserid", dtfriendBeanList.get(i).getUserid());
                    startActivity(intent);
                } else if (isWho == 2) {
                    Intent intent = new Intent(SearchJieguoActivity.this, NoteDetailsActivity.class);
                    LogUtils.e("----pos--" + i);
                    intent.putExtra("noteId", noteEntityList.get(i).getNoteid());
                    intent.putExtra("repostid", noteEntityList.get(i).getRepostid());
                    startActivity(intent);
                }else if (isWho==3){
                    if (i <size) {
                        Intent intent = new Intent(SearchJieguoActivity.this, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dtfriendBeanList.get(i).getUserid());
                        startActivity(intent);
                    } else  {
                        Intent intent = new Intent(SearchJieguoActivity.this, NoteDetailsActivity.class);
                        LogUtils.e("----pos--" + i);
                        intent.putExtra("noteId", dtfriendBeanList.get(i).getNoteid());
                        intent.putExtra("repostid", dtfriendBeanList.get(i).getRepostid());
                        startActivity(intent);

                    }
                }

            }
        });
        LogUtils.e("searchContent = =" + searchContent);
        tv_head_title.setText("搜索结果");
        isWho = getIntent().getIntExtra("isWho", -1);
        if (isWho == 1) {
            searchUser();
        } else if (isWho == 2) {
            getNoteList();
            prsv_ScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        } else if (isWho == 3) {
            prsv_ScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            getNoteAndUserList();
        }


        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex = 1;
                getNoteList();
            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex++;

                if (isWho == 3) {
                    getNoteAndUserList();
                } else if (isWho == 2) {
                    getNoteList();
                }
            }
        });
    }

    /**
     * 搜索帖子
     */
    private void getNoteList() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("pagenum", "10");
        map.put("pageindex", String.valueOf(pageindex));
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("keywords", searchContent);
        map.put("deviceid", SharedPreferencesUtil.getDeviceid(this));
        NetworkRequest.getRequest(RequestPath.GET_GETNOTE, map, this);
    }

    /**
     * 综合搜索
     */
    private void getNoteAndUserList() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("pagenum", "10");
        map.put("pageindex", String.valueOf(pageindex));
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("keywords", searchContent);
        map.put("deviceid", SharedPreferencesUtil.getDeviceid(this));
        NetworkRequest.getRequest(RequestPath.GETUSERNOTEBYSEARCH, map, this);
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
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        Gson gson = new Gson();
        switch (isUrl) {
            case RequestPath.GET_ADDFRIENDBYSEARCH:
                searchFriendBean = gson.fromJson(content, SearchFriendBean.class);
                if (searchFriendBean.getResult() == 1) {
                    dtfriendBeanList = new ArrayList<>();
//                    if(searchFriendBean.getDtfriend("我的好友")!=null&&searchFriendBean.getDtfriend("我的好友").size()>0){
//                    }
                    dtfriendBeanList.addAll(searchFriendBean.getDtfriend("我的好友"));
                    dtfriendBeanList.addAll(searchFriendBean.getDtfocus("我的关注"));
                    dtfriendBeanList.addAll(searchFriendBean.getDtfans("我的粉丝"));
                    dtfriendBeanList.addAll(searchFriendBean.getData("陌生人"));
                    if (dtfriendBeanList.size() > 0) {
                        if (sousuoAdapter == null) {
                            sousuoAdapter = new SousuoAdapter(SearchJieguoActivity.this, dtfriendBeanList, this);
                            list_sosuo.setAdapter(sousuoAdapter);
                        } else {
                            sousuoAdapter.setmDatas(dtfriendBeanList);
                            sousuoAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ShowUtil.showToast(this, "抱歉，未找到相关用户");
                    }
                } else {
                    ShowUtil.showToast(this, "抱歉，未找到相关用户");
                }
                break;
            case RequestPath.GETUSERNOTEBYSEARCH://综合搜索
                searchFriendBean = gson.fromJson(content, SearchFriendBean.class);
                if (searchFriendBean.getResult() == 1) {
//                    if(searchFriendBean.getDtfriend("我的好友")!=null&&searchFriendBean.getDtfriend("我的好友").size()>0){
//                    }
                    if (pageindex > 1) {
                        if (searchFriendBean.getDtnotes().size() > 0) {
                            dtfriendBeanList.addAll(searchFriendBean.getDtnotes());
                            zongheAdapter.notifyDataSetChanged();
                        } else {
                            pageindex--;
                            ShowUtil.showToast(this, "没有更多数据了");
                        }
                    } else {
                        dtfriendBeanList = searchFriendBean.getDtfriend("我的好友");
                        dtfriendBeanList.addAll(searchFriendBean.getDtfocus("我的关注"));
                        dtfriendBeanList.addAll(searchFriendBean.getDtfans("我的粉丝"));
                        dtfriendBeanList.addAll(searchFriendBean.getDtusers());//"陌生人"
                        size = dtfriendBeanList.size();
                        dtfriendBeanList.addAll(searchFriendBean.getDtnotes("信息"));

                        if (dtfriendBeanList.size() > 0) {
                            zongheAdapter = new Sousuo_zongheAdapter(SearchJieguoActivity.this, dtfriendBeanList, this, size);
                            list_sosuo.setAdapter(zongheAdapter);
                        } else {
                            ShowUtil.showToast(this, "抱歉，未找到相关内容");
                        }
                    }
                } else {
                    ShowUtil.showToast(this, "抱歉，未找到相关内容");
                }
                break;
            case RequestPath.GET_GETNOTE:
                noteBeen = gson.fromJson(content, NoteBean.class);
                if (noteBeen.getResult().equals("1")) {

                    if (pageindex > 1) {
                        if (noteBeen.data != null && noteBeen.data.size() > 0) {
                            noteEntityList.addAll(noteBeen.data);
                            adapter.notifyDataSetChanged();
                        } else {
                            pageindex--;
                            ShowUtil.showToast(this, "没有更多数据了");
                        }

                    } else {
                        if (noteBeen.data != null && noteBeen.data.size() > 0) {
                            noteEntityList = noteBeen.data;
                            adapter = new SouSuoNoteAdapter(noteEntityList, this);
                            list_sosuo.setAdapter(adapter);
                        } else {
                            ShowUtil.showToast(this, "抱歉，未找到相关内容");
                        }
                    }
                } else {
                    ShowUtil.showToast(this, "抱歉，未找到相关内容");
                }
                break;
            default:
                searchUser();
                JSONObject objectAddFriend = null;
                String messageAddFriend = "";
                try {
                    objectAddFriend = new JSONObject(content);
                    messageAddFriend = objectAddFriend.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShowUtil.showToast(this, messageAddFriend);
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        prsv_ScrollView.onRefreshComplete();

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

    private void searchUser() {
        //是搜索用户
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("searchnick", searchContent);
        NetworkRequest.getRequest(RequestPath.GET_ADDFRIENDBYSEARCH, map, this);
    }
}
