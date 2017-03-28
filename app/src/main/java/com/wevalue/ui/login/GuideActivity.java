package com.wevalue.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.ChoiceChannelActivity;
import com.wevalue.LauncherActivity;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-25.
 * 引导页
 */

public class GuideActivity extends BaseActivity {


    private ViewPager vp_guide_viewpager;
    private TextView tv_jinru_weizi;
    private int[] imgs = {R.mipmap.welcome1, R.mipmap.welcome2, R.mipmap.welcome3, R.mipmap.welcome4,R.mipmap.welcome5};
    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        vp_guide_viewpager = (ViewPager) findViewById(R.id.vp_guide_viewpager);
        tv_jinru_weizi = (TextView) findViewById(R.id.tv_jinru_weizi);
        tv_jinru_weizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vp_guide_viewpager.getCurrentItem() == imgs.length-1) {
                    Intent intent = new Intent(GuideActivity.this, ChoiceChannelActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

        for (int i = 0; i < imgs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgs[i]);
            imageViewList.add(imageView);
        }

        GoodDetailImagePagerAdapter imagePagerAdapter = new GoodDetailImagePagerAdapter(this, imageViewList);
        vp_guide_viewpager.setAdapter(imagePagerAdapter);
        vp_guide_viewpager.setCurrentItem(0);


    }


    /**
     * Title:  GoodDetailImagePagerAdapter<br>
     * Description: TODO  大图适配器<br>
     *
     * @since JDK 1.7
     */
    public class GoodDetailImagePagerAdapter extends PagerAdapter {
        private Context context;
        private List<ImageView> imageViewList;

        public GoodDetailImagePagerAdapter(Context context, List<ImageView> imageViewList) {
            super();
            this.context = context;
            this.imageViewList = imageViewList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // TODO Auto-generated method stub
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }
    }
}
