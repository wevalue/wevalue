package com.wevalue.ui.details.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.adapter.UserNoteListAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/11
 * 类说明：用户详情页的用户转发帖子信息
 */

public class UserTransmitListFragment extends BaseFragment implements WZHttpListener {
    private View view;
    private Context mContext;
    private UserNoteListAdapter mAdapter;
    private int pageindex = 1;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private List<NoteBean.NoteEntity> noteInfo;
    private static final int pagenum = 10;//页大小
    private String detailuserid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        view = LayoutInflater.from(mContext).inflate(R.layout.frame_list, null);
        initView();
        return view;
    }

    private void initView() {
        detailuserid = getArguments().getString("detailuserid");
        LogUtils.e("detailuserid", detailuserid);
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                intent.putExtra("noteId", noteInfo.get(position).getNoteid());
                intent.putExtra("repostid", "0");
                startActivity(intent);
            }
        });
        queryData();
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                下拉
                pageindex = 1;
                queryData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                上拉
                pageindex++;
                queryData();
            }
        });

    }

    @Override
    public void onSuccess(String content, String isUrl) {
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content, NoteBean.class);
        prsv_ScrollView.onRefreshComplete();
        if (noteBean.getResult().equals("1")) {
            noteInfo = noteBean.data;
            if (pageindex > 1) {
                if (noteBean.getData().size() > 0) {
                    noteInfo.addAll(noteBean.data);
                    mAdapter.setmDatas(noteInfo);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ShowUtil.showToast(getActivity(), "没有更多数据了");
                    pageindex--;
                }
            } else {
                if (!MainActivity.isEditChannel && mAdapter != null && noteInfo != null && noteInfo.size() > 0) {
                    noteInfo = noteBean.getData();
//                    if (noteInfo != null && noteInfo.size() > 2) {
//                        noteInfo.add(0, noteInfo.get(0));
//                    }
                    mAdapter.setmDatas(noteInfo);
                    LogUtils.e("mHlistDatas.size = " + noteInfo.size());
                    LogUtils.e("noteBean.getData()= " + noteBean.getData().size());
                    mAdapter.notifyDataSetChanged();
                } else {
                    noteInfo = noteBean.getData();
                    if (noteInfo == null || noteInfo.size() <= 0) {
                        return;
                    }
//                    if (noteInfo != null && noteInfo.size() > 2) {
//                        noteInfo.add(0, noteInfo.get(0));
//                        noteInfo.add(1, noteInfo.get(1));
//                        noteInfo.add(2, noteInfo.get(2));
//                    }

                    mAdapter = new UserNoteListAdapter(noteInfo, getActivity());
//                    mAdapter.notifyDataSetChanged();
                    mNoScrollListview.setAdapter(mAdapter);
                }
            }
        } else {
            LogUtils.e("轮播  -  onSuccess   else {");
        }
    }

    @Override
    public void onFailure(String content) {

    }

    private void queryData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("detailuserid", detailuserid);
        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
        map.put("pagenum", pagenum + "");
        map.put("pageindex", pageindex + "");
        map.put("detaildata", "2");//2代表用户转发的页面
        NetworkRequest.getRequest(RequestPath.GET_USERDETAILS, map, this);
    }
}
