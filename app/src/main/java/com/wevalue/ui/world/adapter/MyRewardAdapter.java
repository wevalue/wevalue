package com.wevalue.ui.world.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.RewardBean.RewardEntity;
import com.wevalue.net.RequestPath;

import java.util.List;

/**
 * Created by xuhua on 2016/8/24.
 */
public class MyRewardAdapter extends BaseAdapter{
    private Context context;
    private List<RewardEntity> mDatas;
    private LayoutInflater minflater;


    public MyRewardAdapter(Context context, List<RewardEntity> reward) {
        this.context = context;
        this.mDatas=reward;
        minflater = LayoutInflater.from(context);
    }

    public void setmData(List<RewardEntity> mDate) {

        this.mDatas = mDate;
    }



    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if(view==null){
            viewHolder = new ViewHolder();
            view = minflater.inflate(R.layout.item_my_reward,null);
            viewHolder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            viewHolder.tv_dengji = (TextView) view.findViewById(R.id.tv_dengji);
            viewHolder.tv_qian = (TextView) view.findViewById(R.id.tv_qian);
            viewHolder.tv_xxnicheng = (TextView) view.findViewById(R.id.tv_xxnicheng);
            viewHolder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            viewHolder.tv_day = (TextView) view.findViewById(R.id.tv_day);
            viewHolder.iv_user_img = (ImageView) view.findViewById(R.id.iv_user_img);
            viewHolder.iv_infotouxiang = (ImageView) view.findViewById(R.id.iv_infotouxiang);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }

        RewardEntity rewardEntity = mDatas.get(i);

        viewHolder.tv_nickname.setText(rewardEntity.getUsernickname());
        viewHolder.tv_dengji.setText(rewardEntity.getUserlevel());
        viewHolder.tv_qian.setText("¥"+rewardEntity.getRewardmoney());
        viewHolder.tv_info.setText(rewardEntity.getInitial_content());
        viewHolder.tv_xxnicheng.setText(rewardEntity.getInitial_usernickname());
        viewHolder.tv_day.setText(rewardEntity.getRewardtime());
        setImgData(rewardEntity.getUserface(),viewHolder.iv_user_img);
        setImgData(rewardEntity.getInitial_userface(),viewHolder.iv_infotouxiang);

        return view;
    }

    class ViewHolder{
        TextView tv_nickname;//昵称
        TextView tv_dengji;//等级
        TextView tv_qian;//钱
        TextView tv_xxnicheng;//信息发布者昵称
        TextView tv_info;//发布信息
        TextView tv_day;//日期
        ImageView iv_user_img;//头像
        ImageView iv_infotouxiang;//信息发布者头像
    }

    private void setImgData(String imgUrl,ImageView iv){
        Glide.with(context)
                .load(RequestPath.SERVER_PATH+imgUrl)
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }
}
