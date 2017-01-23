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
public class AddrChoiceListViewAdapter extends BaseAdapter {

    private List<CityBean> mDatas;
    private Context mContext;

    public AddrChoiceListViewAdapter(List<CityBean> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    public void setmDatas(List<CityBean> mDatas) {
        this.mDatas = mDatas;
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
        ViewHolder viewHolder = null;

        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_choice,null);
            viewHolder=new ViewHolder();
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.tv_city_name);
            viewHolder.zimu = (TextView) convertView.findViewById(R.id.tv_zimu);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.cityName.setText(mDatas.get(position).getCityname());


        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.zimu.setVisibility(View.VISIBLE);
            viewHolder.zimu.setText(mDatas.get(position).getSortLetters());
        }else{
            viewHolder.zimu.setVisibility(View.GONE);
        }

//        String zimu =mDatas.get(position).getSpell().substring(0,1);
//        viewHolder.zimu.setText(zimu);
//        if(position>0){
//            String zimu_2 =mDatas.get(position-1).getSpell().substring(0,1);
//            if(zimu.equals(zimu_2)){
//                viewHolder.zimu.setVisibility(View.GONE);
//            }else {
//                viewHolder.zimu.setVisibility(View.VISIBLE);
//            }
//        }


        return convertView;
    }


    class ViewHolder{
        TextView cityName;
        TextView zimu;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mDatas.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }
}
