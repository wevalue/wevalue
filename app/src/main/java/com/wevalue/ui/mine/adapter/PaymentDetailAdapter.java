package com.wevalue.ui.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.PaymentDetailModel;
import com.wevalue.utils.DateTiemUtils;

import java.util.List;

/**
 * Created by xuhua on 2016/8/26.
 * 收支詳情的適配器
 */
public class PaymentDetailAdapter extends BaseAdapter {
    private List<PaymentDetailModel.DataBean> mDataBeanList;

    public PaymentDetailAdapter(Context context, List<PaymentDetailModel.DataBean> mDataBeanList) {
        this.context = context;
        this.mDataBeanList = mDataBeanList;
    }

    private Context context;

    @Override
    public int getCount() {
        return mDataBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shouzhi_mingxi, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_fabuinfo = (TextView) convertView.findViewById(R.id.tv_fabuinfo);
            viewHolder.tv_morry = (TextView) convertView.findViewById(R.id.tv_morry);
            viewHolder.tv_shijian = (TextView) convertView.findViewById(R.id.tv_shijian);
            viewHolder.ll_shouzhi = (LinearLayout) convertView.findViewById(R.id.ll_shouzhi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String symbol = "";
        if (0 == mDataBeanList.get(i).getType()) {
            symbol = "- ¥";
        } else {
            symbol = "+ ¥";
        }
        viewHolder.tv_fabuinfo.setText(mDataBeanList.get(i).getTitle());
        viewHolder.tv_morry.setText(symbol + mDataBeanList.get(i).getMoney());
        viewHolder.tv_shijian.setText(DateTiemUtils.editTime(mDataBeanList.get(i).getAddtime(),true));
//        viewHolder.ll_shouzhi.setOnClickListener(news View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = news Intent(context, ShouZhiDetailsActivity.class);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_fabuinfo;
        TextView tv_morry;
        TextView tv_shijian;
        LinearLayout ll_shouzhi;
    }
}
