package com.wevalue.ui.add.activity;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.ui.mine.fragment.RankListFragment;
import com.wevalue.utils.LogUtils;
import com.wevalue.view.LazyViewPager;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-11.
 */
public class RankingListActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_head_title;

    private LazyViewPager lvp_ranking_viewpager;
    private RadioButton rb_shouyi;
    private RadioButton rb_zhuanfa;
    private RadioButton rb_dashang;

    private ArrayList<RankListFragment> fragmentList;
    private MyViewPagerAdapter myViewPagerAdapter;

    private RankListFragment mEarningFragment;
    private RankListFragment mTransmitFragment;
    private RankListFragment mDaShangFragment;
    private ImageView cursor;
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        initView();
        InitImageView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("排行榜");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rb_shouyi = (RadioButton) findViewById(R.id.rb_shouyi);
        rb_zhuanfa = (RadioButton) findViewById(R.id.rb_zhuanfa);
        rb_dashang = (RadioButton) findViewById(R.id.rb_dashang);
        iv_back.setOnClickListener(this);
        rb_shouyi.setOnClickListener(this);
        rb_zhuanfa.setOnClickListener(this);
        rb_dashang.setOnClickListener(this);
        lvp_ranking_viewpager = (LazyViewPager) findViewById(R.id.lvp_ranking_viewpager);
        fragmentList = new ArrayList<RankListFragment>();
        mEarningFragment = new RankListFragment();
        mDaShangFragment = new RankListFragment();
        mTransmitFragment = new RankListFragment();
        Bundle dashang = new Bundle();
        Bundle transmit = new Bundle();
        Bundle earn = new Bundle();
//        0世界 1好友 2影响力==关注+好友
        earn.putString("notezone", "0");
        transmit.putString("notezone", "0");
        dashang.putString("notezone", "0");
//        1收益 2转发 3打赏
        earn.putString("rankType", "1");
        transmit.putString("rankType", "2");
        dashang.putString("rankType", "3");

        mEarningFragment.setArguments(earn);
        mDaShangFragment.setArguments(dashang);
        mTransmitFragment.setArguments(transmit);
        fragmentList.add(mEarningFragment);
        fragmentList.add(mTransmitFragment);
        fragmentList.add(mDaShangFragment);

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        lvp_ranking_viewpager.setAdapter(myViewPagerAdapter);
        lvp_ranking_viewpager.setScrollble(false);
        lvp_ranking_viewpager.setCurrentItem(0);
        lvp_ranking_viewpager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rb_shouyi:
                lvp_ranking_viewpager.setCurrentItem(0);

                lineAnimation(0);
                break;
            case R.id.rb_zhuanfa:
                lvp_ranking_viewpager.setCurrentItem(1);

                lineAnimation(1);
                break;
            case R.id.rb_dashang:
                lvp_ranking_viewpager.setCurrentItem(2);

                lineAnimation(2);
                break;
        }
    }


    /**
     * Title:  InitImageView<br>
     * Description: TODO  初始化线的动画<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpW = (BitmapFactory.decodeResource(getResources(), R.mipmap.myreleaseline).getWidth());// 获取图片宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置

    }

    /**
     * Title:  lineAnimation<br>
     * Description: TODO  xia<br>
     *
     * @param index
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private void lineAnimation(int index) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    /**
     * Title: MyViewPagerAdapter<br>
     * Description: TODO Viewpager适配器<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
