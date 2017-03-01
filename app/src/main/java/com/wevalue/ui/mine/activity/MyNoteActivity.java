package com.wevalue.ui.mine.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.wevalue.base.BaseActivity;
import com.wevalue.ui.mine.fragment.MyNoteFragment;
import com.wevalue.utils.LogUtils;

/**
 * Created by Administrator on 2016-08-09.
 * 我的发布 我的转发 我的打赏
 */
public class MyNoteActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private TextView mTvWorld;
    private TextView mTvInfluence;
    private TextView mTvFriends;
    private TextView mTitle;
    private MyNoteFragment myNoteFragmentFriend;
    private MyNoteFragment myNoteFragmentAll;
    private MyNoteFragment myNoteFragmentInfWorld;
    String status; //1 我的发布  2 我的转发  3 我的大赏
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int screenW;//获取控件的宽度
    private ImageView cursor;// 动画图片
    private LinearLayout ll_getview_width;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment`
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        setContentView(R.layout.activity_mynote);
        status = getIntent().getStringExtra("status");
        initView();
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
        switch (status) {
            case "3":
                mTitle.setText("我的打赏");
                break;
            case "2":
                mTitle.setText("我的转发");
                break;
            case "1":
                mTitle.setText("我的发布");
                break;
        }
        initAnimation();
    }

    private void setDefaultFragment() {
        FragmentManager fm;
        FragmentTransaction transaction;
        fm = getSupportFragmentManager();
        //开启切换事务
        transaction = fm.beginTransaction();
        myNoteFragmentFriend = new MyNoteFragment();
        myNoteFragmentInfWorld = new MyNoteFragment();
        myNoteFragmentAll = new MyNoteFragment();
//        myNoteFragmentAll = new MyNoteFragment(mTvInfluence,mTvWorld,mTvFriends);
        Bundle world = new Bundle();
        Bundle friend = new Bundle();
        Bundle all = new Bundle();
        world.putString("notezone", "0");// 世界
        friend.putString("notezone", "1");// 朋友
        all.putString("notezone", "-1");// 全部

        world.putString("status", status);// 世界
        friend.putString("status", status);// 朋友
        all.putString("status", status);// 全部

        if (status.equals("2")) {
            world.putString("notezone", "2");// 世界
        }
        myNoteFragmentInfWorld.setArguments(world);
        myNoteFragmentAll.setArguments(all);
        myNoteFragmentFriend.setArguments(friend);
        mTvInfluence.setTextColor(getResources().getColor(R.color.blue));
        transaction.add(R.id.ft_content, myNoteFragmentAll);
        currentFragment = myNoteFragmentAll;
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_world:
                lineAnimation(1);
                mTvWorld.setTextColor(getResources().getColor(R.color.blue));
                mTvInfluence.setTextColor(Color.BLACK);
                mTvFriends.setTextColor(Color.BLACK);
                showFragment(myNoteFragmentInfWorld);
                break;
            case R.id.tv_influence:
                lineAnimation(0);
                mTvWorld.setTextColor(Color.BLACK);
                mTvInfluence.setTextColor(getResources().getColor(R.color.blue));
                mTvFriends.setTextColor(Color.BLACK);
                showFragment(myNoteFragmentAll);
                break;
            case R.id.tv_friends:
                lineAnimation(2);
                mTvWorld.setTextColor(Color.BLACK);
                mTvInfluence.setTextColor(Color.BLACK);
                mTvFriends.setTextColor(getResources().getColor(R.color.blue));
                showFragment(myNoteFragmentFriend);
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
        if (myNoteFragmentFriend.isVisible()) {
            if (!showFragment.isAdded()) {
                transaction.hide(myNoteFragmentFriend).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(myNoteFragmentFriend).show(showFragment).commit();
            }
        } else if (myNoteFragmentAll.isVisible()) {

            if (!showFragment.isAdded()) {
                transaction.hide(myNoteFragmentAll).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(myNoteFragmentAll).show(showFragment).commit();
            }
        } else if (myNoteFragmentInfWorld.isVisible()) {

            if (!showFragment.isAdded()) {
                transaction.hide(myNoteFragmentInfWorld).add(R.id.ft_content, showFragment).commit();
            } else {
                transaction.hide(myNoteFragmentInfWorld).show(showFragment).commit();
            }
        }
    }
}
