package com.wevalue.ui.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.QuanXianEntity;
import com.wevalue.ui.mine.activity.BuyPermissionActivity;
import com.wevalue.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2016-11-19.
 */
public class QuanXianAdapter extends BaseAdapter {

    private List<QuanXianEntity.DataBean> mDatas;
    private Context mContext;

    public QuanXianAdapter(List<QuanXianEntity.DataBean> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    public void setmDatas(List<QuanXianEntity.DataBean> mDatas) {
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
        ViewHolder vh = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itme_quanxian,null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.tv_qx_itme);

            convertView.setTag(vh);
        }else {

            vh = (ViewHolder) convertView.getTag();

        }



        vh.tv.setText(mDatas.get(position).getNumber()
                +"条/"+mDatas.get(position).getMoney()+"元");

//        if(mDatas.get(position).getIsClick()){
//            vh. tv.setBackgroundResource(R.mipmap.me_login_btn);
//            LogUtils.e("---if-position--="+position+"--isclick="+mDatas.get(position).getIsClick());
//        }else {
//            vh.tv.setBackgroundResource(R.mipmap.me_rect);
//
//            LogUtils.e("----else-position--="+position+"--isclick="+mDatas.get(position).getIsClick());
//        }
        return convertView;
    }

    public class ViewHolder{
        TextView tv;
    }

    public interface IsClickItme{
        void changeBg(int position);

    }
}