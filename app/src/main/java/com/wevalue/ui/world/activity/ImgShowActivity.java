package com.wevalue.ui.world.activity;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.PhotoViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImgShowActivity extends BaseActivity {
    private TextView tv_img_number;
    private TextView tv_baocun;
    private String[] mImgUrl;
    private List<String> mDatas;
    private List<ImageView> imageViewList;
    private PhotoViewPager vp_img_show;
    private ImageView iv_donghua;
    private int index;//选择的图片位置
    private AnimationDrawable animaition;
    Handler handler = new Handler();
    private File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_show);
        initView();
    }


    private void initView() {
        vp_img_show = (PhotoViewPager) findViewById(R.id.vp_img_show);
//        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
//        tv_head_title.setText("图片详情");
//        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_img_number = (TextView) findViewById(R.id.tv_img_number);

        iv_donghua = (ImageView) findViewById(R.id.iv_donghua);

        animaition = (AnimationDrawable) iv_donghua.getDrawable();
        animaition.setOneShot(false);

        tv_baocun = (TextView) findViewById(R.id.tv_baocun);
        tv_baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        saveImgViewDrawable();
                    }
                }.start();

            }
        });
        index = getIntent().getIntExtra("index", -1);
        if ("yes".equals(getIntent().getStringExtra("imPreview"))) {
            tv_img_number.setVisibility(View.GONE);
        }
        LogUtils.e("log", "----index--" + index);
        mImgUrl = getIntent().getStringArrayExtra("imgUrl");
        if (mImgUrl != null && mImgUrl.length > 0) {
            LogUtils.e("log", "----mimgurl--" + mImgUrl[0]);
        }
        initGoodPhoto(mImgUrl);
        vp_img_show.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                tv_img_number.setText((arg0 + 1) + "/" + vp_img_show.getAdapter().getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });


    }

    public void saveImgViewDrawable() {
        Drawable drawable = imageViewList.get(vp_img_show.getCurrentItem()).getDrawable();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable1 = getDrawable(R.drawable.pic_lunbo);
            if(drawable.equals(drawable1)){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        ShowUtil.showToast(ImgShowActivity.this,"图片还没加载完成...");
                    }
                });
                return;
            }
        }

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

//        BitmapDrawable bd = (BitmapDrawable) drawable;
//        Bitmap bitmap = bd.getBitmap();

        String PHOTO_FILE_NAME = "weizhixiazai_img.jpg";
        Long suijishu = System.currentTimeMillis();
        saveMyBitmap(Environment.getExternalStorageDirectory() + "/微值/" + suijishu + PHOTO_FILE_NAME, bitmap);

    }

    /**
     * Title:  saveMyBitmap<br>
     * Description: TODO  保存图片<br>
     *
     * @param bitName
     * @param mBitmap
     * @since JDK 1.7
     */
    public File saveMyBitmap(String bitName, Bitmap mBitmap) {
         f = new File(bitName);
        File path = new File(Environment.getExternalStorageDirectory() + "/微值/");
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e("保存的图片路径=" + f.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        sendBroadcast(intent);
        handler.post(new Runnable() {
            @Override
            public void run() {

                ShowUtil.showToast(ImgShowActivity.this,"图片已保存至" + f.getAbsolutePath());
            }
        });
//        ShowUtil.showToast(this, "图片已保存至" + f.getAbsolutePath());

        return f;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        animaition.stop();
    }

    /**
     * Title: initGoodPhoto<br>
     * Description: TODO 商品大图展示<br>
     *
     * @param imageList
     * @since JDK 1.7
     */
    private void initGoodPhoto(String[] imageList) {
        // TODO Auto-generated method stub
        if (imageList == null || imageList.length == 0) {
            return;
        }
        tv_img_number.setText(index + 1 + "/" + imageList.length);
        if (index != -3) {
            if (imageList.length == 1) {
                tv_img_number.setVisibility(View.GONE);
            } else {
                tv_img_number.setVisibility(View.VISIBLE);
            }
        }

        if (imageViewList == null) {
            imageViewList = new ArrayList<ImageView>();
        }
        imageViewList.clear();
        tv_baocun.setVisibility(View.GONE);
        for (String url : imageList) {
            LogUtils.e("url=" + url);
            if ("yes".equals(getIntent().getStringExtra("imPreview"))) {
                imPreview(url);
            } else {
                if (url.endsWith(".gif")){
                    setGifImage(url);
                }else {
                    setImage(url);
                }
            }

        }
        GoodDetailImagePagerAdapter imagePagerAdapter = new GoodDetailImagePagerAdapter(this, imageViewList);
        vp_img_show.setAdapter(imagePagerAdapter);
        vp_img_show.setCurrentItem(index);
    }

    /**
     * 预览
     * @param url
     */
    private void imPreview(String url) {
        final PhotoView imageView = new PhotoView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(R.drawable.pic_lunbo);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        animaition = (AnimationDrawable) imageView.getDrawable();
        animaition.setOneShot(false);
        animaition.start();
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.mipmap.pictures_no)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        animaition.stop();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        animaition.stop();
                        return false;
                    }
                })
                .into(imageView);
        imageViewList.add(imageView);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                finish();
            }

        });
    }

    /**
     * 加载图片
     * @param url
     */
    private void setImage(String url){
        final PhotoView imageView = new PhotoView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        imageView.setImageResource(R.drawable.pic_lunbo);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        animaition = (AnimationDrawable) imageView.getDrawable();
        animaition.setOneShot(false);
        animaition.start();

        ViewTarget viewTarget = new ViewTarget(imageView) {
            @Override
            public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                {
                    animaition.stop();
                    tv_baocun.setVisibility(View.VISIBLE);
                    Drawable drawable ;
                    drawable = (Drawable) resource;
                    if (drawable.getMinimumHeight() - drawable.getMinimumWidth() > drawable.getMinimumWidth() * 2) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageDrawable(drawable);
                    } else {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setImageDrawable(drawable);
                    }
                }
            }
        };

        Glide.with(this)
                .load(RequestPath.SERVER_PATH + url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.pictures_no)
                //.crossFade()
                .into(viewTarget);
        imageViewList.add(imageView);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                finish();
            }

        });
    }
    /**
     * 加载gif图片
     * @param url
     */
    private  void setGifImage(String url){
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(R.drawable.pic_lunbo);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        animaition = (AnimationDrawable) imageView.getDrawable();
        animaition.setOneShot(false);
        animaition.start();
        Glide.with(this)
                .load(RequestPath.SERVER_PATH + url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.pictures_no)
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        animaition.stop();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        animaition.stop();
                        return false;
                    }
                })
                .into(imageView);

        imageViewList.add(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
