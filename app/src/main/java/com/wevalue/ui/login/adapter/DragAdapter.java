package com.wevalue.ui.login.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.ChannelBean;

import java.util.List;

public class DragAdapter extends BaseAdapter {
	/** TAG*/
	private final static String TAG = "DragAdapter";
	/** 是否显示底部的ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;
	/** 是否可见 */
	boolean isVisible = true;
	/** 可以拖动的列表（即用户选择的频道列表） */
	public List<ChannelBean.Channel> channelList;
	/** TextView 频道内容 */
	private TextView item_text;
	private ImageView iv_delete;
	/** 要删除的position */
	public int remove_position = -1;
	/** 是否编辑 */
	public boolean isEdit =false;

	public TextView tv_edit;

	public DragAdapter(Context context, List<ChannelBean.Channel> channelList,TextView tv_edit) {
		this.context = context;
		this.channelList = channelList;
		this.tv_edit = tv_edit;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ChannelBean.Channel getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.subscribe_category_item, null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
		ChannelBean.Channel channel = getItem(position);
		item_text.setText(channel.getTypename());
		if(isEdit){
			iv_delete.setVisibility(View.VISIBLE);
		}else {
			iv_delete.setVisibility(View.GONE);
		}
		if (position == 0){
//			item_text.setTextColor(context.getResources().getColor(R.color.black));
			item_text.setEnabled(false);
			iv_delete.setVisibility(View.GONE);
		}
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if(remove_position == position){
			item_text.setText("");
		}
		return view;
	}

	/** 添加频道列表 */
	public void addItem(ChannelBean.Channel channel) {
		channelList.add(channel);
		notifyDataSetChanged();
	}

	/** 拖动变更频道排序 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		ChannelBean.Channel dragItem = getItem(dragPostion);
		Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/** 获取频道列表 */
	public List<ChannelBean.Channel> getChannnelLst() {
		return channelList;
	}

	/** 设置删除的position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
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
	/** 显示放下的ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}

	public void setIsEdit(boolean isEdit){
		this.isEdit =isEdit;
		if(isEdit){
			tv_edit.setText("完成");
		}else {
			tv_edit.setText("编辑");
		}
	}
	public boolean getIsEdit(){
		return isEdit;
	}
}