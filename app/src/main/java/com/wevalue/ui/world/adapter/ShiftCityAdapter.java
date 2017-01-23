package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.SortModel;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/3
 * 类说明：
 */

public class ShiftCityAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;//需要排序的列表
    private Context mContext;//上下文对象

    public ShiftCityAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ShiftCityAdapter.ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ShiftCityAdapter.ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_shift_city, null);
            viewHolder.tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
            viewHolder.tv_letter = (TextView) view.findViewById(R.id.tv_letter);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ShiftCityAdapter.ViewHolder) view.getTag();
        }
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tv_letter.setVisibility(View.VISIBLE);
            viewHolder.tv_letter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tv_letter.setVisibility(View.GONE);
        }
        String s = list.get(position).getName();
        viewHolder.tv_city_name.setText(list.get(position).getName());
        return view;
    }

    final static class ViewHolder {
        TextView tv_city_name;
        TextView tv_letter;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
