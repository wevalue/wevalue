package com.wevalue.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.CarouselBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xox on 2017/3/6.
 */

public class CarouselView<T extends CarouselBean> extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private ViewPager vp_viewpager;
    private LinearLayout ll_dd; //放点点的地方
    private VPAdapter vpAdapter;
    private long delayTime = 4000;
    private List<T> lists = new ArrayList<T>();
    /**
     * 导航点点的颜色
     */
    private GradientDrawable drawable_p;
    private GradientDrawable drawable_n;

    public CarouselView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_carousel, this, true);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_carousel, this, true);
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void init(Activity activity, List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.lists = list;

        drawable_p = getMyDraw(10);
        drawable_n = getMyDraw(7);


        vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);
        ll_dd = (LinearLayout) findViewById(R.id.ll_dd);
        if (vp_viewpager.getAdapter() == null) {
            vpAdapter = new VPAdapter(activity);
            vp_viewpager.setAdapter(vpAdapter);
            vp_viewpager.addOnPageChangeListener(this);
            addDian(activity, ll_dd, list);
        }
    }

    /**
     * 添加导航点
     *
     * @param activity
     * @param dianLayout
     * @param list
     */
    private void addDian(Activity activity, LinearLayout dianLayout, List<T> list) {
        // TODO Auto-generated method stub
        if (list.size() <= 1)
            return;
        dianLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(13, 13);
            View view = new View(activity);
            params.setMargins(10, 10, 10, 10);
            view.setBackground(drawable_n);
            view.setLayoutParams(params);
            dianLayout.addView(view);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //handler延迟处理切换
            if (vp_viewpager == null) return;
            if (!isStop) {
                int currentItem = vp_viewpager.getCurrentItem();
                currentItem++;
                vp_viewpager.setCurrentItem(currentItem);
                startCarousel();
            } else {
                stopCarousel();
            }

        }
    };
    private boolean isStop = false;

    public void stopCarousel() {
        if (handler != null) {
            Log.d("CarouselView", "停止轮播");
            handler.removeCallbacksAndMessages(null);// 移除消息,停止轮播
            isStop = true;
        }
    }

    public void startCarousel() {
        if (handler != null) {
            Log.d("CarouselView", "开始轮播轮播");
            stopCarousel();
            handler.sendEmptyMessageDelayed(0, delayTime);
            isStop = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
                // 移除消息,停止轮播
                stopCarousel();
                break;
            case MotionEvent.ACTION_UP:// 抬起
                startCarousel();
                break;
            case MotionEvent.ACTION_MOVE:// 滑动
                // 移除消息,停止轮播
                stopCarousel();
                break;
            default:
                break;
        }
        return false;
    }

    private View lastView;

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int currentItem) {
        Log.e("CarouselView", "onPageSelected = " + currentItem);
        //切换viewpager的 导航点点
        if (lists.isEmpty())return;
        View currentText = ll_dd.getChildAt(currentItem
                % lists.size());
        if (currentText != null) {
            currentText.setBackground(drawable_p);
            ViewGroup.LayoutParams params = currentText.getLayoutParams();
            params.height = 20;
            params.width = 20;
            currentText.setLayoutParams(params);
        }
        if (lastView != null) {
            lastView.setBackground(drawable_n);
            ViewGroup.LayoutParams params = lastView.getLayoutParams();
            params.height = 13;
            params.width = 13;
            lastView.setLayoutParams(params);
        }
        lastView = currentText;
    }


    /**
     * 制造原点
     *
     * @param radius
     * @return
     */
    private GradientDrawable getMyDraw(int radius) {
        int strokeWidth = 1; // 3dp 边框宽度
        int roundRadius = radius; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#2E3135");//边框颜色
        int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色

        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 适配器
     */
    public class VPAdapter extends PagerAdapter {

        Activity activity;

        public VPAdapter(Activity context) {
            activity = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (lists.isEmpty()) return container;
            T data = lists.get(position % lists.size());
            if (carouselLinster != null) {
                //实现每个View的细节
                View view = carouselLinster.instantiateView(data, container, position);
                container.addView(view);
                return view;
            } else {
                new Throwable("setCarouselLinster is not find");
                return null;
            }
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }

        /**
         * 判断 view和object的对应关系
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewGroup) container).removeView((View) object);
            object = null;
            //super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }
    }

    public void setCarouselLinster(CarouselLinster carouselLinster) {
        this.carouselLinster = carouselLinster;
    }

    private CarouselLinster carouselLinster;

    //监听器
    public interface CarouselLinster<E extends CarouselBean> {
        //实现每个View的细节 Object 其实是个view
        View instantiateView(E date, ViewGroup container, int position);
    }
}
