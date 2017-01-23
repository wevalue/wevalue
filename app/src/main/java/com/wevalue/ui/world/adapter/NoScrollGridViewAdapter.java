package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wevalue.R;

import com.wevalue.ui.release.ReleaseNoteActivity;
import com.wevalue.utils.LogUtils;

import java.io.File;
import java.util.List;

public class NoScrollGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ReleaseNoteActivity sendNoteActivity;

    private List<String> mList;


    public NoScrollGridViewAdapter(ReleaseNoteActivity context) {
        sendNoteActivity = context;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    public void setmList(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        if (mList.size() < 9) {
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pic_choice_grid, null);
            vh = new ViewHolder();
            vh.id_item_image = (ImageView) convertView.findViewById(R.id.id_item_image);
            vh.id_item_select = (ImageButton) convertView.findViewById(R.id.id_item_select);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.id_item_select.setImageResource(R.mipmap.delete);
        if (position == mList.size()) {
            vh.id_item_select.setVisibility(View.GONE);
            vh.id_item_image.setImageResource(R.mipmap.tianjiatupian);
            Glide.with(mContext).load(R.mipmap.tianjiatupian).into(vh.id_item_image);
//			vh.id_item_image.setScaleType(ImageView.ScaleType.MATRIX);
        } else {
            vh.id_item_select.setVisibility(View.VISIBLE);
            LogUtils.e("mlist.size = " + mList.size());
            if (mList.size() > 0) {
                Glide.with(mContext).load(new File(mList.get(position))).thumbnail(0.1f).into(vh.id_item_image);
//                vh.id_item_image.setImageBitmap(mList.get(position));
            }
        }


        vh.id_item_select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != sendNoteActivity) {
                    sendNoteActivity.itemDeleteClick(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView id_item_image;
        ImageButton id_item_select;
    }

}
