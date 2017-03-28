package com.wevalue.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.net.RequestPath;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 网络图片加载处理
 *
 * Created by xox on 2017/3/8.
 */

public class ImageUitls {

    /**
     * 设置头像
     * @param url
     * @param view
     */
    public static void setHead(String url,ImageView view){
        if (url!=null)
        if (!url.contains("http"))
            url = RequestPath.SERVER_WEB_PATH+url;
        Glide.with(WeValueApplication.applicationContext)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.default_head)
                .into(view);
    }


    /**
     * 设置图片
     * @param url
     * @param view
     */
    public static void setImg(String url,ImageView view,int rid){
        if (null!=url&&!url.contains("http"))
            url = RequestPath.SERVER_WEB_PATH+url;
        Glide.with(WeValueApplication.applicationContext)
                .load(url)
                .placeholder(rid)
                .into(view);
    }

    /**
     * 设置图片
     * @param url
     * @param view
     */
    public static void setImg(String url,ImageView view){
        if (null!=url&&!url.contains("http"))
            url = RequestPath.SERVER_WEB_PATH+url;
        Glide.with(WeValueApplication.applicationContext)
                .load(url)
                .placeholder(R.mipmap.default_img)
                .into(view);
    }

    /**
     * 缩略图
     * @param url
     * @param view
     */
    public static void setThumImg(String url,ImageView view){
        if (null!=url&&!url.contains("http"))
            url = RequestPath.SERVER_WEB_PATH+url;
        Glide.with(WeValueApplication.applicationContext)
                .load(url)
                .asBitmap()
                .thumbnail(0.5f)
                .placeholder(R.mipmap.default_img)
                .into(view);
    }

    /**
     * 图片处理
     * @param url
     * @param view
     */
    public  static void setImg(String url,ImageView view,Transformation<Bitmap> trans){
        if (null!=url&&!url.contains("http"))
            url = RequestPath.SERVER_WEB_PATH+url;

        Glide.with(WeValueApplication.applicationContext)
                .load(url)
                .bitmapTransform(trans)
                .thumbnail(0.5f)
                .placeholder(R.mipmap.default_img)
                .into(view);
        //模糊处理
        //Transformation<Bitmap> trans = new BlurTransformation(WeValueApplication.applicationContext,23,4)
        //灰度处理
        //Transformation<Bitmap> trans = new GrayscaleTransformation(WeValueApplication.applicationContext)

        //圆角处理
        //Transformation<Bitmap> trans = new RoundedCornersTransformation(this,30,0, RoundedCornersTransformation.CornerType.ALL)
        //圆形裁剪
        //Transformation<Bitmap> trans = new CropCircleTransformation(this)

    }
}
