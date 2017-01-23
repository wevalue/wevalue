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
import com.wevalue.model.SortModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.we.adapter.FriendsAdapter;
import com.wevalue.utils.SharedPreferencesUtil;
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
 * 类说明：我们中“好友”的展示界面
 */
public class We_FriendsFragment extends Fragment implements WZHttpListener {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private FriendsAdapter adapter;
    View view;
    private List<SortModel> sourceDateList;
    private List<SortModel> friendNameList;
    private String userId;
    ProgressBar pgb;
    WeFragment weFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friendsfriends, null);
        initView();
        userId = SharedPreferencesUtil.getUid(getActivity());
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
        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
    }

    private void setAdapter() {
        if (friendNameList != null && friendNameList.size() > 0) {
            sourceDateList = filledData(friendNameList);
            Collections.sort(sourceDateList, new PinyinComparator());
            adapter = new FriendsAdapter(getActivity(), sourceDateList);
            sortListView.setAdapter(adapter);
            initDatas();
            initEvents();
            sideBar.setVisibility(View.VISIBLE);
            sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("detailuserid", sourceDateList.get(position).getUserId());
//                    SharedPreferencesUtil.setDetailUserid(getActivity(), sourceDateList.get(position).getUserId());
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
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            sortModel.setJianjie(date.get(i).getJianjie());
            sortModel.setIcon(date.get(i).getIcon());
            sortModel.setUserId(date.get(i).getUserId());
            sortModel.setUserlevel(date.get(i).getUserlevel());
            String pinyin = PinyinUtils.getPingYin(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.setSortLetters("#");
                if (!indexString.contains("#")) {
                    indexString.add("#");
                }
            }
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
        SortModel sortModel;
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.getString("result").equals("1")) {
                friendNameList = new ArrayList<>();
                JSONArray friendsArray;
                friendsArray = obj.getJSONArray("friendlist");
                if (friendsArray != null && friendsArray.length() > 0) {
                    for (int j = 0; j < friendsArray.length(); j++) {
                        JSONObject friend = friendsArray.getJSONObject(j);
                        sortModel = new SortModel(friend.getString("usernickname"), friend.getString("userid"), friend.getString("userface"), friend.getString("userinfo"), "", friend.getString("userlevel"), "");
                        friendNameList.add(sortModel);
                    }
                }
            } else {
//                ShowUtil.showToast(getActivity(), obj.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter();
        weFragment.initWeInfo();
    }

    @Override
    public void onFailure(String content) {
    }

    protected void queryData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", userId);
        map.put("wepagedata", "2");
        NetworkRequest.getRequest(RequestPath.GET_US, map, this);
    }
}
