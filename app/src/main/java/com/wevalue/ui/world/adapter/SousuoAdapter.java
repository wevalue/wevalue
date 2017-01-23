package com.wevalue.ui.world.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.wevalue.R;
import com.wevalue.model.SearchFriendBean;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by Administrator on 2016-06-08.
 */
public class SousuoAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchFriendBean.DtfriendBean> mDatas;
    private Activity mActivity;
    private FriendManagerInterface friendManagerInterface;

    public SousuoAdapter(Activity context, List<SearchFriendBean.DtfriendBean> list, FriendManagerInterface pInterface) {
        mActivity = context;
        mContext = context;
        mDatas = list;
        friendManagerInterface = pInterface;
    }

    public void setmDatas(List<SearchFriendBean.DtfriendBean> mDatas) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sousuojieguo_list, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.catalog = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.tv_tel_name = (TextView) convertView.findViewById(R.id.tv_tel_name);
            viewHolder.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            viewHolder.tv_phonename = (TextView) convertView.findViewById(R.id.tv_phonename);
            viewHolder.tv_haoyou = (TextView) convertView.findViewById(R.id.tv_haoyou);
            viewHolder.tv_jiaguanzhu = (TextView) convertView.findViewById(R.id.tv_jiaguanzhu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String userType = mDatas.get(position).getUserType();

        Glide.with(mContext)
                .load(RequestPath.SERVER_PATH + mDatas.get(position).getUserface())
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(viewHolder.image);
        LogUtils.e("userType=" + userType);
        if (TextUtils.isEmpty(userType)) {
            viewHolder.catalog.setVisibility(View.GONE);
        } else {
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(mDatas.get(position).getUserType());
        }
        if ("1".equals(mDatas.get(position).getIsfriend())) {
            //用户的好友隐藏显示加好友菜单
            viewHolder.tv_jiaguanzhu.setVisibility(View.GONE);
            viewHolder.tv_haoyou.setVisibility(View.GONE);
            viewHolder.tv_haoyou.setText("加好友");
        } else {
            if ("3".equals(mDatas.get(position).getIsfriend())) {
                viewHolder.tv_jiaguanzhu.setVisibility(View.GONE);
                viewHolder.tv_haoyou.setVisibility(View.VISIBLE);
                viewHolder.tv_haoyou.setText("申请中");
                viewHolder.tv_haoyou.setEnabled(false);
            } else {
                if ("1".equals(mDatas.get(position).getIsfocus())) {
                    //用户的粉丝隐藏加关注界面
                    viewHolder.tv_haoyou.setVisibility(View.VISIBLE);
                    viewHolder.tv_jiaguanzhu.setVisibility(View.GONE);
                    viewHolder.tv_haoyou.setText("加好友");
                } else {
                    viewHolder.tv_jiaguanzhu.setVisibility(View.VISIBLE);
                    viewHolder.tv_haoyou.setVisibility(View.VISIBLE);
                    viewHolder.tv_haoyou.setText("加好友");
                }
            }
        }
        viewHolder.tv_tel_name.setText(mDatas.get(position).getUsernickname());
        viewHolder.tv_dengji.setText(mDatas.get(position).getUserlevel());
        viewHolder.tv_phonename.setText(mDatas.get(position).getUserinfo());
        viewHolder.tv_haoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(mContext))) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
//                    if (viewHolder.tv_haoyou.getText().toString().equals())
                    PopuUtil.initManageFriend(mActivity, viewHolder.tv_haoyou.getText().toString(), friendManagerInterface, mDatas.get(position).getUserid());
                }
            }
        });
        viewHolder.tv_jiaguanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(mContext))) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                } else {
                    PopuUtil.initManageFriend(mActivity, viewHolder.tv_jiaguanzhu.getText().toString(), friendManagerInterface, mDatas.get(position).getUserid());
                }
            }
        });


//        if (mDatas.get(position).getUserid().equals(SharedPreferencesUtil.getUid(mContext))) {
//            viewHolder.tv_jiaguanzhu.setVisibility(View.GONE);
//            viewHolder.tv_haoyou.setVisibility(View.GONE);
//        } else {
//            viewHolder.tv_jiaguanzhu.setVisibility(View.VISIBLE);
//            viewHolder.tv_haoyou.setVisibility(View.VISIBLE);
//        }
        return convertView;
    }

    class ViewHolder {
        ImageView image;/*头像*/
        TextView catalog;/*分组*/
        TextView tv_tel_name;/*昵称*/
        TextView tv_dengji;/*等级*/
        TextView tv_phonename;/*个人简介*/
        TextView tv_haoyou;/*加好友*/
        TextView tv_jiaguanzhu;/*加关注*/
    }
}

