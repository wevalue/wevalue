package com.wevalue.ui.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.net.RequestPath;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/12/2
 * 类说明：认证内容的图片展示
 */

public class RenZhengZhongAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    public RenZhengZhongAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pic, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            holder.iv_qingxu = (ImageView) convertView.findViewById(R.id.iv_qingxu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(RequestPath.SERVER_PATH + list.get(i)).placeholder(R.mipmap.picplaceholder).into(holder.iv_qingxu);
        return convertView;
    }


    private class ViewHolder {
        TextView tv;
        ImageView iv_qingxu;
    }
}
