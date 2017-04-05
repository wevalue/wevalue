package com.wevalue.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.model.NoteBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.NoteDetailActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.ActivityUtils;
import com.wevalue.utils.ImageUitls;
import com.wevalue.view.AudioView;
import com.wevalue.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-21.
 */
public class NoteListAdapter extends ArrayAdapter {

    private List<NoteBean.NoteEntity> mDatas = new ArrayList<>();;
    private List<NoteBean.NoteEntity> jiageData = new ArrayList<>();
    private Activity mActivity;
    private NoteRequestBase mNoteRequestBase;
    private WorldListGridViewAdapter mGirdViewAdapter;
    private String orderType;

    public NoteListAdapter(List<NoteBean.NoteEntity> mDatas, Activity mContext) {
        super(mContext,0);
        this.mDatas = mDatas;
        this.mActivity = mContext;
    }

    public NoteListAdapter(Activity mActivity) {
        super(mActivity,0);
        this.mActivity = mActivity;
    }

    @Override
    public void clear() {
        mDatas.clear();
    }

    public void setOrderType(String type) {
        this.orderType = type;
    }

    public void setmDatas(List<NoteBean.NoteEntity> mDatas) {
        this.mDatas.addAll(mDatas);
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
        if (mDatas == null) return convertView;
        final NoteBean.NoteEntity noteEntity = mDatas.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_note_list_a, null);

            viewHolder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            viewHolder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);


            viewHolder.tv_transmit = (TextView) convertView.findViewById(R.id.tv_transmit);
            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.layout_single_img = (RelativeLayout) convertView.findViewById(R.id.layout_single_img);

            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            //单图
            viewHolder.iv_single = (ImageView) convertView.findViewById(R.id.iv_single);
            //宫格多图
            viewHolder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
            //视频父布局
            viewHolder.video_layout = (RelativeLayout) convertView.findViewById(R.id.video_layout);
            //视频图片
            viewHolder.video_img = (ImageView) convertView.findViewById(R.id.video_img);
            //音频 此界面不播放音频
            viewHolder.audio = (AudioView) convertView.findViewById(R.id.audio);
            //评论 点赞 转发
            viewHolder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
            viewHolder.tv_zhuanfa_num = (TextView) convertView.findViewById(R.id.tv_zhuanfa_num);
            viewHolder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_praise_num);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUitls.setHead(noteEntity.getUserface(), viewHolder.iv_user_img);
        viewHolder.tv_nickname.setText(noteEntity.getUsernickname());
        viewHolder.tv_dengji.setText(noteEntity.getUserlevel());

        viewHolder.tv_price.setText("价格 "+noteEntity.getPaynum());
        viewHolder.tv_income.setText("收益 "+noteEntity.getShouyi());
        viewHolder.tv_day.setText("微值号 "+noteEntity.getUsernumber());

        viewHolder.tv_title.setText(noteEntity.getTitle());

        viewHolder.tv_comment_num.setText(noteEntity.getCommcount());
        viewHolder.tv_zhuanfa_num.setText(noteEntity.getRepostcount());
        viewHolder.tv_praise_num.setText(noteEntity.getZancount());

        //默认隐藏所有view
        viewHolder.tv_content.setVisibility(View.GONE);
        viewHolder.iv_single.setVisibility(View.GONE);
        viewHolder.gridview.setVisibility(View.GONE);
        viewHolder.video_layout.setVisibility(View.GONE);
        viewHolder.audio.setVisibility(View.GONE);
        //免费的信息隐藏价格和收益
        if ("1".equals(noteEntity.getIsfree())){
            viewHolder.tv_price.setVisibility(View.INVISIBLE);
            viewHolder.tv_income.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.tv_price.setVisibility(View.VISIBLE);
            viewHolder.tv_income.setVisibility(View.VISIBLE);
        }

        //帖子类型
        final String notetype = noteEntity.getNotetype();
        //图片集合
        List<NoteBean.ImgUrl> imgUrls = noteEntity.getList_1();
        switch (notetype) {
            case "1"://视频
                viewHolder.video_layout.setVisibility(View.VISIBLE);
                setImg(noteEntity.getNotevideopic(), viewHolder.video_img);
                break;
            case "2"://音频
                viewHolder.audio.setVisibility(View.VISIBLE);
                break;
            case "5"://图文混排
                Log.d("test", "noteType = 图文混排");
            case "3"://图文
                if (imgUrls != null && imgUrls.size() == 1) {
                    viewHolder.iv_single.setVisibility(View.VISIBLE);
                    String url = (String) imgUrls.get(0).getUrl();
                    setImg(url, viewHolder.iv_single);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content.setText(noteEntity.getContent());
                } else if (imgUrls != null && imgUrls.size() > 1) {
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity);
                    if (imgUrls.size() == 4 || imgUrls.size() == 2) {
                        viewHolder.gridview.setNumColumns(2);
                    } else {
                        viewHolder.gridview.setNumColumns(3);
                    }
                    viewHolder.gridview.setAdapter(mGirdViewAdapter);
                    viewHolder.gridview.setVisibility(View.VISIBLE);
                }
                break;
            case "4"://纯文字
                viewHolder.tv_content.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(noteEntity.getContent());
                if (imgUrls != null && imgUrls.size() >= 1) {
                    viewHolder.iv_single.setVisibility(View.VISIBLE);
                    String url = (String) imgUrls.get(0).getUrl();
                    setImg(url, viewHolder.iv_single);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                }
                break;
        }

        //如果转发id==0 则此信息不是转发的 转发的不显示标题
        final String repsotid = mDatas.get(position).getRepostid();
        if (repsotid.equals("0")) {
            viewHolder.tv_transmit.setVisibility(View.GONE);
            viewHolder.tv_author.setVisibility(View.GONE);
            viewHolder.layout_single_img.setBackgroundResource(R.color.white);
        } else {
            viewHolder.tv_transmit.setVisibility(View.VISIBLE);
            viewHolder.tv_transmit.setText(noteEntity.getRepostcontent());
            viewHolder.tv_author.setVisibility(View.VISIBLE);
            viewHolder.tv_author.setText("@" + mDatas.get(position).getOldusernickname());
            viewHolder.layout_single_img.setBackgroundResource(R.color.background);
        }

        viewHolder.layout_single_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到转发帖子详情页
                gotoNoteDetails(noteEntity);
            }
        });
        //点击图片
        viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                if (notetype.equals("5")) {//如果是图文 直接跳转详情界面
                    //跳转到转发帖子详情页
                    ActivityUtils.gotoNoteDetails(mActivity,noteEntity,"0");
                } else {
                    openImg(noteEntity, index);
                }
            }
        });

        return convertView;
    }

    /**
     * 打开图片
     *
     * @param noteEntity
     * @param index      图片索引
     */
    private void openImg(NoteBean.NoteEntity noteEntity, int index) {
        String[] url = new String[noteEntity.getList().size()];
        for (int i = 0; i < noteEntity.getList().size(); i++) {
            url[i] = noteEntity.getList().get(i).getUrl();
        }
        Intent intent = new Intent(mActivity, ImgShowActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("imgUrl", url);
        mActivity.startActivity(intent);
    }

    /**
     * 跳转帖子转发详情页
     *
     */
    private void gotoNoteDetails(NoteBean.NoteEntity noteEntity) {
        Intent intent = null;
        //跳转到转发帖子详情页
        intent = new Intent(mActivity, NoteDetailActivity.class);
        intent.putExtra("noteId", noteEntity.getNoteid());
        intent.putExtra("repostid", noteEntity.getRepostid());
        mActivity.startActivity(intent);
    }

    public static void setImg(String url, ImageView view) {
        Glide.with(WeValueApplication.applicationContext)
                .load(RequestPath.SERVER_WEB_PATH + url)
                .asBitmap()
                .placeholder(R.mipmap.default_head)
                .into(view);
    }

    private ViewHolder viewHolder = null;

    class ViewHolder {
        ImageView iv_user_img;//用户头像
        TextView tv_nickname; //昵称
        TextView tv_price;//单价
        TextView tv_income;//总收益
        TextView tv_dengji;//等级
        TextView tv_day;//日期

        RelativeLayout layout_single_img;//转发布局
        TextView tv_transmit;//转发信息
        TextView tv_author;//@作者
        TextView tv_title;//标题
        TextView tv_content;//纯文字信息内容
        NoScrollGridView gridview;//图片列表;
        RelativeLayout video_layout;
        ImageView video_img;//视频 图片
        AudioView audio;//音频 图片
        ImageView iv_single;//单图

        TextView tv_comment_num;//评论数
        TextView tv_zhuanfa_num;//转发数
        TextView tv_praise_num;//点赞数
    }
}
