package com.wevalue.ui.we.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.model.SortModel;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.we.adapter.FansAndAttenAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.sortview.view.PinyinComparator;
import com.wevalue.view.sortview.view.PinyinUtils;
import com.wevalue.view.sortview.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/9/29
 * 类说明：好友界面我关注的人界面
 */

public class We_AttentionFragment extends Fragment implements WZHttpListener, FriendManagerInterface {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private FansAndAttenAdapter adapter;
    View view;
    private List<SortModel> sourceDateList;
    private List<SortModel> friendNameList;
    WeFragment weFragment;
    ProgressBar pgb;
    private String bottomInfo;
    private View footView;
    private TextView tv_hao_you;//好友数量

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friendsfriends, null);
        footView = LayoutInflater.from(WeValueApplication.applicationContext).inflate(R.layout.footview, null);
        initView();
        sourceDateList = new ArrayList<>();
        weFragment = (WeFragment) getActivity().getSupportFragmentManager().getFragments().get(2);
        queryData();
        return view;
    }


    private void initView() {
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        tv_hao_you = (TextView) view.findViewById(R.id.tv_hao_you);
    }


    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    //给列表填充数据
    private List<SortModel> filledData(List<SortModel> date) {
        List<SortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        SortModel sortModel;
        for (int i = 0; i < date.size(); i++) {
            sortModel = new SortModel(date.get(i).getName(), date.get(i).getUserId(), date.get(i).getIcon(), date.get(i).getJianjie(), null, date.get(i).getUserlevel(), date.get(i).getIsFriend());
            String pinyin = PinyinUtils.getPingYin(sortModel.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (!sortString.matches("[A-Z]")) {
                sortModel.setSortLetters("#");
                if (!indexString.contains("#")) {
                    indexString.add("#");
                }
            } else if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);

        for (int i = 0; i < indexString.size(); i++) {
            LogUtils.e("--首字母--" + indexString.get(i));
        }
        if (indexString != null && indexString.size() > 0) {

            if (indexString.contains("#")) {
                indexString.add(indexString.size(), indexString.get(0));
                indexString.remove(0);
            }
            sideBar.setIndexText(indexString);
        }
        return mSortList;
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        pgb.setVisibility(View.GONE);
        switch (isUrl) {
            case RequestPath.GET_US:
                SortModel sortModel = null;
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        friendNameList = new ArrayList<>();
                        JSONArray friendsArray;
                        friendsArray = obj.getJSONArray("focuslist");
                        bottomInfo = obj.getString("focusnull");
                        if (obj.getString("focusnull") != null) {
                            tv_hao_you.setVisibility(View.VISIBLE);
                            tv_hao_you.setText(obj.getString("focusnull").replace("我的", ""));
                        } else {
                            tv_hao_you.setVisibility(View.GONE);
                        }
//                        setFootView(bottomInfo);
                        if (friendsArray != null && friendsArray.length() > 0) {
                            for (int j = 0; j < friendsArray.length(); j++) {
                                JSONObject friend = friendsArray.getJSONObject(j);
                                sortModel = new SortModel(friend.getString("usernickname"), friend.getString("userid"), friend.getString("userface"), friend.getString("userinfo"), "", friend.getString("userlevel"), friend.getString("isfriend"));
                                friendNameList.add(sortModel);
                            }
                            setAdapter();
                        }
                    } else {
//                        ShowUtil.showToast(getActivity(), obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case RequestPath.POST_DELFOCUES:
                weFragment.initWeInfo();
                JSONObject object_2 = null;
                String message_2 = "";
                try {
                    object_2 = new JSONObject(content);
                    message_2 = object_2.getString("message");
                    if (object_2.getString("result").equals("1")) {
                        ShowUtil.showToast(getActivity(), message_2);
                    } else {
                        ShowUtil.showToast(getActivity(), message_2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                //默认是对当前用户进行的好友管理
                JSONObject object = null;
                String message = "";
                try {
                    object = new JSONObject(content);
                    message = object.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShowUtil.showToast(getActivity(), message);
                weFragment.initWeInfo();
                queryData();
                break;
        }
    }

    private void setAdapter() {
        sourceDateList = filledData(friendNameList);
        if (sourceDateList.size() > 0) {
            Collections.sort(sourceDateList, new PinyinComparator());
            adapter = new FansAndAttenAdapter(getActivity(), sourceDateList, this, "attention");
            sortListView.setAdapter(adapter);
            sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("detailuserid", sourceDateList.get(position).getUserId());
                    getActivity().startActivity(intent);
                }
            });
        } else {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        if (friendNameList.size() > 0) {
            initDatas();
            initEvents();
            sideBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(String content) {

    }

    protected void queryData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
        map.put("wepagedata", "3");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }

    @Override
    public void manageFriend(String content) {

    }

    @Override
    public void manageFriend(String content, String userid) {
        LogUtils.e("userid" + userid + "+++++++++++++++");
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(getActivity(), userid, this);
                queryData();
                break;
            case "解除好友":
                FriendManageBase.DelFriend(getActivity(), userid, this);
                queryData();
                break;
            case "加关注":
                FriendManageBase.AddFocus(getActivity(), userid, this);
                queryData();
                break;
            case "取消关注":
                FriendManageBase.DelFocus(getActivity(), userid, this);
                queryData();
                break;
        }
    }

//    private void setFootView(String s) {
////        TextView textView = (TextView) footView.findViewById(R.id.tv_foottext);
////        textView.setText(s);
//        if (!TextUtils.isEmpty(s)) {
//            LogUtils.e("tv_foottext", s);
//        }
////        sortListView.addFooterView(footView);
//    }
}

