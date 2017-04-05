package com.wevalue.ui.world.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.ui.world.activity.ShiftCityActivity;
import com.wevalue.ui.world.adapter.WorldListAdapter;
import com.wevalue.ui.world.adapter.WorldListAdapters;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.RealmUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;

/**
 * Created by lt on 2015/12/14.
 */
public class MyTypeFragment extends BaseFragment implements WZHttpListener {

    private View view;
    private Context mContext;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private WorldListAdapters mHAdapter;
    private List<NoteBean.NoteEntity> mHListData;
    private List<NoteBean.NoteEntity> mListData_jiage;
    //帖子的标签
    private String noteclas = "1";
    private NoteRequestBase mNoteRequestBase;
    private int pageindex = 1;
    private Realm realm;
    private RelativeLayout ll_cityInfo;
    private TextView tv_shiftcity;
    private TextView tv_cityname;
    private String cityName = "";
    private ProgressBar pgb;
    private String getDataTime;

    public MyTypeFragment() {
    }

    @SuppressLint("ValidFragment")
    public MyTypeFragment(String noteclas) {
        this.noteclas = noteclas;
        LogUtils.e("MyTypeFragment", noteclas);

    }

    MainActivity mainActivity;
    private boolean isFirstLoad = true;

    /**
     * 获取北京时间
     */
    private String getDateTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String date = dff.format(new Date());
        getDataTime = String.valueOf(DateTiemUtils.dateToTime(date));

        LogUtils.e("单前时间=" + getDataTime);
        return getDataTime;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(WeValueApplication.applicationContext);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                if (mNoteRequestBase == null) {
                    mNoteRequestBase = NoteRequestBase.getNoteRequestBase(getActivity());
                }
                getNoteClassFromList();
                isFirstLoad = false;
                LogUtils.e("noteclas", noteclas);
            }
            cityName = SharedPreferencesUtil.getUserLikeCity(getActivity());
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mainActivity = (MainActivity) this.getActivity();
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_type, null);
        initView();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        LogUtils.e("轮播  -  initView");
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        mNoScrollListview.setFocusable(false);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("noteId", mHListData.get(position).getNoteid());
                intent.putExtra("notevideopic", mHListData.get(position).getNotevideopic());
                intent.putExtra("notevide", mHListData.get(position).getNotevideo());
                intent.putExtra("repostid", mHListData.get(position).getRepostid());
                intent.putExtra("repostfrom", "1");
                startActivity(intent);
            }
        });

        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex = 1;
                mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), noteclas, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);

            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex++;
                mNoteRequestBase.getNoteListData(getDataTime, String.valueOf(pageindex), noteclas, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);

            }
        });
        realm = Realm.getDefaultInstance();
        RealmUtils realmUtils = new RealmUtils();
        try {
            if (realm == null) return;
            noteclas = realmUtils.loadChanelByName(realm, noteclas).getId();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    //刷新数据
    public void refreshData() {
        pageindex = 1;
        if (prsv_ScrollView == null) {
            return;
        }
        ScrollView scrollView = prsv_ScrollView.getRefreshableView();
        scrollView.smoothScrollTo(0, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(cityName)) {
                    mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), noteclas, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);
                } else {
                    mNoteRequestBase.getNoteListDataForCity(getDateTime(), String.valueOf(pageindex), noteclas, cityName, SharedPreferencesUtil.getDeviceid(WeValueApplication.applicationContext), MyTypeFragment.this);
                }
            }
        }, 300);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && requestCode == 101) {
            }
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        switch (isUrl) {
            case RequestPath.GET_GETNOTE:
                Gson gson = new Gson();
                pgb.setVisibility(View.GONE);
                NoteBean noteBean = gson.fromJson(content, NoteBean.class);

                if (noteBean != null && "1".equals(noteBean.getResult())) {
                    mListData_jiage = noteBean.data_jiage;
                    if (pageindex > 1) {
                        if (noteBean.getData().size() > 0) {
                            mHListData.addAll(noteBean.data);
                            mHAdapter.setmDatas(mHListData);
                            mHAdapter.notifyDataSetChanged();
                        } else {
                            ShowUtil.showToast(getActivity(), "没有更多数据了");
                        }
                    } else {
                        if (!MainActivity.isEditChannel && mHAdapter != null && mHListData != null && mHListData.size() > 0) {
                            mHListData = noteBean.getData();
                            if (mListData_jiage != null && mListData_jiage.size() > 0) {
                                removeRepeat(mHListData, mListData_jiage);
                                mHListData.addAll(0, getJiage(mListData_jiage));
                            }
                            mHAdapter.setmDatas(mHListData);
                            mHAdapter.notifyDataSetChanged();
                        } else {
                            mHListData = noteBean.getData();
                            if (mListData_jiage != null) {
                                removeRepeat(mHListData, mListData_jiage);
                                mHListData.addAll(0, getJiage(mListData_jiage));
                            }
                            mHAdapter = new WorldListAdapters(mHListData, mListData_jiage, mainActivity, "nnn");
                            mHAdapter.notifyDataSetChanged();
                            mNoScrollListview.setAdapter(mHAdapter);
                        }
                    }
                } else {
                    LogUtils.e("轮播  -  onSuccess   else {");
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        prsv_ScrollView.onRefreshComplete();
    }

    //只显示前三个
    private List<NoteBean.NoteEntity> getJiage(List<NoteBean.NoteEntity> mListData_jiage) {
        if (mListData_jiage != null && mListData_jiage.size() > 0)
            if (mListData_jiage.size() > 3) {
                mListData_jiage.remove(mListData_jiage.size() - 1);
                getJiage(mListData_jiage);
            }
        return mListData_jiage;
    }

    private List<NoteBean.NoteEntity> removeRepeat(List<NoteBean.NoteEntity> mHListData, List<NoteBean.NoteEntity> mListData_jiage) {
        if (mListData_jiage == null && mListData_jiage.size() > 0) {
            if (mHListData != null && mHListData.size() > 0) {
                for (int i = 0; i < mListData_jiage.size(); i++) {
                    mHListData.remove(mListData_jiage.get(i));
                }
            }
        }
        return mHListData;
    }

    //从帖子的标签类型获取帖子标签类型的ID
    private void getNoteClassFromList() {
        if (null!=pgb)pgb.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(cityName)) {
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), noteclas, null, SharedPreferencesUtil.getDeviceid(getActivity()), this);
        } else {
            mNoteRequestBase.getNoteListDataForCity(getDateTime(), String.valueOf(pageindex), noteclas, cityName, SharedPreferencesUtil.getDeviceid(getActivity()), this);
        }
    }
}
