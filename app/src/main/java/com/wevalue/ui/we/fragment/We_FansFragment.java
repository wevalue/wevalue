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
 * 类说明：好友界面粉丝的显示界面
 */
public class We_FansFragment extends Fragment implements WZHttpListener, FriendManagerInterface {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private TextView tv_hao_you;
    private FansAndAttenAdapter adapter;
    View view;
    private List<SortModel> sourceDateList;
    private List<SortModel> friendNameList;
    ProgressBar pgb;
    WeFragment weFragment;
    private String bottomInfo;
    private View footView;

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

    //    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            queryData();
//        } else {
//        }
//    }
    private void initView() {
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        tv_hao_you = (TextView) view.findViewById(R.id.tv_hao_you);
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
    }

    private void setAdapter() {
        if (!friendNameList.isEmpty()) {
            sourceDateList = filledData(friendNameList);
        }
//        sourceDateList.add(news SortModel(bottomInfo, "", "", "", ""));
        if (sourceDateList.size() > 0) {
            Collections.sort(sourceDateList, new PinyinComparator());

            adapter = new FansAndAttenAdapter(getActivity(), sourceDateList, this, "fans");
            sortListView.setAdapter(adapter);
            initDatas();
            initEvents();
            sideBar.setVisibility(View.VISIBLE);
            sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("detailuserid", sourceDateList.get(position).getUserId());
                    getActivity().startActivity(intent);
                }
            });
        }
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
            sortModel = new SortModel(date.get(i).getName(), date.get(i).getUserId(), date.get(i).getIcon(), date.get(i).getJianjie(), date.get(i).getIsFocuse(), date.get(i).getUserlevel(),date.get(i).getIsFriend());
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
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
//            if (sortString.matches("[A-Z]")) {
//                sortModel.setSortLetters(sortString.toUpperCase());
//                if (!indexString.contains(sortString)) {
//                    indexString.add(sortString);
//                }
//            } else {
//                sortModel.setSortLetters("#");
//                if (!indexString.contains("#")) {
//                    indexString.add("#");
//                }
//            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
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
                        friendsArray = obj.getJSONArray("fanslist");
//                        bottomInfo = obj.getString("focusnull");
                        if (obj.getString("fansnull") != null) {
                            tv_hao_you.setVisibility(View.VISIBLE);
                            tv_hao_you.setText(obj.getString("fansnull").replace("我的", ""));
                        } else {
                            tv_hao_you.setVisibility(View.GONE);
                        }

//                        setFootView(bottomInfo);
                        if (friendsArray != null && friendsArray.length() > 0) {
                            for (int j = 0; j < friendsArray.length(); j++) {
                                JSONObject friend = friendsArray.getJSONObject(j);

                                sortModel = new SortModel(friend.getString("usernickname"), friend.getString("userid"), friend.getString("userface"), friend.getString("userinfo"), friend.getString("isfocus"), friend.getString("userlevel"),friend.getString("isfriend"));
                                friendNameList.add(sortModel);
                            }
                            setAdapter();
                        }
                    } else {
                        ShowUtil.showToast(getActivity(), obj.getString("message"));
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
                    ShowUtil.showToast(getActivity(), message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //好友管理成功后  对另外两个好友展示页更新数据
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                weFragment.initWeInfo();
                queryData();
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }

    private void queryData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
        map.put("wepagedata", "4");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }

    @Override
    public void manageFriend(String content) {

    }

    @Override
    public void manageFriend(String content, String userid) {
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(getActivity(), userid, this);
                break;
            case "解除好友":
                FriendManageBase.DelFriend(getActivity(), userid, this);
                break;
            case "加关注":
                FriendManageBase.AddFocus(getActivity(), userid, this);
                break;
            case "取消关注":
                FriendManageBase.DelFocus(getActivity(), userid, this);
                break;
        }
    }
//    private void setFootView(String s) {
//        TextView textView = (TextView) footView.findViewById(R.id.tv_foottext);
//        textView.setText(s);
//    }
}
