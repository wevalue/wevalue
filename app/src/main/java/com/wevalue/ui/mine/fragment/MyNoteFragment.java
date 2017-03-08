package com.wevalue.ui.mine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.model.RewardBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.adapter.NoteListAdapter;
import com.wevalue.ui.world.adapter.MyRewardAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/31
 * 类说明：我的发布我的转发 我的打赏 通用的fragment
 */

public class MyNoteFragment extends BaseFragment implements WZHttpListener, View.OnClickListener {
    private String notezone = ""; //发布范围 // 全部 -1  // 世界 0  // 朋友1
    private String status = "";//状态
    private int pageindex = 1;
    private List<NoteBean.NoteEntity> mNoteList;
    //我的发布 我的转发适配器
    private NoteListAdapter mAdapter;
    private NoteRequestBase mNoteRequestBase;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    View view;
    private List<RewardBean.RewardEntity> rewardEntities;
    //我的打赏适配器
    private MyRewardAdapter adapter;
    AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    private TextView tv_quanbu;
    private TextView tv_shijie;
    private TextView tv_pengyoumen;
    private Activity context;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.e("isVisibleToUser", "isVisibleToUser");
        } else {
            LogUtils.e("isVisibleToUser", "noVisibleToUser");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String delNoteId = SharedPreferencesUtil.getUserDelNoteId(context);
        if (!TextUtils.isEmpty(delNoteId)) {
            if (mNoteList==null)return;
            for (int i = 0; i < mNoteList.size(); i++) {
                if (mNoteList.get(i).getNoteid().equals(delNoteId)) {
                    mNoteList.remove(i);
                    mAdapter.setmDatas(mNoteList);
                    mAdapter.notifyDataSetChanged();
                    SharedPreferencesUtil.setUserDelNoteId(context, "");
                    tv_quanbu.setText("全部（" + mNoteList.size() + "）");
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(context);
        notezone = bundle.getString("notezone");
        status = bundle.getString("status");
        if (!notezone.equals("0")) {
            asyncTask.execute();
        }
        view = inflater.inflate(R.layout.fragment_mynote, null);
        context = getActivity();
        initView();
        return view;
    }

    private void initView() {
        tv_quanbu = (TextView) context.getWindow().getDecorView().getRootView().findViewById(R.id.tv_influence);
        tv_shijie = (TextView) context.getWindow().getDecorView().getRootView().findViewById(R.id.tv_world);
        tv_pengyoumen = (TextView) context.getWindow().getDecorView().getRootView().findViewById(R.id.tv_friends);


        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview.setFocusable(false);
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
        queryData();
    }

    private void queryData() {
        mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, status, MyNoteFragment.this);
        LogUtils.e("msg", "pageindex");
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        switch (status) {
            case "3"://打赏内容的处理
                final RewardBean rewardBean = new Gson().fromJson(content, RewardBean.class);
                if (rewardBean == null) {
                    return;
                }
                if ("1".equals(rewardBean.getResult())) {
                    tv_quanbu.setText("全部（" + rewardBean.allcount + "）");
                    tv_shijie.setText("世界（" + rewardBean.wordcount + "）");
                    tv_pengyoumen.setText("朋友们（" + rewardBean.friendcount + "）");
                    if (pageindex > 1) {
                        if (rewardEntities != null && rewardEntities.size() > 0 && rewardBean.getData().size() != 0) {
                            rewardEntities.addAll(rewardBean.getData());
                            adapter.setmData(rewardEntities);
                            adapter.notifyDataSetChanged();
                        } else {
                            pageindex--;
                            ShowUtil.showToast(context, "没有更多数据了");
                        }
                    } else if (pageindex == 1) {
                        if (adapter != null && rewardEntities != null && rewardEntities.size() > 0) {
                            rewardEntities = rewardBean.getData();
                            adapter.setmData(rewardEntities);
                            adapter.notifyDataSetChanged();
                        } else {
                            rewardEntities = rewardBean.getData();
                            adapter = new MyRewardAdapter(context, rewardEntities);
                            mNoScrollListview.setAdapter(adapter);
                            mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //跳转到帖子详情页 或转发详情页

                                    Intent intent = new Intent(context, NoteDetailActivity.class);
                                    intent.putExtra("noteId", rewardEntities.get(i).getNoteid());
                                    intent.putExtra("repostid", "0");
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                } else {
                }
                break;
            default:
                //发布和转发的事件处理
                LogUtils.e("  信息列表 =" + content);
                NoteBean noteBean = new Gson().fromJson(content, NoteBean.class);
                if (noteBean == null) {
                    return;
                }
                if ("1".equals(noteBean.getResult())) {
                    tv_quanbu.setText("全部（" + noteBean.allcount + "）");
                    if (status.equals("2")) {
                        tv_shijie.setText("影响力（" + noteBean.wordcount + "）");
                    } else {
                        tv_shijie.setText("世界（" + noteBean.wordcount + "）");
                    }
                    tv_pengyoumen.setText("朋友们（" + noteBean.friendcount + "）");
                    if (pageindex > 1) {
                        if (mNoteList != null && mNoteList.size() > 0 && noteBean.data.size() != 0) {
                            mNoteList.addAll(noteBean.data);
                            mAdapter.setmDatas(mNoteList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            pageindex--;
                            ShowUtil.showToast(context, "没有更多数据了");
                        }
                    } else if (pageindex == 1) {
                        if (mAdapter != null && mNoteList != null && mNoteList.size() > 0) {
                            mNoteList = noteBean.getData();
                            mAdapter.setmDatas(mNoteList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mNoteList = noteBean.getData();
                            mAdapter = new NoteListAdapter(mNoteList, context);
                            mNoScrollListview.setAdapter(mAdapter);
                            mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = null;
                                    //跳转到转发帖子详情页
                                    if (mNoteList.get(i).getRepostid().equals("0")) {
                                        intent = new Intent(context, NoteDetailActivity.class);
                                        intent.putExtra("noteId", mNoteList.get(i).getNoteid());
                                        intent.putExtra("repostid", mNoteList.get(i).getRepostid());
                                        startActivity(intent);
                                    } else {
                                        intent = new Intent(context, NoteDetailActivity.class);
                                        intent.putExtra("noteId", mNoteList.get(i).getNoteid());
                                        intent.putExtra("repostid", mNoteList.get(i).getRepostid());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                } else {
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        prsv_ScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {

    }
}
