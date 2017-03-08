package com.wevalue.ui.mine.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.model.EarningsRankModel;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.mine.adapter.EarningRankAdapter;
import com.wevalue.ui.mine.adapter.NoteRankAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.view.NoScrollListview;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/21
 * 类说明：周排行榜中的fragment
 */
public class RankListFragment extends Fragment implements WZHttpListener {
    String rankType;
    //用户收益的列表和适配器
    private List<EarningsRankModel.DataBean> mBeanList;
    private EarningsRankModel mRankModel;
    EarningRankAdapter mRankListAdapter;
    //内容收益和内容转发数量的列表和适配器
    private NoteBean mNoteBean;
    private List<NoteBean.NoteEntity> mNoteEntityList;
    private NoteRankAdapter mWorldListAdapter;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    View view;
    private String notezone;//帖子发布的范围


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        LogUtils.e("cccc", "onCreateView");
        rankType = bundle.getString("rankType");
        notezone = bundle.getString("notezone");
//        if (!notezone.equals("0")) {
//            asyncTask.execute();
//        }
        view = inflater.inflate(R.layout.fragment_ranklist, null);
        initView();
        return view;
    }

    private void initView() {
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                queryData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });


//        news Thread(){
//            @Override
//            public void run() {
//                super.run();
//
//
//            }
//        }.start();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                queryData();
            }
        });

    }

    private void queryData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("notezone", notezone);
        map.put("ranktype", rankType);
        map.put("userid", SharedPreferencesUtil.getUid(getActivity().getApplicationContext()));
        NetworkRequest.getRequest(RequestPath.GET_RANKLIST, map, this);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        Gson gson = new Gson();
        switch (rankType) {
            case "1":
                mRankModel = gson.fromJson(content, EarningsRankModel.class);
                if (mRankModel.getResult() == 1) {
                    mBeanList = mRankModel.getData();
                    setIncomeAdapter();
                }
                break;
            case "2":
                mNoteBean = gson.fromJson(content, NoteBean.class);
                if (mNoteBean.getResult().equals("1")) {
                    mNoteEntityList = mNoteBean.getData();
                    setContentAdapter("2");
                }
                break;
            case "3":
                mNoteBean = gson.fromJson(content, NoteBean.class);
                if (mNoteBean.getResult().equals("1")) {
                    mNoteEntityList = mNoteBean.getData();
                    setContentAdapter("3");
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
    }

    private void setIncomeAdapter() {
        if (mRankListAdapter == null) {
            mRankListAdapter = new EarningRankAdapter(getActivity(), mBeanList);
            mNoScrollListview.setAdapter(mRankListAdapter);
            mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LogUtils.e("msg", "收益排行榜");
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("detailuserid", mBeanList.get(position).getUserid());
//                    SharedPreferencesUtil.setDetailUserid(getActivity(), mBeanList.get(position).getUserid());
                    startActivity(intent);
                }
            });
        } else {
            mRankListAdapter.notifyDataSetChanged();
        }
    }

    private void setContentAdapter(String type) {
        if (mWorldListAdapter == null) {
            mWorldListAdapter = new NoteRankAdapter(mNoteEntityList, getActivity(), type);
            mNoScrollListview.setAdapter(mWorldListAdapter);
            mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LogUtils.e("msg", "打赏排行榜");
                    if (mNoteEntityList.get(position).getIsfree().equals("1")) {
                        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                        intent.putExtra("noteId", mNoteEntityList.get(position).getNoteid());
                        intent.putExtra("repostid", mNoteEntityList.get(position).getRepostid());
                        LogUtils.e("msg", mNoteEntityList.get(position).getNoteid() + mNoteEntityList.get(position).getRepostid());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                        intent.putExtra("noteId", mNoteEntityList.get(position).getNoteid());
                        intent.putExtra("repostid", mNoteEntityList.get(position).getRepostid());
                        LogUtils.e("msg", mNoteEntityList.get(position).getNoteid() + mNoteEntityList.get(position).getRepostid());
                        startActivity(intent);
                    }

                }
            });
        } else {
            mWorldListAdapter.notifyDataSetChanged();
        }
    }
}
