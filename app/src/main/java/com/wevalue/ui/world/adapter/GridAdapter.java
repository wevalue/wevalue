package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.EmotionBean;
import com.wevalue.utils.LogUtils;

import java.util.List;


/**
 * Created by xuhua on 2016/8/15.
 */

public class GridAdapter extends BaseAdapter {
     private TextView tv_item;
    private  List<EmotionBean> myData;
    private LayoutInflater inflater;
    private Context mContext;

    public GridAdapter(Context context,List<EmotionBean>  list) {

        this.myData = list;
        inflater = LayoutInflater.from(context);
        mContext = context;

    }

    public List<EmotionBean> getMyData() {
        return myData;
    }

    public void setMyData(List<EmotionBean> myData) {
        this.myData = myData;
    }

    /**
     * 数据总数
     */
    @Override
    public int getCount() {

        return myData.size();
    }

    /**
     * 获取当前数据
     */
    @Override
    public Object getItem(int position) {

        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView=this.inflater.inflate(R.layout.item_emotion, null);
            holder.tv=(TextView) convertView.findViewById(R.id.tv_item);
            holder.iv_qingxu=(ImageView) convertView.findViewById(R.id.iv_qingxu);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder) convertView.getTag();
        }
        holder.tv.setText(myData.get(position).getName());
        LogUtils.e("myData.get(position).getIsClick()="+myData.get(position).getIsClick());
        holder.iv_qingxu.setBackgroundResource(myData.get(position).getImgint());
        if (myData.get(position).getIsClick().equals("1")){
//            holder.tv.setBackgroundResource(R.drawable.wz_text_shape_button_blue);
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.login_text_blue_2));



        }else {
//            holder.tv.setBackgroundResource(R.drawable.wz_text_shape_button_blue_2);
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.but_text_color));
        }
        return convertView;
    }
    private class ViewHolder{

        TextView tv;
        ImageView iv_qingxu;
    }

}