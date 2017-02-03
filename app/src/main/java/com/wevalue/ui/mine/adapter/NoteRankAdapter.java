package com.wevalue.ui.mine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.view.NoScrollGridView;

import java.util.List;

/**
 * 作者：邹永奎
 * 创建时间：2016/11/10
 * 类说明：帖子的排行页面
 */

public class NoteRankAdapter extends BaseAdapter {
    ViewHolder viewHolder = null;
    NoteBean.NoteEntity noteEntity;
    private List<NoteBean.NoteEntity> mDatas;
    private Context mContext;
    private Activity mActivity;
    private WorldListGridViewAdapter mGirdViewAdapter;
    private String type = null;



    public NoteRankAdapter(List<NoteBean.NoteEntity> mDatas, Context mContext, String type) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mActivity = (Activity) mContext;
        this.type = type;
    }

    public void setmDatas(List<NoteBean.NoteEntity> mDatas) {
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
        if (position == 0) {
            LogUtils.e("getView ------= " + position);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ranknote, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.iv_video_img = (ImageView) convertView.findViewById(R.id.iv_video_img);
            viewHolder.iv_audio_img = (ImageView) convertView.findViewById(R.id.iv_audio_img);
            viewHolder.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            viewHolder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_content_content = (TextView) convertView.findViewById(R.id.tv_content_content);
            viewHolder.tv_img_content = (TextView) convertView.findViewById(R.id.tv_img_content);
            viewHolder.in_audio_video_ui = convertView.findViewById(R.id.in_audio_video_ui);
            viewHolder.ll_imgAndAudioAndVideo_ui = (LinearLayout) convertView.findViewById(R.id.ll_imgAndAudioAndVideo_ui);
            viewHolder.nsgv_world_list_gridview = (NoScrollGridView) convertView.findViewById(R.id.nsgv_world_list_gridview);
            viewHolder.rl_zhuan_content_ui = (RelativeLayout) convertView.findViewById(R.id.rl_zhuan_content_ui);
            viewHolder.rl_rank_content_ui = (RelativeLayout) convertView.findViewById(R.id.rl_rank_content_ui);
            viewHolder.tv_transmit_num = (TextView) convertView.findViewById(R.id.tv_transmit_num);
            viewHolder.ll_transmit_info = (LinearLayout) convertView.findViewById(R.id.ll_transmit_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        noteEntity = mDatas.get(position);
        viewHolder.iv_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("DETAIL", position + "");
                Intent intent = new Intent(mActivity, UserDetailsActivity.class);
                intent.putExtra("detailuserid", mDatas.get(position).getUserid());
//                SharedPreferencesUtil.setDetailUserid(mContext, mDatas.get(position).getUserid());
                mActivity.startActivity(intent);
            }
        });
        imgViewSetData(mDatas.get(position).getUserface(), viewHolder.iv_user_img);
        viewHolder.tv_nickname.setText(noteEntity.getUsernickname());
        viewHolder.tv_img_content.setText(noteEntity.getContent());
        viewHolder.tv_dengji.setText(noteEntity.getUserlevel());
        viewHolder.tv_price.setText("¥" + noteEntity.getPaynum());
        viewHolder.tv_income.setText("¥" + noteEntity.getShouyi());
        viewHolder.tv_day.setText(DateTiemUtils.editTime(noteEntity.getAddtime()));
        if (null != type) {
            //需要显示转发信息
            viewHolder.ll_head_info = (LinearLayout) convertView.findViewById(R.id.ll_head_info);
            viewHolder.ll_transmit_info.setVisibility(View.VISIBLE);
            switch (type) {
                case "2"://转发
                    viewHolder.tv_transmit_num.setText(mDatas.get(position).getRankrepost());
                    break;
                case "3"://打赏
                    viewHolder.tv_transmit_num.setText(mDatas.get(position).getRankreward());
                    break;
            }
        } else {

            if (noteEntity.getIszan().equals("1")) {
            } else if (noteEntity.getIszan().equals("0")) {
            }
            if (noteEntity.getRepostid().equals("0")) {
                viewHolder.ll_transmit_info.setVisibility(View.GONE);
            } else {
                viewHolder.ll_transmit_info.setVisibility(View.VISIBLE);
                viewHolder.tv_transmit_num.setText(noteEntity.getRepostcontent());
            }
        }
        final String noteId = noteEntity.getNoteid();
        String notetype = noteEntity.getNotetype();
        viewHolder.iv_audio_img.setVisibility(View.GONE);
        viewHolder.iv_video_img.setVisibility(View.GONE);
        switch (notetype) {
            case "4"://文字
                if (noteEntity.getList() != null && noteEntity.getList().size() > 0) {
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                    mGirdViewAdapter.notifyDataSetChanged();
                    viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                } else {
                    viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content_content.setText(noteEntity.getContent());
                    viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.GONE);
                }
                break;
            case "1"://视频文
                viewHolder.tv_content_content.setVisibility(View.GONE);
                viewHolder.nsgv_world_list_gridview.setVisibility(View.GONE);
                viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                viewHolder.iv_video_img.setVisibility(View.VISIBLE);
                imgViewSetData(mDatas.get(position).getNotevideopic(), viewHolder.iv_video_img);
                break;
            case "2"://音频文
                viewHolder.tv_content_content.setVisibility(View.GONE);
                viewHolder.nsgv_world_list_gridview.setVisibility(View.GONE);
                viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                viewHolder.iv_audio_img.setVisibility(View.VISIBLE);
                viewHolder.iv_audio_img.setImageResource(R.mipmap.ic_music);
                break;
            case "3"://图文
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    int l = noteEntity.getList_1().size();
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                    mGirdViewAdapter.notifyDataSetChanged();
                    viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                }else {

                    viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content_content.setText(noteEntity.getContent());
                    viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.GONE);
                }
                break;
            case "5":
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
//                        int l = noteEntity.getList().size();
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);

                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                    viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                }else {
                    if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                        viewHolder.tv_content_content.setVisibility(View.GONE);
                        viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                        viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                        viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
//                        mGirdViewAdapter.notifyDataSetChanged();
                        viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                        viewHolder.tv_img_content.setText(noteEntity.getContent());
                        viewHolder.ll_imgAndAudioAndVideo_ui.setVisibility(View.GONE);
                    }

                }

                break;
        }


        viewHolder.rl_rank_content_ui.setBackgroundResource(R.color.background);

        viewHolder.nsgv_world_list_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                String[] url = new String[mDatas.get(position).getList().size()];
                for (int i = 0; i < mDatas.get(position).getList().size(); i++) {
                    url[i] = mDatas.get(position).getList().get(i).getUrl();
                }
                Intent intent = new Intent(mActivity, ImgShowActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("imgUrl", url);
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void imgViewSetData(String url, ImageView iv) {
        Glide.with(mActivity)
                .load(RequestPath.SERVER_PATH + url)
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }

    class ViewHolder {
        LinearLayout ll_transmit_info;//转发信息的布局
        LinearLayout ll_head_info;//用户信息
        TextView tv_transmit_num;//转发信息的打赏或收益情况
        ImageView iv_user_img;    //用户头像
        ImageView iv_video_img;//视频 图片
        ImageView iv_audio_img;//音频 图片
        TextView tv_nickname;//昵称
        TextView tv_dengji;//等级
        TextView tv_day;//日期
        TextView tv_price;//单价
        TextView tv_income;//总收益
        TextView tv_content_content;//纯文字信息内容
        TextView tv_img_content;//图文信息内容
        View in_audio_video_ui;//视频音频的图片区域;
        NoScrollGridView nsgv_world_list_gridview;//图片列表;
        LinearLayout ll_imgAndAudioAndVideo_ui;
        RelativeLayout rl_zhuan_content_ui;
        RelativeLayout rl_rank_content_ui;
    }
}

