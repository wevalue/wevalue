package com.wevalue.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.activity.RepostNoteDetailActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.activity.TransmitNoteActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-21.
 */
public class NoteListAdapter extends BaseAdapter {
    ViewHolder viewHolder = null;

    private List<NoteBean.NoteEntity> mDatas;
    private List<NoteBean.NoteEntity> mLunBoDatas = new ArrayList<>();
    private List<NoteBean.NoteEntity> mJiaGeDatas = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private NoteRequestBase mNoteRequestBase;
    private WorldListGridViewAdapter mGirdViewAdapter;

    private String orderType = "0";
    Drawable iszan;
    Drawable nozan;

    public NoteListAdapter(List<NoteBean.NoteEntity> mDatas, Context mContext, List<NoteBean.NoteEntity> mLunBoDatas, List<NoteBean.NoteEntity> mJiaGeDatas) {
        this.mDatas = mDatas;
        this.mLunBoDatas = mLunBoDatas;
        this.mJiaGeDatas = mJiaGeDatas;
        this.mContext = mContext;
        mActivity = (Activity) mContext;
        initDrawable();
    }

    public NoteListAdapter(List<NoteBean.NoteEntity> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mActivity = (Activity) mContext;
        initDrawable();
    }

    private void initDrawable() {
        nozan = mContext.getResources().getDrawable(R.mipmap.note_like_n);
        iszan = mContext.getResources().getDrawable(R.mipmap.note_like_p);
        iszan.setBounds(0, 0, iszan.getMinimumWidth(), iszan.getMinimumHeight()); //设置边界
        nozan.setBounds(0, 0, nozan.getMinimumWidth(), nozan.getMinimumHeight()); //设置边界

    }

    public void setmDatas(List<NoteBean.NoteEntity> mDatas) {
        this.mDatas = mDatas;
    }

    public void setOrderType(String type) {
        this.orderType = type;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note_list, null);
            viewHolder = new ViewHolder();
            viewHolder.layout_bg = (LinearLayout) convertView.findViewById(R.id.layout_bg);
            viewHolder.iv_play = (ImageView) convertView.findViewById(R.id.iv_play);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.iv_video_img = (ImageView) convertView.findViewById(R.id.iv_video_img);
            viewHolder.iv_audio_img = (ImageView) convertView.findViewById(R.id.iv_audio_img);
            viewHolder.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            viewHolder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
            viewHolder.tv_zhuanfa_num = (TextView) convertView.findViewById(R.id.tv_zhuanfa_num);
            viewHolder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_praise_num);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content_content = (TextView) convertView.findViewById(R.id.tv_content_content);
            viewHolder.tv_zanAndImg = (TextView) convertView.findViewById(R.id.tv_zanAndImg);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.in_audio_video_ui = convertView.findViewById(R.id.in_audio_video_ui);
            viewHolder.ll_praise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
            viewHolder.nsgv_world_list_gridview = (NoScrollGridView) convertView.findViewById(R.id.nsgv_world_list_gridview);
            viewHolder.rl_note_content_ui = (RelativeLayout) convertView.findViewById(R.id.rl_note_content_ui);
            viewHolder.tv_transmit_num = (TextView) convertView.findViewById(R.id.tv_transmit_num);
            viewHolder.ll_transmit_info = (LinearLayout) convertView.findViewById(R.id.ll_transmit_info);
            viewHolder.ll_head_info = (LinearLayout) convertView.findViewById(R.id.ll_head_info);
            viewHolder.ll_ZF_but = (LinearLayout) convertView.findViewById(R.id.ll_ZF_but);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NoteBean.NoteEntity noteEntity = mDatas.get(position);

        //头像点击事件
        viewHolder.iv_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("DETAIL", position + "");
                Intent intent = new Intent(mActivity, UserDetailsActivity.class);
                intent.putExtra("detailuserid", mDatas.get(position).getUserid());
                mActivity.startActivity(intent);
            }
        });

        imgViewSetData(mDatas.get(position).getUserface(), viewHolder.iv_user_img);

        viewHolder.tv_nickname.setText(noteEntity.getUsernickname());
        if (TextUtils.isEmpty(noteEntity.getTitle())) {
            viewHolder.tv_title.setText(noteEntity.getContent());
        } else {
            viewHolder.tv_title.setText(noteEntity.getTitle());
        }
        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
        viewHolder.tv_content_content.setText(noteEntity.getContent());
        viewHolder.tv_dengji.setText(noteEntity.getUserlevel());
        viewHolder.tv_price.setText("¥" + noteEntity.getPaynum());
        viewHolder.tv_income.setText("¥" + noteEntity.getShouyi());
        viewHolder.tv_day.setText(DateTiemUtils.editTime(noteEntity.getAddtime()));
        viewHolder.tv_comment_num.setText(noteEntity.getCommcount());
        viewHolder.tv_zhuanfa_num.setText(noteEntity.getRepostcount());
        viewHolder.tv_praise_num.setText(noteEntity.getZancount());
        //如果是免费发布的 则隐藏转发 和 价格
        if (mDatas.get(position).getIsfree().equals("1")) {
            viewHolder.ll_ZF_but.setVisibility(View.INVISIBLE);
//            viewHolder.tv_price.setVisibility(View.INVISIBLE);
//            viewHolder.tv_income.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.ll_ZF_but.setVisibility(View.VISIBLE);
        }
        if ("1".equals(noteEntity.getIszan())) {
            viewHolder.tv_zanAndImg.setCompoundDrawables(iszan, null, null, null);
        } else if (("0").equals(noteEntity.getIszan())) {
            viewHolder.tv_zanAndImg.setCompoundDrawables(nozan, null, null, null);
        }
        try {
            final String noteId = noteEntity.getNoteid();
            String notetype = noteEntity.getNotetype();
            viewHolder.iv_audio_img.setVisibility(View.GONE);
            viewHolder.iv_play.setVisibility(View.GONE);
            viewHolder.iv_video_img.setVisibility(View.GONE);

            switch (notetype) {
                case "4"://文字
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
//                        mGirdViewAdapter.notifyDataSetChanged();
                    viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content_content.setText(noteEntity.getContent());
                    break;
                case "1"://视频文
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                    viewHolder.iv_video_img.setVisibility(View.VISIBLE);
                    viewHolder.iv_play.setVisibility(View.VISIBLE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.GONE);
                    viewHolder.iv_play.setImageResource(R.mipmap.note_play);
                    imgViewSetData(mDatas.get(position).getNotevideopic(), viewHolder.iv_video_img);
                    break;
                case "2"://音频文
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.VISIBLE);
                    viewHolder.iv_audio_img.setVisibility(View.VISIBLE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.GONE);
                    viewHolder.iv_audio_img.setImageResource(R.mipmap.ic_music);
                    break;
                case "3"://图文
                    viewHolder.tv_content_content.setVisibility(View.GONE);
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {

                        viewHolder.tv_content_content.setVisibility(View.GONE);
                        viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                        viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
//                        int l = noteEntity.getList_1().size();
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
//                        mGirdViewAdapter.notifyDataSetChanged();
                        viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                        viewHolder.tv_content_content.setText(noteEntity.getContent());
                    }
                    break;

                case "5":
                    viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content_content.setText(noteEntity.getContent());
                    viewHolder.in_audio_video_ui.setVisibility(View.GONE);
                    viewHolder.nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity/*,mBitmap,bitmapDisplayConfig*/);
                    viewHolder.nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    break;
            }
            //如果转发id==0 则此信息不是转发的 转发的不显示标题
            final String repsotid = mDatas.get(position).getRepostid();
            if (repsotid.equals("0")) {
                viewHolder.ll_transmit_info.setVisibility(View.GONE);
                viewHolder.rl_note_content_ui.setBackgroundResource(R.color.white);
                viewHolder.tv_title.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_transmit_info.setVisibility(View.VISIBLE);
                viewHolder.tv_transmit_num.setText(mDatas.get(position).getRepostcontent());
                viewHolder.tv_transmit_num.setVisibility(View.VISIBLE);
                String context_2 = "@" + mDatas.get(position).getOldusernickname() + "：" + mDatas.get(position).getContent();
                SpannableStringBuilder style_2 = new SpannableStringBuilder(context_2);
                style_2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.blue)), 0,
                        mDatas.get(position).getOldusernickname().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_title.setVisibility(View.GONE);
                viewHolder.tv_content_content.setVisibility(View.VISIBLE);
                viewHolder.tv_content_content.setText(style_2);
                viewHolder.rl_note_content_ui.setBackgroundResource(R.color.background);
            }
            viewHolder.rl_note_content_ui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    //跳转到帖子详情页
                    intent = new Intent(mContext, NoteDetailsActivity.class);
                    intent.putExtra("noteId", mDatas.get(position).getNoteid());
                    intent.putExtra("repostid", mDatas.get(position).getRepostid());
                    mContext.startActivity(intent);
                }
            });
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
            viewHolder.ll_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!TextUtils.isEmpty(SharedPreferencesUtil.getUid(mContext))) {
                        if (mDatas.get(position).getIszan().equals("1")) {
                            ShowUtil.showToast(mContext, "取消点赞");
                            String s = String.valueOf(Integer.parseInt(mDatas.get(position).getZancount()) - 1);
                            mDatas.get(position).setZancount(s);
                            mDatas.get(position).setIszan("0");
                        } else {
                            ShowUtil.showToast(mContext, "点赞成功");
                            String s = String.valueOf(Integer.parseInt(mDatas.get(position).getZancount()) + 1);
                            mDatas.get(position).setZancount(s);
                            mDatas.get(position).setIszan("1");
                        }
                        notifyDataSetChanged();
                        mNoteRequestBase.postNoteLike((Activity) mContext, noteId, mDatas.get(position).getRepostid(), new WZHttpListener() {
                            public void onSuccess(String content, String isUrl) {

                            }

                            @Override
                            public void onFailure(String content) {

                            }
                        });
                    } else {
                        ShowUtil.showToast(mContext, "对不起,请您先登录");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return convertView;
        }

        viewHolder.textView.setText("送给朋友们");
        viewHolder.tv_zhuanfa_num.setText(noteEntity.getCommcount());

        viewHolder.ll_ZF_but.setOnClickListener(new View.OnClickListener() {
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


        // 是否是热门的帖子
        boolean isHot = false;
        //一般是前三条
        if (position<3&&mLunBoDatas!=null)
            for (int i = 0; i < mLunBoDatas.size(); i++) {
                if (mLunBoDatas.get(i)==noteEntity){
                    isHot = true;
                    break;
                }
            }
        if (isHot){
            viewHolder.layout_bg.setBackgroundResource(R.mipmap.bianxian);
        }else {
            viewHolder.layout_bg.setBackgroundResource(R.color.white);
        }

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
        LinearLayout layout_bg;//整个item布局
        LinearLayout ll_transmit_info;//转发信息的布局
        LinearLayout ll_head_info;//用户信息
        TextView tv_transmit_num;//转发信息的打赏或收益情况

        ImageView iv_user_img;             //用户头像
        ImageView iv_video_img;//视频 图片
        ImageView iv_audio_img;//音频 图片
        TextView tv_nickname;//昵称
        TextView tv_dengji;//等级
        TextView tv_day;//日期
        TextView tv_price;//单价
        TextView tv_income;//总收益
        TextView tv_comment_num;//评论数
        TextView tv_zhuanfa_num;//转发数
        TextView tv_praise_num;//点赞数
        TextView tv_zanAndImg;//赞
        TextView tv_title;//纯文字信息内容
        TextView tv_content_content;//图文信息内容
        TextView textView;//转发或者评论 的文字按钮
        View in_audio_video_ui;//视频音频的图片区域;
        NoScrollGridView nsgv_world_list_gridview;//图片列表;
        LinearLayout ll_praise;
        LinearLayout ll_ZF_but;
        RelativeLayout rl_note_content_ui;
        ImageView iv_play;//播放图标

    }
}
