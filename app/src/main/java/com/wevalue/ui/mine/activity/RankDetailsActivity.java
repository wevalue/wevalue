package com.wevalue.ui.mine.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.ui.mine.fragment.RankListFragment;
import com.wevalue.utils.LogUtils;

//排行榜的详情
public class RankDetailsActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private TextView mTvWorld;
    private TextView mTvInfluence;
    private TextView mTvFriends;
    private TextView mTitle;
    private RankListFragment mFriendsRankFragment;
    private RankListFragment mInFluistFragment;
    private RankListFragment mWorldRankFragment;

    String rankType;
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int screenW;//获取控件的宽度
    private ImageView cursor;// 动画图片
    private LinearLayout ll_getview_width;
    private Fragment currentFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list_details);
        rankType = getIntent().getStringExtra("type");
        initView();
    }

    private void initView() {
        ll_getview_width = (LinearLayout) findViewById(R.id.ll_catalog);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mTvWorld = (TextView) findViewById(R.id.tv_world);
        mTvInfluence = (TextView) findViewById(R.id.tv_influence);
        mTvFriends = (TextView) findViewById(R.id.tv_friends);
        mTitle = (TextView) findViewById(R.id.tv_head_title);
        mTvWorld.setOnClickListener(this);
        mTvFriends.setOnClickListener(this);
        mTvInfluence.setOnClickListener(this);
        setDefaultFragment();
        switch (rankType) {
            case "3":
                mTitle.setText("打赏收益排行榜");
                break;
            case "2":
                mTitle.setText("转发量排行榜");
                break;
            case "1":
                mTitle.setText("收益排行榜");
                break;
        }

        initAnimation();
    }

    private void initAnimation() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ViewTreeObserver vto2 = ll_getview_width.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_getview_width.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                screenW = ll_getview_width.getWidth();
                LogUtils.e("ww=" + screenW);
                bmpW = (BitmapFactory.decodeResource(getResources(), R.mipmap.hengxian).getWidth());// 获取图片宽度
                offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
                Matrix matrix = new Matrix();
                matrix.postTranslate(0, 0);
                cursor.setImageMatrix(matrix);// 设置动画初始位置
                LogUtils.e("ww=1111----" + screenW);
            }
        });
    }

    private void lineAnimation(int index) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    private void setDefaultFragment() {
        FragmentManager fm;
        FragmentTransaction transaction;
        fm = getSupportFragmentManager();
        //开启切换事务
        transaction = fm.beginTransaction();
        mInFluistFragment = new RankListFragment();
        mFriendsRankFragment = new RankListFragment();
        mWorldRankFragment = new RankListFragment();
        Bundle influBundle = new Bundle();
        Bundle fridBundle = new Bundle();
        Bundle wldBundle = new Bundle();
        influBundle.putString("rankType", rankType);// ("fragData",fragData);
        fridBundle.putString("rankType", rankType);// ("fragData",fragData);
        wldBundle.putString("rankType", rankType);// ("fragData",fragData);
        influBundle.putString("notezone", "2");
        fridBundle.putString("notezone", "1");
        wldBundle.putString("notezone", "0");
        mInFluistFragment.setArguments(influBundle);
        mFriendsRankFragment.setArguments(fridBundle);
        mWorldRankFragment.setArguments(wldBundle);
        mTvWorld.setTextColor(getResources().getColor(R.color.blue));
        transaction.add(R.id.ft_content, mWorldRankFragment);
        currentFragment = mWorldRankFragment;
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_world:
                lineAnimation(0);
                mTvWorld.setTextColor(getResources().getColor(R.color.blue));
                mTvInfluence.setTextColor(Color.BLACK);
                mTvFriends.setTextColor(Color.BLACK);
                showFragment(mWorldRankFragment);
                break;
            case R.id.tv_influence:
                lineAnimation(1);
                mTvWorld.setTextColor(Color.BLACK);
                mTvInfluence.setTextColor(getResources().getColor(R.color.blue));
                mTvFriends.setTextColor(Color.BLACK);
                showFragment(mInFluistFragment);
                break;
            case R.id.tv_friends:
                lineAnimation(2);
                mTvWorld.setTextColor(Color.BLACK);
                mTvInfluence.setTextColor(Color.BLACK);
                mTvFriends.setTextColor(getResources().getColor(R.color.blue));
                showFragment(mFriendsRankFragment);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showFragment(Fragment showFragment) {
        if (showFragment.equals(currentFragment)) {
            LogUtils.e("return", "return");
            return;
        }
        currentFragment = showFragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (mFriendsRankFragment.isVisible()) {
            if (!showFragment.isAdded()) {
                transaction.hide(mFriendsRankFragment).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(mFriendsRankFragment).show(showFragment).commit();
            }
        } else if (mInFluistFragment.isVisible()) {
            if (!showFragment.isAdded()) {
                transaction.hide(mInFluistFragment).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(mInFluistFragment).show(showFragment).commit();
            }
        } else if (mWorldRankFragment.isVisible()) {
            if (!showFragment.isAdded()) {
                transaction.hide(mWorldRankFragment).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(mWorldRankFragment).show(showFragment).commit();
            }
        }
        LogUtils.e("return", "666");

    }
}
