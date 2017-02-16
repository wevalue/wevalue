package com.wevalue.ui.add.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.NearbyEntity;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.List;


/**
 * 作者：邹永奎
 * 创建时间：2016/10/9
 * 类说明：附近的人适配器
 */

public class NearbyAdapter extends BaseAdapter {
    List<NearbyEntity.NearbyUser> nearbyUserList;
    Context context;
    private FriendManagerInterface friendManagerInterface;
    String isFriend, isFocus, isFans, userSex;//该用户是否为好友或者粉丝

    public NearbyAdapter(List<NearbyEntity.NearbyUser> nearbyUserList, Context context, FriendManagerInterface friendManagerInterface) {
        this.nearbyUserList = nearbyUserList;
        this.context = context;
        this.friendManagerInterface = friendManagerInterface;
    }

    public void setDatas(List<NearbyEntity.NearbyUser> nearbyUserList) {
        this.nearbyUserList = nearbyUserList;
    }

    @Override
    public int getCount() {
        return nearbyUserList.size();
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
        NearbyAdapter.ViewHolder viewHolder = null;
        final NearbyEntity.NearbyUser nearbyUser = nearbyUserList.get(position);
        if (convertView == null) {
            viewHolder = new NearbyAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nearbyuser, null);
            viewHolder.ll_rootview = (LinearLayout) convertView.findViewById(R.id.ll_rootview);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_guanxi = (TextView) convertView.findViewById(R.id.tv_guanxi);
            viewHolder.tv_deng_ji = (TextView) convertView.findViewById(R.id.tv_deng_ji);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.tv_add_friends = (TextView) convertView.findViewById(R.id.tv_add_friend);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_add_focus = (TextView) convertView.findViewById(R.id.tv_add_focus);
            viewHolder.tv_catagory = (TextView) convertView.findViewById(R.id.tv_catagory);
            viewHolder.userSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NearbyAdapter.ViewHolder) convertView.getTag();
        }
        isFriend = nearbyUserList.get(position).getIsfriend();
        isFocus = nearbyUserList.get(position).getIsfocus();
        isFans = nearbyUserList.get(position).getIsfans();
        userSex = nearbyUserList.get(position).getUsersex();
        viewHolder.distance.setText(nearbyUser.getDistance() + " km");
        viewHolder.tv_name.setText(nearbyUser.getUsernickname());
        viewHolder.tv_deng_ji.setText(nearbyUser.getUserlevel());
        //设置用户性别头像
        if (userSex.equals("男")) {
            viewHolder.userSex.setImageResource(R.mipmap.icon_man);
        } else {
            viewHolder.userSex.setImageResource(R.mipmap.icon_woman);
        }
        if (isFriend.equals("1")) {
            //该用户为好友
//            SpannableStringBuilder style_2 = news SpannableStringBuilder(nearbyUser.getUsernickname() + "  " + nearbyUser.getUserlevel() + "（好友）");
//            style_2.setSpan(news ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
//                    nearbyUser.getUsernickname().length(),
//                    nearbyUser.getUsernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            viewHolder.tv_name.setText(style_2);
            viewHolder.tv_guanxi.setText("（好友）");
            viewHolder.tv_add_focus.setVisibility(View.INVISIBLE);
            viewHolder.tv_add_friends.setVisibility(View.INVISIBLE);
        } else {
            //用户不是本人的好友
            if (isFriend.equals("3") || isFriend.equals("2")) {
//                //已添加等待同意
//                SpannableStringBuilder style_2 = news SpannableStringBuilder(nearbyUser.getUsernickname() + "  " +
//                        nearbyUser.getUserlevel() + "（已申请）");
//                style_2.setSpan(news ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
//                        nearbyUser.getUsernickname().length(),
//                        nearbyUser.getUsernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.tv_name.setText(style_2);
                viewHolder.tv_guanxi.setText("（已申请）");
//            viewHolder.tv_name.setText(nearbyUser.getUsernickname() + " " + nearbyUser.getUserlevel() + "（好友）");
                if (!isFocus.equals("1")) {
                    viewHolder.tv_add_focus.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_add_focus.setVisibility(View.INVISIBLE);
                }
                viewHolder.tv_add_friends.setVisibility(View.VISIBLE);
                viewHolder.tv_add_friends.setText("已申请");
            } else {
                if (isFocus.equals("1")) {
                    //该用户是本人关注的人
//                    SpannableStringBuilder style_2 = news SpannableStringBuilder(nearbyUser.getUsernickname() + "  " + nearbyUser.getUserlevel() + "（关注）");
//                    style_2.setSpan(news ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
//                            nearbyUser.getUsernickname().length(),
//                            nearbyUser.getUsernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    viewHolder.tv_name.setText(style_2);
                    viewHolder.tv_guanxi.setText("加好友");
                    viewHolder.tv_add_friends.setVisibility(View.VISIBLE);
                    viewHolder.tv_add_friends.setText("加好友");
                    viewHolder.tv_add_focus.setVisibility(View.INVISIBLE);
                } else {
                    //该用户不是好友也不是关注的人
                    viewHolder.tv_add_focus.setVisibility(View.VISIBLE);
                    viewHolder.tv_add_friends.setVisibility(View.VISIBLE);
                    viewHolder.tv_add_friends.setText("加好友");
                    viewHolder.tv_add_focus.setText("加关注");
                    if (isFans.equals("1")) {
                        //该好友是本人的粉丝
//                        SpannableStringBuilder style_2 = news SpannableStringBuilder(nearbyUser.getUsernickname() + "  " + nearbyUser.getUserlevel() + "（粉丝）");
//                        style_2.setSpan(news ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
//                                nearbyUser.getUsernickname().length(),
//                                nearbyUser.getUsernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        viewHolder.tv_name.setText(style_2);
                        viewHolder.tv_guanxi.setText("（粉丝）");

//                viewHolder.tv_name.setText(nearbyUser.getUsernickname() + " " + nearbyUser.getUserlevel() + "（粉丝）");
                    } else {
                        //该好友和本人是陌生人关系
//                        SpannableStringBuilder style_2 = news SpannableStringBuilder(nearbyUser.getUsernickname() + "  " + nearbyUser.getUserlevel() + "（陌生人）");
//                        style_2.setSpan(news ForegroundColorSpan(context.getResources().getColor(R.color.blue)),
//                                nearbyUser.getUsernickname().length(),
//                                nearbyUser.getUsernickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        viewHolder.tv_name.setText(style_2);
                        viewHolder.tv_guanxi.setText("（陌生人）");
//                viewHolder.tv_name.setText(nearbyUser.getUsernickname() + " " + nearbyUser.getUserlevel() + "(陌生人)");
                    }
                }
            }
        }

        viewHolder.tv_add_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin())return;
                PopuUtil.initManageFriend((Activity) context, "加关注", friendManagerInterface, nearbyUser.getUserid());
//                nearbyUserList.get(position).setIsfocus("1");
//                notifyDataSetChanged();
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.tv_add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("已申请".equals(finalViewHolder.tv_add_friends.getText().toString())) {
                    return;
                }
                if (!checkLogin())return;
                PopuUtil.initManageFriend((Activity) context, "加好友", friendManagerInterface, nearbyUser.getUserid());
//                nearbyUserList.get(position).setIsfriend("2");
            }
        });
        String imgurl = RequestPath.SERVER_PATH + nearbyUser.getUserface();
        Glide.with(context)
                .load(imgurl).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .into(viewHolder.iv_icon);
        viewHolder.ll_rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("detailuserid", nearbyUserList.get(position).getUserid());
                context.startActivity(intent);
            }
        });
        viewHolder.tv_catagory.setVisibility(View.GONE);
        return convertView;
    }
    private boolean checkLogin(){
        String uid = SharedPreferencesUtil.getUid(context);
        if (TextUtils.isEmpty(uid)) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }
    class ViewHolder {
        LinearLayout ll_rootview;
        TextView tv_name, distance, tv_add_focus, tv_add_friends;
        TextView tv_catagory, tv_deng_ji, tv_guanxi;
        ImageView iv_icon, userSex;
    }
}
