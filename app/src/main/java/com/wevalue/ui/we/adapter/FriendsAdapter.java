package com.wevalue.ui.we.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.SortModel;
import com.wevalue.net.RequestPath;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/9/29
 * 类说明：我们模块“好友”的适配器
 */

public class FriendsAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;

    public FriendsAdapter(Context mContext, List<SortModel> list) {
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
        FriendsAdapter.ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new FriendsAdapter.ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friend_model, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_letter = (TextView) view.findViewById(R.id.tv_catagory);
            viewHolder.tv_user_info = (TextView) view.findViewById(R.id.tv_user_info);
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

            view.setTag(viewHolder);
        } else {
            viewHolder = (FriendsAdapter.ViewHolder) view.getTag();
        }
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tv_letter.setVisibility(View.VISIBLE);
            viewHolder.tv_letter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tv_letter.setVisibility(View.GONE);
        }
        viewHolder.tv_name.setText(this.list.get(position).getName() + "  " + list.get(position).getUserlevel());
        viewHolder.tv_user_info.setText(list.get(position).getJianjie());
        String imgurl = RequestPath.SERVER_PATH + list.get(position).getIcon();

        Glide.with(mContext)
                .load(imgurl).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .into(viewHolder.iv_icon);
        return view;
    }

    final static class ViewHolder {
        TextView tv_letter;
        TextView tv_name, tv_user_info;
        ImageView iv_icon;
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