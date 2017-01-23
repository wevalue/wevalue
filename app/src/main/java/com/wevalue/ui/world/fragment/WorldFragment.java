package com.wevalue.ui.world.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseFragment;
import com.wevalue.ui.login.TypeChoiceActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.RealmUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Administrator on 2016-06-27.
 */
public class WorldFragment extends BaseFragment implements View.OnClickListener, GestureDetector.OnGestureListener {
    private View view;
    private Context mContext;
    public TabLayout tablayout;
    private MyViewPager viewPager;
    private String[] titles;
    private MainActivity mainActivity;
    private String userlike;
    private String userlike_2;
    private ArrayList<Fragment> fragments;
    private ArrayList<MyTypeFragment> fragments_type;
    private TabFragmentAdapter pagerAdapter;
    FragmentManager fragmentManager;
    //默认的频道
    String defaultChannel = "推荐,视频,地区";
    private GestureDetector gesture; //手势识别
    MainActivity.MyOnTouchListener myOnTouchListener;
    private ProgressBar pgb;
    private ImageView iv_add_class;
    private RelativeLayout rl_is_show_tablayout;
    private boolean isFirstLoad = true, isVisibleToUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        userlike = SharedPreferencesUtil.getUserlike(getActivity());
        gesture = new GestureDetector(getActivity(), this);
        myOnTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = gesture.onTouchEvent(ev);
                return result;
            }
        };
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            mainActivity = (MainActivity) getActivity();
            mainActivity.setViewIsShow(true, 0);
            if (((MainActivity) getActivity()) != null) {
                ((MainActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
            }
            if (isFirstLoad) {
                isFirstLoad = false;
            }else {
                LogUtils.e("lifeStyle", "世界進入頁面統計");
                MobclickAgent.onPageStart("home_world");
            }
        } else {
            if (!isFirstLoad){
                MobclickAgent.onPageEnd("home_world");
                LogUtils.e("lifeStyle", "世界退出頁面統計");
            }
            if (((MainActivity) getActivity()) != null) {
                ((MainActivity) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_world, null);
        initView();
//        根据父窗体getActivity()为fragment设置手势识别
        return view;
    }

    //刷新数据
    public void refreshData() {
        if (viewPager == null || fragment_tuijian == null || pgb == null) {
            return;
        }
        pgb.setVisibility(View.VISIBLE);

        int i = viewPager.getCurrentItem();
        setUPDON(false);
        if (i == 0) {
            fragment_tuijian.refreshData();
        } else {
            fragments_type.get(i - 1).refreshData();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pgb.setVisibility(View.GONE);
            }
        }, 1500);
//        for (int i = 0; i < fragments.size(); i++) {
//            LogUtils.e("fragments.get(i).getClass().getName()="+fragments.get(i).getClass().getName());
//        }
    }

    /**
     * 初始化控件
     **/
    private void initView() {
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        rl_is_show_tablayout = (RelativeLayout) view.findViewById(R.id.rl_is_show_tablayout);
        iv_add_class = (ImageView) view.findViewById(R.id.iv_add_class);
        pgb = (ProgressBar) view.findViewById(R.id.pgb);
        viewPager = (MyViewPager) view.findViewById(R.id.viewPager);
        viewPager.setNoScroll(false);
        fragments = new ArrayList<>();
        if (userlike.isEmpty()) {
            return;
//            titles = defaultChannel.split(",");
        } else {
            titles = userlike.split(",");
        }
        //通过获取用户选择的频道展示对应的fragment
        switchFragment();
        pagerAdapter = new TabFragmentAdapter(fragments, getChildFragmentManager(), mContext);
        pagerAdapter.setTitles_adap(titles);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabTextColors(Color.BLACK, getResources().getColor(R.color.blue));
        iv_add_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TypeChoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 添加新的频道后刷新界面
     **/
    public void resetUI() {
        if (view != null) {
            int currentItem = viewPager.getCurrentItem();
            viewPager.setOffscreenPageLimit(titles.length);
            reSetTitles();
            userlike_2 = SharedPreferencesUtil.getUserlike(getActivity());
            if (!userlike_2.equals(userlike)) {
                if (userlike_2.isEmpty()) {
                    return;
                } else {
                    titles = userlike_2.split(",");
                    LogUtils.e("str==I", "resetUI");
                    switchFragment();
                    pagerAdapter.setTitles_adap(titles);
                    pagerAdapter.setFragments(fragments);
                    pagerAdapter.notifyDataSetChanged();
                    tablayout.setTabsFromPagerAdapter(pagerAdapter);
                    viewPager.setCurrentItem(currentItem);
                    userlike = userlike_2;
                }
            }
        }
    }

    public void setDefaultChannel() {
        titles = defaultChannel.split(",");
        MainActivity.isEditChannel = true;
        switchFragment();
//        LogUtils.e("resetUI===========先走吗?=================resetUI" + titles.length);
        pagerAdapter = new TabFragmentAdapter(fragments, getChildFragmentManager(), mContext);
        pagerAdapter.setTitles_adap(titles);
        viewPager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabTextColors(Color.BLACK, getResources().getColor(R.color.blue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tablayout:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetUI();
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageStart("home_world");
            LogUtils.e("lifeStyle", "世界進入頁面統計");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isFirstLoad && isVisibleToUser) {
            MobclickAgent.onPageEnd("home_world");
            LogUtils.e("lifeStyle", "世界退出頁面統計");
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 != null && e2 != null) {
            try {
                if ((e1.getY() - e2.getY() > 100) && Math.abs(velocityY) > 200) {
                    LogUtils.e("上滑了");
                    setUPDON(true);
                    return true;
                } else if ((e2.getY() - e1.getY() > 100) && Math.abs(velocityY) > 200) {
                    LogUtils.e("下滑滑滑了");
                    setUPDON(false);
                    return true;
                }
            } catch (Exception e) {
            }
        } else {
            LogUtils.e("都是空的");
        }
        return false;
    }

    /**
     * viewpager 适配器
     */
    class TabFragmentAdapter extends FragmentStatePagerAdapter {

        private Context context;
        private List<Fragment> fragments;
        private String[] titles_adap;

        public TabFragmentAdapter(List<Fragment> fragments, FragmentManager fm, Context context) {
            super(fm);
            fragmentManager = fm;
            this.context = context;
            this.fragments = fragments;
            notifyDataSetChanged();
        }

        public void setTitles_adap(String[] titles) {
            titles_adap = titles;
        }

        public void setFragments(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position != 0) {
                super.destroyItem(container, position, object);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return titles_adap.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles_adap[position];
        }
    }

    /**
     * 设置 tablayout 隐藏或显示
     */
    public void setUPDON(boolean is) {
        if (is) {
//            tablayout.setMinimumHeight(-1);
            rl_is_show_tablayout.setVisibility(View.GONE);
        } else {
//            tablayout.setMinimumHeight( tableHeight);
            rl_is_show_tablayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取viewpager当前页index
     */
    public int getVpIndex() {
        return viewPager.getCurrentItem();
    }


    MyType_tuijianFragment fragment_tuijian;
    MyTypeFragment fragment;

    //绑定标题和相应的fragment
    private void switchFragment() {
        fragments_type = new ArrayList<>();
        int size = titles.length;
        fragments.clear();
        for (int i = 0; i < size; i++) {
            LogUtils.e("str==", i + "");
            if (i == 0) {
                fragment_tuijian = new MyType_tuijianFragment(i, WorldFragment.this);
                fragments.add(fragment_tuijian);
            } else {
                fragment = new MyTypeFragment(titles[i]);
                fragments.add(fragment);
                fragments_type.add(fragment);
            }
        }
        if (pagerAdapter != null) {
//            pagerAdapter.notifyDataSetChanged();
        }
    }

    public void reSetTitles() {
        LogUtils.e("reSetTitles", "reSetTitles");
        String userlikeCity = SharedPreferencesUtil.getUserLikeCity(getActivity());
        String lastCity = SharedPreferencesUtil.getLastCity(getActivity());
        if (TextUtils.isEmpty(userlikeCity)) {
            userlikeCity = "地区";
        }
        if (TextUtils.isEmpty(lastCity)) {
            lastCity = "地区";
        }
        if (!userlikeCity.equals(lastCity)) {
            for (int i = 0; i < titles.length; i++) {
                LogUtils.e("lastCity", titles[i]);
                if (titles[i].equals(lastCity)) {
                    titles[i] = userlikeCity;
                    LogUtils.e("lastCity", "666");
                    tablayout.getTabAt(i).setText(userlikeCity);
                    LogUtils.e("lastCity666", lastCity);

                    String userlike = SharedPreferencesUtil.getUserlike(getActivity());

                    SharedPreferencesUtil.setUserlike(getActivity(), userlike.replaceAll(lastCity, userlikeCity));

                    SharedPreferencesUtil.setLastCity(getActivity(), userlikeCity);

                    SharedPreferencesUtil.setAllChannel(getActivity(), SharedPreferencesUtil.getAllChannel(getActivity()).replaceAll(lastCity, userlikeCity));

                    RealmUtils realmUtils = new RealmUtils();
                    try {
                        realmUtils.updateChanelsById(Realm.getDefaultInstance(), userlikeCity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
