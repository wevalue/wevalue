package com.wevalue.ui.world.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.wevalue.view.AudioView;
import com.wevalue.utils.ImageUitls;
import com.wevalue.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-21.
 */
public class WorldListAdapters extends BaseAdapter {


    private List<NoteBean.NoteEntity> mDatas;
    private List<NoteBean.NoteEntity> jiageData = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private NoteRequestBase mNoteRequestBase;
    private WorldListGridViewAdapter mGirdViewAdapter;
    private String mType;

    public WorldListAdapters(List<NoteBean.NoteEntity> mDatas, List<NoteBean.NoteEntity> jiageData, Activity mContext, String type) {
        this.mDatas = mDatas;
        this.jiageData = jiageData;
        this.mContext = mContext;
        mActivity = mContext;
        this.mType = type;
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
        if (mDatas == null) return convertView;
        NoteBean.NoteEntity noteEntity = mDatas.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_world_list_a, null);
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
            viewHolder.audio = (ImageView) convertView.findViewById(R.id.audio);

            viewHolder.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            //收益
            viewHolder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUitls.setHead(noteEntity.getUserface(), viewHolder.iv_user_img);
        viewHolder.tv_nickname.setText(noteEntity.getUsernickname());
        viewHolder.tv_income.setText(noteEntity.getShouyi());
        viewHolder.tv_title.setText(noteEntity.getTitle());
        //默认隐藏所有view
        viewHolder.tv_content.setVisibility(View.GONE);
        viewHolder.iv_single.setVisibility(View.GONE);
        viewHolder.gridview.setVisibility(View.GONE);
        viewHolder.video_layout.setVisibility(View.GONE);
        viewHolder.audio.setVisibility(View.GONE);
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
                Log.d("test","noteType = 图文混排");
            case "3"://图文
                if (imgUrls != null && imgUrls.size() == 1) {
                    viewHolder.iv_single.setVisibility(View.VISIBLE);
                    String url = (String) imgUrls.get(0).getUrl();
                    setImg(url,viewHolder.iv_single);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content.setText(noteEntity.getContent());
                }else if (imgUrls != null && imgUrls.size() >1){
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), mActivity);
                    if (imgUrls.size()==4||imgUrls.size()==2){
                        viewHolder.gridview.setNumColumns(2);
                    }else {
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
                    setImg(url,viewHolder.iv_single);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                }
                break;
        }

        //点击图片
        viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                if (notetype.equals("5")){//如果是图文 直接跳转详情界面
                    Intent intent = null;
                    //跳转到转发帖子详情页
                    intent = new Intent(mContext, NoteDetailActivity.class);
                    intent.putExtra("noteId", mDatas.get(position).getNoteid());
                    intent.putExtra("repostid", "0");
                    mContext.startActivity(intent);
                }else {
                    String[] url = new String[mDatas.get(position).getList().size()];
                    for (int i = 0; i < mDatas.get(position).getList().size(); i++) {
                        url[i] = mDatas.get(position).getList().get(i).getUrl();
                    }
                    Intent intent = new Intent(mActivity, ImgShowActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("imgUrl", url);
                    mActivity.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public static void setImg(String url, ImageView view) {
        Glide.with(WeValueApplication.applicationContext)
                .load(RequestPath.SERVER_WEB_PATH+url)
                .asBitmap()
                .placeholder(R.mipmap.default_head)
                .into(view);
    }
    private ViewHolder viewHolder = null;

    class ViewHolder {
        //图片布局
        TextView tv_title;//标题
        TextView tv_content;//纯文字信息内容
        ImageView iv_user_img;//用户头像
        TextView tv_nickname; //昵称
        NoScrollGridView gridview;//图片列表;
        RelativeLayout video_layout;
        ImageView video_img;//视频 图片
        ImageView audio;//音频 图片
        ImageView iv_single;//单图
        TextView tv_income;//总收益
    }
}
