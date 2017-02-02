package com.wevalue.ui.world.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseFragment;
import com.wevalue.model.NoteBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.ui.world.adapter.WorldListAdapter;
import com.wevalue.ui.world.adapter.WorldListmAdapter_Copy;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.LazyViewPager;
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
import java.util.TimerTask;

/**
 * Created by lt on 2015/12/14.
 */
public class MyType_tuijianFragment extends BaseFragment implements WZHttpListener {
    private View view;
    private Context mContext;
    private ArrayList<View> viewList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LazyViewPager vp_world;
    private long mExitTime = 0;
    private int mViewpagerIndex = 0;
    private TextView[] imageViews = null;//小圆点
    private RelativeLayout rl_xs;
    public boolean isContinue = true;
    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private WorldListmAdapter_Copy mHAdapter;
    private List<NoteBean.NoteEntity> mHListData;
    private List<NoteBean.NoteEntity> mListData_lunbo;
    private List<NoteBean.NoteEntity> mListData_jiage;
    private List<NoteBean.NoteEntity> mListData_jiage_2 = new ArrayList<>();
    private int viewIndex = -1;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mViewpagerIndex == viewList.size()) {
                        mViewpagerIndex = 0;
                    } else {
                        mViewpagerIndex++;
                    }
                    vp_world.setCurrentItem(mViewpagerIndex);
                    break;
            }
        }
    };
    private int clickIndex;
    private int mItmePosition = 100;
    private Timer timer;
    private GestureDetector gesture; //手势识别
    private WorldFragment mWorldFragment;
    private NoteRequestBase mNoteRequestBase;
    private int pageindex = 1;
    private ImageView iv_tuijian_img_1;
    private ImageView iv_tuijian_img_2;
    private ImageView iv_tuijian_img_3;
    private String getDataTime;
//    private ProgressBar pgb;

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

        LogUtils.e("单前时间=" + getDataTime);
        return getDataTime;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = WeValueApplication.applicationContext;
        mainActivity = (MainActivity) this.getActivity();
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(getActivity());
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_my_type_tuijian, null);
        LogUtils.e("viewIndex =onCreateView= " + viewIndex);
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
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
        } else {
            paserJson(tuijianJson);
            mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
        }
        LogUtils.e("轮播  onCreateView");
        return view;
    }

    //刷新数据
    public void refreshData() {
        pageindex = 1;
        if (prsv_ScrollView == null) {
            return;
        }
//        vp_world.setFocusable(true);
//        vp_world.setFocusableInTouchMode(true);
//        vp_world.requestFocus();
        ScrollView scrollView = prsv_ScrollView.getRefreshableView();
        scrollView.smoothScrollTo(0, 0);
//        pgb.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex),
                        "1", "1", "", SharedPreferencesUtil.getDeviceid(getActivity()),
                        MyType_tuijianFragment.this);
            }
        }, 300);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        LogUtils.e("轮播  -  initView");
        rl_xs = (RelativeLayout) view.findViewById(R.id.rl_xs);
        prsv_ScrollView = (PullToRefreshScrollView) view.findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) view.findViewById(R.id.mNoScrollListview);
        mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItmePosition = position;
                Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
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
                        countMap.put("hotword",mHListData.get(position).getHotword());
                        if (mHListData.get(position).getIsself().equals("0")){
                            countMap.put("isOrigin","yuanchaung");
                        }else {
                            countMap.put("isOrigin","feiyuanchaung");
                        }
                        countMap.put("moodcount",mHListData.get(position).getMoodcount());
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
                mNoteRequestBase.getNoteListData(getDateTime(), String.valueOf(pageindex), "1", "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                LogUtils.e("log", "onPullUp");
//                上拉
                pageindex++;
                mNoteRequestBase.getNoteListData(getDataTime, String.valueOf(pageindex), "1", "1", "", SharedPreferencesUtil.getDeviceid(getActivity()), MyType_tuijianFragment.this);
            }
        });
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpager() {
        if (null == getActivity()) {
            return;
        }
        vp_world = (LazyViewPager) view.findViewById(R.id.vp_world);
        ViewGroup.LayoutParams ls = vp_world.getLayoutParams();
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.bianxian, options);
        ls.height = options.outHeight;
        vp_world.setLayoutParams(ls);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.ll_viewGroup);
        viewList = new ArrayList<>();
        if (null != mListData_lunbo && mListData_lunbo.size() > 0) {
            for (int i = 0; i < mListData_lunbo.size(); i++) {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_world_tuijian_copy, null);
                setViewData(view1, i);
                viewList.add(view1);
            }
        }
        if (null == timer && null != mListData_lunbo && mListData_lunbo.size() > 1) {//如果定时器没有打开  就开始定时功能
            launchTimerTask();
        }
        // 初始化小圆点
        imageViews = new TextView[viewList.size()];
        for (int i = 0; i < viewList.size(); i++) {
            TextView iv = new TextView(getActivity());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            params.setMargins(5, 0, 5, 0);
            iv.setLayoutParams(params);
            imageViews[i] = iv;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.wz_text_yuandian_lan);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.wz_text_yuandian_bai);
            }
            group.addView(imageViews[i]);
        }
        if (myViewPagerAdapter == null) {
            myViewPagerAdapter = new MyViewPagerAdapter();
        }
        vp_world.setAdapter(myViewPagerAdapter);
        vp_world.setOnPageChangeListener(new GuidePageChangeListener());
        vp_world.setCurrentItem(mViewpagerIndex);
        vp_world.setOffscreenPageLimit(0);
    }

    /**
     * 设置viewpager当前页的数据
     */
    private void setViewData(View v, int index) {

        if (index == 0) {
            LogUtils.e("轮播  -  setViewData  --" + index);
        }
        ImageView iv_img = (ImageView) v.findViewById(R.id.iv_img);
        imgViewSetData("",iv_img);
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
                .load(RequestPath.SERVER_PATH + url)
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }

    /**
     * 给图片类型的帖子的imageview 赋值
     */
    private void lunboImageviewSetData(NoteBean.NoteEntity noteEntity) {
        LogUtils.e("轮播  -  lunboImageviewSetData");
        switch (noteEntity.getList().size()) {
            case 1:
                iv_tuijian_img_1.setVisibility(View.VISIBLE);
                iv_tuijian_img_2.setVisibility(View.INVISIBLE);
                iv_tuijian_img_3.setVisibility(View.INVISIBLE);
                imgViewSetData(noteEntity.getList().get(0).getUrl(), iv_tuijian_img_1);
                break;
            case 2:
                iv_tuijian_img_1.setVisibility(View.VISIBLE);
                iv_tuijian_img_2.setVisibility(View.VISIBLE);
                iv_tuijian_img_3.setVisibility(View.INVISIBLE);
                imgViewSetData(noteEntity.getList().get(0).getUrl(), iv_tuijian_img_1);
                imgViewSetData(noteEntity.getList().get(1).getUrl(), iv_tuijian_img_2);
                break;
            case 3:
                iv_tuijian_img_1.setVisibility(View.VISIBLE);
                iv_tuijian_img_2.setVisibility(View.VISIBLE);
                iv_tuijian_img_3.setVisibility(View.VISIBLE);
                imgViewSetData(noteEntity.getList().get(0).getUrl(), iv_tuijian_img_1);
                imgViewSetData(noteEntity.getList().get(1).getUrl(), iv_tuijian_img_2);
                imgViewSetData(noteEntity.getList().get(2).getUrl(), iv_tuijian_img_3);
                break;
            default:
                iv_tuijian_img_1.setVisibility(View.VISIBLE);
                iv_tuijian_img_2.setVisibility(View.VISIBLE);
                iv_tuijian_img_3.setVisibility(View.VISIBLE);
                imgViewSetData(noteEntity.getList().get(0).getUrl(), iv_tuijian_img_1);
                imgViewSetData(noteEntity.getList().get(1).getUrl(), iv_tuijian_img_2);
                imgViewSetData(noteEntity.getList().get(2).getUrl(), iv_tuijian_img_3);
                break;
        }
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
        if (vp_world != null && timer == null) {
//            launchTimerTask();
            LogUtils.e("轮播  ---------- onResume");
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItmePosition < 3) {

                    LogUtils.e(" mListData_jiage.size =" + mListData_jiage.size());
                    LogUtils.e(" mListData_jiage_   22222.size =" + mListData_jiage_2.size());

                    if (mListData_jiage != null && mListData_jiage.size() > 0) {
                        mListData_jiage_2.add(mHListData.get(mItmePosition));
                        mHListData.set(mItmePosition, mListData_jiage.get(0));
                        mListData_jiage.remove(0);
                        mItmePosition = 100;
                    } else {
                        if (mListData_jiage_2 != null && mListData_jiage_2.size() > 0) {
                            mListData_jiage.add(mHListData.get(mItmePosition));
                            mHListData.set(mItmePosition, mListData_jiage_2.get(0));
                            mListData_jiage_2.remove(0);
                            mItmePosition = 100;
                        }

                    }
                    mHAdapter.notifyDataSetChanged();
                }

            }
        }, 500);


        LogUtils.e("轮播   onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
        LogUtils.e("轮播   onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
        LogUtils.e("轮播   onStop");

    }

    /**
     * 定时器
     */
    public void launchTimerTask() {
//        LogUtils.e("轮播  -  launchTimerTask");
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 2000, 3000);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        isPause = true;
        LogUtils.e("轮播  -  onDestroy");
    }

    /**
     * Title:  GuidePageChangeListener<br>
     * Description: TODO  viewpager侧滑事件监听<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private final class GuidePageChangeListener implements LazyViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            mViewpagerIndex = arg0;
//            if (mViewpagerIndex==viewList.size())
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0 % mListData_lunbo.size()].setBackgroundResource(R.drawable.wz_text_yuandian_lan);
                if (arg0 % mListData_lunbo.size() != i) {
                    imageViews[i].setBackgroundResource(R.drawable.wz_text_yuandian_bai);
                }
            }
        }
    }

    /**
     * Title: MyViewPagerAdapter<br>
     * Description: TODO Viewpager适配器<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        public MyViewPagerAdapter() {
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
//            ((LazyViewPager) arg0).removeView(viewList.get(arg1 % viewList.size()));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (viewList.get(position % viewList.size()).getParent() != null) {
                ((LazyViewPager) viewList.get(position % viewList.size()).getParent()).removeView(viewList.get(position % viewList.size()));
            }
//            ((LazyViewPager) container).addView(viewList.get(position % viewList.size()), 0);4
            container.addView(viewList.get(position));
            View v = viewList.get(position % viewList.size());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e("被点击了了--" + position % viewList.size());
                    clickIndex = position % viewList.size();
                    Intent intent = new Intent(getActivity(), NoteDetailsActivity.class);
                    intent.putExtra("noteId", mListData_lunbo.get(clickIndex).getNoteid());
                    intent.putExtra("repostid", mListData_lunbo.get(clickIndex).getRepostid());
                    LogUtils.e("repostcontent", mListData_lunbo.get(clickIndex).getRepostid());
                    intent.putExtra("repostfrom", "1");
                    startActivity(intent);
                }
            });
            return viewList.get(position % viewList.size());
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(ViewGroup container) {

            super.finishUpdate(container);
        }
    }

//    @Override
//    public boolean onDown(MotionEvent arg0) {
//        return false;
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (e1 != null && e2 != null) {
//            try {
//                if ((e1.getY() - e2.getY() > 100) && Math.abs(velocityY) > 200) {
//                    LogUtils.e("上滑了");
//                    mWorldFragment.setUPDON(true);
//                    return true;
//                } else if ((e2.getY() - e1.getY() > 100) && Math.abs(velocityY) > 200) {
//                    LogUtils.e("下滑滑滑了");
//                    mWorldFragment.setUPDON(false);
//                    return true;
//                }
//            } catch (Exception e) {
//            }
//        } else {
//            LogUtils.e("都是空的");
//        }
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//                            float distanceY) {
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }

    //数据获取成功后  对json进行解析绑定适配器的方法
    private void paserJson(String content) {
        if (content.equals("")) {
            return;
        }
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content, NoteBean.class);
        prsv_ScrollView.onRefreshComplete();
//        pgb.setVisibility(View.GONE);
        if (noteBean.getResult().equals("1")) {
            mListData_lunbo = noteBean.data_lunbo;
            mListData_jiage = noteBean.data_jiage;
            if (mListData_lunbo != null && mListData_lunbo.size() > 0) {
                if (myViewPagerAdapter == null) {
                    initViewpager();
                } else {
                    if (viewList.size() == mListData_lunbo.size()) {
                        for (int i = 0; i < mListData_lunbo.size(); i++) {

                            setViewData(viewList.get(i), i);
                        }
                    }

                    myViewPagerAdapter.notifyDataSetChanged();
                }
                LogUtils.e("轮播  -  if (vp_world==null)");
            } else {
//                if (myViewPagerAdapter == null) {
//                    myViewPagerAdapter.notifyDataSetChanged();
//                }
            }
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
                    mHAdapter = new WorldListmAdapter_Copy(mHListData, mListData_jiage, mainActivity, "tuijian");
                    mHAdapter.notifyDataSetChanged();
                    mNoScrollListview.setAdapter(mHAdapter);
                }
            }
        } else {
            LogUtils.e("轮播  -  onSuccess   else {");
        }
    }
}
