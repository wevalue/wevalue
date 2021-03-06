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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.adapter.NoteListAdapter;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.CarouselBean;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.activity.SearchActivity;
import com.wevalue.utils.ActivityUtils;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;
import com.wevalue.youmeng.StatisticsConsts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class InfluenceFragment extends BaseFragment implements WZHttpListener, View.OnClickListener {
    private View view = null;
    private Context mContext;
    private NoScrollListview mNoScrollListview;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoteListAdapter mHAdapter;
    //private List<NoteBean.NoteEntity> mHListData;
    private List<CarouselBean> mListData_lunbo;
    //去掉价格  所以 给个初始值 为 size = 0
    private List<NoteBean.NoteEntity> mListData_jiage = new ArrayList<NoteBean.NoteEntity>();
    private int pageindex = 1;

    private String notezone = "1"; // 1 朋友们  2 影响力
    private String orderstatus = "1";//1倒序   0 正序
    private String ordertype = "0";
    private TextView tv_heat; //热度
    private TextView tv_price; //价格
    private TextView tv_time; //时间
    private ProgressBar pgb;
    private boolean isFirstLoad = true;
    private Drawable drawable_heat_p;
    private Drawable drawable_heat_n;
    private Drawable drawable_price_p;
    private Drawable drawable_price_n;
    private Drawable drawable_time_p;
    private Drawable drawable_time_n;
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
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        tv_heat = (TextView) view.findViewById(R.id.tv_heat);
        tv_heat.setOnClickListener(this);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_price.setOnClickListener(this);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);

        drawable_heat_n = getResources().getDrawable(R.mipmap.influence_heat_n);
        drawable_heat_p = getResources().getDrawable(R.mipmap.influence_heat_p);
        drawable_price_n = getResources().getDrawable(R.mipmap.influence_price_n);
        drawable_price_p = getResources().getDrawable(R.mipmap.influence_price_p);
        drawable_time_n = getResources().getDrawable(R.mipmap.influence_time_n);
        drawable_time_p = getResources().getDrawable(R.mipmap.influence_time_p);

        drawable_heat_n.setBounds(0, 0, drawable_heat_n.getMinimumWidth(), drawable_heat_n.getMinimumHeight());
        drawable_heat_p.setBounds(0, 0, drawable_heat_n.getMinimumWidth(), drawable_heat_n.getMinimumHeight());
        drawable_price_n.setBounds(0, 0, drawable_price_n.getMinimumWidth(), drawable_price_n.getMinimumHeight());
        drawable_price_p.setBounds(0, 0, drawable_price_p.getMinimumWidth(), drawable_price_p.getMinimumHeight());
        drawable_time_n.setBounds(0, 0, drawable_time_n.getMinimumWidth(), drawable_time_n.getMinimumHeight());
        drawable_time_p.setBounds(0, 0, drawable_time_p.getMinimumWidth(), drawable_time_p.getMinimumHeight());
       // tv_iszan.setCompoundDrawables(iszan, null, null, null);

        //mHListData = new ArrayList<>();
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setFocusable(false);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //跳转到转发帖子详情页
                NoteBean.NoteEntity noteEntity = (NoteBean.NoteEntity) parent.getAdapter().getItem(position);
                ActivityUtils.gotoNoteDetails(getActivity(),noteEntity,"2");
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
           // mListData_jiage = noteBean.data_jiage;
            if (pageindex > 1) {
                if (noteBean.getData().size() > 0) {
                    mHAdapter.setmDatas(noteBean.data);
                    mHAdapter.setOrderType(ordertype);
                    mHAdapter.notifyDataSetChanged();
                } else {
                    pageindex--;
                    ShowUtil.showToast(getActivity(), "没有更多数据了");
                }
            } else {
                mHAdapter = new NoteListAdapter(new ArrayList<NoteBean.NoteEntity>(), getActivity());
                mHAdapter.setOrderType(ordertype);
                mNoScrollListview.setAdapter(mHAdapter);
                if (noteBean.data != null && noteBean.data.size() > 0) {
                    mHAdapter.setmDatas(noteBean.data);
                    mHAdapter.notifyDataSetChanged();
                }
                mHAdapter.notifyDataSetChanged();
            }
        } else {
            ShowUtil.showToast(mContext, noteBean.getMessage());
        }
    }

    @Override
    public void onFailure(String content) {
        pgb.setVisibility(View.GONE);
        prsv_ScrollView.onRefreshComplete();
    }
    private void selecedTile(String notezone,TextView titleView){
        this.notezone = notezone;
    if (notezone.equals("2")){
        statisticsEvent("tv_influence");
        titleView.setText("");
        titleView.setBackgroundResource(R.mipmap.iv_gz);
    }else {
        statisticsEvent("tv_friend");
        titleView.setText("");
        titleView.setBackgroundResource(R.mipmap.iv_py);
    }
        pgb.setVisibility(View.VISIBLE);
        pageindex = 1;
        //获取时间
        getDateTime();
        //恢复选择的 ordertype
        selecedText(null,"0");
        //获取数据
        getNoteList();
    }

    /**
     *
     * @param textView
     */
    private void selecedText(TextView textView,String ordertype){
        //恢复默认样式
        this.ordertype = ordertype;
        tv_heat.setTextColor(getActivity().getResources().getColor(R.color.black_444));
        tv_price.setTextColor(getActivity().getResources().getColor(R.color.black_444));
        tv_time.setTextColor(getActivity().getResources().getColor(R.color.black_444));
        tv_heat.setCompoundDrawables(drawable_heat_n, null, null, null);
        tv_price.setCompoundDrawables(drawable_price_n, null, null, null);
        tv_time.setCompoundDrawables(drawable_time_n, null, null, null);
        //设置选中样式
        if (textView==null)return;
        textView.setTextColor(getActivity().getResources().getColor(R.color.blue_price));
        switch (textView.getId()){
            case R.id.tv_heat :
                tv_heat.setCompoundDrawables(drawable_heat_p, null, null, null);
                break;
            case R.id.tv_price :
                tv_price.setCompoundDrawables(drawable_price_p, null, null, null);
                break;
            case R.id.tv_time :
                tv_time.setCompoundDrawables(drawable_time_p, null, null, null);
                break;
        }
        i = 1 ;
        pageindex = 1;
        pgb.setVisibility(View.VISIBLE);
        getDateTime();
        getNoteList();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {//
//            case R.id.tv_head_title: //影响力
//                selecedTile("2");
//                break;
//            case R.id.tv_head1_title://朋友们
//                selecedTile("1");
//                break;
//            case R.id.iv_search://搜索
//                MobclickAgent.onEvent(getActivity(), StatisticsConsts.event_search);//友盟统计搜索启动
//                startActivity(new Intent(getActivity(), SearchActivity.class));
//                break;
//            case R.id.iv_more: //更多
//                PopuUtil.initpopu(getActivity(), iv_more);
//                break;
            case R.id.tv_heat:
                statisticsEvent("tv_heat");
                selecedText(tv_heat,"1");
                break;
            case R.id.tv_price:
                statisticsEvent("tv_price");
                selecedText(tv_price,"2");
                break;

            case R.id.tv_time:
                statisticsEvent("tv_time");
                selecedText(tv_time,"3");
                break;
        }
    }

    private void getNoteList() {
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
        NetworkRequest.getRequest(RequestPath.GET_INFLUENCENOTES, map, InfluenceFragment.this);
    }

//    @Override
//    public void onClickOk(String content) {
//        switch (content) {
//            case "影响力":
//                notezone = "2";
//                break;
//            case "朋友们":
//                notezone = "1";
//                break;
//        }
//        ordertype = "0";
//        pgb.setVisibility(View.VISIBLE);
//        getNoteList();
//    }

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
    public void setTitlt(final TextView titleView){
        titleView.setText("");
        if (notezone.equals("1")){
            titleView.setBackgroundResource(R.mipmap.iv_py);
        }else {
            titleView.setBackgroundResource(R.mipmap.iv_gz);
        }
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notezone.equals("1"))
                    notezone = "2";
                else {
                    notezone = "1";
                }
                selecedTile(notezone,titleView);
            }
        });
    }
}
