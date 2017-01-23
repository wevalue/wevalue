package com.wevalue.view.sortview;

import android.app.Activity;
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
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.utils.PopuUtil;

import java.util.List;

//通讯录和消息页的适配器
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;//需要排序的列表
    private Context mContext;//上下文对象
    private String mSortType;//判断调用适配器的对象是联系人界面还是城市界面
    private String mFriendType;
    FriendManagerInterface mFriendManagerInterface;

//    public SortAdapter(Context mContext, List<SortModel> list, FriendManagerInterface friendManagerInterface, String mFriendType) {
//        this.list = list;
//        this.mContext = mContext;
//        this.mFriendManagerInterface = friendManagerInterface;
//        mSortType = "we";
//        this.mFriendType = mFriendType;
//    }

    public SortAdapter(Context mContext, List<SortModel> list, FriendManagerInterface friendManagerInterface) {
        this.mContext = mContext;
        this.list = list;
        this.mFriendManagerInterface = friendManagerInterface;
        mSortType = "contact";
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
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_city_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            viewHolder.tv_jianjie = (TextView) view.findViewById(R.id.tv_jianjie);
            viewHolder.tv_add_friends = (TextView) view.findViewById(R.id.tv_add_friend);
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_cancel_attention = (TextView) view.findViewById(R.id.tv_cancel_attention);
            viewHolder.tv_level = (TextView) view.findViewById(R.id.tv_level);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        String s = list.get(position).getName();
        viewHolder.tvTitle.setText(list.get(position).getName());

        switch (mSortType) {
            case "city":
                viewHolder.tv_cancel_attention.setVisibility(View.GONE);
                viewHolder.tv_add_friends.setVisibility(View.GONE);
                viewHolder.tv_jianjie.setVisibility(View.GONE);
                viewHolder.iv_icon.setVisibility(View.GONE);
                viewHolder.tv_level.setVisibility(View.GONE);
                break;
            case "contact":
                String relation = "";
                if (mContent.getIsFriend().equals("1")) {
                    relation = "(好友)";
                } else if (mContent.getIsFans().equals("1") && mContent.getIsFocuse().equals("1")) {
                    relation = "(互粉)";
                } else {
                    if (mContent.getIsFans().equals("1")) {
                        relation = "(粉丝)";
                    } else if (mContent.getIsFocuse().equals("1")) {
                        relation = "(关注)";
                    } else {
                        relation = "(陌生人)";
                    }
                }
                viewHolder.tvTitle.setVisibility(View.GONE);
                viewHolder.tv_level.setVisibility(View.VISIBLE);
                viewHolder.tv_level.setText(list.get(position).getName() + " " + mContent.getUserlevel() + " " + relation);
                viewHolder.tv_cancel_attention.setText("加关注");

                //加好友点击事件
                viewHolder.tv_add_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopuUtil.initManageFriend((Activity) mContext, "加好友", mFriendManagerInterface, mContent.getUserId());
                    }
                });
                //加关注点击事件
                viewHolder.tv_cancel_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopuUtil.initManageFriend((Activity) mContext, "加关注", mFriendManagerInterface, mContent.getUserId());
                    }
                });

                break;
            case "we":
                switch (mFriendType) {
                    case "fans":
                        viewHolder.tv_cancel_attention.setText("加好友");
                        if (mContent.getIsFans().equals("1")) {
                            viewHolder.tv_add_friends.setText("加关注");
                        } else {
                            viewHolder.tv_add_friends.setText("取消关注");
                        }
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
                        viewHolder.tv_cancel_attention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopuUtil.initManageFriend((Activity) mContext, "加好友", mFriendManagerInterface, mContent.getUserId());
                            }
                        });
                        break;
                    case "attention":
                        viewHolder.tv_cancel_attention.setText("取消关注");
                        viewHolder.tv_add_friends.setText("加好友");
                        //加好友点击事件
                        viewHolder.tv_add_friends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PopuUtil.initManageFriend((Activity) mContext, "加好友", mFriendManagerInterface, mContent.getUserId());
//                                mFriendManagerInterface.manageFriend("添加好友", mContent.getUserId());
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
                    case "friend":

                        break;
                }
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
        TextView tvTitle;
        TextView tvLetter;
        TextView tv_jianjie, tv_cancel_attention, tv_add_friends, tv_level;
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