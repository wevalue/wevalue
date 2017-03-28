package com.wevalue.ui.we.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.details.adapter.ReplyCommentActivity;
import com.wevalue.ui.we.activity.PushContentActivity;
import com.wevalue.utils.ImageUitls;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 作者：邹永奎
 * 创建时间：2016/10/171
 * 类说明：消息的adapter
 */

public class DaVAdapter extends ArrayAdapter<SiteMessageModel.DataBean> {
    Activity mContext;

    public DaVAdapter(Activity mContext) {
        super(mContext, 0);
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public SiteMessageModel.DataBean getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_davl, null);
            viewHolder.iv_face = (RoundImageView) convertView.findViewById(R.id.iv_face);
            viewHolder.iv_user_v = (ImageView) convertView.findViewById(R.id.iv_user_v);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            viewHolder.tv_weizhi = (TextView) convertView.findViewById(R.id.tv_weizhi);
            viewHolder.tv_guanzhu = (TextView) convertView.findViewById(R.id.tv_guanzhu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SiteMessageModel.DataBean dataBean = getItem(position);
        ImageUitls.setHead(dataBean.getUserface(), viewHolder.iv_face);
        if ("1".equals(dataBean.getUserv())) {
            viewHolder.iv_user_v.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_user_v.setVisibility(View.INVISIBLE);
        }
        if ("1".equals(dataBean.getIsfocus())){
            viewHolder.tv_guanzhu.setText("已关注");
            viewHolder.tv_guanzhu.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.tv_guanzhu.setText("加关注");
            viewHolder.tv_guanzhu.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_name.setText(dataBean.getUsernickname());
        viewHolder.tv_dengji.setText(dataBean.getUserlevel());
        viewHolder.tv_weizhi.setText("微值号：" + dataBean.getUsernumber());
        viewHolder.tv_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendManageBase.AddFocus(mContext, dataBean.getUserid(), new WZHttpListener() {
                    @Override
                    public void onSuccess(String content, String isUrl) {
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            String result = jsonObject.getString("result");
                            String message = jsonObject.getString("message");
                            if (result.equals("1")) {
                                dataBean.setIsfocus("1");
                                notifyDataSetChanged();
                            }
                            ShowUtil.showToast(mContext, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String content) {
                    }
                });
            }
        });
        return convertView;
    }

    class ViewHolder {
        RoundImageView iv_face;//头像
        ImageView iv_user_v;//是否大V
        TextView tv_name;//昵称
        TextView tv_dengji;     //等级
        TextView tv_weizhi;     //微值号
        TextView tv_guanzhu; //关注按钮

    }
}
