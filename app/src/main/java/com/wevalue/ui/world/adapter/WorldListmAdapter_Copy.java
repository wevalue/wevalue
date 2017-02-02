package com.wevalue.ui.world.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.RepostNoteDetailActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.activity.TransmitNoteActivity;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollGridView;

import java.util.List;

/**
 * Created by Administrator on 2016-07-21.
 */
public class WorldListmAdapter_Copy extends BaseAdapter {
    ViewHolder viewHolder = null;
    NoteBean.NoteEntity noteEntity;
    private List<NoteBean.NoteEntity> mDatas;
    private List<NoteBean.NoteEntity> jiageData;
    private Context mContext;
    private Activity mActivity;
    private NoteRequestBase mNoteRequestBase;
    private WorldListGridViewAdapter mGirdViewAdapter;
    private String mType;
    // 图片下载器
    Drawable iszan;
    Drawable nozan;

    public WorldListmAdapter_Copy(List<NoteBean.NoteEntity> mDatas, List<NoteBean.NoteEntity> jiageData, Context mContext, String type) {
        this.mDatas = mDatas;
        this.jiageData = jiageData;
        this.mContext = mContext;
        mActivity = (Activity) mContext;
        this.mType = type;
        initDrawable();
    }

    private void initDrawable() {
        nozan = mContext.getResources().getDrawable(R.mipmap.note_praise);
        iszan = mContext.getResources().getDrawable(R.mipmap.notedetail_hongxin);
        iszan.setBounds(0, 0, iszan.getMinimumWidth(), iszan.getMinimumHeight()); //设置边界
        nozan.setBounds(0, 0, nozan.getMinimumWidth(), nozan.getMinimumHeight()); //设置边界
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
//         ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_world_list_copy, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_content_content = (TextView) convertView.findViewById(R.id.tv_content_content);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.in_audio_video_ui = (LinearLayout) convertView.findViewById(R.id.in_audio_video_ui);
            viewHolder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
            viewHolder.iv_video_and_audio_img = (ImageView) convertView.findViewById(R.id.iv_video_and_audio_img);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_zhuanfa_num = (TextView) convertView.findViewById(R.id.tv_zhuanfa_num);
            viewHolder.nsgv_world_list_gridview = (NoScrollGridView) convertView.findViewById(R.id.nsgv_world_list_gridview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        noteEntity = mDatas.get(position);

        //头像点击事件
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
        //头像点击事件
        viewHolder.tv_nickname.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.tv_nickname.setText(noteEntity.getUsernickname()+"-"+noteEntity.getNotetype());
        viewHolder.tv_price.setText("¥" + noteEntity.getPaynum());
        viewHolder.tv_income.setText("¥" + noteEntity.getShouyi());
        viewHolder.tv_zhuanfa_num.setText("送给朋友们("+noteEntity.getRepostcount()+")");

        viewHolder.tv_content_content.setVisibility(View.GONE);
        viewHolder.tv_title.setVisibility(View.GONE);
        viewHolder.iv_play.setVisibility(View.GONE);
        viewHolder.iv_video_and_audio_img.setVisibility(View.GONE);
        viewHolder.nsgv_world_list_gridview.setVisibility(View.GONE);

        try {
            final String noteId = noteEntity.getNoteid();
            String notetype = noteEntity.getNotetype();
            switch (notetype) {
                case "4"://文字
                    if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                        //如果有图片 那就显示 标题和图片
                        viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                        viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                    }
                    break;
                case "1"://视频文
                    viewHolder.tv_title.setVisibility(View.VISIBLE);
                    viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                    viewHolder.iv_play.setVisibility(View.VISIBLE);
                    viewHolder.iv_video_and_audio_img.setVisibility(View.VISIBLE);
                    viewHolder.tv_title.setText(noteEntity.getContent());
                    viewHolder.iv_play.setImageResource(R.mipmap.note_play);
                    imgViewSetData(mDatas.get(position).getNotevideopic(), viewHolder.iv_video_and_audio_img);
                    break;
                case "2"://音频文
                    viewHolder.iv_play.setVisibility(View.VISIBLE);
                    viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                    viewHolder.iv_video_and_audio_img.setVisibility(View.VISIBLE);
                    viewHolder.tv_title.setVisibility(View.VISIBLE);
                    viewHolder.tv_title.setText(noteEntity.getContent());
                    viewHolder.iv_play.setImageResource(R.mipmap.btn_music_bf);
                   // viewHolder.iv_video_and_audio_img.setImageResource(R.mipmap.bg_yinpinbg);
                    Glide.with(mActivity).load(R.mipmap.bg_yinpinbg).into(viewHolder.iv_video_and_audio_img);
                    break;
                case "3"://图文
                    if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                        viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                            viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                            mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                            viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                        }
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                    }
                    break;
                case "5": // 转发 图文混排
                    if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
//                        int l = noteEntity.getList().size();
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                        viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        viewHolder.tv_title.setVisibility(View.VISIBLE);
                        viewHolder.tv_title.setText(noteEntity.getContent());
                        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                    }

                    break;
            }

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

            mNoteRequestBase = NoteRequestBase.getNoteRequestBase(mContext);

            viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    //跳转到转发帖子详情页
                    intent = new Intent(mContext, RepostNoteDetailActivity.class);
                    intent.putExtra("noteId", mDatas.get(position).getNoteid());
                    intent.putExtra("repostid", mDatas.get(position).getRepostid());
                    intent.putExtra("repostfrom", "1");
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }
        //转发点击事件
        viewHolder.tv_zhuanfa_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransmitNoteActivity.class);
                intent.putExtra("repostid", mDatas.get(position).getRepostid());
                intent.putExtra("noteId", mDatas.get(position).getNoteid());
                intent.putExtra("nickname", mDatas.get(position).getUsernickname());
                intent.putExtra("imgurl", mDatas.get(position).getUserface());
                intent.putExtra("notecontent", mDatas.get(position).getContent());
                intent.putExtra("isself", mDatas.get(position).getIsself());
                intent.putExtra("noteFee", mDatas.get(position).getIsfree());
                intent.putExtra("paynum", mDatas.get(position).getPaynum());
                intent.putExtra("repostfrom", "1");
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private void imgViewSetData(String url, ImageView iv) {
        Glide.with(mActivity)
                .load(RequestPath.SERVER_PATH + url)
                .thumbnail(0.5f) // 用一般大小作为缩略图
                .placeholder(R.mipmap.default_video)
                .error(R.mipmap.default_video)
                .crossFade()
                .into(iv);
    }

    class ViewHolder {
        TextView tv_title;//标题
        TextView tv_content_content;//纯文字信息内容
        TextView tv_price;//单价
        TextView tv_income;//总收益
        //视频 音频
        LinearLayout in_audio_video_ui;
        ImageView iv_play;//播放图标
        ImageView iv_video_and_audio_img;//视频音频 图片
        //图片布局
        NoScrollGridView nsgv_world_list_gridview;//图片列表;

        ImageView iv_user_img;             //用户头像
        TextView tv_nickname;//昵称
        TextView tv_zhuanfa_num;//转发数
    }
}