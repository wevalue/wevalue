package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.CityBean;

import java.util.List;

/**
 * Created by Administrator on 2016-07-18.
 */
public class AddrChoiceGridViewAdapter extends BaseAdapter {

    private List<CityBean> mDatas;
    private Context mContext;

    public AddrChoiceGridViewAdapter(List<CityBean> mDatas, Context mContext) {
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
        ViewHolder viewHolder =null;

        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_type_choice,null);
            viewHolder=new ViewHolder();
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.tv_type_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.cityName.setText(mDatas.get(position).getCityname());


        return convertView;
    }


    class ViewHolder{
        TextView cityName;
    }
}
