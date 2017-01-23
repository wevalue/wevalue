package com.wevalue.ui.mine.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.wevalue.R;
import com.wevalue.ui.mine.activity.FeedbackActivity;

import java.util.List;

/**
 * Created by Administrator on 2016-09-24.
 */
public class FeedbackImgAdapter extends BaseAdapter {


    private List<Bitmap> mDatas;
    private FeedbackActivity mActivity;

    public FeedbackImgAdapter(List<Bitmap> mDatas, FeedbackActivity mActivity) {
        this.mDatas = mDatas;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        if(mDatas.size()==3){
            return mDatas.size();
        }else {
            return mDatas.size()+1;
        }

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;

        if(convertView==null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_feedback_img,null);
            vh = new ViewHolder();
            vh.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            vh.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);


            convertView.setTag(vh);

        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(mDatas.size()==position){
            vh.iv_delete.setVisibility(View.GONE);
            vh.iv_img.setImageResource(R.mipmap.add_picture);
        }else {
            vh.iv_delete.setVisibility(View.VISIBLE);
            vh.iv_img.setImageBitmap(mDatas.get(position));
        }

        vh.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.deleteImg(position);
            }
        });


        return convertView;
    }


    class ViewHolder{

        ImageView iv_img;
        ImageView iv_delete;

    }
}
