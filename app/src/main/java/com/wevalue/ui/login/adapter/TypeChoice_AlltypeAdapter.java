package com.wevalue.ui.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.ChannelBean;
import com.wevalue.ui.login.TypeChoiceActivity;

import java.util.List;

/**
 * 所有类别的适配器
 */
public class TypeChoice_AlltypeAdapter extends BaseAdapter {

    private List<ChannelBean.Channel> channelList;
    private TypeChoiceActivity mActivity;

    private Context context;
    private TextView item_text;
    /** 是否可见 */
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;

    public TypeChoice_AlltypeAdapter(List<ChannelBean.Channel> mDatas, TypeChoiceActivity mActivity) {
        this.context = mActivity;
        this.channelList = mDatas;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelBean.Channel getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ChannelBean.Channel channel = getItem(position);
        item_text.setText(channel.getTypename());
        if (!isVisible && (position == -1 + channelList.size())){
            item_text.setText("");
        }
        if(remove_position == position){
            item_text.setText("");
        }
        return view;
    }

    /** 获取频道列表 */
    public List<ChannelBean.Channel> getChannnelLst() {
        return channelList;
    }

    /** 添加频道列表 */
    public void addItem(ChannelBean.Channel channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }
    /** 设置频道列表 */
    public void setListDate(List<ChannelBean.Channel> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
