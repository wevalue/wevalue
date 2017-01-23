package com.wevalue.ui.login.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wevalue.R;
import com.wevalue.ui.login.RenZhengActivity;

import java.util.List;

/**
 * Created by Administrator on 2016-07-20.
 */
public class RenZhengImgAdapter extends BaseAdapter {

    private List<Bitmap> mDatas;
    private RenZhengActivity mContext;
    private String isWho;
    private DelPic delPic;

    public interface DelPic {
        void delPic(int position);
    }

    public RenZhengImgAdapter(List<Bitmap> mDatas, RenZhengActivity mContext, String isWho, DelPic delPic) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        this.isWho = isWho;
        this.delPic = delPic;
    }

    public void setmDatas(List<Bitmap> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {

        if (isWho.equals("1")) {
            if (mDatas.size() == 3) {
                return mDatas.size();
            } else {
                return mDatas.size() + 1;
            }
        } else if (isWho.equals("2")) {
            if (mDatas.size() == 9) {
                return mDatas.size();
            } else {
                return mDatas.size() + 1;
            }
        } else {
            return 0;
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

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_renzheng_img, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.del = (ImageView) convertView.findViewById(R.id.iv_del);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position != mDatas.size()) {
            viewHolder.img.setImageBitmap(mDatas.get(position));
            viewHolder.del.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img.setImageResource(R.mipmap.add_picture);
            viewHolder.del.setVisibility(View.GONE);
        }
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delPic.delPic(position);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView img;
        ImageView del;
    }
}
