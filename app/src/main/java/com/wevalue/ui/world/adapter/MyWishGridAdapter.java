package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhua on 2016/8/23.
 */
public class MyWishGridAdapter extends BaseAdapter {

    private Context context;
    private List<String> mDate;
    private LayoutInflater minflater;

    public MyWishGridAdapter(Context context, List<String> mDate) {
        this.context = context;
        this.mDate=mDate;
        minflater = LayoutInflater.from(context);
    }

    public void setmData(List<String> mDate) {

        this.mDate = mDate;
    }




    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int i) {
        return mDate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = minflater.inflate(R.layout.item_emotion_wish,null);
            holder.tv_item= (TextView) view.findViewById(R.id.tv_item);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_item.setText(mDate.get(i));

        return view;
    }
    class ViewHolder{
        TextView tv_item;
    }
}
