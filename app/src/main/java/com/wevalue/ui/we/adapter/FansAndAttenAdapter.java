package com.wevalue.ui.we.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.SortModel;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;

import java.util.List;

//粉丝和关注的人界面的适配器
public class FansAndAttenAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;//需要排序的列表
    private Context mContext;//上下文对象
    private String mFriendType;
    FriendManagerInterface mFriendManagerInterface;

    public FansAndAttenAdapter(Context mContext, List<SortModel> list, FriendManagerInterface friendManagerInterface, String mFriendType) {
        this.list = list;
        this.mContext = mContext;
        this.mFriendManagerInterface = friendManagerInterface;
        this.mFriendType = mFriendType;
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
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
            viewHolder.tv_bottominfo = (TextView) view.findViewById(R.id.tv_bottominfo);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            viewHolder.tv_jianjie = (TextView) view.findViewById(R.id.tv_jianjie);
            viewHolder.tv_add_friends = (TextView) view.findViewById(R.id.tv_add_friend);
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_cancel_attention = (TextView) view.findViewById(R.id.tv_cancel_attention);
            viewHolder.tv_level = (TextView) view.findViewById(R.id.tv_level);
            viewHolder.ll_user_info = (LinearLayout) view.findViewById(R.id.ll_user_info);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
//        if (position == list.size() - 1) {
//            viewHolder.tv_bottominfo.setVisibility(View.VISIBLE);
//            viewHolder.tv_bottominfo.setText(mContent.getName());
//            viewHolder.ll_user_info.setVisibility(View.GONE);
//            return view;
//        }
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(list.get(position).getName() + "  " + list.get(position).getUserlevel());

        switch (mFriendType) {
            case "fans":
                if ("3".equals(list.get(position).getIsFriend())||"2".equals(list.get(position).getIsFriend())) {
                    viewHolder.tv_cancel_attention.setText("申请中");
                } else {
                    viewHolder.tv_cancel_attention.setText("加好友");
                }
                if ("0".equals(list.get(position).getIsFocuse())) {
                    viewHolder.tv_add_friends.setText("加关注");
                } else if ("1".equals(list.get(position).getIsFocuse())) {
                    viewHolder.tv_add_friends.setText("取消关注");
                }
                LogUtils.e("FANS", list.get(position).getIsFocuse());
                //加好友点击事件
                final ViewHolder finalViewHolder = viewHolder;
                viewHolder.tv_add_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalViewHolder.tv_add_friends.getText().toString().equals("加关注")) {
                            PopuUtil.initManageFriend((Activity) mContext, "加关注", mFriendManagerInterface, mContent.getUserId());
                        } else {
                            PopuUtil.initManageFriend((Activity) mContext, "取消关注", mFriendManagerInterface, mContent.getUserId());
                        }
                    }
                });
                //加关注点击事件
                final ViewHolder finalViewHolder1 = viewHolder;
                viewHolder.tv_cancel_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("申请中".equals(finalViewHolder1.tv_cancel_attention.getText().toString())) {
                            return;
                        }
                        PopuUtil.initManageFriend((Activity) mContext, "加好友", mFriendManagerInterface, mContent.getUserId());
                    }
                });
                break;
            case "attention":
                viewHolder.tv_cancel_attention.setText("取消关注");
                if ("3".equals(list.get(position).getIsFriend())||"2".equals(list.get(position).getIsFriend())) {
                    viewHolder.tv_add_friends.setText("申请中");
                } else {
                    viewHolder.tv_add_friends.setText("加好友");
                }
                //加好友点击事件
                final ViewHolder finalViewHolder2 = viewHolder;
                viewHolder.tv_add_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("申请中".equals(finalViewHolder2.tv_add_friends.getText().toString())) {
                            return;
                        }
                        PopuUtil.initManageFriend((Activity) mContext, "加好友", mFriendManagerInterface, mContent.getUserId());
                    }
                });
                //加关注点击事件
                viewHolder.tv_cancel_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopuUtil.initManageFriend((Activity) mContext, "取消关注", mFriendManagerInterface, mContent.getUserId());
                    }
                });
                break;
        }
        viewHolder.tv_jianjie.setText(mContent.getJianjie());
        //加载图片的方法
        String imgurl = RequestPath.SERVER_PATH + mContent.getIcon();
        Glide.with(mContext)
                .load(imgurl).placeholder(R.mipmap.default_head)
                .into(viewHolder.iv_icon);
        return view;
    }

    final static class ViewHolder {
        TextView tvTitle, tv_bottominfo;
        TextView tvLetter;
        TextView tv_jianjie, tv_cancel_attention, tv_add_friends, tv_level;
        ImageView iv_icon;
        LinearLayout ll_user_info;
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
