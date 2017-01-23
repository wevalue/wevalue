package com.wevalue.ui.influence;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.FriendsNoteDetailsActivity;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.activity.RepostNoteDetailActivity;
import com.wevalue.ui.influence.adapter.InfluenceAdapter;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class InfluenceFragment extends BaseFragment implements WZHttpListener, View.OnClickListener, PopClickInterface {
    private View view = null;
    private Context mContext;
    private NoScrollListview mNoScrollListview;
    private PullToRefreshScrollView prsv_ScrollView;
    private InfluenceAdapter mHAdapter;
    private List<NoteBean.NoteEntity> mHListData;
    private List<NoteBean.NoteEntity> mListData_lunbo;
    private List<NoteBean.NoteEntity> mListData_jiage;
    private int pageindex = 1;
    private String notezone = "2";
    private String orderstatus = "1";//1倒序   0 正序
    private String ordertype = "0";
    private TextView tv_friends;
    private TextView tv_heat;
    private TextView tv_price;
    private TextView tv_time;
    private LinearLayout ll_quanbu_ui;
    private ProgressBar pgb;
    private boolean isFirstLoad = true;
    private Drawable drawable;
    private Drawable drawable1;
    private Drawable drawable2;
    private int i = 0;
    private String getDataTime;
    private String lastClickButton = "tv_influence";
    private boolean isVisibleToUser;
    //相当于onResume
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        String s = String.valueOf(isVisibleToUser);
        LogUtils.e("influence", s);
        if (isVisibleToUser) {
            if (isFirstLoad) {
                pgb.setVisibility(View.VISIBLE);
                getDateTime();
                getNoteList();
                MobclickAgent.onPageStart(lastClickButton);
                LogUtils.e("lifeStyle", "影響力進入頁面統計" + lastClickButton);
                isFirstLoad = false;
            } else {
                MobclickAgent.onPageStart(lastClickButton);
                LogUtils.e("lifeStyle", "影響力進入頁面統計" + lastClickButton);
            }

        } else {
            if (!isFirstLoad) {
                MobclickAgent.onPageEnd(lastClickButton);
                LogUtils.e("lifeStyle", "影響力退出頁面統計" + lastClickButton);
            }
        }
    }

    @Override
    public void onResume() {
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageStart(lastClickButton);
            LogUtils.e("lifeStyle", "影響力進入頁面統計" + lastClickButton);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageEnd(lastClickButton);
            LogUtils.e("lifeStyle", "影響力退出頁面統計" + lastClickButton);
        }
        super.onPause();
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_influence, null);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        tv_friends = (TextView) view.findViewById(R.id.tv_friends);
        tv_heat = (TextView) view.findViewById(R.id.tv_heat);
        tv_heat.setOnClickListener(this);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_price.setOnClickListener(this);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        ll_quanbu_ui = (LinearLayout) view.findViewById(R.id.ll_quanbu_ui);
        ll_quanbu_ui.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        mHListData = new ArrayList<>();
//        iv_heat_down = (ImageView) view.findViewById(R.id.iv_heat_down);
//        iv_heat_down.setOnClickListener(this);
//        iv_price_up = (ImageView) view.findViewById(R.id.iv_price_up);
//        iv_price_up.setOnClickListener(this);
//        iv_price_down = (ImageView) view.findViewById(R.id.iv_price_down);
//        iv_price_down.setOnClickListener(this);
//        iv_time_up = (ImageView) view.findViewById(R.id.iv_time_up);
//        iv_time_up.setOnClickListener(this);
//        iv_time_down = (ImageView) view.findViewById(R.id.iv_time_down);
//        iv_time_down.setOnClickListener(this);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = null;
                //跳转到转发帖子详情页
                if (mHListData.get(position).getIsfree().equals("1")) {
                    intent = new Intent(getActivity(), FriendsNoteDetailsActivity.class);
                    LogUtils.e("----pos--" + position);
                    intent.putExtra("noteId", mHListData.get(position).getNoteid());
                    intent.putExtra("repostid", mHListData.get(position).getRepostid());
                    intent.putExtra("repostfrom", "2");
                    startActivity(intent);
                } else {
                    if (mHListData.get(position).getRepostid().equals("0")) {
                        intent = new Intent(getActivity(), NoteDetailsActivity.class);
                        LogUtils.e("----pos--" + position);
                        intent.putExtra("noteId", mHListData.get(position).getNoteid());
                        intent.putExtra("repostid", mHListData.get(position).getRepostid());
                        intent.putExtra("repostfrom", "2");
                        startActivity(intent);
                    } else {
                        RelativeLayout rl_note_content_ui = (RelativeLayout) view.findViewById(R.id.rl_note_content_ui);
                        rl_note_content_ui.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, NoteDetailsActivity.class);
                                intent.putExtra("noteId", mHListData.get(position).getNoteid());
                                intent.putExtra("repostid", "0");
                                intent.putExtra("repostfrom", "2");
                                mContext.startActivity(intent);
                            }
                        });
                        intent = new Intent(mContext, RepostNoteDetailActivity.class);
                        intent.putExtra("noteId", mHListData.get(position).getNoteid());
                        intent.putExtra("repostid", mHListData.get(position).getRepostid());
                        intent.putExtra("repostfrom", "2");
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        mNoScrollListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            //下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex = 1;
                getDateTime();
                getNoteList();
            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex++;
                getNoteList();
            }
        });
    }

    //刷新数据
    public void refreshData() {
        if (prsv_ScrollView == null || pgb == null) {
            return;
        }
        pageindex = 1;
        LogUtils.e("----refreshData----");
        ScrollView scrollView = prsv_ScrollView.getRefreshableView();
        scrollView.smoothScrollTo(0, 0);
        pgb.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDateTime();
                getNoteList();
            }
        }, 300);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content.replaceAll("#换行#", "\r\n"), NoteBean.class);
        pgb.setVisibility(View.GONE);
        prsv_ScrollView.onRefreshComplete();
        if (noteBean.getResult().equals("1")) {
            mListData_lunbo = noteBean.data_lunbo;
            mListData_jiage = noteBean.data_jiage;
            if (pageindex > 1) {
                if (noteBean.getData().size() > 0) {
                    mHListData.addAll(noteBean.data);
                    mHAdapter.setmDatas(mHListData);
                    mHAdapter.setOrderType(ordertype);
                    mHAdapter.notifyDataSetChanged();
                } else {
                    pageindex--;
                    ShowUtil.showToast(getActivity(), "没有更多数据了");
                }
            } else {

                if (mHListData != null) {
                    mHListData.clear();
                }
                if (ordertype.equals("0")) {
                    if (mListData_lunbo != null && mListData_lunbo.size() > 0) {
                        mHListData.addAll(mListData_lunbo);
                    }
                    if (mListData_jiage != null && mListData_jiage.size() > 0) {
                        mHListData.addAll(mListData_jiage);
                    }
                }

                if (noteBean.data != null && noteBean.data.size() > 0) {
                    mHListData.addAll(noteBean.data);
                }
                mHAdapter = new InfluenceAdapter(mHListData, getActivity(), mListData_lunbo, mListData_jiage);
                mHAdapter.setOrderType(ordertype);
                mNoScrollListview.setAdapter(mHAdapter);

            }
        } else {
            ShowUtil.showToast(mContext, noteBean.getMessage());
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_heat:
                statisticsEvent("tv_heat");
                i = 1;
                tv_heat.setTextColor(getActivity().getResources().getColor(R.color.login_text_blue));
                tv_price.setTextColor(getActivity().getResources().getColor(R.color.black));
                tv_time.setTextColor(getActivity().getResources().getColor(R.color.black));

                drawable = getResources().getDrawable(R.mipmap.icon_redu_sel);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                tv_heat.setCompoundDrawables(null, null, drawable, null);//画在右边

                drawable1 = getResources().getDrawable(R.mipmap.influence_price);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
                tv_price.setCompoundDrawables(null, null, drawable1, null);//画在右边

                drawable2 = getResources().getDrawable(R.mipmap.influence_time);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight()); //设置边界
                tv_time.setCompoundDrawables(null, null, drawable2, null);//画在右边
                ordertype = "1";
                pageindex = 1;
//                switch (orderstatus) {
//                    case "0":
//                        orderstatus = "1";
//                        break;
//                    case "1":
//                        orderstatus = "0";
//                        break;
//                }
                pgb.setVisibility(View.VISIBLE);
                getDateTime();
                getNoteList();
                break;
//            case R.id.iv_heat_down:
//                orderstatus = "1";
//                ordertype = "1";
//                prsv_ScrollView.setRefreshing(true);
//                break;
            case R.id.tv_price:
                statisticsEvent("tv_price");
                i = 1;
                tv_price.setTextColor(getActivity().getResources().getColor(R.color.login_text_blue));
                tv_heat.setTextColor(getActivity().getResources().getColor(R.color.black));
                tv_time.setTextColor(getActivity().getResources().getColor(R.color.black));

                drawable = getResources().getDrawable(R.mipmap.influence_heat);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                tv_heat.setCompoundDrawables(null, null, drawable, null);//画在右边

                drawable1 = getResources().getDrawable(R.mipmap.icon_jiage_sel);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
                tv_price.setCompoundDrawables(null, null, drawable1, null);//画在右边

                drawable2 = getResources().getDrawable(R.mipmap.influence_time);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight()); //设置边界
                tv_time.setCompoundDrawables(null, null, drawable2, null);//画在右边
//                switch (orderstatus) {
//                    case "0":
//                        orderstatus = "1";
//                        break;
//                    case "1":
//                        orderstatus = "0";
//                        break;
//                }
                ordertype = "2";
                pageindex = 1;
                pgb.setVisibility(View.VISIBLE);
                getDateTime();
                getNoteList();
                break;
//            case R.id.iv_price_down:
//                orderstatus = "1";
//                ordertype = "2";
//                prsv_ScrollView.setRefreshing(true);
//                break;
            case R.id.tv_time:
                statisticsEvent("tv_time");
                i = 1;
                tv_time.setTextColor(getActivity().getResources().getColor(R.color.login_text_blue));
                tv_heat.setTextColor(getActivity().getResources().getColor(R.color.black));
                tv_price.setTextColor(getActivity().getResources().getColor(R.color.black));

                drawable = getResources().getDrawable(R.mipmap.influence_heat);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                tv_heat.setCompoundDrawables(null, null, drawable, null);//画在右边

                drawable1 = getResources().getDrawable(R.mipmap.influence_price);
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
                tv_price.setCompoundDrawables(null, null, drawable1, null);//画在右边

                drawable2 = getResources().getDrawable(R.mipmap.icon_shijian_sel);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight()); //设置边界
                tv_time.setCompoundDrawables(null, null, drawable2, null);//画在右边
//                switch (orderstatus) {
//                    case "0":
//                        orderstatus = "1";
//                        break;
//                    case "1":
//                        orderstatus = "0";
//                        break;
//                }
                ordertype = "3";
                pageindex = 1;
                pgb.setVisibility(View.VISIBLE);
                getDateTime();
                getNoteList();
                break;
//            case R.id.iv_time_down:
//                orderstatus = "1";
//                ordertype = "3";
//                prsv_ScrollView.setRefreshing(true);
//                break;
            case R.id.ll_quanbu_ui:

                if (i == 1) {
                    tv_heat.setTextColor(getActivity().getResources().getColor(R.color.black));
                    tv_price.setTextColor(getActivity().getResources().getColor(R.color.black));
                    tv_time.setTextColor(getActivity().getResources().getColor(R.color.black));

                    drawable = getResources().getDrawable(R.mipmap.influence_heat);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    tv_heat.setCompoundDrawables(null, null, drawable, null);//画在右边

                    drawable1 = getResources().getDrawable(R.mipmap.influence_price);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight()); //设置边界
                    tv_price.setCompoundDrawables(null, null, drawable1, null);//画在右边

                    drawable2 = getResources().getDrawable(R.mipmap.influence_time);
                    drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight()); //设置边界
                    tv_time.setCompoundDrawables(null, null, drawable2, null);//画在右边
                    i = 0;

                } else if (i == 0) {
                    if (tv_friends.getText().toString().equals("影响力")) {
                        statisticsEvent("tv_friend");
                        notezone = "1";
                        tv_friends.setText("朋友们");
                    } else if (tv_friends.getText().toString().equals("朋友们")) {
                        statisticsEvent("tv_friend");
                        notezone = "2";
                        tv_friends.setText("影响力");
                        statisticsEvent("tv_influence");
                    }
//                PopuUtil.initpopu(getActivity(), ll_quanbu_ui, tv_friends, notezone, this);
                }
                ordertype = "0";
                pgb.setVisibility(View.VISIBLE);
                pageindex = 1;
                getDateTime();
                getNoteList();
                break;
        }
    }

    private void getNoteList() {
        LogUtils.e("notezone", notezone);
        LogUtils.e("orderstatus", orderstatus);
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(mContext));
        map.put("pagenum", "10");
        map.put("pageindex", String.valueOf(pageindex));
        map.put("notezone", notezone);
        map.put("ordertype", ordertype);
        map.put("deviceid", SharedPreferencesUtil.getDeviceid(mContext));
        map.put("orderstatus", orderstatus);
        map.put("requesttime", getDataTime);
        LogUtils.e("notezone", notezone);
        LogUtils.e("notezoneorderstatus", orderstatus);
        NetworkRequest.getRequest(RequestPath.GET_INFLUENCENOTES, map, InfluenceFragment.this);
    }

    @Override
    public void onClickOk(String content) {
        switch (content) {
            case "影响力":
                notezone = "2";
                break;
            case "朋友们":
                notezone = "1";
                break;
        }
        ordertype = "0";
        pgb.setVisibility(View.VISIBLE);
        getNoteList();
    }

    /*友盟统计页面时长*/
    private void statisticsEvent(String page) {
//        MobclickAgent.onEvent(getActivity(), "event_influence", page);
        if (page.equals(lastClickButton)) {
            return;
        }
        switch (page) {
            case "tv_heat":
                LogUtils.e("lifeStyle", lastClickButton + "退出頁面統計");
                MobclickAgent.onPageEnd(lastClickButton);
                MobclickAgent.onPageStart(page);
                LogUtils.e("lifeStyle", page + "進入頁面統計");
                break;
            case "tv_influence":
                LogUtils.e("lifeStyle", lastClickButton + "退出頁面統計");
                MobclickAgent.onPageEnd(lastClickButton);
                MobclickAgent.onPageStart(page);
                LogUtils.e("lifeStyle", page + "進入頁面統計");
                break;
            case "tv_friend":
                LogUtils.e("lifeStyle", lastClickButton + "退出頁面統計");
                MobclickAgent.onPageEnd(lastClickButton);
                MobclickAgent.onPageStart(page);
                LogUtils.e("lifeStyle", page + "進入頁面統計");
                break;
            case "tv_price":
                LogUtils.e("lifeStyle", lastClickButton + "退出頁面統計");
                MobclickAgent.onPageEnd(lastClickButton);
                MobclickAgent.onPageStart(page);
                LogUtils.e("lifeStyle", page + "進入頁面統計");
                break;
            case "tv_time":
                LogUtils.e("lifeStyle", lastClickButton + "退出頁面統計");
                MobclickAgent.onPageEnd(lastClickButton);
                MobclickAgent.onPageStart(page);
                LogUtils.e("lifeStyle", page + "進入頁面統計");
                break;
        }
        lastClickButton = page;
    }
}
