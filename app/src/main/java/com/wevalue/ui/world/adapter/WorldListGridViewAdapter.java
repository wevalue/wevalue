package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;

import java.util.List;

/**
 * Created by Administrator on 2016-07-27.
 */
public class WorldListGridViewAdapter extends BaseAdapter{
    private List<NoteBean.ImgUrl> mDatas;
    private Context mContext;

    // 图片下载器
//    private BitmapUtils mBitmap;
//    private BitmapDisplayConfig bitmapDisplayConfig;

    public WorldListGridViewAdapter(List<NoteBean.ImgUrl> mDatas, Context mContext
            /*,BitmapUtils mBitmap,BitmapDisplayConfig bitmapDisplayConfig*/) {

//        this.mBitmap = mBitmap;
//        this.bitmapDisplayConfig = bitmapDisplayConfig;
        this.mDatas = mDatas;
        this.mContext = mContext;


    }

    @Override
    public int getCount() {
        return mDatas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder  viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_world_list_gridview,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_gridview_item = (ImageView) convertView.findViewById(R.id.iv_gridview_item);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(RequestPath.SERVER_WEB_PATH+mDatas.get(position).getUrl())
                .crossFade()
                .into(viewHolder.iv_gridview_item);
        return convertView;
    }
    class ViewHolder{
        ImageView iv_gridview_item;
    }

}
