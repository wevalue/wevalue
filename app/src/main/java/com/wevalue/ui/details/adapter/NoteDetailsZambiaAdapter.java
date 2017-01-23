package com.wevalue.ui.details.adapter;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.NoteInfoBean;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;

import java.util.List;

/**
 * Created by Administrator on 2016-08-17.
 */
public class NoteDetailsZambiaAdapter extends BaseAdapter {

    private List<NoteInfoBean.NoteInfoEntity> mDatas;
    private Context mContext;
    private int isWho;

    public interface ReplyCommentInterface {
        void replyComment(int positon);
    }

    ReplyCommentInterface mInterface;



    public NoteDetailsZambiaAdapter(Context context, List<NoteInfoBean.NoteInfoEntity> list, ReplyCommentInterface mInterface) {
        mDatas = list;
        mContext = context;
        this.mInterface = mInterface;
    }

    public void setmDatas(List<NoteInfoBean.NoteInfoEntity> mDatas, int isWho) {
        this.mDatas = mDatas;
        this.isWho = isWho;
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
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note_info_zan, null);
            vh = new ViewHolder();
            vh.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            vh.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            vh.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            vh.iv_isRenzheng = (ImageView) convertView.findViewById(R.id.iv_isRenzheng);
            vh.iv_comment_img = (ImageView) convertView.findViewById(R.id.iv_comment_img);
            vh.rl_010 = (RelativeLayout) convertView.findViewById(R.id.rl_010);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        NoteInfoBean.NoteInfoEntity noteInfoEntity = mDatas.get(position);

        switch (isWho) {
            case 1:
                vh.iv_user_img.setVisibility(View.VISIBLE);
                vh.tv_day.setVisibility(View.VISIBLE);
                vh.tv_nickname.setVisibility(View.VISIBLE);
                vh.tv_dengji.setVisibility(View.VISIBLE);
                vh.rl_010.setVisibility(View.VISIBLE);
                vh.tv_content.setText(noteInfoEntity.getRepostcontent());
                vh.tv_day.setText(noteInfoEntity.getReposttime());
                setImgData(noteInfoEntity.getUserface(), vh.iv_user_img);
                vh.tv_nickname.setText(noteInfoEntity.getUsernickname());
                vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
                vh.iv_comment_img.setVisibility(View.GONE);
                vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", mDatas.get(position).getUserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 2:
                //评论信息的内容填充
                if (noteInfoEntity.getReplycommid() != null && noteInfoEntity.getReplycommid().equals("0")) {
                    vh.iv_user_img.setVisibility(View.VISIBLE);
                    vh.tv_day.setVisibility(View.VISIBLE);
                    vh.tv_nickname.setVisibility(View.VISIBLE);
                    vh.tv_dengji.setVisibility(View.VISIBLE);
                    vh.rl_010.setVisibility(View.VISIBLE);

                    vh.tv_content.setText(noteInfoEntity.getCommcontent());
                    vh.tv_day.setText(noteInfoEntity.getCommtime());
                    vh.tv_nickname.setText(noteInfoEntity.getCommuser());
                    vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
                    setImgData(noteInfoEntity.getCommface(), vh.iv_user_img);
                    if (!TextUtils.isEmpty(noteInfoEntity.getCommimg())) {
                        vh.iv_comment_img.setVisibility(View.VISIBLE);
                        setImgData(noteInfoEntity.getCommimg(), vh.iv_comment_img);
                    } else {
                        vh.iv_comment_img.setVisibility(View.GONE);
                    }

                } else {
                    vh.iv_user_img.setVisibility(View.GONE);
                    vh.tv_day.setVisibility(View.GONE);
                    vh.tv_nickname.setVisibility(View.GONE);
                    vh.tv_dengji.setVisibility(View.GONE);
                    vh.rl_010.setVisibility(View.GONE);
                    String content = noteInfoEntity.commuser + " 回复 " + noteInfoEntity.getReplyuser() + "：" + noteInfoEntity.getCommcontent();
                    SpannableStringBuilder builder = new SpannableStringBuilder(content);
                    ForegroundColorSpan blueSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.login_text_blue));
                    builder.setSpan(blueSpan, 0, (noteInfoEntity.commuser + " 回复 " + noteInfoEntity.getReplyuser() + "：").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    vh.tv_content.setText(builder);
                    if (!TextUtils.isEmpty(noteInfoEntity.getCommimg())) {
                        vh.iv_comment_img.setVisibility(View.VISIBLE);
                        setImgData(noteInfoEntity.getCommimg(), vh.iv_comment_img);
                    } else {
                        vh.iv_comment_img.setVisibility(View.GONE);
                    }
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInterface.replyComment(position);
                    }
                });
                vh.iv_comment_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ImgShowActivity.class);
                        String[] url = {mDatas.get(position).getCommimg()};
                        intent.putExtra("index", 0);
                        intent.putExtra("imgUrl", url);
                        mContext.startActivity(intent);
                    }
                });
                vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", mDatas.get(position).getCommuserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 3:
                vh.iv_user_img.setVisibility(View.VISIBLE);
                vh.tv_day.setVisibility(View.VISIBLE);
                vh.tv_nickname.setVisibility(View.VISIBLE);
                vh.tv_dengji.setVisibility(View.VISIBLE);
                vh.rl_010.setVisibility(View.VISIBLE);
                vh.tv_content.setText(noteInfoEntity.getMoodcontent());
                vh.tv_day.setText(noteInfoEntity.getMoodtime());
                setImgData(noteInfoEntity.getUserface(), vh.iv_user_img);
                vh.tv_nickname.setText(noteInfoEntity.getUsernickname());
                vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
                vh.iv_comment_img.setVisibility(View.GONE);
                vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", mDatas.get(position).getUserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 4:
                vh.iv_user_img.setVisibility(View.VISIBLE);
                vh.tv_day.setVisibility(View.VISIBLE);
                vh.tv_nickname.setVisibility(View.VISIBLE);
                vh.tv_dengji.setVisibility(View.VISIBLE);
                vh.rl_010.setVisibility(View.VISIBLE);
                vh.tv_content.setText("");
                vh.tv_day.setText(noteInfoEntity.getZantime());
                setImgData(noteInfoEntity.getUserface(), vh.iv_user_img);
                vh.tv_nickname.setText(noteInfoEntity.getUsernickname());
                vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
                vh.iv_comment_img.setVisibility(View.GONE);
                vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", mDatas.get(position).getUserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 5:
                vh.iv_user_img.setVisibility(View.VISIBLE);
                vh.tv_day.setVisibility(View.VISIBLE);
                vh.tv_nickname.setVisibility(View.VISIBLE);
                vh.tv_dengji.setVisibility(View.VISIBLE);
                vh.rl_010.setVisibility(View.VISIBLE);
                vh.tv_content.setText("打赏了发布者¥ " + noteInfoEntity.getRewardmoney());
                vh.tv_day.setText(noteInfoEntity.getRewardtime());
                setImgData(noteInfoEntity.getUserface(), vh.iv_user_img);
                vh.tv_nickname.setText(noteInfoEntity.getUsernickname());
                vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
                vh.iv_comment_img.setVisibility(View.GONE);
                vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", mDatas.get(position).getUserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        return convertView;
    }


    class ViewHolder {
        ImageView iv_user_img;
        ImageView iv_isRenzheng;
        TextView tv_nickname;
        TextView tv_dengji;
        TextView tv_day;
        TextView tv_content;
        ImageView iv_comment_img;
        RelativeLayout rl_010;
    }

    private void setImgData(String imgUrl, ImageView iv) {
        Glide.with(mContext)
                .load(RequestPath.SERVER_PATH + imgUrl)
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }
}
