package com.wevalue.ui.we.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.youmeng.StatisticsConsts;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/5
 * 类说明：我们界面信息类的适配器
 */

public class MessageAdapter extends BaseAdapter {
    Context mContext;
    List<SiteMessageModel.DataBean> dataBeanList;
    Activity mActivity;

    public MessageAdapter(Context mContext, List<SiteMessageModel.DataBean> dataBeanList) {
        this.mContext = mContext;
        this.dataBeanList = dataBeanList;
        mActivity = (Activity) mContext;
    }


    @Override
    public int getCount() {
        return dataBeanList.size();
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
        if (convertView == null) {
            viewHolder = new MessageAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemmessage, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_tittle = (TextView) convertView.findViewById(R.id.tv_tittle);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_red_cycle = (TextView) convertView.findViewById(R.id.tv_red_cycle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessageAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv_tittle.setText(dataBeanList.get(position).getMesstitle());
        viewHolder.tv_time.setText(dataBeanList.get(position).getAddtime());
        viewHolder.tv_content.setText(dataBeanList.get(position).getMesscontent());
        if (dataBeanList.get(position).getMessstate() == 1) {
            viewHolder.tv_red_cycle.setVisibility(View.GONE);//隐藏消息数量显示区域
        } else {
            if (!TextUtils.isEmpty(dataBeanList.get(position).getThistypenum())) {
                switch (dataBeanList.get(position).getMesstype()) {
                    case 1:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的好友");
                        break;
                    case 2:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的粉丝");
                        break;
                    case 3:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的打赏");
                        break;
                    case 4:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的转发");
                        break;
                    case 5:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的点赞");
                        break;
                    case 6:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "新的评论");
                        break;
                    case 7:
                        MobclickAgent.onEvent(mActivity, StatisticsConsts.event_newMessage, "系统消息");
                        break;
                }
                viewHolder.tv_red_cycle.setVisibility(View.VISIBLE);//显示消息数量的数字
                viewHolder.tv_red_cycle.setText(dataBeanList.get(position).getThistypenum());
            }
        }
        switch (dataBeanList.get(position).getMesstype()) {
            case 1:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_newfriend);
                break;
            case 2:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_newfans);
                break;
            case 3:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_sahng);
                break;
            case 4:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_zhuanfa);
                break;
            case 5:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_zan);
                break;
            case 6:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_message_pinglun);
                break;
            case 7:
                viewHolder.iv_icon.setImageResource(R.mipmap.we_sysmess);
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_tittle, tv_content, tv_time, tv_red_cycle;
    }
}
