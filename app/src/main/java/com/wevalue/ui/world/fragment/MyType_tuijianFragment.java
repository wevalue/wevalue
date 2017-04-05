package com.wevalue.ui.world.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.CarouselBean;
import com.wevalue.model.NoteBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.ui.mine.activity.FeedbackActivity;
import com.wevalue.ui.mine.activity.WebActivity;
import com.wevalue.ui.world.adapter.WorldListAdapters;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.DialogUtil;
import com.wevalue.utils.ImageUitls;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.CarouselView;
import com.wevalue.view.NoScrollListview;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

/**
 * Created by lt on 2015/12/14.
 */
public class MyType_tuijianFragment extends BaseFragment implements WZHttpListener, CarouselView.CarouselLinster {
    private View view;
    private Context mContext;
    private ArrayList<View> viewList;
    private CarouselView carouselView;
    private long mExitTime = 0;
    private int mViewpagerIndex = 0;
    private TextView[] imageViews = null;//小圆点
    public boolean isContinue = true;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private WorldListAdapters mHAdapter;
    private List<NoteBean.NoteEntity> mHListData;
    private List<CarouselBean> mListData_lunbo;
    private List<NoteBean.NoteEntity> mListData_jiage;
    private List<NoteBean.NoteEntity> mListData_jiage_2 = new ArrayList<>();
    private int viewIndex = -1;

    private int clickIndex;
    private int mItmePosition = 100;
    private Timer timer;
    private GestureDetector gesture; //手势识别
    private WorldFragment mWorldFragment;
    private NoteRequestBase mNoteRequestBase;
    private int pageindex = 1;
    private ImageView iv_img1;
    private ImageView iv_img2;
    private ImageView iv_img3;
    private String getDataTime;
    private  String noteClass = "3";
//    private ProgressBar pgb;
public Dialog loadingDialog ;
    public MyType_tuijianFragment() {

    }

    @SuppressLint("ValidFragment")
    public MyType_tuijianFragment(int index, WorldFragment worldFragment) {
        viewIndex = index;
        mWorldFragment = worldFragment;
    }

    MainActivity mainActivity;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {

        }
    }

    /**
     * 获取北京时间
     */
    private String getDateTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String date = dff.format(new Date());
        getDataTime = String.valueOf(DateTiemUtils.dateToTime(date));

        return getDataTime;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        mainActivity = (MainActivity) this.getActivity();
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(getActivity());
        loadingDialog = DialogUtil.createLoadingDialog(getActivity());
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_type_tuijian, null);

        //根据父窗体getActivity()为fragment设置手势识别
//        gesture = news GestureDetector(this.getActivity(), this);
//        MainActivity.MyOnTouchListener myOnTouchListener = news MainActivity.MyOnTouchListener() {
//            @Override
//            public boolean onTouch(MotionEvent ev) {
//                boolean result = gesture.onTouchEvent(ev);
//                return result;
//            }
//        };
//        ((MainActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
        initView();

        String tuijianJson = SharedPreferencesUtil.getContent(getActivity(), "tuijian");
        if (TextUtils.isEmpty(tuijianJson)) {
            loadingDialog.show();
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", noteClass, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
        } else {
            paserJson(tuijianJson);
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", noteClass, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
        }
        return view;
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
                mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex),
                        "1", noteClass, "", SharedPreferencesUtil.getDeviceid(getActivity()),
                        MyType_tuijianFragment.this);
            }
        }, 300);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        LogUtils.e("轮播  -  initView");
        //轮播图
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, (int)(width/2.5));
        carouselView.setLayoutParams(params);
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItmePosition = position;
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("noteId", mHListData.get(position).getNoteid());
                intent.putExtra("repostid", mHListData.get(position).getRepostid());
                intent.putExtra("repostfrom", "1");
                startActivity(intent);
            }
        });
        //长按添加不喜欢功能
        mNoScrollListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                PopuUtil.initDislikePopuwindow(getActivity(), new PopClickInterface() {
                    @Override
                    public void onClickOk(String content) {
                        mHAdapter.notifyDataSetChanged();
                        HashMap map = new HashMap();
                        map.put("code", RequestPath.CODE);
                        map.put("userid", SharedPreferencesUtil.getUid(getActivity()));
                        map.put("deviceid", SharedPreferencesUtil.getDeviceid(getActivity()));
                        LogUtils.e("deviceid", SharedPreferencesUtil.getDeviceid(getActivity()));
                        map.put("noteid", mHListData.get(position).getNoteid());
                        LogUtils.e("taaaa", SharedPreferencesUtil.getUid(getActivity()));
                        NetworkRequest.postRequest(RequestPath.POST_HIDENOTE, map, MyType_tuijianFragment.this);

                        HashMap countMap = new HashMap();
                        countMap.put("contenttag", "tuijian");
                        countMap.put("hotword", mHListData.get(position).getHotword());
                        if (mHListData.get(position).getIsself().equals("0")) {
                            countMap.put("isOrigin", "yuanchaung");
                        } else {
                            countMap.put("isOrigin", "feiyuanchaung");
                        }
                        countMap.put("moodcount", mHListData.get(position).getMoodcount());
                        MobclickAgent.onEvent(mContext, StatisticsConsts.event_dislike, countMap);
                        mHListData.remove(position);
                    }
                }, parent, position);
                return true;
            }
        });
        mNoScrollListview.setFocusable(false);
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                下拉
                pageindex = 1;
                mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", noteClass, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtils.e("log", "onPullUp");
//                上拉
                pageindex++;
                mNoteRequestBase.getNoteListData(getDataTime, String.valueOf(pageindex), "1", noteClass, "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
            }
        });
    }

    /**
     * 给imageview 赋值
     */
    private void imgViewSetData(String url, ImageView iv) {
        LogUtils.e("轮播  -  imgViewSetData");
        if (getActivity() == null) {
            //判断当前Activity是否为空
            return;
        }
        Glide.with(getActivity())
                .load(RequestPath.SERVER_WEB_PATH + url)
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(iv);
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
    public void onResume() {
        super.onResume();
        if (carouselView != null)
            carouselView.startCarousel();
        LogUtils.e("TuiJian", "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (carouselView != null)
            carouselView.stopCarousel();
        LogUtils.e("TuiJian", "onPause");
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
        prsv_ScrollView.onRefreshComplete();
        switch (isUrl) {
            case RequestPath.GET_GETNOTE:
                //调用解析json的方法
                paserJson(content);
                if (pageindex == 1) {
                    SharedPreferencesUtil.setContent(getActivity(), "tuijian", content);
                }
                break;
            case RequestPath.POST_HIDENOTE:
                JSONObject jsonObject = null;
                String message = "";
                try {
                    jsonObject = new JSONObject(content);
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShowUtil.showToast(getActivity(), message);
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        prsv_ScrollView.onRefreshComplete();
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //数据获取成功后  对json进行解析绑定适配器的方法
    private void paserJson(String content) {
        if (content.equals("")) {
            return;
        }
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content, NoteBean.class);
        if (noteBean.getResult().equals("1")) {
            mListData_lunbo = noteBean.data_lunbo;
            carouselView.init(getActivity(), mListData_lunbo);
            carouselView.setCarouselLinster(this);
            carouselView.startCarousel();
            if (pageindex > 1) {
                if (noteBean.getData().size() > 0) {
                    mHListData.addAll(noteBean.data);
                    mHAdapter.setmDatas(mHListData);
                    mHAdapter.notifyDataSetChanged();
                } else {
                    pageindex--;
                    ShowUtil.showToast(getActivity(), "没有更多数据了");
                }
            } else {
                if (!MainActivity.isEditChannel && mHAdapter != null && mHListData != null && mHListData.size() > 0) {
                    mHListData = noteBean.getData();
                    if (mListData_jiage != null && mListData_jiage.size() > 0) {

                        for (int i = 0; i < mListData_jiage.size(); i++) {
                            mHListData.add(i, mListData_jiage.get(i));
                            if (i == 2) {
                                for (int j = 2; j > -1; j--) {
                                    mListData_jiage.remove(j);
                                }
                                break;
                            }
                        }

                    }
                    mHAdapter.setmDatas(mHListData);
                    LogUtils.e("mHlistDatas.size = " + mHListData.size());
                    LogUtils.e("noteBean.getData()= " + noteBean.getData().size());
                    mHAdapter.notifyDataSetChanged();
                } else {
                    mHListData = noteBean.getData();
                    if (mListData_jiage != null && mListData_jiage.size() > 0) {

                        for (int i = 0; i < mListData_jiage.size(); i++) {
                            mHListData.add(i, mListData_jiage.get(i));
                            if (i == 2) {
                                for (int j = 2; j > -1; j--) {
                                    mListData_jiage.remove(j);
                                }
                                break;
                            }
                        }

                    }
                    mHAdapter = new WorldListAdapters(mHListData, mListData_jiage, mainActivity, "tuijian");
                    mHAdapter.notifyDataSetChanged();
                    mNoScrollListview.setAdapter(mHAdapter);
                }
            }
        } else {
            LogUtils.e("轮播  -  onSuccess   else {");
        }
    }

    //轮播图 具体View的实现方法
    @Override
    public View instantiateView(CarouselBean date, ViewGroup container, int position) {
        final CarouselBean carouselBean = (CarouselBean) date;

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_carousel_item,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_img);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
        tv_text.setText(carouselBean.getLunbotitle());
        ImageUitls.setImg(carouselBean.getLunboimage(), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = carouselBean.getLunbotype();
                String data = carouselBean.getLunbourl();
                startActByType(type,data);
            }
        });
        return view;
    }

    public void startActByType(String type, String data) {
        Intent in = new Intent();
        switch (type) {
            case "0"://跳web界面
                in.setClass(getContext(), WebActivity.class);
                in.putExtra("url", data);
                in.putExtra("isWho", -1);
                startActivity(in);
                break;
            case "1":
                ((MainActivity)getActivity()).showView(2);
                break;
            case "2":
                in.setClass(getContext(), UserDetailsActivity.class);
                in.putExtra("detailuserid",data);
                startActivity(in);
                break;
            case "3":
                in.setClass(getContext(), NoteDetailActivity.class);
                in.putExtra("noteId",data);
                in.putExtra("repostid","0");
                startActivity(in);
                break;
            case "4":
                in.setClass(getContext(), FeedbackActivity.class);
                startActivity(in);
                break;
        }
    }
}
