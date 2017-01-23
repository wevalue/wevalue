package com.wevalue.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.EarningsRankModel;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.UserDetailsActivity;

import java.util.List;

import static com.wevalue.R.id.tv_level;

/**
 * 作者：邹永奎
 * 创建时间：2016/10/21
 * 类说明：排行榜的收益排行适配器
 */
public class EarningRankAdapter extends BaseAdapter {
    Context mContext;
    private List<EarningsRankModel.DataBean> list = null;
    public EarningRankAdapter(Context context, List<EarningsRankModel.DataBean> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final EarningsRankModel.DataBean rankModel = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_earning, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_city_name);
            viewHolder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_cancel_attention = (TextView) convertView.findViewById(R.id.tv_cancel_attention);
            viewHolder.tv_cancel_attention.setVisibility(View.GONE);
            viewHolder.tv_level = (TextView) convertView.findViewById(tv_level);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(RequestPath.SERVER_PATH + rankModel.getUserface())
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .into(viewHolder.iv_icon);
        viewHolder.tvTitle.setText(rankModel.getUsernickname() + "  " + rankModel.getUserlevel());
        viewHolder.tv_level.setVisibility(View.GONE);
        viewHolder.tv_jianjie.setText(rankModel.getUserinfo());
        viewHolder.tv_cancel_attention.setText(rankModel.getRankcontent());
//        viewHolder.tv_cancel_attention.setTextColor(Color.RED);
//        viewHolder.tv_cancel_attention.setGravity(Gravity.RIGHT);
        viewHolder.tv_cancel_attention.setVisibility(View.VISIBLE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserDetailsActivity.class);
                intent.putExtra("detailuserid", list.get(position).getUserid());
//                SharedPreferencesUtil.setDetailUserid(mContext, list.get(position).getUserid());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    final static class ViewHolder {
        TextView tvTitle;
        TextView tv_jianjie, tv_cancel_attention, tv_level;
        ImageView iv_icon;
    }
}
