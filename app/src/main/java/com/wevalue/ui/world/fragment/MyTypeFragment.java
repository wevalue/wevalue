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
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.world.activity.ShiftCityActivity;
import com.wevalue.ui.world.adapter.WorldListAdapter;
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
    private WorldListAdapter mHAdapter;
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
            if (getActivity() != null && isFirstLoad) {
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
        if (!isFirstLoad && noteclas.equals("3")) {
            String userlike = SharedPreferencesUtil.getUserLikeCity(getActivity());
            if (TextUtils.isEmpty(userlike)) {
                userlike = SharedPreferencesUtil.getLocationCity(getActivity());
            }
            if (TextUtils.isEmpty(userlike)) {
                userlike = ("北京");
            }
            if (userlike.equals("地区")) {
                userlike = "北京";
            }
            tv_cityname.setText("当前城市：" + userlike);
        }
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
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                intent.putExtra("noteId", mHListData.get(position).getNoteid());
                intent.putExtra("notevideopic", mHListData.get(position).getNotevideopic());
                intent.putExtra("notevide", mHListData.get(position).getNotevideo());
                intent.putExtra("repostid", mHListData.get(position).getRepostid());
                intent.putExtra("repostfrom", "1");
                startActivity(intent);
            }
        });
        //长按添加不喜欢功能
//        mNoScrollListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
//                PopuUtil.initDislikePopuwindow(getActivity(), new PopClickInterface() {
//                    @Override
//                    public void onClickOk(String content) {
//                        mHAdapter.notifyDataSetChanged();
//                        HashMap map = new HashMap();
//                        map.put("code", RequestPath.CODE);
//                        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
//                        map.put("deviceid", SharedPreferencesUtil.getDeviceid(getActivity()));
//                        LogUtils.e("deviceid", SharedPreferencesUtil.getDeviceid(getActivity()));
//                        map.put("noteid", mHListData.get(position).getNoteid());
//                        LogUtils.e("taaaa", SharedPreferencesUtil.getUid(getActivity()));
//                        NetworkRequest.postRequest(RequestPath.POST_HIDENOTE, map, MyTypeFragment.this);
//                        HashMap countMap = new HashMap();
//                        countMap.put("contenttag", noteclas);
//                        countMap.put("hotword", mHListData.get(position).getHotword());
//                        if (mHListData.get(position).getIsself().equals("0")) {
//                            countMap.put("isOrigin", "yuanchaung");
//                        } else {
//                            countMap.put("isOrigin", "feiyuanchaung");
//                        }
//                        countMap.put("moodcount", mHListData.get(position).getMoodcount());
//                        MobclickAgent.onEvent(mContext, StatisticsConsts.event_dislike, countMap);
//                        mHListData.remove(position);
//                    }
//                }, parent, position);
//                return true;
//            }
//        });
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex = 1;
                if (TextUtils.isEmpty(cityName)) {
                    mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), noteclas, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);
                } else {
                    mNoteRequestBase.getNoteListDataForCity(getDateTime(), String.valueOf(pageindex), noteclas, cityName, SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);
                }
            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtils.e("log", "onPullUp");
                pageindex++;
                if (TextUtils.isEmpty(cityName)) {
                    mNoteRequestBase.getNoteListData(getDataTime, String.valueOf(pageindex), noteclas, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);
                } else {
                    mNoteRequestBase.getNoteListDataForCity(getDataTime, String.valueOf(pageindex), noteclas, cityName, SharedPreferencesUtil.getDeviceid(getActivity()), MyTypeFragment.this);
                }
            }
        });
        realm = Realm.getDefaultInstance();
        RealmUtils realmUtils = new RealmUtils();
        try {
            noteclas = realmUtils.loadChanelByName(realm, noteclas).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("3".equals(noteclas)) {
            cityName = SharedPreferencesUtil.getUserLikeCity(getActivity());
            getNoteClassFromList();
            initCityView(true);
        } else {
            initCityView(false);
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

    private void initCityView(boolean flag) {
        ll_cityInfo = (RelativeLayout) view.findViewById(R.id.ll_cityInfo);
        tv_shiftcity = (TextView) view.findViewById(R.id.tv_shiftcity);
        tv_cityname = (TextView) view.findViewById(R.id.tv_cityname);
        if (flag) {
            ll_cityInfo.setVisibility(View.VISIBLE);
        } else {
            ll_cityInfo.setVisibility(View.GONE);
        }
        String userlike = SharedPreferencesUtil.getUserLikeCity(getActivity());
        if (TextUtils.isEmpty(userlike)) {
            userlike = SharedPreferencesUtil.getLocationCity(getActivity());
        }
        if (TextUtils.isEmpty(userlike)) {
            userlike = ("北京");
        }
        if (userlike.equals("地区")) {
            userlike = "北京";
        }
        tv_cityname.setText("当前城市：" + userlike);
        tv_shiftcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShiftCityActivity.class);
                startActivity(intent);
            }
        });
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
        switch (isUrl) {
            case RequestPath.GET_GETNOTE:
                Gson gson = new Gson();
                pgb.setVisibility(View.GONE);
                NoteBean noteBean = gson.fromJson(content, NoteBean.class);
                prsv_ScrollView.onRefreshComplete();
                if (noteBean != null && "1".equals(noteBean.getResult())) {
                    mListData_jiage = noteBean.data_jiage;
                    if (pageindex > 1) {
                        if (noteBean.getData().size() > 0) {
                            mHListData.addAll(noteBean.data);
//                            if (mListData_jiage != null && mListData_jiage.size() > 2) {
//                                mHListData.add(0, mListData_jiage.get(0));
//                                mHListData.add(1, mListData_jiage.get(1));
//                                mHListData.add(2, mListData_jiage.get(2));
//                            }
                            mHAdapter.setmDatas(mHListData);
                            mHAdapter.notifyDataSetChanged();
                        } else {
                            ShowUtil.showToast(getActivity(), "没有更多数据了");
                        }
                    } else {
                        if (!MainActivity.isEditChannel && mHAdapter != null && mHListData != null && mHListData.size() > 0) {
                            mHListData = noteBean.getData();
                            if (mListData_jiage != null && mListData_jiage.size() > 2) {
                                mHListData.add(0, mListData_jiage.get(0));
                                mHListData.add(1, mListData_jiage.get(1));
                                mHListData.add(2, mListData_jiage.get(2));
                            }
                            mHAdapter.setmDatas(mHListData);
                            LogUtils.e("mHlistDatas.size = " + mHListData.size());
                            LogUtils.e("noteBean.getData()= " + noteBean.getData().size());
                            mHAdapter.notifyDataSetChanged();
                        } else {
                            mHListData = noteBean.getData();
                            if (mListData_jiage != null && mListData_jiage.size() > 2) {
                                mHListData.add(0, mListData_jiage.get(0));
                                mHListData.add(1, mListData_jiage.get(1));
                                mHListData.add(2, mListData_jiage.get(2));
                            }
                            mHAdapter = new WorldListAdapter(mHListData, mListData_jiage, mainActivity, "nnn");
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
    }

    //从帖子的标签类型获取帖子标签类型的ID
    private void getNoteClassFromList() {
        if (TextUtils.isEmpty(cityName)) {
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), noteclas, null, SharedPreferencesUtil.getDeviceid(getActivity()), this);
        } else {
            mNoteRequestBase.getNoteListDataForCity(getDateTime(), String.valueOf(pageindex), noteclas, cityName, SharedPreferencesUtil.getDeviceid(getActivity()), this);
        }
    }
}
